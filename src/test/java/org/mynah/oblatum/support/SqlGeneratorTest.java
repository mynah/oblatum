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
    public void testGenerateSql() throws Exception {
        assertThat(10, is(10));
        String tableName = "test_user";
        sqlGenerator.generateSql(tableName);
    }

    @Test
    public void testGenerateModel() throws Exception {
        String tableName = "TEST";
        sqlGenerator.generateModel(tableName);
    }


} 
