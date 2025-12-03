/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krispachi.KasirTokoKomik.singleton;

/**
 *
 * @author Krisna
 */
public class Session {
    private static int userId;
    private static String username;
    private static String nama_lengkap;
    private static String role;

    public static void setUser(int id, String user, String nama, String r){
        userId = id;
        username = user;
        nama_lengkap = nama;
        role = r;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUsername() {
        return username;
    }
    
    public static String getNamaLengkap() {
        return nama_lengkap;
    }

    public static String getRole() {
        return role;
    }

    public static void clear(){
        userId = 0;
        username = null;
        nama_lengkap = null;
        role = null;
    }
}
