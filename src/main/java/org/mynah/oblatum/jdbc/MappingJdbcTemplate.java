package org.mynah.oblatum.jdbc;

import java.beans.PropertyDescriptor;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mynah.oblatum.util.CamelCaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import static org.mynah.oblatum.util.Constants.*;

public class MappingJdbcTemplate implements MappingJdbcOperations {

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private MappingJdbcTemplate() {
    }

    public MappingJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public <T> T queryForObject(T t, Class<T> type) throws DataAccessException {
        return namedJdbcTemplate.queryForObject(queryJoin(t), new BeanPropertySqlParameterSource(t), new BeanPropertyRowMapper<T>(type));
    }

    public <T> List<T> query(T t, Class<T> type) throws DataAccessException {
        return namedJdbcTemplate.query(queryJoin(t), new BeanPropertySqlParameterSource(t), new BeanPropertyRowMapper<T>(type));
    }

    private <T> String queryJoin(T t) {
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT).append(SPACE).append(ASTERISK).append(SPACE);
        sql.append(FROM).append(SPACE).append(t.getClass().getSimpleName().toLowerCase()).append(SPACE);
        sql.append(WHERE).append(SPACE).append(IDENTICAL).append(SPACE);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String name = pd.getName();
            if (!CLASS.equals(name) && beanWrapper.isReadableProperty(name) && beanWrapper.getPropertyValue(name) != null) {
                sql.append(AND).append(SPACE).append(CamelCaseUtils.underscoreName(name)).append(SPACE);
                sql.append(EQUAL).append(SPACE).append(COLON).append(name).append(SPACE);
            }
        }
        return sql.toString();
    }

    private <T> void parameterJoin(T t, StringBuffer sql) {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String name = pd.getName();
            if (!CLASS.equals(name) && beanWrapper.isReadableProperty(name) && beanWrapper.getPropertyValue(name) != null) {
                sql.append(AND).append(SPACE).append(CamelCaseUtils.underscoreName(name)).append(SPACE);
                sql.append(EQUAL).append(SPACE).append(COLON).append(name).append(SPACE);
            }
        }
    }

    public <T> int update(T where, T t) throws DataAccessException {
        StringBuilder top = new StringBuilder();
        top.append(UPDATE).append(SPACE).append(where.getClass().getSimpleName().toLowerCase()).append(SPACE);
        top.append(SET).append(SPACE);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
        int count = 0;
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String name = pd.getName();
            if (!CLASS.equals(name) && beanWrapper.isReadableProperty(name) && beanWrapper.getPropertyValue(name) != null) {
                top.append(CamelCaseUtils.underscoreName(name)).append(SPACE);
                top.append(EQUAL).append(SPACE).append(COLON).append(name).append(SPACE);
                top.append(COMMA).append(SPACE);
                count++;
            }
        }
        StringBuffer sql = new StringBuffer();
        if (count <= 0) {
            logger.error("no values for update");
            return 0;
        } else {
            sql.append(top.toString().substring(0, top.lastIndexOf(COMMA)));
        }
        sql.append(WHERE).append(SPACE).append(IDENTICAL).append(SPACE);
        parameterJoin(where, sql);
        return namedJdbcTemplate.update(sql.toString(), new BeanPropertySqlParameterSource(t));
    }

    public <T> int delete(T t) throws DataAccessException {
        StringBuilder sql = new StringBuilder();
        sql.append(DELETE).append(SPACE).append(FROM).append(SPACE).append(t.getClass().getSimpleName().toLowerCase()).append(SPACE);
        sql.append(WHERE).append(SPACE).append(IDENTICAL).append(SPACE);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String name = pd.getName();
            if (!CLASS.equals(name) && beanWrapper.isReadableProperty(name) && beanWrapper.getPropertyValue(name) != null) {
                sql.append(AND).append(SPACE).append(CamelCaseUtils.underscoreName(name)).append(SPACE);
                sql.append(EQUAL).append(SPACE).append(COLON).append(name).append(SPACE);
            }
        }
        return namedJdbcTemplate.update(sql.toString(), new BeanPropertySqlParameterSource(t));
    }

    public <T> int insert(T t) throws DataAccessException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        columns.append(INSERT).append(SPACE).append(t.getClass().getSimpleName().toLowerCase()).append(SPACE).append(LEFT_BRACKET);
        values.append(VALUES).append(LEFT_BRACKET);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
        int count = 0;
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String name = pd.getName();
            if (!CLASS.equals(name) && beanWrapper.isReadableProperty(name) && beanWrapper.getPropertyValue(name) != null) {
                columns.append(CamelCaseUtils.underscoreName(name)).append(COMMA).append(SPACE);
                values.append(COLON).append(name).append(COMMA).append(SPACE);
                count++;
            }
        }
        StringBuilder sql = new StringBuilder();
        if (count <= 0) {
            logger.error("no values for insert");
            return 0;
        } else {
            sql.append(columns.toString().substring(0, columns.lastIndexOf(COMMA))).append(RIGHT_BRACKET);
            sql.append(values.toString().substring(0, values.lastIndexOf(COMMA))).append(RIGHT_BRACKET);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.namedJdbcTemplate.update(sql.toString(), new BeanPropertySqlParameterSource(t), keyHolder);
        return keyHolder.getKey().intValue();
    }

}
