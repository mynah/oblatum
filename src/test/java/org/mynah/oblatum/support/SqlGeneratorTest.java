package org.mynah.oblatum.support;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context.xml")
public class SqlGeneratorTest {

    public static final String TABLE_NAME_TEST = "test";

    @Autowired
    private SqlGenerator sqlGenerator;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testConstruct() throws Exception {
        SqlGenerator sqlGenerator = new SqlGenerator();
        assertNull(sqlGenerator.getDataSource());
        SqlGenerator nullSqlGenerator = new SqlGenerator(null);
        assertNull(nullSqlGenerator.getDataSource());
    }

    @Test
    public void testGenerateInsertSql() throws Exception {
        String sql = sqlGenerator.generateInsertSql(TABLE_NAME_TEST);
        System.out.println(sql);
    }

    @Test
    public void testGenerateSql() throws Exception {
        assertThat(10, is(10));
        sqlGenerator.generateSql(TABLE_NAME_TEST);
    }

    @Test
    public void testGenerateModel() throws Exception {
        String tableName = "TEST";
        sqlGenerator.generateModel(tableName);
    }


} 
