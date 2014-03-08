package org.mynah.oblatum.support;

import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mynah.oblatum.model.Column;
import org.mynah.oblatum.util.CamelCaseUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.mynah.oblatum.util.Constants.*;

public class SqlGenerator implements SqlOperations {

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());
    private DataSource dataSource;

    public SqlGenerator() {
    }

    public SqlGenerator(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public List<String> getPrimaryKeys(String tableName) throws SQLException {
        List<String> primaryKeys = new ArrayList<String>();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet primaryKeysResultSet = metaData.getPrimaryKeys(null, metaData.getUserName(), tableName);
        while (primaryKeysResultSet.next()) {
            primaryKeys.add(primaryKeysResultSet.getString("COLUMN_NAME").toLowerCase());
        }
        DataSourceUtils.releaseConnection(connection, dataSource);
        return primaryKeys;
    }

    public List<Column> getColumns(String tableName) throws SQLException {
        List<Column> columns = new ArrayList<Column>();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columnsResultSet = metaData.getColumns(null, metaData.getUserName(), tableName, "%");
        while (columnsResultSet.next()) {
            Column column = new Column();
            column.setColumnName(columnsResultSet.getString("COLUMN_NAME").toLowerCase());
            column.setDataType(columnsResultSet.getInt("DATA_TYPE"));
            column.setTypeName(columnsResultSet.getString("TYPE_NAME"));
            column.setColumnSize(columnsResultSet.getInt("COLUMN_SIZE"));
            column.setNullable(columnsResultSet.getInt("NULLABLE"));
            column.setRemarks(columnsResultSet.getString("REMARKS"));
            column.setColumnDef(columnsResultSet.getString("COLUMN_DEF"));
            column.setOrdinalPosition(columnsResultSet.getInt("ORDINAL_POSITION"));
            column.setIsNullable(columnsResultSet.getString("IS_NULLABLE"));
            column.setIsAutoincrement(columnsResultSet.getString("IS_AUTOINCREMENT"));
            columns.add(column);
        }
        DataSourceUtils.releaseConnection(connection, dataSource);
        return columns;
    }

    @Override
    public String generateInsertSql(String tableName) throws SQLException {
        List<Column> list = this.getColumns(tableName);
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        columns.append(INSERT).append(SPACE).append(tableName.toLowerCase()).append(SPACE).append(LEFT_BRACKET);
        values.append(VALUES).append(SPACE).append(LEFT_BRACKET);
        int count = list.size() - 1;
        for (int i = 0; i <= count; i++) {
            Column column = list.get(i);
            String columnName = column.getColumnName();
            columns.append(columnName);
            values.append(COLON).append(CamelCaseUtils.convertUnderscoreNameToPropertyName(columnName));
            if (i < count) {
                columns.append(COMMA).append(SPACE);
                values.append(COMMA).append(SPACE);
            }
        }
        columns.append(RIGHT_BRACKET).append(SPACE).append(values).append(RIGHT_BRACKET);
        return columns.toString();
    }

    @Override
    public String generateDeleteSql(String tableName) throws SQLException {
        List<String> primaryKeys = this.getPrimaryKeys(tableName);
        StringBuilder sql = new StringBuilder();
        sql.append(DELETE).append(SPACE).append(FROM).append(SPACE).append(tableName.toLowerCase());
        this.appendPrimaryKeys(sql, primaryKeys);
        return sql.toString();
    }

    @Override
    public String generateUpdateSql(String tableName) throws SQLException {
        List<Column> list = this.getColumns(tableName);
        List<String> primaryKeys = this.getPrimaryKeys(tableName);
        StringBuilder sql = new StringBuilder();
        sql.append(UPDATE).append(SPACE).append(tableName.toLowerCase()).append(SPACE);
        sql.append(SET);
        int count = list.size() - 1;
        for (int i = 0; i <= count; i++) {
            Column column = list.get(i);
            String columnName = column.getColumnName();
            if (!primaryKeys.contains(columnName)) {
                sql.append(SPACE).append(columnName).append(SPACE).append(EQUAL).append(SPACE).append(COLON);
                sql.append(CamelCaseUtils.convertUnderscoreNameToPropertyName(columnName));
                if (i < count) {
                    sql.append(COMMA);
                }
            }
        }
        this.appendPrimaryKeys(sql, primaryKeys);
        return sql.toString();
    }

    @Override
    public String generateSelectSql(String tableName) throws SQLException {
        List<String> primaryKeys = this.getPrimaryKeys(tableName);
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(SPACE).append(ASTERISK).append(SPACE);
        sql.append(FROM).append(SPACE).append(tableName.toLowerCase());
        this.appendPrimaryKeys(sql, primaryKeys);
        return sql.toString();
    }

    @Override
    public String generatePropertyName(String tableName) throws SQLException {
        StringBuilder text = new StringBuilder();
        text.append(CamelCaseUtils.convertUnderscoreNameToClassName(tableName)).append(LINE_SEPARATOR);
        List<Column> list = this.getColumns(tableName);
        for (int i = 0; i < list.size(); i++) {
            text.append(PRIVATE_STRING);
            text.append(CamelCaseUtils.convertUnderscoreNameToPropertyName(list.get(i).getColumnName()));
            text.append(SEMICOLON).append(LINE_SEPARATOR);
        }
        return text.toString();
    }

    @Override
    public void generate(String tableName) throws SQLException {
        logger.info(this.generateSelectSql(tableName));
        logger.info(this.generateInsertSql(tableName));
        logger.info(this.generateDeleteSql(tableName));
        logger.info(this.generateUpdateSql(tableName));
        logger.info(this.generatePropertyName(tableName));
    }

    private void appendPrimaryKeys(StringBuilder sql, List<String> primaryKeys) {
        sql.append(SPACE).append(WHERE);
        for (int i = 0; i < primaryKeys.size(); i++) {
            sql.append(SPACE);
            if (i > 0) {
                sql.append(AND).append(SPACE);
            }
            String primaryKey = primaryKeys.get(i);
            sql.append(primaryKey).append(SPACE).append(EQUAL).append(SPACE);
            sql.append(COLON).append(CamelCaseUtils.convertUnderscoreNameToPropertyName(primaryKey));
        }
    }

}
