package org.mynah.oblatum.support;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
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
    public void testGenerateDeleteSql() throws Exception {
        String sql = sqlGenerator.generateDeleteSql(TABLE_NAME_TEST);
        System.out.println(sql);
    }


    @Test
    public void testGenerateUpdateSql() throws Exception {
        String sql = sqlGenerator.generateUpdateSql(TABLE_NAME_TEST);
        System.out.println(sql);
    }

    @Test
    public void testGenerateSelectSql() throws Exception {
        String sql = sqlGenerator.generateSelectSql(TABLE_NAME_TEST);
        System.out.println(sql);
    }

    @Test
    public void testGenerateModel() throws Exception {
        String tableName = "TEST";
        String text = sqlGenerator.generateModel(tableName);
        System.out.println(text);
    }


} 
