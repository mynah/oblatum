package org.mynah.oblatum.support;

import javax.sql.DataSource;
import java.sql.*;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class SqlGenerator {

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

    public String generateSql(String tableName) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
        while (columns.next()) {
            System.out.println(columns.getString("COLUMN_NAME"));
        }
        DataSourceUtils.releaseConnection(connection, dataSource);
        return null;
    }


    public String generateModel(String tableName) {

        return tableName;
    }

}
