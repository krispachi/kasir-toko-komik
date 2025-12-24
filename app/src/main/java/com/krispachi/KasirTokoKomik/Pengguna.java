/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.krispachi.KasirTokoKomik;

import com.krispachi.KasirTokoKomik.singleton.KoneksiDatabase;
import com.krispachi.KasirTokoKomik.singleton.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Krisna
 */
public class Pengguna extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Pengguna.class.getName());
    private DefaultTableModel model;
    private int selectedId = -1;

    /**
     * Creates new form Pengguna
     */
    public Pengguna() {
        initComponents();

        // Menambahkan placeholder ke inputfield
        txtUsername.putClientProperty("JTextField.placeholderText", "Masukkan Username");
        txtPassword.putClientProperty("JTextField.placeholderText", "Masukkan Password");
        txtNamaLengkap.putClientProperty("JTextField.placeholderText", "Masukkan Nama Lengkap");
        txtFilter.putClientProperty("JTextField.placeholderText", "Cari berdasarkan username/nama");

        // Inisialisasi kolom tabel
        String[] judulKolom = { "ID", "Username", "Nama Lengkap", "Role" };
        model = new DefaultTableModel(judulKolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelPengguna.setModel(model);

        // Panggil fungsi untuk menampilkan data yang sudah ada di DB
        loadDataTabel();
    }

    private void loadDataTabel() {
        // Menghapus data di model tabel agar tidak duplikat saat dipanggil ulang
        model.setRowCount(0);

        String sql = "SELECT id, username, nama_lengkap, role FROM pengguna";

        try (Connection conn = KoneksiDatabase.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nama_lengkap"),
                        rs.getString("role")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat data tabel: " + e.getMessage());
        }
    }

    private void loadDataTabelFiltered(String keyword) {
        model.setRowCount(0);

        String sql = "SELECT id, username, nama_lengkap, role FROM pengguna WHERE username LIKE ? OR nama_lengkap LIKE ?";

        try (Connection conn = KoneksiDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nama_lengkap"),
                        rs.getString("role")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat data tabel: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtNamaLengkap.setText("");
        cbRole.setSelectedIndex(0);
        selectedId = -1;
        txtUsername.requestFocus();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        Container = new javax.swing.JPanel();
        JudulText = new javax.swing.JLabel();
        TableScrollPane = new javax.swing.JScrollPane();
        tabelPengguna = new javax.swing.JTable();

        // Panel Form
        panelUsername = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();

        panelPassword = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();

        panelNamaLengkap = new javax.swing.JPanel();
        lblNamaLengkap = new javax.swing.JLabel();
        txtNamaLengkap = new javax.swing.JTextField();

        panelRole = new javax.swing.JPanel();
        lblRole = new javax.swing.JLabel();
        cbRole = new javax.swing.JComboBox<>();

        // Panel Tombol
        panelTambah = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();

        panelKosongkan = new javax.swing.JPanel();
        btnKosongkan = new javax.swing.JButton();

        panelUbah = new javax.swing.JPanel();
        btnUbah = new javax.swing.JButton();

        panelHapus = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();

        // Panel Filter
        panelFilter = new javax.swing.JPanel();
        txtFilter = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        // Panel Kembali
        panelKembali = new javax.swing.JPanel();
        btnKembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Halaman Pengguna");
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setPreferredSize(new java.awt.Dimension(1000, 700));
        setSize(new java.awt.Dimension(1000, 700));

        Container.setMinimumSize(new java.awt.Dimension(1000, 700));
        Container.setPreferredSize(new java.awt.Dimension(1000, 700));

        JudulText.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14));
        JudulText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JudulText.setText("Kelola Akun Pengguna");

        // Tabel
        tabelPengguna.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        tabelPengguna.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Username", "Nama Lengkap", "Role" }));
        tabelPengguna.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelPengguna.setMinimumSize(new java.awt.Dimension(300, 100));
        tabelPengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPenggunaMouseClicked(evt);
            }
        });
        TableScrollPane.setViewportView(tabelPengguna);

        // Username Field
        lblUsername.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        lblUsername.setText("Username");
        txtUsername.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        txtUsername.setToolTipText("Masukkan Username");

        javax.swing.GroupLayout panelUsernameLayout = new javax.swing.GroupLayout(panelUsername);
        panelUsername.setLayout(panelUsernameLayout);
        panelUsernameLayout.setHorizontalGroup(
                panelUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUsernameLayout.createSequentialGroup()
                                .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUsername)));
        panelUsernameLayout.setVerticalGroup(
                panelUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUsernameLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblUsername)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE));

        // Password Field
        lblPassword.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        lblPassword.setText("Password");
        txtPassword.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        txtPassword.setToolTipText("Masukkan Password");

        javax.swing.GroupLayout panelPasswordLayout = new javax.swing.GroupLayout(panelPassword);
        panelPassword.setLayout(panelPasswordLayout);
        panelPasswordLayout.setHorizontalGroup(
                panelPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPasswordLayout.createSequentialGroup()
                                .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPassword)));
        panelPasswordLayout.setVerticalGroup(
                panelPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPasswordLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblPassword)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE));

        // Nama Lengkap Field
        lblNamaLengkap.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        lblNamaLengkap.setText("Nama Lengkap");
        txtNamaLengkap.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        txtNamaLengkap.setToolTipText("Masukkan Nama Lengkap");

        javax.swing.GroupLayout panelNamaLengkapLayout = new javax.swing.GroupLayout(panelNamaLengkap);
        panelNamaLengkap.setLayout(panelNamaLengkapLayout);
        panelNamaLengkapLayout.setHorizontalGroup(
                panelNamaLengkapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelNamaLengkapLayout.createSequentialGroup()
                                .addComponent(lblNamaLengkap, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNamaLengkap)));
        panelNamaLengkapLayout.setVerticalGroup(
                panelNamaLengkapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelNamaLengkapLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblNamaLengkap)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(txtNamaLengkap, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE));

        // Role ComboBox
        lblRole.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        lblRole.setText("Role");
        cbRole.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kasir", "admin" }));
        cbRole.setToolTipText("Pilih Role");

        javax.swing.GroupLayout panelRoleLayout = new javax.swing.GroupLayout(panelRole);
        panelRole.setLayout(panelRoleLayout);
        panelRoleLayout.setHorizontalGroup(
                panelRoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelRoleLayout.createSequentialGroup()
                                .addComponent(lblRole, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        panelRoleLayout.setVerticalGroup(
                panelRoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelRoleLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblRole)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(cbRole, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE));

        // Tombol Tambah
        btnTambah.setBackground(new java.awt.Color(153, 255, 153));
        btnTambah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnTambah.setText("Tambah");
        btnTambah.setToolTipText("Klik Untuk Tambah Data");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTambahLayout = new javax.swing.GroupLayout(panelTambah);
        panelTambah.setLayout(panelTambahLayout);
        panelTambahLayout.setHorizontalGroup(
                panelTambahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 152,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        panelTambahLayout.setVerticalGroup(
                panelTambahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        // Tombol Kosongkan
        btnKosongkan.setBackground(new java.awt.Color(153, 255, 255));
        btnKosongkan.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnKosongkan.setText("Kosongkan Input");
        btnKosongkan.setToolTipText("Klik Untuk Mengosongkan Input");
        btnKosongkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKosongkanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKosongkanLayout = new javax.swing.GroupLayout(panelKosongkan);
        panelKosongkan.setLayout(panelKosongkanLayout);
        panelKosongkanLayout.setHorizontalGroup(
                panelKosongkanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnKosongkan, javax.swing.GroupLayout.PREFERRED_SIZE, 152,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        panelKosongkanLayout.setVerticalGroup(
                panelKosongkanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnKosongkan, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        // Tombol Ubah
        btnUbah.setBackground(new java.awt.Color(255, 204, 102));
        btnUbah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnUbah.setText("Ubah");
        btnUbah.setToolTipText("Klik Untuk Ubah Data");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUbahLayout = new javax.swing.GroupLayout(panelUbah);
        panelUbah.setLayout(panelUbahLayout);
        panelUbahLayout.setHorizontalGroup(
                panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 152,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        panelUbahLayout.setVerticalGroup(
                panelUbahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        // Tombol Hapus
        btnHapus.setBackground(new java.awt.Color(255, 102, 102));
        btnHapus.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.setToolTipText("Klik Untuk Hapus Data");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHapusLayout = new javax.swing.GroupLayout(panelHapus);
        panelHapus.setLayout(panelHapusLayout);
        panelHapusLayout.setHorizontalGroup(
                panelHapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 152,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        panelHapusLayout.setVerticalGroup(
                panelHapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        // Panel Filter
        txtFilter.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        txtFilter.setToolTipText("Cari Pengguna");

        btnFilter.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnFilter.setText("Filter");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFilterLayout = new javax.swing.GroupLayout(panelFilter);
        panelFilter.setLayout(panelFilterLayout);
        panelFilterLayout.setHorizontalGroup(
                panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFilterLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtFilter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReset)
                                .addContainerGap()));
        panelFilterLayout.setVerticalGroup(
                panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFilterLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelFilterLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtFilter, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnReset)
                                        .addComponent(btnFilter))
                                .addContainerGap()));

        // Tombol Kembali
        btnKembali.setBackground(new java.awt.Color(255, 51, 102));
        btnKembali.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
        btnKembali.setForeground(new java.awt.Color(255, 255, 255));
        btnKembali.setText("Kembali");
        btnKembali.setToolTipText("Klik Untuk Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKembaliLayout = new javax.swing.GroupLayout(panelKembali);
        panelKembali.setLayout(panelKembaliLayout);
        panelKembaliLayout.setHorizontalGroup(
                panelKembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnKembali, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE));
        panelKembaliLayout.setVerticalGroup(
                panelKembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        // Main Container Layout
        javax.swing.GroupLayout ContainerLayout = new javax.swing.GroupLayout(Container);
        Container.setLayout(ContainerLayout);
        ContainerLayout.setHorizontalGroup(
                ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ContainerLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(ContainerLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(JudulText, javax.swing.GroupLayout.DEFAULT_SIZE, 310,
                                                Short.MAX_VALUE)
                                        .addComponent(panelUsername, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelPassword, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelNamaLengkap, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelRole, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(ContainerLayout.createSequentialGroup()
                                                .addGroup(ContainerLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(panelTambah,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(panelUbah, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(ContainerLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(panelKosongkan,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(panelHapus,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(panelKembali, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(40, 40, 40)
                                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(TableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 570,
                                                Short.MAX_VALUE)
                                        .addComponent(panelFilter, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        ContainerLayout.setVerticalGroup(
                ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ContainerLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(JudulText, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelUsername, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelNamaLengkap, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelRole, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelKosongkan, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panelTambah, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelUbah, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panelHapus, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350,
                                        Short.MAX_VALUE)
                                .addComponent(panelKembali, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(ContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelFilter, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TableScrollPane)));

        getContentPane().add(Container, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void tabelPenggunaMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tabelPengguna.getSelectedRow();
        if (row >= 0) {
            selectedId = (int) model.getValueAt(row, 0);
            txtUsername.setText((String) model.getValueAt(row, 1));
            txtNamaLengkap.setText((String) model.getValueAt(row, 2));
            String role = (String) model.getValueAt(row, 3);
            cbRole.setSelectedItem(role);
            txtPassword.setText(""); // Password tidak ditampilkan untuk keamanan
        }
    }

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String namaLengkap = txtNamaLengkap.getText().trim();
        String role = (String) cbRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || namaLengkap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hash password dengan BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String sql = "INSERT INTO pengguna (username, password_hash, nama_lengkap, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = KoneksiDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, namaLengkap);
            ps.setString(4, role);

            ps.executeUpdate();

            loadDataTabel();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data Pengguna Berhasil Ditambah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnKosongkanActionPerformed(java.awt.event.ActionEvent evt) {
        resetForm();
    }

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah dari tabel!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String namaLengkap = txtNamaLengkap.getText().trim();
        String role = (String) cbRole.getSelectedItem();

        if (username.isEmpty() || namaLengkap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Nama Lengkap harus diisi!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql;
        boolean updatePassword = !password.isEmpty();

        if (updatePassword) {
            sql = "UPDATE pengguna SET username = ?, password_hash = ?, nama_lengkap = ?, role = ? WHERE id = ?";
        } else {
            sql = "UPDATE pengguna SET username = ?, nama_lengkap = ?, role = ? WHERE id = ?";
        }

        try (Connection conn = KoneksiDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (updatePassword) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                ps.setString(3, namaLengkap);
                ps.setString(4, role);
                ps.setInt(5, selectedId);
            } else {
                ps.setString(1, username);
                ps.setString(2, namaLengkap);
                ps.setString(3, role);
                ps.setInt(4, selectedId);
            }

            ps.executeUpdate();

            loadDataTabel();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data Pengguna Berhasil Diubah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus dari tabel!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus pengguna ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM pengguna WHERE id = ?";

        try (Connection conn = KoneksiDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, selectedId);
            ps.executeUpdate();

            loadDataTabel();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data Pengguna Berhasil Dihapus!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtFilter.getText().trim();
        if (keyword.isEmpty()) {
            loadDataTabel();
        } else {
            loadDataTabelFiltered(keyword);
        }
    }

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {
        txtFilter.setText("");
        loadDataTabel();
    }

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {
        if (Session.getInstance() != null
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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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

        java.awt.EventQueue.invokeLater(() -> new Pengguna().setVisible(true));
    }

    // Variables declaration
    private javax.swing.JPanel Container;
    private javax.swing.JLabel JudulText;
    private javax.swing.JScrollPane TableScrollPane;
    private javax.swing.JTable tabelPengguna;

    private javax.swing.JPanel panelUsername;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtUsername;

    private javax.swing.JPanel panelPassword;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JPasswordField txtPassword;

    private javax.swing.JPanel panelNamaLengkap;
    private javax.swing.JLabel lblNamaLengkap;
    private javax.swing.JTextField txtNamaLengkap;

    private javax.swing.JPanel panelRole;
    private javax.swing.JLabel lblRole;
    private javax.swing.JComboBox<String> cbRole;

    private javax.swing.JPanel panelTambah;
    private javax.swing.JButton btnTambah;

    private javax.swing.JPanel panelKosongkan;
    private javax.swing.JButton btnKosongkan;

    private javax.swing.JPanel panelUbah;
    private javax.swing.JButton btnUbah;

    private javax.swing.JPanel panelHapus;
    private javax.swing.JButton btnHapus;

    private javax.swing.JPanel panelFilter;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnReset;

    private javax.swing.JPanel panelKembali;
    private javax.swing.JButton btnKembali;
}
