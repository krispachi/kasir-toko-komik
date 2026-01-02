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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Krisna
 */
public class Penjualan extends javax.swing.JFrame {

        private static final java.util.logging.Logger logger = java.util.logging.Logger
                        .getLogger(Penjualan.class.getName());
        private DefaultTableModel modelKeranjang;
        private DefaultTableModel modelHistori;
        private int selectedKomikId = -1;
        private double selectedHargaJual = 0;
        private int selectedStok = 0;

        public Penjualan() {
                initComponents();

                txtCariKomik.putClientProperty("JTextField.placeholderText", "Cari ISBN atau Judul Komik");
                txtJumlah.putClientProperty("JTextField.placeholderText", "Jumlah");
                txtFilterHistori.putClientProperty("JTextField.placeholderText", "Cari transaksi");

                // Inisialisasi tabel keranjang
                String[] kolomKeranjang = { "ID Komik", "Judul", "Harga", "Jumlah", "Subtotal" };
                modelKeranjang = new DefaultTableModel(kolomKeranjang, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tabelKeranjang.setModel(modelKeranjang);

                // Inisialisasi tabel histori
                String[] kolomHistori = { "ID", "Invoice", "Tanggal", "Kasir", "Total", "Jumlah Item" };
                modelHistori = new DefaultTableModel(kolomHistori, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tabelHistori.setModel(modelHistori);

                loadHistoriPenjualan();
                updateTotalBelanja();
        }

        private void loadHistoriPenjualan() {
                modelHistori.setRowCount(0);

                String sql = "SELECT p.id, p.nomor_invoice, p.created_at, u.nama_lengkap as kasir, p.total_harga, p.total_jumlah "
                                +
                                "FROM penjualan p " +
                                "LEFT JOIN pengguna u ON p.pengguna_id = u.id " +
                                "ORDER BY p.created_at DESC LIMIT 100";

                try (Connection conn = KoneksiDatabase.getConnection();
                                Statement st = conn.createStatement();
                                ResultSet rs = st.executeQuery(sql)) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                        while (rs.next()) {
                                Object[] row = {
                                                rs.getLong("id"),
                                                rs.getString("nomor_invoice"),
                                                sdf.format(rs.getTimestamp("created_at")),
                                                rs.getString("kasir"),
                                                nf.format(rs.getDouble("total_harga")),
                                                rs.getInt("total_jumlah")
                                };
                                modelHistori.addRow(row);
                        }
                } catch (SQLException e) {
                        System.err.println("Gagal memuat histori: " + e.getMessage());
                }
        }

        private void loadHistoriFiltered(String keyword) {
                modelHistori.setRowCount(0);

                String sql = "SELECT p.id, p.nomor_invoice, p.created_at, u.nama_lengkap as kasir, p.total_harga, p.total_jumlah "
                                +
                                "FROM penjualan p " +
                                "LEFT JOIN pengguna u ON p.pengguna_id = u.id " +
                                "WHERE u.nama_lengkap LIKE ? OR p.nomor_invoice LIKE ? " +
                                "ORDER BY p.created_at DESC LIMIT 100";

                try (Connection conn = KoneksiDatabase.getConnection();
                                PreparedStatement ps = conn.prepareStatement(sql)) {

                        ps.setString(1, "%" + keyword + "%");
                        ps.setString(2, "%" + keyword + "%");
                        ResultSet rs = ps.executeQuery();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                        while (rs.next()) {
                                Object[] row = {
                                                rs.getLong("id"),
                                                rs.getString("nomor_invoice"),
                                                sdf.format(rs.getTimestamp("created_at")),
                                                rs.getString("kasir"),
                                                nf.format(rs.getDouble("total_harga")),
                                                rs.getInt("total_jumlah")
                                };
                                modelHistori.addRow(row);
                        }
                } catch (SQLException e) {
                        System.err.println("Gagal memuat histori: " + e.getMessage());
                }
        }

        private void updateTotalBelanja() {
                double total = 0;
                for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                        total += (double) modelKeranjang.getValueAt(i, 4);
                }
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                lblTotalBelanja.setText("Total: " + nf.format(total));
        }

        private void resetKeranjang() {
                modelKeranjang.setRowCount(0);
                txtCariKomik.setText("");
                txtJumlah.setText("");
                lblKomikInfo.setText("Pilih komik untuk ditambahkan");
                selectedKomikId = -1;
                selectedHargaJual = 0;
                selectedStok = 0;
                updateTotalBelanja();
        }

        private String generateInvoiceNumber() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                return "INV-" + sdf.format(new java.util.Date()) + "-"
                                + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        }

        @SuppressWarnings("unchecked")
        private void initComponents() {
                Container = new javax.swing.JPanel();
                JudulText = new javax.swing.JLabel();

                // Panel Kiri (Transaksi)
                panelTransaksi = new javax.swing.JPanel();
                lblTransaksi = new javax.swing.JLabel();

                panelCari = new javax.swing.JPanel();
                txtCariKomik = new javax.swing.JTextField();
                btnCari = new javax.swing.JButton();

                panelKomikInfo = new javax.swing.JPanel();
                lblKomikInfo = new javax.swing.JLabel();

                panelJumlah = new javax.swing.JPanel();
                lblJumlah = new javax.swing.JLabel();
                txtJumlah = new javax.swing.JTextField();
                btnTambahKeranjang = new javax.swing.JButton();

                scrollKeranjang = new javax.swing.JScrollPane();
                tabelKeranjang = new javax.swing.JTable();

                panelAksiKeranjang = new javax.swing.JPanel();
                btnHapusDariKeranjang = new javax.swing.JButton();
                btnKosongkanKeranjang = new javax.swing.JButton();

                panelTotal = new javax.swing.JPanel();
                lblTotalBelanja = new javax.swing.JLabel();
                btnProsesBayar = new javax.swing.JButton();

                // Panel Kanan (Histori)
                panelHistori = new javax.swing.JPanel();
                lblHistori = new javax.swing.JLabel();

                panelFilterHistori = new javax.swing.JPanel();
                txtFilterHistori = new javax.swing.JTextField();
                btnFilterHistori = new javax.swing.JButton();
                btnResetHistori = new javax.swing.JButton();

                scrollHistori = new javax.swing.JScrollPane();
                tabelHistori = new javax.swing.JTable();

                panelAksiHistori = new javax.swing.JPanel();
                btnLihatDetail = new javax.swing.JButton();
                btnHapusTransaksi = new javax.swing.JButton();

                panelKembali = new javax.swing.JPanel();
                btnKembali = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Halaman Penjualan");
                setMinimumSize(new java.awt.Dimension(1200, 700));
                setPreferredSize(new java.awt.Dimension(1200, 700));

                Container.setMinimumSize(new java.awt.Dimension(1200, 700));

                JudulText.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 16));
                JudulText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                JudulText.setText("Transaksi Penjualan");

                // Panel Transaksi
                panelTransaksi.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)),
                                "Transaksi Baru", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("JetBrains Mono SemiBold", 1, 12)));

                // Cari Komik
                txtCariKomik.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                btnCari.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                btnCari.setText("Cari");
                btnCari.addActionListener(e -> btnCariActionPerformed());

                javax.swing.GroupLayout panelCariLayout = new javax.swing.GroupLayout(panelCari);
                panelCari.setLayout(panelCariLayout);
                panelCariLayout.setHorizontalGroup(
                                panelCariLayout.createSequentialGroup()
                                                .addComponent(txtCariKomik, javax.swing.GroupLayout.DEFAULT_SIZE, 350,
                                                                Short.MAX_VALUE)
                                                .addGap(6)
                                                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));
                panelCariLayout.setVerticalGroup(
                                panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(txtCariKomik, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                // Info Komik
                lblKomikInfo.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                lblKomikInfo.setText("Pilih komik untuk ditambahkan");
                panelKomikInfo.add(lblKomikInfo);

                // Jumlah
                lblJumlah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                lblJumlah.setText("Jumlah:");
                txtJumlah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                txtJumlah.setColumns(5);
                btnTambahKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                btnTambahKeranjang.setBackground(new java.awt.Color(153, 255, 153));
                btnTambahKeranjang.setText("+ Keranjang");
                btnTambahKeranjang.addActionListener(e -> btnTambahKeranjangActionPerformed());

                javax.swing.GroupLayout panelJumlahLayout = new javax.swing.GroupLayout(panelJumlah);
                panelJumlah.setLayout(panelJumlahLayout);
                panelJumlahLayout.setHorizontalGroup(
                                panelJumlahLayout.createSequentialGroup()
                                                .addComponent(lblJumlah)
                                                .addGap(6)
                                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(6)
                                                .addComponent(btnTambahKeranjang));
                panelJumlahLayout.setVerticalGroup(
                                panelJumlahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(lblJumlah)
                                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnTambahKeranjang,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                // Tabel Keranjang
                tabelKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                scrollKeranjang.setViewportView(tabelKeranjang);

                // Aksi Keranjang
                btnHapusDariKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                btnHapusDariKeranjang.setBackground(new java.awt.Color(255, 102, 102));
                btnHapusDariKeranjang.setForeground(java.awt.Color.WHITE);
                btnHapusDariKeranjang.setText("Hapus Item");
                btnHapusDariKeranjang.addActionListener(e -> btnHapusDariKeranjangActionPerformed());

                btnKosongkanKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                btnKosongkanKeranjang.setBackground(new java.awt.Color(153, 255, 255));
                btnKosongkanKeranjang.setText("Kosongkan");
                btnKosongkanKeranjang.addActionListener(e -> resetKeranjang());

                javax.swing.GroupLayout panelAksiKeranjangLayout = new javax.swing.GroupLayout(panelAksiKeranjang);
                panelAksiKeranjang.setLayout(panelAksiKeranjangLayout);
                panelAksiKeranjangLayout.setHorizontalGroup(
                                panelAksiKeranjangLayout.createSequentialGroup()
                                                .addComponent(btnHapusDariKeranjang)
                                                .addGap(6)
                                                .addComponent(btnKosongkanKeranjang));
                panelAksiKeranjangLayout.setVerticalGroup(
                                panelAksiKeranjangLayout.createParallelGroup()
                                                .addComponent(btnHapusDariKeranjang,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnKosongkanKeranjang,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                // Total dan Bayar
                lblTotalBelanja.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14));
                lblTotalBelanja.setText("Total: Rp0");
                btnProsesBayar.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12));
                btnProsesBayar.setBackground(new java.awt.Color(0, 153, 51));
                btnProsesBayar.setForeground(java.awt.Color.WHITE);
                btnProsesBayar.setText("PROSES BAYAR");
                btnProsesBayar.addActionListener(e -> btnProsesBayarActionPerformed());

                javax.swing.GroupLayout panelTotalLayout = new javax.swing.GroupLayout(panelTotal);
                panelTotal.setLayout(panelTotalLayout);
                panelTotalLayout.setHorizontalGroup(
                                panelTotalLayout.createSequentialGroup()
                                                .addComponent(lblTotalBelanja, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                250, Short.MAX_VALUE)
                                                .addComponent(btnProsesBayar));
                panelTotalLayout.setVerticalGroup(
                                panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(lblTotalBelanja)
                                                .addComponent(btnProsesBayar, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                35, javax.swing.GroupLayout.PREFERRED_SIZE));

                // Layout Panel Transaksi
                javax.swing.GroupLayout panelTransaksiLayout = new javax.swing.GroupLayout(panelTransaksi);
                panelTransaksi.setLayout(panelTransaksiLayout);
                panelTransaksiLayout.setHorizontalGroup(
                                panelTransaksiLayout.createParallelGroup()
                                                .addGroup(panelTransaksiLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(panelTransaksiLayout.createParallelGroup()
                                                                                .addComponent(panelCari,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(panelKomikInfo,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(panelJumlah,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(scrollKeranjang)
                                                                                .addComponent(panelAksiKeranjang,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(panelTotal,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                panelTransaksiLayout.setVerticalGroup(
                                panelTransaksiLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(panelCari, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(6)
                                                .addComponent(panelKomikInfo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(6)
                                                .addComponent(panelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10)
                                                .addComponent(scrollKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                280, Short.MAX_VALUE)
                                                .addGap(6)
                                                .addComponent(panelAksiKeranjang,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10)
                                                .addComponent(panelTotal, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap());

                // Panel Histori
                panelHistori.setBorder(javax.swing.BorderFactory.createTitledBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)),
                                "Histori Penjualan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                new java.awt.Font("JetBrains Mono SemiBold", 1, 12)));

                // Filter Histori
                txtFilterHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                btnFilterHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                btnFilterHistori.setText("Filter");
                btnFilterHistori.addActionListener(e -> {
                        String kw = txtFilterHistori.getText().trim();
                        if (kw.isEmpty())
                                loadHistoriPenjualan();
                        else
                                loadHistoriFiltered(kw);
                });
                btnResetHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                btnResetHistori.setText("Reset");
                btnResetHistori.addActionListener(e -> {
                        txtFilterHistori.setText("");
                        loadHistoriPenjualan();
                });

                javax.swing.GroupLayout panelFilterHistoriLayout = new javax.swing.GroupLayout(panelFilterHistori);
                panelFilterHistori.setLayout(panelFilterHistoriLayout);
                panelFilterHistoriLayout.setHorizontalGroup(
                                panelFilterHistoriLayout.createSequentialGroup()
                                                .addComponent(txtFilterHistori, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                300, Short.MAX_VALUE)
                                                .addGap(6)
                                                .addComponent(btnFilterHistori)
                                                .addGap(6)
                                                .addComponent(btnResetHistori));
                panelFilterHistoriLayout.setVerticalGroup(
                                panelFilterHistoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(txtFilterHistori, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnFilterHistori, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnResetHistori, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                28, javax.swing.GroupLayout.PREFERRED_SIZE));

                // Tabel Histori
                tabelHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                scrollHistori.setViewportView(tabelHistori);

                // Aksi Histori
                btnLihatDetail.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                btnLihatDetail.setBackground(new java.awt.Color(102, 178, 255));
                btnLihatDetail.setText("Lihat Detail");
                btnLihatDetail.addActionListener(e -> btnLihatDetailActionPerformed());

                btnHapusTransaksi.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                btnHapusTransaksi.setBackground(new java.awt.Color(255, 102, 102));
                btnHapusTransaksi.setForeground(java.awt.Color.WHITE);
                btnHapusTransaksi.setText("Hapus Transaksi");
                btnHapusTransaksi.addActionListener(e -> btnHapusTransaksiActionPerformed());

                javax.swing.GroupLayout panelAksiHistoriLayout = new javax.swing.GroupLayout(panelAksiHistori);
                panelAksiHistori.setLayout(panelAksiHistoriLayout);
                panelAksiHistoriLayout.setHorizontalGroup(
                                panelAksiHistoriLayout.createSequentialGroup()
                                                .addComponent(btnLihatDetail)
                                                .addGap(6)
                                                .addComponent(btnHapusTransaksi));
                panelAksiHistoriLayout.setVerticalGroup(
                                panelAksiHistoriLayout.createParallelGroup()
                                                .addComponent(btnLihatDetail, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnHapusTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                28, javax.swing.GroupLayout.PREFERRED_SIZE));

                // Layout Panel Histori
                javax.swing.GroupLayout panelHistoriLayout = new javax.swing.GroupLayout(panelHistori);
                panelHistori.setLayout(panelHistoriLayout);
                panelHistoriLayout.setHorizontalGroup(
                                panelHistoriLayout.createParallelGroup()
                                                .addGroup(panelHistoriLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(panelHistoriLayout.createParallelGroup()
                                                                                .addComponent(panelFilterHistori,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(scrollHistori)
                                                                                .addComponent(panelAksiHistori,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));
                panelHistoriLayout.setVerticalGroup(
                                panelHistoriLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(panelFilterHistori,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10)
                                                .addComponent(scrollHistori, javax.swing.GroupLayout.DEFAULT_SIZE, 400,
                                                                Short.MAX_VALUE)
                                                .addGap(6)
                                                .addComponent(panelAksiHistori, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap());

                // Tombol Kembali
                btnKembali.setBackground(new java.awt.Color(255, 51, 102));
                btnKembali.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12));
                btnKembali.setForeground(java.awt.Color.WHITE);
                btnKembali.setText("Kembali ke Menu Utama");
                btnKembali.addActionListener(e -> btnKembaliActionPerformed());

                javax.swing.GroupLayout panelKembaliLayout = new javax.swing.GroupLayout(panelKembali);
                panelKembali.setLayout(panelKembaliLayout);
                panelKembaliLayout.setHorizontalGroup(
                                panelKembaliLayout.createSequentialGroup()
                                                .addComponent(btnKembali, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                panelKembaliLayout.setVerticalGroup(
                                panelKembaliLayout.createSequentialGroup()
                                                .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE));

                // Main Layout
                javax.swing.GroupLayout ContainerLayout = new javax.swing.GroupLayout(Container);
                Container.setLayout(ContainerLayout);
                ContainerLayout.setHorizontalGroup(
                                ContainerLayout.createParallelGroup()
                                                .addGroup(ContainerLayout.createSequentialGroup()
                                                                .addGap(20)
                                                                .addGroup(ContainerLayout.createParallelGroup()
                                                                                .addComponent(JudulText,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(ContainerLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(panelTransaksi,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                500,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(20)
                                                                                                .addGroup(ContainerLayout
                                                                                                                .createParallelGroup()
                                                                                                                .addComponent(panelHistori,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(panelKembali,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                Short.MAX_VALUE))))
                                                                .addGap(20)));
                ContainerLayout.setVerticalGroup(
                                ContainerLayout.createSequentialGroup()
                                                .addGap(15)
                                                .addComponent(JudulText, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15)
                                                .addGroup(ContainerLayout.createParallelGroup()
                                                                .addComponent(panelTransaksi,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(ContainerLayout.createSequentialGroup()
                                                                                .addComponent(panelHistori,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGap(10)
                                                                                .addComponent(panelKembali,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(15));

                getContentPane().add(Container, java.awt.BorderLayout.CENTER);
                pack();
                setLocationRelativeTo(null);
        }

        private void btnCariActionPerformed() {
                String keyword = txtCariKomik.getText().trim();
                if (keyword.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Masukkan ISBN atau judul komik!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                String sql = "SELECT id, isbn, judul, harga_jual, stok FROM komik WHERE isbn LIKE ? OR judul LIKE ? LIMIT 1";

                try (Connection conn = KoneksiDatabase.getConnection();
                                PreparedStatement ps = conn.prepareStatement(sql)) {

                        ps.setString(1, "%" + keyword + "%");
                        ps.setString(2, "%" + keyword + "%");
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                                selectedKomikId = rs.getInt("id");
                                selectedHargaJual = rs.getDouble("harga_jual");
                                selectedStok = rs.getInt("stok");

                                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                lblKomikInfo.setText(rs.getString("judul") + " | " + nf.format(selectedHargaJual)
                                                + " | Stok: " + selectedStok);
                                txtJumlah.setText("1");
                                txtJumlah.requestFocus();
                        } else {
                                lblKomikInfo.setText("Komik tidak ditemukan!");
                                selectedKomikId = -1;
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private void btnTambahKeranjangActionPerformed() {
                if (selectedKomikId == -1) {
                        JOptionPane.showMessageDialog(this, "Pilih komik terlebih dahulu!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int jumlah;
                try {
                        jumlah = Integer.parseInt(txtJumlah.getText().trim());
                        if (jumlah <= 0)
                                throw new NumberFormatException();
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Jumlah harus angka positif!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                if (jumlah > selectedStok) {
                        JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + selectedStok,
                                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                // Cek apakah komik sudah ada di keranjang
                for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                        if ((int) modelKeranjang.getValueAt(i, 0) == selectedKomikId) {
                                int existingQty = (int) modelKeranjang.getValueAt(i, 3);
                                int newQty = existingQty + jumlah;
                                if (newQty > selectedStok) {
                                        JOptionPane.showMessageDialog(this, "Total jumlah melebihi stok!", "Peringatan",
                                                        JOptionPane.WARNING_MESSAGE);
                                        return;
                                }
                                modelKeranjang.setValueAt(newQty, i, 3);
                                modelKeranjang.setValueAt(selectedHargaJual * newQty, i, 4);
                                updateTotalBelanja();
                                txtCariKomik.setText("");
                                lblKomikInfo.setText("Pilih komik untuk ditambahkan");
                                selectedKomikId = -1;
                                return;
                        }
                }

                // Tambah item baru
                String sql = "SELECT judul FROM komik WHERE id = ?";
                try (Connection conn = KoneksiDatabase.getConnection();
                                PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, selectedKomikId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                                Object[] row = {
                                                selectedKomikId,
                                                rs.getString("judul"),
                                                selectedHargaJual,
                                                jumlah,
                                                selectedHargaJual * jumlah
                                };
                                modelKeranjang.addRow(row);
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                updateTotalBelanja();
                txtCariKomik.setText("");
                lblKomikInfo.setText("Pilih komik untuk ditambahkan");
                selectedKomikId = -1;
        }

        private void btnHapusDariKeranjangActionPerformed() {
                int row = tabelKeranjang.getSelectedRow();
                if (row >= 0) {
                        modelKeranjang.removeRow(row);
                        updateTotalBelanja();
                } else {
                        JOptionPane.showMessageDialog(this, "Pilih item yang akan dihapus!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                }
        }

        private void btnProsesBayarActionPerformed() {
                if (modelKeranjang.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                double totalHarga = 0;
                int totalJumlah = 0;
                for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                        totalHarga += (double) modelKeranjang.getValueAt(i, 4);
                        totalJumlah += (int) modelKeranjang.getValueAt(i, 3);
                }
                
                //Diskon 10% jika > 200.000
                double diskon = 0;
                if (totalHarga > 200000) {
                    diskon = totalHarga * 0.10;
                }
                double grandTotal = totalHarga - diskon;
                
                // Formatter Rupiah
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // 4. Input Uang Pembayaran (Menggantikan Confirm Dialog biasa)
                String message = "Total Belanja: " + nf.format(totalHarga) +
                                 "\nDiskon (10%): " + nf.format(diskon) +
                                 "\n------------------------------" +
                                 "\nTOTAL BAYAR: " + nf.format(grandTotal) +
                                 "\n\nMasukkan Uang Pembayaran:";

                String inputUang = JOptionPane.showInputDialog(this, message, "Proses Pembayaran", JOptionPane.PLAIN_MESSAGE);
                
                /*
                int confirm = JOptionPane.showConfirmDialog(this,
                                "Total belanja: " + NumberFormat.getCurrencyInstance(new Locale("id", "ID"))
                                                .format(totalHarga) + "\nProses pembayaran?",
                                "Konfirmasi", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION)
                        return;
                */
                // Jika user menekan Cancel atau input kosong
                if (inputUang == null || inputUang.trim().isEmpty()) {
                    return;
                }

                double jumlah_bayar = 0;
                double kembalian = 0;

                try {
                    // Membersihkan input dari karakter non-angka (jika user mengetik rp/titik)
                    jumlah_bayar = Double.parseDouble(inputUang.replaceAll("[^0-9]", ""));

                    if (jumlah_bayar < grandTotal) {
                        JOptionPane.showMessageDialog(this, "Uang pembayaran kurang!", "Gagal", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    kembalian = jumlah_bayar - grandTotal;

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Input uang tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn = null;
                try {
                        conn = KoneksiDatabase.getConnection();
                        conn.setAutoCommit(false);

                        String invoiceNumber = generateInvoiceNumber();

                        // Insert penjualan
                        String sqlPenjualan = "INSERT INTO penjualan (nomor_invoice, pengguna_id, total_jumlah, total_harga, jumlah_bayar) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement psPenjualan = conn.prepareStatement(sqlPenjualan,
                                        Statement.RETURN_GENERATED_KEYS);
                        psPenjualan.setString(1, invoiceNumber);
                        psPenjualan.setInt(2, Session.getInstance().getUserId());
                        psPenjualan.setInt(3, totalJumlah);
                        psPenjualan.setDouble(4, totalHarga);
                        psPenjualan.setDouble(5, jumlah_bayar);
                        psPenjualan.executeUpdate();

                        ResultSet generatedKeys = psPenjualan.getGeneratedKeys();
                        long penjualanId = 0;
                        if (generatedKeys.next()) {
                                penjualanId = generatedKeys.getLong(1);
                        }

                        // Insert detail dan update stok
                        String sqlDetail = "INSERT INTO penjualan_detail (penjualan_id, komik_id, jumlah, harga, subtotal) VALUES (?, ?, ?, ?, ?)";
                        String sqlUpdateStok = "UPDATE komik SET stok = stok - ? WHERE id = ?";

                        PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                        PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok);

                        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                                int komikId = (int) modelKeranjang.getValueAt(i, 0);
                                double harga = (double) modelKeranjang.getValueAt(i, 2);
                                int jumlah = (int) modelKeranjang.getValueAt(i, 3);
                                double subtotal = (double) modelKeranjang.getValueAt(i, 4);

                                psDetail.setLong(1, penjualanId);
                                psDetail.setInt(2, komikId);
                                psDetail.setInt(3, jumlah);
                                psDetail.setDouble(4, harga);
                                psDetail.setDouble(5, subtotal);
                                psDetail.addBatch();

                                psStok.setInt(1, jumlah);
                                psStok.setInt(2, komikId);
                                psStok.addBatch();
                        }

                        psDetail.executeBatch();
                        psStok.executeBatch();

                        conn.commit();

                        //JOptionPane.showMessageDialog(this, "Transaksi berhasil!\nInvoice: " + invoiceNumber, "Sukses",
                        //                JOptionPane.INFORMATION_MESSAGE);
                        
                        // 6. Tampilkan Struk Virtual ke Layar
                        tampilkanStrukDiLayar(invoiceNumber, totalHarga, diskon, grandTotal, jumlah_bayar, kembalian);
                        
                        resetKeranjang();
                        loadHistoriPenjualan();

                } catch (SQLException e) {
                        if (conn != null) {
                                try {
                                        conn.rollback();
                                } catch (SQLException ex) {
                                        /* ignore */ }
                        }
                        JOptionPane.showMessageDialog(this, "Error transaksi: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                } finally {
                        if (conn != null) {
                                try {
                                        conn.setAutoCommit(true);
                                        conn.close();
                                } catch (SQLException ex) {
                                        /* ignore */ }
                        }
                }
        }
        
        private void tampilkanStrukDiLayar(String noInvoice, double subTotal, double diskon, double grandTotal, double bayar, double kembali) {
            // StringBuilder untuk menyusun teks struk
            StringBuilder sb = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            // --- HEADER ---
            sb.append("==========================================\n");
            sb.append("           TOKO KOMIK JAVA            \n");
            sb.append("       Jl. Koding No. 1, Bali      \n");
            sb.append("==========================================\n");
            sb.append("No Invoice : ").append(noInvoice).append("\n");
            sb.append("Tanggal    : ").append(sdf.format(new java.util.Date())).append("\n");
            sb.append("Kasir      : ").append(Session.getInstance().getUserId()).append("\n");
            sb.append("------------------------------------------\n");

            // --- ITEM BELANJA ---
            // Format: Nama Barang (qty x harga) -> Subtotal
            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                String nama = (String) modelKeranjang.getValueAt(i, 1);
                int qty = (int) modelKeranjang.getValueAt(i, 3);
                double harga = (double) modelKeranjang.getValueAt(i, 2);
                double totalItem = (double) modelKeranjang.getValueAt(i, 4);

                sb.append(nama).append("\n");
                String rincian = String.format("   %d x %-10s %18s", qty, nf.format(harga), nf.format(totalItem));
                sb.append(rincian).append("\n");
            }

            // --- FOOTER (TOTAL & PEMBAYARAN) ---
            sb.append("------------------------------------------\n");
            sb.append(String.format("Total Belanja  : %24s\n", nf.format(subTotal)));

            if (diskon > 0) {
                sb.append(String.format("Diskon (10%%)   : %24s\n", "-" + nf.format(diskon)));
            }

            sb.append(String.format("TOTAL HARGA    : %24s\n", nf.format(grandTotal)));
            sb.append("------------------------------------------\n");
            sb.append(String.format("Tunai          : %24s\n", nf.format(bayar)));
            sb.append(String.format("Kembali        : %24s\n", nf.format(kembali)));
            sb.append("==========================================\n");
            sb.append("    TERIMA KASIH ATAS KUNJUNGAN ANDA      \n");
            sb.append("==========================================\n");

            // Menampilkan ke JTextArea
            javax.swing.JTextArea textArea = new javax.swing.JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 12)); 
            textArea.setColumns(40);
            textArea.setRows(20);

            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);

            JOptionPane.showMessageDialog(this, scrollPane, "Cetak Struk", JOptionPane.PLAIN_MESSAGE);
        }

        private void btnLihatDetailActionPerformed() {
                int row = tabelHistori.getSelectedRow();
                if (row < 0) {
                        JOptionPane.showMessageDialog(this, "Pilih transaksi untuk melihat detail!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                long penjualanId = (long) modelHistori.getValueAt(row, 0);
                String invoice = (String) modelHistori.getValueAt(row, 1);

                StringBuilder sb = new StringBuilder();
                sb.append("Detail Transaksi ").append(invoice).append("\n\n");

                String sql = "SELECT k.judul, d.jumlah, d.harga, d.subtotal " +
                                "FROM penjualan_detail d JOIN komik k ON d.komik_id = k.id " +
                                "WHERE d.penjualan_id = ?";

                try (Connection conn = KoneksiDatabase.getConnection();
                                PreparedStatement ps = conn.prepareStatement(sql)) {

                        ps.setLong(1, penjualanId);
                        ResultSet rs = ps.executeQuery();
                        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                        while (rs.next()) {
                                sb.append("- ").append(rs.getString("judul"))
                                                .append(" x").append(rs.getInt("jumlah"))
                                                .append(" @ ").append(nf.format(rs.getDouble("harga")))
                                                .append(" = ").append(nf.format(rs.getDouble("subtotal")))
                                                .append("\n");
                        }

                        JOptionPane.showMessageDialog(this, sb.toString(), "Detail Transaksi",
                                        JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private void btnHapusTransaksiActionPerformed() {
                int row = tabelHistori.getSelectedRow();
                if (row < 0) {
                        JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                long penjualanId = (long) modelHistori.getValueAt(row, 0);
                String invoice = (String) modelHistori.getValueAt(row, 1);

                int confirm = JOptionPane.showConfirmDialog(this,
                                "Hapus transaksi " + invoice + "?\nStok akan dikembalikan.",
                                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION)
                        return;

                Connection conn = null;
                try {
                        conn = KoneksiDatabase.getConnection();
                        conn.setAutoCommit(false);

                        // Kembalikan stok
                        String sqlGetDetail = "SELECT komik_id, jumlah FROM penjualan_detail WHERE penjualan_id = ?";
                        PreparedStatement psGet = conn.prepareStatement(sqlGetDetail);
                        psGet.setLong(1, penjualanId);
                        ResultSet rs = psGet.executeQuery();

                        String sqlUpdateStok = "UPDATE komik SET stok = stok + ? WHERE id = ?";
                        PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok);

                        while (rs.next()) {
                                psStok.setInt(1, rs.getInt("jumlah"));
                                psStok.setInt(2, rs.getInt("komik_id"));
                                psStok.addBatch();
                        }
                        psStok.executeBatch();

                        // Hapus penjualan (detail akan terhapus otomatis karena ON DELETE CASCADE)
                        String sqlDelPenjualan = "DELETE FROM penjualan WHERE id = ?";
                        PreparedStatement psDelPenjualan = conn.prepareStatement(sqlDelPenjualan);
                        psDelPenjualan.setLong(1, penjualanId);
                        psDelPenjualan.executeUpdate();

                        conn.commit();

                        JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!", "Sukses",
                                        JOptionPane.INFORMATION_MESSAGE);
                        loadHistoriPenjualan();

                } catch (SQLException e) {
                        if (conn != null) {
                                try {
                                        conn.rollback();
                                } catch (SQLException ex) {
                                        /* ignore */ }
                        }
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                } finally {
                        if (conn != null) {
                                try {
                                        conn.setAutoCommit(true);
                                        conn.close();
                                } catch (SQLException ex) {
                                        /* ignore */ }
                        }
                }
        }

        private void btnKembaliActionPerformed() {
                if (Session.getInstance() != null
                                && (Session.getInstance().getRole().equalsIgnoreCase("kasir")
                                                || Session.getInstance().getRole().equalsIgnoreCase("admin"))) {
                        new MenuUtama().setVisible(true);
                        this.dispose();
                } else {
                        new Login().setVisible(true);
                        this.dispose();
                }
        }

        public static void main(String args[]) {
                try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
                        logger.log(java.util.logging.Level.SEVERE, null, ex);
                }
                java.awt.EventQueue.invokeLater(() -> new Penjualan().setVisible(true));
        }

        // Variables
        private javax.swing.JPanel Container;
        private javax.swing.JLabel JudulText;
        private javax.swing.JPanel panelTransaksi;
        private javax.swing.JLabel lblTransaksi;
        private javax.swing.JPanel panelCari;
        private javax.swing.JTextField txtCariKomik;
        private javax.swing.JButton btnCari;
        private javax.swing.JPanel panelKomikInfo;
        private javax.swing.JLabel lblKomikInfo;
        private javax.swing.JPanel panelJumlah;
        private javax.swing.JLabel lblJumlah;
        private javax.swing.JTextField txtJumlah;
        private javax.swing.JButton btnTambahKeranjang;
        private javax.swing.JScrollPane scrollKeranjang;
        private javax.swing.JTable tabelKeranjang;
        private javax.swing.JPanel panelAksiKeranjang;
        private javax.swing.JButton btnHapusDariKeranjang;
        private javax.swing.JButton btnKosongkanKeranjang;
        private javax.swing.JPanel panelTotal;
        private javax.swing.JLabel lblTotalBelanja;
        private javax.swing.JButton btnProsesBayar;
        private javax.swing.JPanel panelHistori;
        private javax.swing.JLabel lblHistori;
        private javax.swing.JPanel panelFilterHistori;
        private javax.swing.JTextField txtFilterHistori;
        private javax.swing.JButton btnFilterHistori;
        private javax.swing.JButton btnResetHistori;
        private javax.swing.JScrollPane scrollHistori;
        private javax.swing.JTable tabelHistori;
        private javax.swing.JPanel panelAksiHistori;
        private javax.swing.JButton btnLihatDetail;
        private javax.swing.JButton btnHapusTransaksi;
        private javax.swing.JPanel panelKembali;
        private javax.swing.JButton btnKembali;
}
