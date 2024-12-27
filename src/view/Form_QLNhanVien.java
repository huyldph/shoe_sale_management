package view;

import Service.NhanVienService;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.NhanVien;
import repository.Validated;

public class Form_QLNhanVien extends javax.swing.JPanel {

    private final NhanVienService service = new NhanVienService();
    private int row = -1;
    private int pages = 1;
    private final int limit = 5;
    private int numberOfPages = 0;
    private int check;
    private int canExecute = 0;

    public Form_QLNhanVien() {
        initComponents();
        this.fillTable();
        this.loadSearch();
        this.loadFilter();

        btnClear.setVisible(false);
    }

    private void getPages(List<NhanVien> list) {
        if (list.size() % limit == 0) {
            numberOfPages = list.size() / limit;
        } else {
            numberOfPages = (list.size() / limit) + 1;
        }

        lblPages.setText("1");
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);

        String keywod = txtTimKiem.getText();
        List<NhanVien> listPage = service.selectPages(keywod);
        this.getPages(listPage);

        List<NhanVien> list = service.selectKeword(keywod, pages, limit);
        for (NhanVien nhanVien : list) {
            model.addRow(new Object[]{
                nhanVien.getTaiKhoan(),
                nhanVien.getTen(),
                nhanVien.getMatKhau(),
                nhanVien.getSdt(),
                nhanVien.getEmail(),
                nhanVien.loadVaiTro(),
                nhanVien.loadTrangThai()
            });
        }
    }

    private void filter() {
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);

        String keyword = txtTimKiem.getText();

        Integer vaiTro = null;
        Object selectedVaiTro = cbbVT.getSelectedItem();
        if (selectedVaiTro != null && selectedVaiTro instanceof String vt) {
            if ("Quản lý".equals(vt)) {
                vaiTro = 1;
                btnClear.setVisible(true);
            } else if ("Nhân viên".equals(vt)) {
                vaiTro = 0;
                btnClear.setVisible(true);
            } else {
                vaiTro = null;
                btnClear.setVisible(false);
            }
        }

        List<NhanVien> listPage = service.filterPages(keyword, vaiTro);
        this.getPages(listPage);

        List<NhanVien> list = service.filterKeyword(keyword, vaiTro, pages, limit);
        for (NhanVien nhanVien : list) {
            model.addRow(new Object[]{
                nhanVien.getTaiKhoan(),
                nhanVien.getTen(),
                nhanVien.getMatKhau(),
                nhanVien.getSdt(),
                nhanVien.getEmail(),
                nhanVien.loadVaiTro(),
                nhanVien.loadTrangThai()
            });
        }
    }

    private void loadSearch() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fillTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fillTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fillTable();
            }
        });
    }

    private void loadFilter() {
        cbbVT.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                canExecute = 1;
                filter();
                firstPage();
            }
        });
    }

    private void firstPage() {
        pages = 1;
        if (canExecute == 1) {
            this.filter();
        } else {
            this.fillTable();
        }

        lblPages.setText("1");
    }

    private void prevPage() {
        if (pages > 1) {
            pages--;
            if (canExecute == 1) {
                this.filter();
            } else {
                this.fillTable();
            }

            lblPages.setText("" + pages);
        }
    }

    private void nextPage() {
        if (pages < numberOfPages) {
            pages++;
            if (canExecute == 1) {
                this.filter();
            } else {
                this.fillTable();
            }

            lblPages.setText("" + pages);
        }
    }

    private void lastPage() {
        pages = numberOfPages;
        if (canExecute == 1) {
            this.filter();
        } else {
            this.fillTable();
        }

        lblPages.setText("" + pages);
    }

    private void setDataForm(NhanVien nv) {
        txtEmail.setText(nv.getEmail());
        pwdMK.setText(nv.getMatKhau());
        pwdXNMK.setText(nv.getMatKhau());
        txtMa.setText(nv.getTaiKhoan());
        txtSDT.setText(nv.getSdt());
        txtTen.setText(nv.getTen());
        cbbVaiTro.setSelectedItem(nv.loadVaiTro());
        cbbTrangThai.setSelectedItem(nv.loadTrangThai());
    }

    private NhanVien getDataform() {
        char[] passCh = pwdMK.getPassword();
        String pass = new String(passCh);

        char[] xnPassCh = pwdXNMK.getPassword();
        String xnPass = new String(xnPassCh);

        if (!Validated.checkEmpty(txtMa.getText(),
                txtTen.getText(),
                txtEmail.getText(),
                txtSDT.getText(),
                pass, xnPass)) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống form!");
            return null;
        }

        if (!Validated.isEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng email!");
            return null;
        }

        if (!Validated.isPhoneNumber(txtSDT.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số điện thoại!");
            return null;
        }

        if (!Validated.isPassword(passCh)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng mật khẩu từ 6 đến 15 ký tự."
                    + " Trong đó có chứ ít nhất 1 ký tự hoa, 1 ký tự thường, 1 ký tự số!");
            return null;
        }
        if (!pass.equals(xnPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không trùng nhau!");
            return null;
        }

        NhanVien nv = new NhanVien();

        nv.setTaiKhoan(txtMa.getText());
        nv.setTen(txtTen.getText());
        nv.setMatKhau(new String(pwdMK.getPassword()));
        nv.setEmail(txtEmail.getText());
        nv.setSdt(txtSDT.getText());
        String vt = (String) cbbVaiTro.getSelectedItem();
        Integer vaiTro;
        if (vt.equals("Quản lý")) {
            vaiTro = 1;
        } else {
            vaiTro = 0;
        }
        nv.setVaiTro(vaiTro);

        String tt = (String) cbbTrangThai.getSelectedItem();
        Integer trangThai;
        if (tt.equals("Đang đi làm")) {
            trangThai = 0;
        } else {
            trangThai = 1;
        }
        nv.setTrangThai(trangThai);

        return nv;
    }

    private void insert() {
        check = JOptionPane.showConfirmDialog(this, "Bạn thực sự muốn thêm nhân viên này?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        NhanVien nhanVien = this.getDataform();
        try {
            if (nhanVien == null) {
                return;
            }
            service.insert(nhanVien);
            this.fillTable();
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm dữ liệu thất bại!");
        }
    }

    private void update() {
        check = JOptionPane.showConfirmDialog(this, "Bạn thực sự muốn sửa nhân viên này?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        NhanVien nhanVien = this.getDataform();
        try {
            if (nhanVien == null) {
                return;
            }
            service.updateNV(nhanVien);
            this.fillTable();
            JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sửa dữ liệu thất bại!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbbVaiTro = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        pwdMK = new javax.swing.JPasswordField();
        pwdXNMK = new javax.swing.JPasswordField();
        cbbTrangThai = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        cbbVT = new javax.swing.JComboBox<>();
        btnFirst_Product = new javax.swing.JButton();
        btnPrev_Product = new javax.swing.JButton();
        lblPages = new javax.swing.JLabel();
        btnNext_Product = new javax.swing.JButton();
        btnLast_Product = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Quản Lý Nhân Viên");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Mã NV:");

        txtMa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Tên NV:");

        txtTen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTen.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Mật khẩu:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Xác nhận MK:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("SĐT:");

        txtSDT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Vai trò:");

        cbbVaiTro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quản lý", "Nhân viên" }));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Trạng thái");

        pwdMK.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        pwdXNMK.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbbTrangThai.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang đi làm", "Nghỉ việc" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(39, 39, 39)
                        .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207)
                        .addComponent(jLabel6)
                        .addGap(33, 33, 33)
                        .addComponent(txtEmail))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(34, 34, 34)
                        .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207)
                        .addComponent(jLabel7)
                        .addGap(37, 37, 37)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(25, 25, 25)
                        .addComponent(pwdMK, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207)
                        .addComponent(jLabel8)
                        .addGap(25, 25, 25)
                        .addComponent(cbbVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(4, 4, 4)
                        .addComponent(pwdXNMK, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207)
                        .addComponent(jLabel9)
                        .addGap(10, 10, 10)
                        .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(pwdMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8))
                    .addComponent(cbbVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(pwdXNMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel9))
                    .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Xem thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Mật khẩu", "Số điện thoại", "Email", "Vai trò", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblNhanVien);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Tìm kiếm:");

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbbVT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbVT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vai trò", "Quản lý", "Nhân viên" }));

        btnFirst_Product.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnFirst_Product.setText("<<");
        btnFirst_Product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirst_ProductActionPerformed(evt);
            }
        });

        btnPrev_Product.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPrev_Product.setText("<");
        btnPrev_Product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrev_ProductActionPerformed(evt);
            }
        });

        lblPages.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnNext_Product.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNext_Product.setText(">");
        btnNext_Product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNext_ProductActionPerformed(evt);
            }
        });

        btnLast_Product.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLast_Product.setText(">>");
        btnLast_Product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLast_ProductActionPerformed(evt);
            }
        });

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Update.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnFirst_Product)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPrev_Product)
                                .addGap(10, 10, 10)
                                .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNext_Product)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLast_Product))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(cbbVT, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbVT, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnFirst_Product)
                        .addComponent(btnPrev_Product))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNext_Product)
                        .addComponent(btnLast_Product))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        btnThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(394, 394, 394)
                .addComponent(jLabel1))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFirst_ProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirst_ProductActionPerformed
        this.firstPage();
    }//GEN-LAST:event_btnFirst_ProductActionPerformed

    private void btnPrev_ProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrev_ProductActionPerformed
        this.prevPage();
    }//GEN-LAST:event_btnPrev_ProductActionPerformed

    private void btnNext_ProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNext_ProductActionPerformed
        this.nextPage();
    }//GEN-LAST:event_btnNext_ProductActionPerformed

    private void btnLast_ProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLast_ProductActionPerformed
        this.lastPage();
    }//GEN-LAST:event_btnLast_ProductActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        cbbVT.setSelectedIndex(0);
        canExecute = 0;
    }//GEN-LAST:event_btnClearActionPerformed

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        // TODO add your handling code here:
        this.row = tblNhanVien.getSelectedRow();
        String ma = (String) tblNhanVien.getValueAt(row, 0);
        NhanVien nv = service.selectByMa(ma);

        this.setDataForm(nv);
    }//GEN-LAST:event_tblNhanVienMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed

        List<NhanVien> list = service.getAll();
        for (NhanVien nhanVien : list) {
            if (txtMa.getText().equals(nhanVien.getTaiKhoan())) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!");
                return;
            }
            if (txtSDT.getText().equals(nhanVien.getSdt())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại!");
                return;
            }
            if (txtEmail.getText().equals(nhanVien.getEmail())) {
                JOptionPane.showMessageDialog(this, "Email đã tồn tại!");
                return;
            }
        }

        this.insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        this.row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn nhân viên để sửa!");
            return;
        }

        this.update();
    }//GEN-LAST:event_btnSuaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnFirst_Product;
    private javax.swing.JButton btnLast_Product;
    private javax.swing.JButton btnNext_Product;
    private javax.swing.JButton btnPrev_Product;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JComboBox<String> cbbTrangThai;
    private javax.swing.JComboBox<String> cbbVT;
    private javax.swing.JComboBox<String> cbbVaiTro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPages;
    private javax.swing.JPasswordField pwdMK;
    private javax.swing.JPasswordField pwdXNMK;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

}
