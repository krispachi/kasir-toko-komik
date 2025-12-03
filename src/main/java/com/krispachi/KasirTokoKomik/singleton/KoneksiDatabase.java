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
    private static Connection connection;

    private static final String URL  = "jdbc:mariadb://localhost:3306/";
    private static final String DBNAME = "kasir_toko_komik";
    private static final String USER = "root";
    private static final String PASS = "";

    private KoneksiDatabase() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL + DBNAME + "?user=" + USER + "&password=" + PASS);
                System.out.println("[KoneksiDatabase] Berhasil terhubung ke database.");
            } catch (SQLException e) {
                System.err.println("");
            }
        }
        return connection;
    }
}
