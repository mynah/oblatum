package org.mynah.oblatum.jdbc;

import org.springframework.dao.DataAccessException;
import java.util.List;

public interface MappingJdbcOperations {

    <T> T queryForObject(T t, Class<T> type) throws DataAccessException;

    <T> List<T> query(T t, Class<T> type) throws DataAccessException;

    <T> int update(T where, T t) throws DataAccessException;

    <T> int delete(T t) throws DataAccessException;

    <T> int insert(T t) throws DataAccessException;
}
