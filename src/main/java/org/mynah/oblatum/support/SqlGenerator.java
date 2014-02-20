package org.mynah.oblatum.support;

import javax.sql.DataSource;
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
            primaryKeys.add(primaryKeysResultSet.getString("COLUMN_NAME"));
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

    public String generateSelectSql(String tableName) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(SPACE);
        return null;
    }

    public String generateSql(String tableName) throws SQLException {

        return null;
    }


    public String generateModel(String tableName) {

        return tableName;
    }

}
