/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.krispachi.KasirTokoKomik;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.krispachi.KasirTokoKomik.singleton.KoneksiDatabase;
import com.krispachi.KasirTokoKomik.singleton.Session;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
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
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

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

        // Komponen autocomplete dropdown
        private JPopupMenu popupMenu;
        private JList<String> listSuggestions;
        private DefaultListModel<String> listModel;
        private List<KomikItem> komikSuggestions;

        // Inner class untuk menyimpan data komik
        private static class KomikItem {
                int id;
                String isbn;
                String judul;
                double hargaJual;
                int stok;

                KomikItem(int id, String isbn, String judul, double hargaJual, int stok) {
                        this.id = id;
                        this.isbn = isbn;
                        this.judul = judul;
                        this.hargaJual = hargaJual;
                        this.stok = stok;
                }

                @Override
                public String toString() {
                        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                        return judul + " | " + isbn + " | " + nf.format(hargaJual) + " | Stok: " + stok;
                }
        }

        /**
         * Creates new form Penjualan
         */
        public Penjualan() {
                initComponents();

                txtCariKomik.putClientProperty("JTextField.placeholderText", "Cari ISBN atau Judul Komik");
                txtJumlah.putClientProperty("JTextField.placeholderText", "Jumlah");
                txtFilterHistori.putClientProperty("JTextField.placeholderText", "Cari transaksi");

                // Inisialisasi autocomplete dropdown
                setupAutocomplete();

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

        private void setupAutocomplete() {
                komikSuggestions = new ArrayList<>();
                listModel = new DefaultListModel<>();
                listSuggestions = new JList<>(listModel);
                listSuggestions.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11));
                listSuggestions.setVisibleRowCount(7); // Maksimal 7 rows terlihat

                popupMenu = new JPopupMenu();
                JScrollPane scrollPane = new JScrollPane(listSuggestions);
                scrollPane.setPreferredSize(new java.awt.Dimension(400, 150));
                popupMenu.add(scrollPane);
                popupMenu.setFocusable(false);

                // Listener untuk memilih item dari list
                listSuggestions.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                if (evt.getClickCount() == 1) {
                                        selectKomikFromList();
                                }
                        }
                });

                // Listener keyboard untuk navigasi dan enter
                listSuggestions.addKeyListener(new java.awt.event.KeyAdapter() {
                        @Override
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                                        selectKomikFromList();
                                } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                                        popupMenu.setVisible(false);
                                        txtCariKomik.requestFocus();
                                }
                        }
                });

                // DocumentListener untuk mendeteksi perubahan teks
                txtCariKomik.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                                searchKomik();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                                searchKomik();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                                searchKomik();
                        }
                });

                // KeyListener untuk navigasi dengan keyboard
                txtCariKomik.addKeyListener(new java.awt.event.KeyAdapter() {
                        @Override
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                                if (popupMenu.isVisible()) {
                                        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                                                listSuggestions.requestFocus();
                                                if (listSuggestions.getSelectedIndex() < 0 && listModel.size() > 0) {
                                                        listSuggestions.setSelectedIndex(0);
                                                }
                                        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                                                if (listSuggestions.getSelectedIndex() >= 0) {
                                                        selectKomikFromList();
                                                } else if (listModel.size() > 0) {
                                                        listSuggestions.setSelectedIndex(0);
                                                        selectKomikFromList();
                                                }
                                        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                                                popupMenu.setVisible(false);
                                        }
                                }
                        }
                });

                // MouseListener untuk menampilkan semua komik saat field diklik dan kosong
                txtCariKomik.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                if (txtCariKomik.getText().trim().isEmpty()) {
                                        showAllKomik();
                                }
                        }
                });
        }

        private void searchKomik() {
                String keyword = txtCariKomik.getText().trim();

                // Jika kosong, tampilkan semua komik
                if (keyword.isEmpty()) {
                        showAllKomik();
                        return;
                }

                // Gunakan SwingWorker untuk tidak memblokir UI
                javax.swing.SwingUtilities.invokeLater(() -> {
                        komikSuggestions.clear();
                        listModel.clear();

                        String sql = "SELECT id, isbn, judul, harga_jual, stok FROM komik WHERE isbn LIKE ? OR judul LIKE ? ORDER BY judul LIMIT 20";

                        try (Connection conn = KoneksiDatabase.getConnection();
                                        PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setString(1, "%" + keyword + "%");
                                ps.setString(2, "%" + keyword + "%");
                                ResultSet rs = ps.executeQuery();

                                while (rs.next()) {
                                        KomikItem item = new KomikItem(
                                                        rs.getInt("id"),
                                                        rs.getString("isbn"),
                                                        rs.getString("judul"),
                                                        rs.getDouble("harga_jual"),
                                                        rs.getInt("stok"));
                                        komikSuggestions.add(item);
                                        listModel.addElement(item.toString());
                                }

                                if (!komikSuggestions.isEmpty()) {
                                        popupMenu.show(txtCariKomik, 0, txtCariKomik.getHeight());
                                } else {
                                        popupMenu.setVisible(false);
                                }
                        } catch (SQLException e) {
                                System.err.println("Error searching komik: " + e.getMessage());
                        }
                });
        }

        private void showAllKomik() {
                javax.swing.SwingUtilities.invokeLater(() -> {
                        komikSuggestions.clear();
                        listModel.clear();

                        String sql = "SELECT id, isbn, judul, harga_jual, stok FROM komik ORDER BY judul LIMIT 50";

                        try (Connection conn = KoneksiDatabase.getConnection();
                                        PreparedStatement ps = conn.prepareStatement(sql)) {

                                ResultSet rs = ps.executeQuery();

                                while (rs.next()) {
                                        KomikItem item = new KomikItem(
                                                        rs.getInt("id"),
                                                        rs.getString("isbn"),
                                                        rs.getString("judul"),
                                                        rs.getDouble("harga_jual"),
                                                        rs.getInt("stok"));
                                        komikSuggestions.add(item);
                                        listModel.addElement(item.toString());
                                }

                                if (!komikSuggestions.isEmpty()) {
                                        popupMenu.show(txtCariKomik, 0, txtCariKomik.getHeight());
                                } else {
                                        popupMenu.setVisible(false);
                                }
                        } catch (SQLException e) {
                                System.err.println("Error loading all komik: " + e.getMessage());
                        }
                });
        }

        private void selectKomikFromList() {
                int selectedIndex = listSuggestions.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < komikSuggestions.size()) {
                        KomikItem item = komikSuggestions.get(selectedIndex);
                        selectedKomikId = item.id;
                        selectedHargaJual = item.hargaJual;
                        selectedStok = item.stok;

                        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                        lblKomikInfo.setText(
                                        item.judul + " | " + nf.format(selectedHargaJual) + " | Stok: " + selectedStok);
                        txtCariKomik.setText(item.judul);
                        txtJumlah.setText("1");
                        txtJumlah.requestFocus();

                        popupMenu.setVisible(false);
                }
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

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {
                java.awt.GridBagConstraints gridBagConstraints;

                Container = new javax.swing.JPanel();
                JudulText = new javax.swing.JLabel();
                panelTransaksi = new javax.swing.JPanel();
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
                panelHistori = new javax.swing.JPanel();
                panelFilterHistori = new javax.swing.JPanel();
                txtFilterHistori = new javax.swing.JTextField();
                btnFilterHistori = new javax.swing.JButton();
                btnResetHistori = new javax.swing.JButton();
                scrollHistori = new javax.swing.JScrollPane();
                tabelHistori = new javax.swing.JTable();
                panelAksiHistori = new javax.swing.JPanel();
                btnLihatDetail = new javax.swing.JButton();
                btnHapusTransaksi = new javax.swing.JButton();
                btnPreviewInvoice = new javax.swing.JButton();
                btnCetakInvoice = new javax.swing.JButton();
                panelKembali = new javax.swing.JPanel();
                btnKembali = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("Halaman Penjualan");
                setMinimumSize(new java.awt.Dimension(1200, 700));
                getContentPane().setLayout(new java.awt.BorderLayout());

                Container.setMinimumSize(new java.awt.Dimension(1200, 700));
                Container.setLayout(new java.awt.GridBagLayout());

                JudulText.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 16)); // NOI18N
                JudulText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                JudulText.setText("Transaksi Penjualan");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(15, 20, 15, 20);
                Container.add(JudulText, gridBagConstraints);

                panelTransaksi.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaksi Baru"));
                panelTransaksi.setLayout(new javax.swing.BoxLayout(panelTransaksi, javax.swing.BoxLayout.Y_AXIS));

                panelCari.setLayout(new java.awt.FlowLayout());

                txtCariKomik.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                txtCariKomik.setColumns(25);
                panelCari.add(txtCariKomik);

                btnCari.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                btnCari.setText("Cari");
                btnCari.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnCariActionPerformed(evt);
                        }
                });
                panelCari.add(btnCari);

                panelTransaksi.add(panelCari);

                panelKomikInfo.setLayout(new java.awt.FlowLayout());

                lblKomikInfo.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                lblKomikInfo.setText("Pilih komik untuk ditambahkan");
                panelKomikInfo.add(lblKomikInfo);

                panelTransaksi.add(panelKomikInfo);

                panelJumlah.setLayout(new java.awt.FlowLayout());

                lblJumlah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                lblJumlah.setText("Jumlah:");
                panelJumlah.add(lblJumlah);

                txtJumlah.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                txtJumlah.setColumns(5);
                panelJumlah.add(txtJumlah);

                btnTambahKeranjang.setBackground(new java.awt.Color(153, 255, 153));
                btnTambahKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                btnTambahKeranjang.setText("+ Keranjang");
                btnTambahKeranjang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnTambahKeranjangActionPerformed(evt);
                        }
                });
                panelJumlah.add(btnTambahKeranjang);

                panelTransaksi.add(panelJumlah);

                tabelKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                tabelKeranjang.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] { "ID Komik", "Judul", "Harga", "Jumlah", "Subtotal" }));
                scrollKeranjang.setViewportView(tabelKeranjang);

                panelTransaksi.add(scrollKeranjang);

                panelAksiKeranjang.setLayout(new java.awt.FlowLayout());

                btnHapusDariKeranjang.setBackground(new java.awt.Color(255, 102, 102));
                btnHapusDariKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnHapusDariKeranjang.setForeground(new java.awt.Color(255, 255, 255));
                btnHapusDariKeranjang.setText("Hapus Item");
                btnHapusDariKeranjang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnHapusDariKeranjangActionPerformed(evt);
                        }
                });
                panelAksiKeranjang.add(btnHapusDariKeranjang);

                btnKosongkanKeranjang.setBackground(new java.awt.Color(153, 255, 255));
                btnKosongkanKeranjang.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnKosongkanKeranjang.setText("Kosongkan");
                btnKosongkanKeranjang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnKosongkanKeranjangActionPerformed(evt);
                        }
                });
                panelAksiKeranjang.add(btnKosongkanKeranjang);

                panelTransaksi.add(panelAksiKeranjang);

                panelTotal.setLayout(new java.awt.FlowLayout());

                lblTotalBelanja.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 14)); // NOI18N
                lblTotalBelanja.setText("Total: Rp0");
                panelTotal.add(lblTotalBelanja);

                btnProsesBayar.setBackground(new java.awt.Color(0, 153, 51));
                btnProsesBayar.setFont(new java.awt.Font("JetBrains Mono ExtraBold", 1, 12)); // NOI18N
                btnProsesBayar.setForeground(new java.awt.Color(255, 255, 255));
                btnProsesBayar.setText("PROSES BAYAR");
                btnProsesBayar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnProsesBayarActionPerformed(evt);
                        }
                });
                panelTotal.add(btnProsesBayar);

                panelTransaksi.add(panelTotal);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 0.4;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 20, 15, 10);
                Container.add(panelTransaksi, gridBagConstraints);

                panelHistori.setBorder(javax.swing.BorderFactory.createTitledBorder("Histori Penjualan"));
                panelHistori.setLayout(new javax.swing.BoxLayout(panelHistori, javax.swing.BoxLayout.Y_AXIS));

                panelFilterHistori.setLayout(new java.awt.FlowLayout());

                txtFilterHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                txtFilterHistori.setColumns(20);
                panelFilterHistori.add(txtFilterHistori);

                btnFilterHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnFilterHistori.setText("Filter");
                btnFilterHistori.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnFilterHistoriActionPerformed(evt);
                        }
                });
                panelFilterHistori.add(btnFilterHistori);

                btnResetHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnResetHistori.setText("Reset");
                btnResetHistori.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnResetHistoriActionPerformed(evt);
                        }
                });
                panelFilterHistori.add(btnResetHistori);

                panelHistori.add(panelFilterHistori);

                tabelHistori.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                tabelHistori.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] { "ID", "Invoice", "Tanggal", "Kasir", "Total", "Jumlah Item" }));
                scrollHistori.setViewportView(tabelHistori);

                panelHistori.add(scrollHistori);

                panelAksiHistori.setLayout(new java.awt.FlowLayout());

                btnLihatDetail.setBackground(new java.awt.Color(102, 178, 255));
                btnLihatDetail.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnLihatDetail.setText("Lihat Detail");
                btnLihatDetail.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnLihatDetailActionPerformed(evt);
                        }
                });
                panelAksiHistori.add(btnLihatDetail);

                btnHapusTransaksi.setBackground(new java.awt.Color(255, 102, 102));
                btnHapusTransaksi.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnHapusTransaksi.setForeground(new java.awt.Color(255, 255, 255));
                btnHapusTransaksi.setText("Hapus Transaksi");
                btnHapusTransaksi.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnHapusTransaksiActionPerformed(evt);
                        }
                });
                panelAksiHistori.add(btnHapusTransaksi);

                btnPreviewInvoice.setBackground(new java.awt.Color(102, 255, 178));
                btnPreviewInvoice.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnPreviewInvoice.setText("Preview Invoice");
                btnPreviewInvoice.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnPreviewInvoiceActionPerformed(evt);
                        }
                });
                panelAksiHistori.add(btnPreviewInvoice);

                btnCetakInvoice.setBackground(new java.awt.Color(255, 178, 102));
                btnCetakInvoice.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 11)); // NOI18N
                btnCetakInvoice.setText("Cetak PDF");
                btnCetakInvoice.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnCetakInvoiceActionPerformed(evt);
                        }
                });
                panelAksiHistori.add(btnCetakInvoice);

                panelHistori.add(panelAksiHistori);

                panelKembali.setLayout(new java.awt.FlowLayout());

                btnKembali.setBackground(new java.awt.Color(255, 51, 102));
                btnKembali.setFont(new java.awt.Font("JetBrains Mono SemiBold", 0, 12)); // NOI18N
                btnKembali.setForeground(new java.awt.Color(255, 255, 255));
                btnKembali.setText("Kembali ke Menu Utama");
                btnKembali.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnKembaliActionPerformed(evt);
                        }
                });
                panelKembali.add(btnKembali);

                panelHistori.add(panelKembali);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 0.6;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 10, 15, 20);
                Container.add(panelHistori, gridBagConstraints);

                getContentPane().add(Container, java.awt.BorderLayout.CENTER);

                pack();
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCariActionPerformed
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
        }// GEN-LAST:event_btnCariActionPerformed

        private void btnTambahKeranjangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTambahKeranjangActionPerformed
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
        }// GEN-LAST:event_btnTambahKeranjangActionPerformed

        private void btnHapusDariKeranjangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHapusDariKeranjangActionPerformed
                int row = tabelKeranjang.getSelectedRow();
                if (row >= 0) {
                        modelKeranjang.removeRow(row);
                        updateTotalBelanja();
                } else {
                        JOptionPane.showMessageDialog(this, "Pilih item yang akan dihapus!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                }
        }// GEN-LAST:event_btnHapusDariKeranjangActionPerformed

        private void btnKosongkanKeranjangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnKosongkanKeranjangActionPerformed
                resetKeranjang();
        }// GEN-LAST:event_btnKosongkanKeranjangActionPerformed

        private void btnProsesBayarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnProsesBayarActionPerformed
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

                // Diskon 10% jika > 200.000
                double diskon = 0;
                if (totalHarga > 200000) {
                        diskon = totalHarga * 0.10;
                }
                double grandTotal = totalHarga - diskon;

                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                String message = "Total Belanja: " + nf.format(totalHarga) +
                                "\nDiskon (10%): " + nf.format(diskon) +
                                "\n------------------------------" +
                                "\nTOTAL BAYAR: " + nf.format(grandTotal) +
                                "\n\nMasukkan Uang Pembayaran:";

                String inputUang = JOptionPane.showInputDialog(this, message, "Proses Pembayaran",
                                JOptionPane.PLAIN_MESSAGE);

                if (inputUang == null || inputUang.trim().isEmpty()) {
                        return;
                }

                double jumlah_bayar;
                double kembalian;

                try {
                        jumlah_bayar = Double.parseDouble(inputUang.replaceAll("[^0-9]", ""));

                        if (jumlah_bayar < grandTotal) {
                                JOptionPane.showMessageDialog(this, "Uang pembayaran kurang!", "Gagal",
                                                JOptionPane.ERROR_MESSAGE);
                                return;
                        }
                        kembalian = jumlah_bayar - grandTotal;

                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Input uang tidak valid!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                Connection conn = null;
                try {
                        conn = KoneksiDatabase.getConnection();
                        conn.setAutoCommit(false);

                        String invoiceNumber = generateInvoiceNumber();

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
        }// GEN-LAST:event_btnProsesBayarActionPerformed

        private void btnFilterHistoriActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFilterHistoriActionPerformed
                String kw = txtFilterHistori.getText().trim();
                if (kw.isEmpty()) {
                        loadHistoriPenjualan();
                } else {
                        loadHistoriFiltered(kw);
                }
        }// GEN-LAST:event_btnFilterHistoriActionPerformed

        private void btnResetHistoriActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnResetHistoriActionPerformed
                txtFilterHistori.setText("");
                loadHistoriPenjualan();
        }// GEN-LAST:event_btnResetHistoriActionPerformed

        private void btnLihatDetailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLihatDetailActionPerformed
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
        }// GEN-LAST:event_btnLihatDetailActionPerformed

        private void btnHapusTransaksiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHapusTransaksiActionPerformed
                int row = tabelHistori.getSelectedRow();
                if (row < 0) {
                        JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                long penjualanId = (long) modelHistori.getValueAt(row, 0);
                String invoice = (String) modelHistori.getValueAt(row, 1);

                int confirm = JOptionPane.showConfirmDialog(this,
                                "Hapus transaksi " + invoice + "?\nStok akan dikembalikan.", "Konfirmasi Hapus",
                                JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION)
                        return;

                Connection conn = null;
                try {
                        conn = KoneksiDatabase.getConnection();
                        conn.setAutoCommit(false);

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
        }// GEN-LAST:event_btnHapusTransaksiActionPerformed

        private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnKembaliActionPerformed
                if (Session.getInstance() != null
                                && (Session.getInstance().getRole().equalsIgnoreCase("kasir")
                                                || Session.getInstance().getRole().equalsIgnoreCase("admin"))) {
                        new MenuUtama().setVisible(true);
                        this.dispose();
                } else {
                        new Login().setVisible(true);
                        this.dispose();
                }
        }// GEN-LAST:event_btnKembaliActionPerformed

        private void btnPreviewInvoiceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPreviewInvoiceActionPerformed
                int row = tabelHistori.getSelectedRow();
                if (row < 0) {
                        JOptionPane.showMessageDialog(this, "Pilih transaksi untuk preview invoice!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                long penjualanId = (long) modelHistori.getValueAt(row, 0);
                String invoice = (String) modelHistori.getValueAt(row, 1);
                String tanggal = (String) modelHistori.getValueAt(row, 2);
                String kasir = (String) modelHistori.getValueAt(row, 3);
                String totalStr = (String) modelHistori.getValueAt(row, 4);

                previewInvoiceHistori(penjualanId, invoice, tanggal, kasir, totalStr, false);
        }// GEN-LAST:event_btnPreviewInvoiceActionPerformed

        private void btnCetakInvoiceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCetakInvoiceActionPerformed
                int row = tabelHistori.getSelectedRow();
                if (row < 0) {
                        JOptionPane.showMessageDialog(this, "Pilih transaksi untuk cetak invoice!", "Peringatan",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                long penjualanId = (long) modelHistori.getValueAt(row, 0);
                String invoice = (String) modelHistori.getValueAt(row, 1);
                String tanggal = (String) modelHistori.getValueAt(row, 2);
                String kasir = (String) modelHistori.getValueAt(row, 3);
                String totalStr = (String) modelHistori.getValueAt(row, 4);

                previewInvoiceHistori(penjualanId, invoice, tanggal, kasir, totalStr, true);
        }// GEN-LAST:event_btnCetakInvoiceActionPerformed

        private void previewInvoiceHistori(long penjualanId, String noInvoice, String tanggal, String kasir,
                        String totalStr, boolean langungCetakPdf) {
                StringBuilder sb = new StringBuilder();
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Query untuk mendapatkan detail dan total
                String sqlDetail = "SELECT k.judul, d.jumlah, d.harga, d.subtotal " +
                                "FROM penjualan_detail d JOIN komik k ON d.komik_id = k.id " +
                                "WHERE d.penjualan_id = ?";
                String sqlTotal = "SELECT total_harga, jumlah_bayar FROM penjualan WHERE id = ?";

                try (Connection conn = KoneksiDatabase.getConnection();
                                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                                PreparedStatement psTotal = conn.prepareStatement(sqlTotal)) {

                        // Ambil data total
                        psTotal.setLong(1, penjualanId);
                        ResultSet rsTotal = psTotal.executeQuery();

                        double totalHarga = 0;
                        double jumlahBayar = 0;
                        double kembalian = 0;
                        double diskon = 0;

                        if (rsTotal.next()) {
                                totalHarga = rsTotal.getDouble("total_harga");
                                jumlahBayar = rsTotal.getDouble("jumlah_bayar");
                                // Hitung diskon (10% jika > 200000)
                                if (totalHarga > 200000) {
                                        diskon = totalHarga * 0.10;
                                }
                                double grandTotal = totalHarga - diskon;
                                kembalian = jumlahBayar - grandTotal;
                        }

                        // Header struk
                        sb.append("==========================================\n");
                        sb.append("           TOKO KOMIK JAVA            \n");
                        sb.append("       Jl. Koding No. 1, Bali      \n");
                        sb.append("==========================================\n");
                        sb.append("No Invoice : ").append(noInvoice).append("\n");
                        sb.append("Tanggal    : ").append(tanggal).append("\n");
                        sb.append("Kasir      : ").append(kasir != null ? kasir : "-").append("\n");
                        sb.append("------------------------------------------\n");

                        // Ambil detail item
                        psDetail.setLong(1, penjualanId);
                        ResultSet rsDetail = psDetail.executeQuery();

                        while (rsDetail.next()) {
                                String nama = rsDetail.getString("judul");
                                int qty = rsDetail.getInt("jumlah");
                                double harga = rsDetail.getDouble("harga");
                                double subtotal = rsDetail.getDouble("subtotal");

                                sb.append(nama).append("\n");
                                String rincian = String.format("   %d x %-10s %18s", qty, nf.format(harga),
                                                nf.format(subtotal));
                                sb.append(rincian).append("\n");
                        }

                        // Footer struk
                        sb.append("------------------------------------------\n");
                        sb.append(String.format("Total Belanja  : %24s\n", nf.format(totalHarga)));

                        if (diskon > 0) {
                                sb.append(String.format("Diskon (10%%)   : %24s\n", "-" + nf.format(diskon)));
                        }

                        double grandTotal = totalHarga - diskon;
                        sb.append(String.format("TOTAL HARGA    : %24s\n", nf.format(grandTotal)));
                        sb.append("------------------------------------------\n");
                        sb.append(String.format("Tunai          : %24s\n", nf.format(jumlahBayar)));
                        sb.append(String.format("Kembali        : %24s\n", nf.format(kembalian)));
                        sb.append("==========================================\n");
                        sb.append("    TERIMA KASIH ATAS KUNJUNGAN ANDA      \n");
                        sb.append("==========================================\n");

                        // Tampilkan preview
                        javax.swing.JTextArea textArea = new javax.swing.JTextArea(sb.toString());
                        textArea.setEditable(false);
                        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 12));
                        textArea.setColumns(40);
                        textArea.setRows(20);

                        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);

                        JOptionPane.showMessageDialog(this, scrollPane, "Preview Invoice - " + noInvoice,
                                        JOptionPane.PLAIN_MESSAGE);

                        // Jika langsung cetak PDF atau konfirmasi cetak
                        if (langungCetakPdf) {
                                cetakStrukPDF(noInvoice, sb.toString());
                        } else {
                                int jawab = JOptionPane.showConfirmDialog(this,
                                                "Cetak struk ke PDF?",
                                                "Cetak PDF",
                                                JOptionPane.YES_NO_OPTION);

                                if (jawab == JOptionPane.YES_OPTION) {
                                        cetakStrukPDF(noInvoice, sb.toString());
                                }
                        }

                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private void tampilkanStrukDiLayar(String noInvoice, double subTotal, double diskon, double grandTotal,
                        double bayar, double kembali) {
                StringBuilder sb = new StringBuilder();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                sb.append("==========================================\n");
                sb.append("           TOKO KOMIK JAVA            \n");
                sb.append("       Jl. Koding No. 1, Bali      \n");
                sb.append("==========================================\n");
                sb.append("No Invoice : ").append(noInvoice).append("\n");
                sb.append("Tanggal    : ").append(sdf.format(new java.util.Date())).append("\n");
                sb.append("Kasir      : ").append(Session.getInstance().getUserId()).append("\n");
                sb.append("------------------------------------------\n");

                for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                        String nama = (String) modelKeranjang.getValueAt(i, 1);
                        int qty = (int) modelKeranjang.getValueAt(i, 3);
                        double harga = (double) modelKeranjang.getValueAt(i, 2);
                        double totalItem = (double) modelKeranjang.getValueAt(i, 4);

                        sb.append(nama).append("\n");
                        String rincian = String.format("   %d x %-10s %18s", qty, nf.format(harga),
                                        nf.format(totalItem));
                        sb.append(rincian).append("\n");
                }

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

                javax.swing.JTextArea textArea = new javax.swing.JTextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 12));
                textArea.setColumns(40);
                textArea.setRows(20);

                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);

                JOptionPane.showMessageDialog(this, scrollPane, "Cetak Struk", JOptionPane.PLAIN_MESSAGE);

                // Konfirmasi Cetak PDF
                int jawab = JOptionPane.showConfirmDialog(this,
                                "Cetak struk ke PDF?",
                                "Cetak PDF",
                                JOptionPane.YES_NO_OPTION);

                if (jawab == JOptionPane.YES_OPTION) {
                        // Panggil method pembuatan PDF
                        cetakStrukPDF(noInvoice, sb.toString());
                }
        }

        private void cetakStrukPDF(String noInvoice, String isiStruk) {
                try {
                        // Nama File & Ukuran Kertas
                        String namaFile = "Struk_" + noInvoice + ".pdf";

                        Rectangle pageSize = new Rectangle(PageSize.A6);
                        Document document = new Document(pageSize, 10, 10, 10, 10);

                        PdfWriter.getInstance(document, new FileOutputStream(namaFile));

                        document.open();

                        // Set Font Monospace
                        Font fontStruk = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);

                        // Masukkan isi String ke dalam PDF
                        document.add(new Paragraph(isiStruk, fontStruk));

                        document.close();

                        // Otomatis buka file PDF setelah dibuat
                        if (Desktop.isDesktopSupported()) {
                                File myFile = new File(namaFile);
                                Desktop.getDesktop().open(myFile);
                        } else {
                                JOptionPane.showMessageDialog(this, "Struk PDF berhasil disimpan: " + namaFile);
                        }

                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Gagal mencetak PDF: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        /**
         * @param args the command line arguments
         */
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

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel Container;
        private javax.swing.JLabel JudulText;
        private javax.swing.JButton btnCari;
        private javax.swing.JButton btnFilterHistori;
        private javax.swing.JButton btnHapusDariKeranjang;
        private javax.swing.JButton btnHapusTransaksi;
        private javax.swing.JButton btnCetakInvoice;
        private javax.swing.JButton btnPreviewInvoice;
        private javax.swing.JButton btnKembali;
        private javax.swing.JButton btnKosongkanKeranjang;
        private javax.swing.JButton btnLihatDetail;
        private javax.swing.JButton btnProsesBayar;
        private javax.swing.JButton btnResetHistori;
        private javax.swing.JButton btnTambahKeranjang;
        private javax.swing.JLabel lblJumlah;
        private javax.swing.JLabel lblKomikInfo;
        private javax.swing.JLabel lblTotalBelanja;
        private javax.swing.JPanel panelAksiHistori;
        private javax.swing.JPanel panelAksiKeranjang;
        private javax.swing.JPanel panelCari;
        private javax.swing.JPanel panelFilterHistori;
        private javax.swing.JPanel panelHistori;
        private javax.swing.JPanel panelJumlah;
        private javax.swing.JPanel panelKembali;
        private javax.swing.JPanel panelKomikInfo;
        private javax.swing.JPanel panelTotal;
        private javax.swing.JPanel panelTransaksi;
        private javax.swing.JScrollPane scrollHistori;
        private javax.swing.JScrollPane scrollKeranjang;
        private javax.swing.JTable tabelHistori;
        private javax.swing.JTable tabelKeranjang;
        private javax.swing.JTextField txtCariKomik;
        private javax.swing.JTextField txtFilterHistori;
        private javax.swing.JTextField txtJumlah;
        // End of variables declaration//GEN-END:variables
}
