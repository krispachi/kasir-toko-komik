/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krispachi.KasirTokoKomik.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Krisna
 */
public class KoneksiDatabase {
    private static final String URL  = "jdbc:mariadb://localhost:3306/";
    private static final String DBNAME = "kasir_toko_komik";
    private static final String USER = "root";
    private static final String PASS = "";

    // Private constructor agar tidak bisa di-instantiate
    private KoneksiDatabase() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + DBNAME + "?user=" + USER + "&password=" + PASS);
    }
}
