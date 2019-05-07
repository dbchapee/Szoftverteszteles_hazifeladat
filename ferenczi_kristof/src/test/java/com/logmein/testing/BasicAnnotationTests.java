package com.logmein.testing;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicAnnotationTests {
    private static String theTestItem;

    // Run once, e.g. Database connection, connection pool
    @BeforeClass
    public static void runOnceBeforeClass() {
        theTestItem = "item";
        System.out.println("@BeforeClass - initializing DB connection");
    }

    // Run once, e.g close connection, cleanup
    @AfterClass
    public static void runOnceAfterClass() {
        System.out.println("@AfterClass - closing DB connection");
    }

    // Creating a similar object and share for all @Test
    @Before
    public void runBeforeTestMethod() {
        System.out.println("@Before - creating a new test table with super exciting data");
    }

    @After
    public void runAfterTestMethod() {
        System.out.println("@After - deleting the test table");
    }

    @Test
    public void test_method_1() {
        System.out.println("@Test - test_method_1: querying 1 item from the DB: " + theTestItem);
    }

    @Test
    public void test_method_2() {
        System.out.println("@Test - test_method_2: deleting 1 item from the DB: " + theTestItem);
    }

}
