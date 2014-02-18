package org.mynah.oblatum.support;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlGenerator {

    private Connection connection;

    private SqlGenerator() {
    }

    public SqlGenerator(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public SqlGenerator(String driverClassName, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        this.connection = DriverManager.getConnection(url, username, password);
    }

    public String generateSql(String tableName) {

        return null;
    }


    public String generateModel(String tableName) {

        return null;
    }

}
