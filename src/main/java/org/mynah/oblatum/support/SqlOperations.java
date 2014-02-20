package org.mynah.oblatum.support;

import java.sql.SQLException;

public interface SqlOperations {

    String generateSql(String tableName) throws SQLException;

    String generateSelectSql(String tableName) throws SQLException;
    
}
