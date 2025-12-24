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
    private static Session instance;
    private int userId;
    private String username;
    private String nama_lengkap;
    private String role;
    
    // Private constructor agar tidak bisa di-instantiate
    private Session() {}
    
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUser(int id, String user, String nama, String r){
        userId = id;
        username = user;
        nama_lengkap = nama;
        role = r;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
    
    public String getNamaLengkap() {
        return nama_lengkap;
    }

    public String getRole() {
        return role;
    }

    public void clear(){
        userId = 0;
        username = null;
        nama_lengkap = null;
        role = null;
    }
}
