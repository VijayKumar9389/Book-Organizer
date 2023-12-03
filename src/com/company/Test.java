package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/test";

    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);

    }



    }
