Oblatum
=======

> spring jdbc wrapper

Features
========

* MappingJdbcTemplate help you nosql to do simple database operations.
* SqlMap help you store procedures or sql statements using a xml descriptor.
* SqlGenerator hep you automatically generate sql by a table.

Maven dependency
================

```xml
<dependency>
    <groupId>org.mynah</groupId>
    <artifactId>oblatum</artifactId>
    <version>${version.oblatum}</version>
</dependency>
```

Quick start
===========

> MappingJdbcTemplate help you nosql to do simple database operations.

```java
<T> T queryForObject(T t, Class<T> type) throws DataAccessException;

<T> List<T> query(T t, Class<T> type) throws DataAccessException;

<T> int update(T where, T t) throws DataAccessException;

<T> int delete(T t) throws DataAccessException;

<T> int insert(T t) throws DataAccessException;
```

> SqlMap help you store procedures or sql statements using a xml descriptor.

```xml
<bean id="sqlMap" class="org.mynah.oblatum.sql.SqlMap">
    <property name="locationPattern" value="classpath:*.*sql.xml"/>
</bean>
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<sqlMap namespace="test">
    <sql id="select">
        <![CDATA[select id,name from test]]>
    </sql>
</sqlMap>
```
```java
String get(String key);
```

> SqlGenerator hep you automatically generate sql by a table.

```java
public interface SqlOperations {

    String generateSelectSql(String tableName) throws SQLException;

    String generateInsertSql(String tableName) throws SQLException;

    String generateDeleteSql(String tableName) throws SQLException;

    String generateUpdateSql(String tableName) throws SQLException;

    String generatePropertyName(String tableName) throws SQLException;

}
```
License
=======

> Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

> Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
