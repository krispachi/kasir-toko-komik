/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krispachi.KasirTokoKomik.model;

/**
 *
 * @author Krisna
 */
public class KategoriModel {
    private int id;
    private String nama;

    public KategoriModel(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }

    // Saat Kategori.toString() dijalankan, akan return nama
    @Override
    public String toString() {
        return nama; 
    }
}
