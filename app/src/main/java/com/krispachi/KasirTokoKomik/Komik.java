/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.krispachi.KasirTokoKomik;

import com.krispachi.KasirTokoKomik.model.KategoriModel;
import com.krispachi.KasirTokoKomik.singleton.KoneksiDatabase;
import com.krispachi.KasirTokoKomik.singleton.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Krisna
 */
public class Komik extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Komik.class.getName());
    DefaultTableModel model;

    /**
     * Creates new form Komik
     */
    public Komik() {
        initComponents();
        
        // Menambahkan placeholder ke inputfield
        txtIsbn.putClientProperty("JTextField.placeholderText", "Masukkan ISBN");
        txtJudul.putClientProperty("JTextField.placeholderText", "Masukkan Judul");
        txtPenulis.putClientProperty("JTextField.placeholderText", "Masukkan Nama Penulis");
        txtPenerbit.putClientProperty("JTextField.placeholderText", "Masukkan Nama Penerbit");
        txtTahunTerbit.putClientProperty("JTextField.placeholderText", "Masukkan Tahun Terbit");
        txtHargaJual.putClientProperty("JTextField.placeholderText", "Masukkan Harga Jual");
        txtHargaBeli.putClientProperty("JTextField.placeholderText", "Masukkan Harga Beli");
        txtStok.putClientProperty("JTextField.placeholderText", "Masukkan Stok");
        txtFilter.putClientProperty("JTextField.placeholderText", "Tampilkan Data Tertentu");
        
        // Inisialisasi kolom tabel
        String[] judulKolom = {"ISBN", "Judul", "Penulis", "Penerbit", "Tahun", "Harga Jual", "Harga Beli", "Stok", "Kategori"};
        model = new DefaultTableModel(judulKolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Mengembalikan false agar semua sel tidak bisa diedit
                return false;
            }
        };
        tabelKomik.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelKomik.setModel(model);
        
        // Inisialisasi fitur filter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tabelKomik.setRowSorter(sorter);
        
        // Panggil fungsi untuk menampilkan data yang sudah ada di DB
        loadDataTabel();

        // Load kategori ke combobox kategori
        loadComboBoxKategori();
    }
    
    private void loadDataTabel() {
        // Menghapus data di model tabel agar tidak duplikat saat dipanggil ulang
        model.setRowCount(0);

        String sql = "SELECT komik.isbn, komik.judul, komik.penulis, komik.penerbit, komik.tahun_terbit, komik.harga_jual, komik.harga_beli, komik.stok, kategori.nama AS nama_kategori FROM komik JOIN kategori ON komik.kategori_id = kategori.id";

        try (Connection conn = KoneksiDatabase.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Ambil data sesuai nama kolom di database Anda
                Object[] row = {
                    rs.getString("isbn"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getDouble("harga_jual"),
                    rs.getDouble("harga_beli"),
                    rs.getInt("stok"),
                    rs.getString("nama_kategori")
                };
                
                // Tambahkan ke tabel
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat data tabel: " + e.getMessage());
        }
    }
    
    private void loadComboBoxKategori() {
        // Mengosongkan isi combobox sebelum diisi
        cbKategori.removeAllItems();
        
        String sql = "SELECT id, nama FROM kategori";

        try (Connection conn = KoneksiDatabase.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Masukkan objek Kategori ke combobox kategori
                cbKategori.addItem(new KategoriModel(
                    rs.getInt("id"), 
                    rs.getString("nama")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat combobox: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Container = new javax.swing.JPanel();
        JudulText = new javax.swing.JLabel();
        TableScrollPane = new javax.swing.JScrollPane();
        tabelKomik = new javax.swing.JTable();
        HargaText = new javax.swing.JLabel();
        DetailText = new javax.swing.JLabel();
        ISBN = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtIsbn = new javax.swing.JTextField();
        Judul = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        Penulis = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtPenulis = new javax.swing.JTextField();
        Penerbit = new javax.swing.JPanel();
        txtPenerbit = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TahunTerbit = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtTahunTerbit = new javax.swing.JTextField();
        HargaJual = new javax.swing.JPanel();
        txtHargaJual = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Stok = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtStok = new javax.swing.JTextField();
        Kategori = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cbKategori = new javax.swing.JComboBox<>();
        HargaBeli = new javax.swing.JPanel();
        txtHargaBeli = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        Tambah = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        KosongkanInput = new javax.swing.JPanel();
        btnKosongkan = new javax.swing.JButton();
        Ubah = new javax.swing.JPanel();
        btnUbah = new javax.swing.JButton();
        Hapus = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        Filter = new javax.swing.JPanel();
        txtFilter = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnFilter = new javax.swing.JButton();
        TerapkanTabelData = new javax.swing.JPanel();
        btnTerapkanTabelData = new javax.swing.JButton();
        Kembali = new javax.swing.JPanel();
        btnKembali = new javax.swing.JButton();
        DetailData = new javax.swing.JPanel();
        btnLihatDetailData = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Halaman Komik");
        setMinimumSize(new java.awt.Dimension(1000, 750));
        setSize(new java.awt.Dimension(1000, 750));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Container.setMinimumSize(new java.awt.Dimension(1000, 750));
        Container.setPreferredSize(new java.awt.Dimension(1000, 750));

        JudulText.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14)); // NOI18N
        JudulText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JudulText.setText("Komik");

        tabelKomik.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        tabelKomik.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ISBN", "Judul", "Penulis", "Penerbit", "Tahun Terbit", "Harga Jual", "Harga Beli", "Stok", "Kategori"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Short.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelKomik.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelKomik.setMinimumSize(new java.awt.Dimension(300, 100));
        TableScrollPane.setViewportView(tabelKomik);

        HargaText.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14)); // NOI18N
        HargaText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HargaText.setText("Harga");

        DetailText.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14)); // NOI18N
        DetailText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DetailText.setText("Detail");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel2.setText("ISBN");

        txtIsbn.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtIsbn.setToolTipText("Masukkan ISBN");
        txtIsbn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIsbnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ISBNLayout = new javax.swing.GroupLayout(ISBN);
        ISBN.setLayout(ISBNLayout);
        ISBNLayout.setHorizontalGroup(
            ISBNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ISBNLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIsbn))
        );
        ISBNLayout.setVerticalGroup(
            ISBNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ISBNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtIsbn, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel3.setText("Judul");

        txtJudul.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtJudul.setToolTipText("Masukkan Judul");

        javax.swing.GroupLayout JudulLayout = new javax.swing.GroupLayout(Judul);
        Judul.setLayout(JudulLayout);
        JudulLayout.setHorizontalGroup(
            JudulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JudulLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJudul))
        );
        JudulLayout.setVerticalGroup(
            JudulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JudulLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtJudul, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel5.setText("Penulis");

        txtPenulis.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtPenulis.setToolTipText("Masukkan Nama Penulis");

        javax.swing.GroupLayout PenulisLayout = new javax.swing.GroupLayout(Penulis);
        Penulis.setLayout(PenulisLayout);
        PenulisLayout.setHorizontalGroup(
            PenulisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PenulisLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPenulis))
        );
        PenulisLayout.setVerticalGroup(
            PenulisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PenulisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtPenulis, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        txtPenerbit.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtPenerbit.setToolTipText("Masukkan Nama Penerbit");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel6.setText("Penerbit");

        javax.swing.GroupLayout PenerbitLayout = new javax.swing.GroupLayout(Penerbit);
        Penerbit.setLayout(PenerbitLayout);
        PenerbitLayout.setHorizontalGroup(
            PenerbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PenerbitLayout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPenerbit))
        );
        PenerbitLayout.setVerticalGroup(
            PenerbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PenerbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtPenerbit)
                .addComponent(jLabel6))
        );

        jLabel7.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel7.setText("Tahun Terbit");

        txtTahunTerbit.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtTahunTerbit.setToolTipText("Masukkan Tahun Terbit");

        javax.swing.GroupLayout TahunTerbitLayout = new javax.swing.GroupLayout(TahunTerbit);
        TahunTerbit.setLayout(TahunTerbitLayout);
        TahunTerbitLayout.setHorizontalGroup(
            TahunTerbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TahunTerbitLayout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        TahunTerbitLayout.setVerticalGroup(
            TahunTerbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TahunTerbitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtTahunTerbit, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        txtHargaJual.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtHargaJual.setToolTipText("Harga Jual");
        txtHargaJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaJualActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Jual");

        javax.swing.GroupLayout HargaJualLayout = new javax.swing.GroupLayout(HargaJual);
        HargaJual.setLayout(HargaJualLayout);
        HargaJualLayout.setHorizontalGroup(
            HargaJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HargaJualLayout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        HargaJualLayout.setVerticalGroup(
            HargaJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HargaJualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtHargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel11.setText("Stok");

        txtStok.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtStok.setToolTipText("Masukkan Stok");

        javax.swing.GroupLayout StokLayout = new javax.swing.GroupLayout(Stok);
        Stok.setLayout(StokLayout);
        StokLayout.setHorizontalGroup(
            StokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StokLayout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        StokLayout.setVerticalGroup(
            StokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StokLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtStok, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        jLabel12.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel12.setText("Kategori");

        cbKategori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        cbKategori.setToolTipText("Pilih Kategori");
        cbKategori.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout KategoriLayout = new javax.swing.GroupLayout(Kategori);
        Kategori.setLayout(KategoriLayout);
        KategoriLayout.setHorizontalGroup(
            KategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(KategoriLayout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        KategoriLayout.setVerticalGroup(
            KategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(KategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(cbKategori, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        txtHargaBeli.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtHargaBeli.setToolTipText("Harga Jual");
        txtHargaBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaBeliActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Beli");

        javax.swing.GroupLayout HargaBeliLayout = new javax.swing.GroupLayout(HargaBeli);
        HargaBeli.setLayout(HargaBeliLayout);
        HargaBeliLayout.setHorizontalGroup(
            HargaBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HargaBeliLayout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
        );
        HargaBeliLayout.setVerticalGroup(
            HargaBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HargaBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        btnTambah.setBackground(new java.awt.Color(153, 255, 153));
        btnTambah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.setToolTipText("Klik Untuk Tambah Data");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TambahLayout = new javax.swing.GroupLayout(Tambah);
        Tambah.setLayout(TambahLayout);
        TambahLayout.setHorizontalGroup(
            TambahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        TambahLayout.setVerticalGroup(
            TambahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnKosongkan.setBackground(new java.awt.Color(153, 255, 255));
        btnKosongkan.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnKosongkan.setText("Kosongkan Input");
        btnKosongkan.setToolTipText("Klik Untuk Mengosongkan Input");
        btnKosongkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKosongkanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout KosongkanInputLayout = new javax.swing.GroupLayout(KosongkanInput);
        KosongkanInput.setLayout(KosongkanInputLayout);
        KosongkanInputLayout.setHorizontalGroup(
            KosongkanInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnKosongkan, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        KosongkanInputLayout.setVerticalGroup(
            KosongkanInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnKosongkan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnUbah.setBackground(new java.awt.Color(255, 204, 102));
        btnUbah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.setToolTipText("Klik Untuk Ubah Data");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UbahLayout = new javax.swing.GroupLayout(Ubah);
        Ubah.setLayout(UbahLayout);
        UbahLayout.setHorizontalGroup(
            UbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        UbahLayout.setVerticalGroup(
            UbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnHapus.setBackground(new java.awt.Color(255, 102, 102));
        btnHapus.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.setToolTipText("Klik Untuk Hapus Data");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HapusLayout = new javax.swing.GroupLayout(Hapus);
        Hapus.setLayout(HapusLayout);
        HapusLayout.setHorizontalGroup(
            HapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        HapusLayout.setVerticalGroup(
            HapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        txtFilter.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        txtFilter.setToolTipText("Masukkan ISBN");

        btnReset.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnFilter.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnFilter.setText("Filter");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FilterLayout = new javax.swing.GroupLayout(Filter);
        Filter.setLayout(FilterLayout);
        FilterLayout.setHorizontalGroup(
            FilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset)
                .addContainerGap())
        );
        FilterLayout.setVerticalGroup(
            FilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset)
                    .addComponent(btnFilter))
                .addContainerGap())
        );

        btnTerapkanTabelData.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnTerapkanTabelData.setText("Terapkan Tabel Data");
        btnTerapkanTabelData.setToolTipText("Klik Untuk Hapus Data");
        btnTerapkanTabelData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerapkanTabelDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TerapkanTabelDataLayout = new javax.swing.GroupLayout(TerapkanTabelData);
        TerapkanTabelData.setLayout(TerapkanTabelDataLayout);
        TerapkanTabelDataLayout.setHorizontalGroup(
            TerapkanTabelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTerapkanTabelData, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
        );
        TerapkanTabelDataLayout.setVerticalGroup(
            TerapkanTabelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTerapkanTabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnKembali.setBackground(new java.awt.Color(255, 51, 102));
        btnKembali.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnKembali.setForeground(new java.awt.Color(255, 255, 255));
        btnKembali.setText("Kembali");
        btnKembali.setToolTipText("Klik Untuk Hapus Data");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout KembaliLayout = new javax.swing.GroupLayout(Kembali);
        Kembali.setLayout(KembaliLayout);
        KembaliLayout.setHorizontalGroup(
            KembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnKembali, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
        );
        KembaliLayout.setVerticalGroup(
            KembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, KembaliLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnLihatDetailData.setBackground(new java.awt.Color(153, 204, 255));
        btnLihatDetailData.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
        btnLihatDetailData.setText("Lihat Detail Data");
        btnLihatDetailData.setToolTipText("Klik Untuk Hapus Data");
        btnLihatDetailData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatDetailDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DetailDataLayout = new javax.swing.GroupLayout(DetailData);
        DetailData.setLayout(DetailDataLayout);
        DetailDataLayout.setHorizontalGroup(
            DetailDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLihatDetailData, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
        );
        DetailDataLayout.setVerticalGroup(
            DetailDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLihatDetailData, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout ContainerLayout = new javax.swing.GroupLayout(Container);
        Container.setLayout(ContainerLayout);
        ContainerLayout.setHorizontalGroup(
            ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContainerLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(JudulText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DetailText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Judul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Penulis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Penerbit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TahunTerbit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Stok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Kategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(ContainerLayout.createSequentialGroup()
                            .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Ubah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Tambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(KosongkanInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(ContainerLayout.createSequentialGroup()
                            .addComponent(HargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ISBN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(HargaText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(TerapkanTabelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Kembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DetailData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                    .addGroup(ContainerLayout.createSequentialGroup()
                        .addComponent(Filter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        ContainerLayout.setVerticalGroup(
            ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContainerLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(JudulText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Judul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Penulis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(HargaText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(HargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DetailText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(KosongkanInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Ubah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(TerapkanTabelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(DetailData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(Kembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(ContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableScrollPane))
        );

        getContentPane().add(Container, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtHargaJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaJualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaJualActionPerformed

    private void txtHargaBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaBeliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaBeliActionPerformed

    private void txtIsbnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIsbnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIsbnActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // Ambil data dari Input
        String isbn = txtIsbn.getText();
        String judul = txtJudul.getText();
        String penulis = txtPenulis.getText();
        String penerbit = txtPenerbit.getText();
        String tahunTerbit = txtTahunTerbit.getText();
        String hargaJual = txtHargaJual.getText();
        String hargaBeli = txtHargaBeli.getText();
        String stok = txtStok.getText();
        KategoriModel kategori = (KategoriModel) cbKategori.getSelectedItem();
        
        // PROSES VALIDASI
        // Validasi Field Kosong
        if (isbn.isEmpty() || judul.isEmpty() || penulis.isEmpty() || 
            penerbit.isEmpty() || tahunTerbit.isEmpty() || 
            hargaJual.isEmpty() || hargaBeli.isEmpty() || stok.isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return; // Program berhenti di sini
        }

        // Validasi ComboBox (Pastikan sudah pilih kategori)
        if (kategori == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silahkan pilih kategori terlebih dahulu!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi Format Angka (Tahun, Harga, Stok)
        try {
            Integer.parseInt(tahunTerbit);
            Double.parseDouble(hargaJual);
            Double.parseDouble(hargaBeli);
            Integer.parseInt(stok);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Tahun, Harga, dan Stok harus berupa angka!", "Error Format", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Logika Insert ke Database
        String sql = "INSERT INTO komik (isbn, judul, penulis, penerbit, tahun_terbit, harga_jual, harga_beli, stok, kategori_id) VALUES (?,?,?,?,?,?,?,?,?)";

        // Cek try koneksi database dan prepare statement
        try (Connection conn = KoneksiDatabase.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Bind nilai ke PreparedStatement
            ps.setString(1, isbn);
            ps.setString(2, judul);
            ps.setString(3, penulis);
            ps.setString(4, penerbit);
            ps.setInt(5, Integer.parseInt(tahunTerbit));
            ps.setDouble(6, Double.parseDouble(hargaJual));
            ps.setDouble(7, Double.parseDouble(hargaBeli));
            ps.setInt(8, Integer.parseInt(stok));
            ps.setInt(9, kategori.getId());

            // Eksekusi ke MariaDB
            ps.executeUpdate();

            // Update ke JTable
            model.addRow(new Object[] {isbn, judul, penulis, penerbit, tahunTerbit, hargaJual, hargaBeli, stok, kategori});

            // Bersihkan form
            resetForm();
            JOptionPane.showMessageDialog(this, "Data Berhasil Ditambah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun, Harga, dan Stok harus angka!");
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        // Cek session dan role
        if(Session.getInstance() != null
            && (Session.getInstance().getRole().equalsIgnoreCase("kasir")
            || Session.getInstance().getRole().equalsIgnoreCase("admin"))) {
            
            MenuUtama frame = new MenuUtama();
            frame.setVisible(true);
            this.dispose();
        } else {
            Login frame = new Login();
            frame.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        txtIsbn.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    private void btnTerapkanTabelDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerapkanTabelDataActionPerformed
        // Cek apakah ada baris yang dipilih
        int row = tabelKomik.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data di tabel terlebih dahulu!");
            return;
        }
        
        // Ambil data dari tabel berdasarkan kolom (index dimulai dari 0)
        // model.getValueAt(baris, kolom)
        String isbn = model.getValueAt(row, 0).toString();
        String judul = model.getValueAt(row, 1).toString();
        String penulis = model.getValueAt(row, 2).toString();
        String penerbit = model.getValueAt(row, 3).toString();
        String tahun = model.getValueAt(row, 4).toString();
        String hJual = model.getValueAt(row, 5).toString();
        String hBeli = model.getValueAt(row, 6).toString();
        String stok = model.getValueAt(row, 7).toString();
        String kategori = model.getValueAt(row, 8).toString();
        
        // Set data ke Input Field
        txtIsbn.setText(isbn);
        txtJudul.setText(judul);
        txtPenulis.setText(penulis);
        txtPenerbit.setText(penerbit);
        txtTahunTerbit.setText(tahun);
        txtHargaJual.setText(hJual);
        txtHargaBeli.setText(hBeli);
        txtStok.setText(stok);
        
        // Set JComboBox berdasarkan teks kategori
        // Kita cari item di ComboBox yang namanya sama dengan kategori
        for (int i = 0; i < cbKategori.getItemCount(); i++) {
            Object item = cbKategori.getItemAt(i);
            if (item.toString().equalsIgnoreCase(kategori)) {
                cbKategori.setSelectedIndex(i);
                break;
            }
        }
    }//GEN-LAST:event_btnTerapkanTabelDataActionPerformed

    private void btnLihatDetailDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatDetailDataActionPerformed
        // Cek apakah ada baris yang dipilih
        int baris = tabelKomik.getSelectedRow();
        
        if (baris != -1) {
            // Ambil data dari kolom (indeks kolom mulai dari 0)
            String isbn = tabelKomik.getValueAt(baris, 0).toString();
            String judul = tabelKomik.getValueAt(baris, 1).toString();
            String penulis = tabelKomik.getValueAt(baris, 2).toString();
            String penerbit = tabelKomik.getValueAt(baris, 3).toString();
            String tahunTerbit = tabelKomik.getValueAt(baris, 4).toString();
            String hargaJual = tabelKomik.getValueAt(baris, 5).toString();
            String hargaBeli = tabelKomik.getValueAt(baris, 6).toString();
            String stok = tabelKomik.getValueAt(baris, 7).toString();
            String kategori = tabelKomik.getValueAt(baris, 8).toString();

            // Susun pesan detail
            String pesan = "Detail Data Komik:\n"
                         + "----------------------\n"
                         + "ISBN : " + isbn + "\n"
                         + "Judul : " + judul + "\n"
                         + "Penulis : " + penulis + "\n"
                         + "Penerbit : " + penerbit + "\n"
                         + "Tahun Terbit : " + tahunTerbit + "\n"
                         + "Harga Jual : " + hargaJual + "\n"
                         + "Harga Beli : " + hargaBeli + "\n"
                         + "Stok : " + stok + "\n"
                         + "Kategori : " + kategori;

            // Tampilkan dalam Panel (JOptionPane)
            javax.swing.JOptionPane.showMessageDialog(this, pesan, "Detail Data", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Jika tidak ada baris yang dipilih
            javax.swing.JOptionPane.showMessageDialog(this, "Silahkan pilih baris di tabel terlebih dahulu!");
        }
    }//GEN-LAST:event_btnLihatDetailDataActionPerformed

    private void btnKosongkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKosongkanActionPerformed
        resetForm();
    }//GEN-LAST:event_btnKosongkanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // Ambil baris yang dipilih
        int baris = tabelKomik.getSelectedRow();

        if (baris != -1) {
            // Ambil ISBN dari kolom 0
            String isbn = tabelKomik.getValueAt(baris, 0).toString();
            String judul = tabelKomik.getValueAt(baris, 1).toString(); // Judul untuk konfirmasi

            // Tampilkan konfirmasi
            int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin menghapus komik: " + judul + "?", 
                    "Konfirmasi Hapus", 
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
                try (Connection conn = KoneksiDatabase.getConnection()) {
                    String sql = "DELETE FROM komik WHERE isbn = ?";
                    java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, isbn);

                    int hasil = ps.executeUpdate();
                    if (hasil > 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                        loadDataTabel(); // Refresh tabel
                    }
                } catch (java.sql.SQLException e) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu!");
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // Ambil data dari Input
        String isbn = txtIsbn.getText();
        String judul = txtJudul.getText();
        String penulis = txtPenulis.getText();
        String penerbit = txtPenerbit.getText();
        String tahunTerbit = txtTahunTerbit.getText();
        String hargaJual = txtHargaJual.getText();
        String hargaBeli = txtHargaBeli.getText();
        String stok = txtStok.getText();
        KategoriModel kategori = (KategoriModel) cbKategori.getSelectedItem();
        
        // PROSES VALIDASI
        // Validasi Field Kosong
        if (isbn.isEmpty() || judul.isEmpty() || penulis.isEmpty() || 
            penerbit.isEmpty() || tahunTerbit.isEmpty() || 
            hargaJual.isEmpty() || hargaBeli.isEmpty() || stok.isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return; // Program berhenti di sini
        }

        // Validasi ComboBox (Pastikan sudah pilih kategori)
        if (kategori == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silahkan pilih kategori terlebih dahulu!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi Format Angka (Tahun, Harga, Stok)
        try {
            Integer.parseInt(tahunTerbit);
            Double.parseDouble(hargaJual);
            Double.parseDouble(hargaBeli);
            Integer.parseInt(stok);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Tahun, Harga, dan Stok harus berupa angka!", "Error Format", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tampilkan konfirmasi
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin mengubah komik: " + judul + "?", 
                "Konfirmasi Ubah", 
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.NO_OPTION) {
            return;
        }

        // Proses Update ke Database
        String sql = "UPDATE komik SET judul=?, penulis=?, penerbit=?, tahun_terbit=?, "
                   + "harga_jual=?, harga_beli=?, stok=?, kategori_id=? WHERE isbn=?";

        try (Connection conn = KoneksiDatabase.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, judul);
            ps.setString(2, penulis);
            ps.setString(3, penerbit);
            ps.setInt(4, Integer.parseInt(txtTahunTerbit.getText()));
            ps.setDouble(5, Double.parseDouble(txtHargaJual.getText()));
            ps.setDouble(6, Double.parseDouble(txtHargaBeli.getText()));
            ps.setInt(7, Integer.parseInt(txtStok.getText()));
            ps.setInt(8, kategori.getId());
            ps.setString(9, isbn);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Data Berhasil Diperbarui!");
                loadDataTabel(); // Refresh tabel
                resetForm();
            }
        } catch (SQLException | NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal Update: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        String text = txtFilter.getText();

        // Mendapatkan sorter dari tabel
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tabelKomik.getRowSorter();

        if (text.trim().length() == 0) {
            // Jika kolom filter kosong, tampilkan semua data
            sorter.setRowFilter(null);
        } else {
            // Filter berdasarkan teks (case-insensitive)
            // (?i) membuat pencarian tidak peduli huruf besar atau kecil
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtFilter.setText("");
        // Mendapatkan sorter dari tabel
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tabelKomik.getRowSorter();
        sorter.setRowFilter(null);
    }//GEN-LAST:event_btnResetActionPerformed

    // Reset form
    private void resetForm() {
        txtIsbn.setText("");
        txtJudul.setText("");
        txtPenulis.setText("");
        txtPenerbit.setText("");
        txtTahunTerbit.setText("");
        txtHargaJual.setText("");
        txtHargaBeli.setText("");
        txtStok.setText("");
        cbKategori.setSelectedIndex(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Komik().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Container;
    private javax.swing.JPanel DetailData;
    private javax.swing.JLabel DetailText;
    private javax.swing.JPanel Filter;
    private javax.swing.JPanel Hapus;
    private javax.swing.JPanel HargaBeli;
    private javax.swing.JPanel HargaJual;
    private javax.swing.JLabel HargaText;
    private javax.swing.JPanel ISBN;
    private javax.swing.JPanel Judul;
    private javax.swing.JLabel JudulText;
    private javax.swing.JPanel Kategori;
    private javax.swing.JPanel Kembali;
    private javax.swing.JPanel KosongkanInput;
    private javax.swing.JPanel Penerbit;
    private javax.swing.JPanel Penulis;
    private javax.swing.JPanel Stok;
    private javax.swing.JScrollPane TableScrollPane;
    private javax.swing.JPanel TahunTerbit;
    private javax.swing.JPanel Tambah;
    private javax.swing.JPanel TerapkanTabelData;
    private javax.swing.JPanel Ubah;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnKosongkan;
    private javax.swing.JButton btnLihatDetailData;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTerapkanTabelData;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<KategoriModel> cbKategori;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTable tabelKomik;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JTextField txtHargaBeli;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtIsbn;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtPenerbit;
    private javax.swing.JTextField txtPenulis;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextField txtTahunTerbit;
    // End of variables declaration//GEN-END:variables
}
