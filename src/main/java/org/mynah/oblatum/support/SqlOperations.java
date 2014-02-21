package org.mynah.oblatum.support;

import java.sql.SQLException;

public interface SqlOperations {

    String generateSelectSql(String tableName) throws SQLException;

    String generateInsertSql(String tableName) throws SQLException;

    String generateDeleteSql(String tableName) throws SQLException;

    String generateUpdateSql(String tableName) throws SQLException;

    String generatePropertyName(String tableName) throws SQLException;

}
