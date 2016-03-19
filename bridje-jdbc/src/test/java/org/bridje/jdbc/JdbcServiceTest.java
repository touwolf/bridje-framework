/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class JdbcServiceTest
{
    private static final Logger LOG = Logger.getLogger(JdbcServiceTest.class.getName());

    private static final String DS_NAME = "H2TestDataSource";

    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getDataSource method, of class JdbcService.
     */
    @Test
    public void testGetDataSource() throws SQLException, InterruptedException
    {
        System.out.println("getDataSource");
        JdbcService instance = Ioc.context().find(JdbcService.class);
        DataSource result = instance.getDataSource(DS_NAME);
        assertNotNull(result);
        Connection connection = result.getConnection();
        assertNotNull(connection);
        connection.close();
        Connection otherConnection = result.getConnection();
        assertNotNull(otherConnection);
        //Must be the same connection, only one thread running here
        assertEquals("Must be the same connection", connection, otherConnection);
        otherConnection.close();
       
        createTable();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(() -> runSomeQuerys(1, 10) );
        executor.submit(() -> runSomeQuerys(11, 20) );
        executor.submit(() -> runSomeQuerys(21, 40) );
        executor.submit(() -> runSomeQuerys(41, 60) );
        executor.submit(() -> runSomeQuerys(61, 70) );
        Thread.sleep(500);
        executor.submit(() -> runSomeQuerys(71, 75) );
        executor.submit(() -> runSomeQuerys(76, 85) );
        executor.submit(() -> runSomeQuerys(86, 100) );
        executor.submit(() -> runSomeQuerys(101, 120) );
        executor.submit(() -> runSomeQuerys(121, 130) );
        executor.submit(() -> runSomeQuerys(131, 150) );
        executor.submit(() -> runSomeQuerys(151, 130) );
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);
        selectAll();
        //Close everything
        instance.closeAllDataSource();
    }

    private void selectAll()
    {
        JdbcService jdbcServ = Ioc.context().find(JdbcService.class);
        DataSource dataSource = jdbcServ.getDataSource(DS_NAME);
        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement())
        {
            String sql = "SELECT * FROM my_table;";
            LOG.log(Level.INFO, "{0}{1}", new Object[]{sql, connection.toString()});
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next())
            {
                LOG.log(Level.INFO, "Data: {0} {1}", new Object[]{ resultSet.getLong(1), resultSet.getString(2) });
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    private void createTable()
    {
        JdbcService jdbcServ = Ioc.context().find(JdbcService.class);
        DataSource dataSource = jdbcServ.getDataSource(DS_NAME);
        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement())
        {
            String sql = "CREATE TABLE my_table (id INTEGER PRIMARY KEY NOT NULL, name VARCHAR(50) NOT NULL);";
            LOG.log(Level.INFO, "{0}{1}", new Object[]{sql, connection.toString()});
            stmt.execute(sql);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    private void runSomeQuerys(int minId, int maxId)
    {
        JdbcService jdbcServ = Ioc.context().find(JdbcService.class);
        DataSource dataSource = jdbcServ.getDataSource(DS_NAME);
        try (Connection connection = dataSource.getConnection())
        {
            for(int i = minId; i <= maxId; i++)
            {
                try (Statement stmt = connection.createStatement())
                {
                    String sql = "INSERT INTO my_table (id, name) VALUES (" + i + ", \'" + UUID.randomUUID().toString() + "\');";
                    LOG.log(Level.INFO, "{0}{1}", new Object[]{sql, connection.toString()});
                    stmt.execute(sql);
                }
            }
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
