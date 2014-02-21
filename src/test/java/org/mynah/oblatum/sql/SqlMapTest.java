package org.mynah.oblatum.sql;

import org.junit.Test;

public class SqlMapTest {

    @Test
    public void testGet() throws Exception {
        SqlMap sqlMap = new SqlMap();
        System.out.println("|" + sqlMap.get("test.select") + "|");
        SqlMap nosqlMap = new SqlMap("classpath:*nosql.xml");
        System.out.println("|" + nosqlMap.get("test.select") + "|");
    }

} 
