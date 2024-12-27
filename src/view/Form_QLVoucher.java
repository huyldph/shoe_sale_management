package view;

import Service.NhanVienService;
import Service.VoucherService;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.NhanVien;
import model.Voucher;
import repository.Auth;
import repository.Validated;

public class Form_QLVoucher extends javax.swing.JPanel {

    private final VoucherService service = new VoucherService();
    private final NhanVienService nvSerivce = new NhanVienService();
    private int row = -1;
    private int pages = 1;
    private final int limit = 5;
    private int numberOfPages;
    private int check;
    private int canExecute = 0;

    public Form_QLVoucher() {
        initComponents();
        this.fillTable();
        this.loadSearch();
        this.updateStatusFilter();
    }

    private void getPages(List<Voucher> list) {
        if (list.size() % limit == 0) {
            numberOfPages = list.size() / limit;
        } else {
            numberOfPages = (list.size() / limit) + 1;
        }

        lblPages.setText("1");
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblVoucher.getModel();
        model.setRowCount(0);

        try {
            String keyword = txtSearch.getText();
            List<Voucher> listPage = service.selectByKeyWord(keyword);
            this.getPages(listPage);

            List<Voucher> list = service.searchKeyWord(keyword, pages, limit);
            for (Voucher vc : list) {
                model.addRow(new Object[]{
                    vc.getMa(),
                    vc.getTen(),
                    vc.getNgayTao(),
                    vc.getSoLuong(),
                    vc.loadKieuGiam(),
                    vc.getGiaTri(),
                    vc.getNgBatDau(),
                    vc.getNgKetThuc(),
                    vc.getMoTa(),
                    vc.getNv().getTen(),
                    vc.loadTrangThai()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void filterData() {
        DefaultTableModel model = (DefaultTableModel) tblVoucher.getModel();
        model.setRowCount(0);

        try {
            String keyword = txtSearch.getText();

            Integer nam = ychNam.getYear();

            Integer monthIndex = MchThang.getMonth();
            Integer thang = monthIndex + 1;

            String tt = (String) cbbTTFilter.getSelectedItem();
            Integer trangThai = tt.equals("Đang hoạt động") ? 1 : 2;

            String kg = (String) cbbKGFilter.getSelectedItem();
            Integer kieuGiam = kg.equals("VNĐ") ? 1 : 2;

            List<Voucher> listPage = service.FilterPage(keyword, thang, nam, trangThai, kieuGiam);
            this.getPages(listPage);

            List<Voucher> list = service.FilterData(keyword, thang, nam, trangThai, kieuGiam, pages, limit);
            for (Voucher vc : list) {
                model.addRow(new Object[]{
                    vc.getMa(),
                    vc.getTen(),
                    vc.getNgayTao(),
                    vc.getSoLuong(),
                    vc.loadKieuGiam(),
                    vc.getGiaTri(),
                    vc.getNgBatDau(),
                    vc.getNgKetThuc(),
                    vc.getMoTa(),
                    vc.getNv().getTen(),
                    vc.loadTrangThai()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void setDataForm(Voucher vc) {
        txtTen.setText(vc.getTen());
        txtSoLuong.setText(String.valueOf(vc.getSoLuong()));
        txtGiaTri.setText(String.valueOf(vc.getGiaTri()));
        txtMoTa.setText(vc.getMoTa());
        cbbTrangThai.setSelectedItem(vc.loadTrangThai());
        cbbKieuGiam.setSelectedItem(vc.loadKieuGiam());
        dchNgBatDau.setDate(vc.getNgBatDau());
        dchNgKetThuc.setDate(vc.getNgKetThuc());
    }

    private void editForm() {
        String ma = (String) tblVoucher.getValueAt(row, 0);
        Voucher vc = service.selectByMa(ma);
        this.setDataForm(vc);
    }

    private void clean() {
        txtSoLuong.setText("");
        txtTen.setText("");
        txtGiaTri.setText("");
        txtMoTa.setText("");
        dchNgBatDau.setToolTipText(null);
        dchNgKetThuc.setToolTipText(null);
        cbbTrangThai.setSelectedIndex(0);
        cbbKieuGiam.setSelectedIndex(0);
    }

    //Start phân trang---
    private void firstPage() {
        pages = 1;
        if (canExecute == 0) {
            this.fillTable();
        } else {
            this.filterData();
        }

        lblPages.setText("1");
    }

    private void prevPage() {
        if (pages > 1) {
            pages--;
            if (canExecute == 0) {
                this.fillTable();
            } else {
                this.filterData();
            }

            lblPages.setText("" + pages);
        }
    }

    private void nextPage() {
        if (pages < numberOfPages) {
            pages++;
            if (canExecute == 0) {
                this.fillTable();
            } else {
                this.filterData();
            }

            lblPages.setText("" + pages);
        }
    }

    private void lastPage() {
        pages = numberOfPages;
        if (canExecute == 0) {
            this.fillTable();
        } else {
            this.filterData();
        }

        lblPages.setText("" + pages);
    }

    private void loadSearch() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fillTable();
                firstPage();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fillTable();
                firstPage();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fillTable();
                firstPage();
            }
        });
    }

    private Voucher getData() {
        if (!Validated.checkEmpty(txtTen.getText(), txtSoLuong.getText(), txtGiaTri.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống form!");
            return null;
        }

        if (!Validated.isNumericInt(txtSoLuong.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lại số lượng!");
            return null;
        }

        if (!Validated.isNumericDouble(txtGiaTri.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lại giá trị giảm!");
            return null;
        }

        Voucher vc = new Voucher();

        vc.setTen(txtTen.getText());
        vc.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        vc.setMoTa(txtMoTa.getText());

        Date currentDate = new Date();
        vc.setNgayTao(new java.sql.Date(currentDate.getTime()));

        String maNV = Auth.user.getTaiKhoan();
        NhanVien nv = nvSerivce.selectByMa(maNV);
        vc.setIdNV(nv.getId());

        String status = (String) cbbTrangThai.getSelectedItem();
        vc.setTrangThai(status.equals("Đang hoạt động") ? 1 : 2);
        Integer sl = Integer.parseInt(txtSoLuong.getText());
        if (sl == 0) {
            Integer trangThai = 2;
            vc.setTrangThai(trangThai);
        }

        String kg = (String) cbbKieuGiam.getSelectedItem();
        Integer kieuGiam = (kg.equals("VNĐ")) ? 1 : 2;

        vc.setKieuGiam(kieuGiam);

        if (kg.equals("%") && Double.parseDouble(txtGiaTri.getText()) > 100) {
            JOptionPane.showMessageDialog(this, "Bạn không được giảm quá 100 %");
            return null;
        }

        vc.setGiaTri(Double.parseDouble(txtGiaTri.getText()));

        vc.setNgBatDau(new java.sql.Date(dchNgBatDau.getDate().getTime()));
        vc.setNgKetThuc(new java.sql.Date(dchNgKetThuc.getDate().getTime()));

        return vc;
    }

    private void insert() {
        check = JOptionPane.showConfirmDialog(this, "Xác nhận thêm dữ liệu?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Voucher vc = this.getData();

            if (vc == null) {
                return;
            }

            service.insert(vc);
            this.fillTable();
            this.clean();
            JOptionPane.showMessageDialog(this, "Thêm dữ liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void update() {
        check = JOptionPane.showConfirmDialog(this, "Xác nhận sửa dữ liệu?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Voucher vc = this.getData();

            if (vc == null) {
                return;
            }

            String ma = (String) tblVoucher.getValueAt(row, 0);
            vc.setMa(ma);
            service.update(vc);
            this.fillTable();
            JOptionPane.showMessageDialog(this, "Sửa dữ liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void updateStatusFilter() {
        Boolean checkStatus = (canExecute == 1);
        btnCleanFilter.setEnabled(checkStatus);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdd = new javax.swing.JButton();
        btnNextPages = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnFirstPages = new javax.swing.JButton();
        btnLastPages = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btnFilter = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnCleanFilter = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVoucher = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        cbbKieuGiam = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtGiaTri = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        dchNgBatDau = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        dchNgKetThuc = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        cbbTrangThai = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        btnBackPages = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        lblPages = new javax.swing.JLabel();
        ychNam = new com.toedter.calendar.JYearChooser();
        jLabel10 = new javax.swing.JLabel();
        MchThang = new com.toedter.calendar.JMonthChooser();
        jLabel13 = new javax.swing.JLabel();
        cbbTTFilter = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cbbKGFilter = new javax.swing.JComboBox<>();

        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Add.png"))); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnNextPages.setText(">");
        btnNextPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextPagesActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Quản Lý Voucher");

        btnFirstPages.setText("<<");
        btnFirstPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstPagesActionPerformed(evt);
            }
        });

        btnLastPages.setText(">>");
        btnLastPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastPagesActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Tìm kiếm");

        btnFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Filters.png"))); // NOI18N
        btnFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCleanFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCleanFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Clean.png"))); // NOI18N
        btnCleanFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnCleanFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanFilterActionPerformed(evt);
            }
        });

        tblVoucher.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Ten", "Ngày tạo", "Số lượng", "Kiểu giảm", "Giá trị", "Ngày bắt đầu", "Ngày kết thúc", "Mô tả", "Nhân viên tạo", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVoucherMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVoucher);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Tên Voucher");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Số lượng");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Mô tả");

        txtMoTa.setColumns(20);
        txtMoTa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Kiểu giảm");

        cbbKieuGiam.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbKieuGiam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "VNĐ", "%" }));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Giá trị");

        txtGiaTri.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Ngày bắt đầu");

        dchNgBatDau.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Ngày kết thúc");

        dchNgKetThuc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Trạng thái");

        cbbTrangThai.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSoLuong)
                    .addComponent(txtTen)
                    .addComponent(cbbKieuGiam, 0, 250, Short.MAX_VALUE)
                    .addComponent(txtGiaTri))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel6))
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dchNgKetThuc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbbTrangThai, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(dchNgBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dchNgBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dchNgKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbbKieuGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Năm");

        btnBackPages.setText("<");
        btnBackPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackPagesActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Update.png"))); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        lblPages.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Tháng");

        MchThang.setMonth(0);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Trạng thái");

        cbbTTFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbTTFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" }));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Kiểu giảm");

        cbbKGFilter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbKGFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "VNĐ", "%" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(481, 481, 481)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnFirstPages)
                                .addGap(10, 10, 10)
                                .addComponent(btnBackPages)
                                .addGap(18, 18, 18)
                                .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(btnNextPages)
                                .addGap(12, 12, 12)
                                .addComponent(btnLastPages)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel9)
                        .addGap(10, 10, 10)
                        .addComponent(ychNam, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addGap(10, 10, 10)
                        .addComponent(MchThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addGap(4, 4, 4)
                        .addComponent(cbbTTFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addGap(10, 10, 10)
                        .addComponent(cbbKGFilter, 0, 71, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCleanFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(ychNam, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(MchThang, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(cbbTTFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbbKGFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCleanFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFirstPages)
                    .addComponent(btnBackPages)
                    .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNextPages)
                    .addComponent(btnLastPages))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        if (!Auth.isManager()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền sử dụng chức năng này!");
            return;
        }
        this.insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnNextPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextPagesActionPerformed
        // TODO add your handling code here:
        this.nextPage();
    }//GEN-LAST:event_btnNextPagesActionPerformed

    private void btnFirstPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstPagesActionPerformed
        // TODO add your handling code here:
        this.firstPage();
    }//GEN-LAST:event_btnFirstPagesActionPerformed

    private void btnLastPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastPagesActionPerformed
        // TODO add your handling code here:
        this.lastPage();
    }//GEN-LAST:event_btnLastPagesActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        canExecute = 1;

        this.filterData();
        this.firstPage();
        this.updateStatusFilter();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnCleanFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanFilterActionPerformed
        // TODO add your handling code here:
        canExecute = 0;
        this.fillTable();
        this.firstPage();
        this.updateStatusFilter();
    }//GEN-LAST:event_btnCleanFilterActionPerformed

    private void tblVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVoucherMouseClicked
        // TODO add your handling code here:
        this.row = tblVoucher.getSelectedRow();
        this.editForm();
    }//GEN-LAST:event_tblVoucherMouseClicked

    private void btnBackPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackPagesActionPerformed
        // TODO add your handling code here:
        this.prevPage();
    }//GEN-LAST:event_btnBackPagesActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        if (!Auth.isManager()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền sử dụng chức năng này!");
            return;
        }
        this.row = tblVoucher.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }

        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JMonthChooser MchThang;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBackPages;
    private javax.swing.JButton btnCleanFilter;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnFirstPages;
    private javax.swing.JButton btnLastPages;
    private javax.swing.JButton btnNextPages;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbbKGFilter;
    private javax.swing.JComboBox<String> cbbKieuGiam;
    private javax.swing.JComboBox<String> cbbTTFilter;
    private javax.swing.JComboBox<String> cbbTrangThai;
    private com.toedter.calendar.JDateChooser dchNgBatDau;
    private com.toedter.calendar.JDateChooser dchNgKetThuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPages;
    private javax.swing.JTable tblVoucher;
    private javax.swing.JTextField txtGiaTri;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTen;
    private com.toedter.calendar.JYearChooser ychNam;
    // End of variables declaration//GEN-END:variables
}
