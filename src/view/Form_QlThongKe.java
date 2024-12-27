package view;

import Service.BillService;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.HoaDon;

public class Form_QlThongKe extends javax.swing.JPanel {

    private final BillService hdService = new BillService();

    public Form_QlThongKe() {
        initComponents();
        this.fillCbbMonth();
        this.fillCbbYear();
        this.loadTableAll();
        this.updateRdoYearOrMonth();
        this.loadCbbYearOrMonth();
        cbbMonth.setVisible(false);
        cbbYear.setVisible(false);
        this.doanhThu();
        this.tongHoaDon();
        this.tongKH();
    }

    private void fillCbbMonth() {
        cbbMonth.addItem("");
        for (int i = 1; i <= 12; i++) {
            cbbMonth.addItem(String.valueOf(i));
        }
    }

    private void fillCbbYear() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbYear.getModel();
        model.removeAllElements();
        model.addElement("");

        List<HoaDon> list = hdService.selectAll();
        Set<Integer> listSet = new HashSet<>();

        for (HoaDon hd : list) {
            // Sử dụng Calendar để trích xuất thông tin về năm
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hd.getNgayTao());
            int year = calendar.get(Calendar.YEAR);

            listSet.add(year);
        }

        for (Integer year : listSet) {
            model.addElement(year);
        }
    }

    private void loadTableAll() {
        DefaultTableModel model = (DefaultTableModel) this.tblThongKe.getModel();
        model.setRowCount(0);

        List<HoaDon> list = hdService.selectAllHD();
        for (HoaDon hd : list) {
            Object[] ob = {
                hd.getMa(),
                hd.getThanhToan(),
                hd.getNgayTao(),
                hd.getNv().getTen(),
                hd.getKh().getTen(),
                hd.loadTrangThaiHD()};
            model.addRow(ob);
        }
    }

    private void loadTableYear() {
        DefaultTableModel model = (DefaultTableModel) this.tblThongKe.getModel();
        model.setRowCount(0);

        Integer year = null;
        if (rdoYear.isSelected()) {
            Object selectedYear = cbbYear.getSelectedItem();
            if (selectedYear instanceof Integer) {
                year = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    year = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectYear(year);
        for (HoaDon hd : list) {
            Object[] ob = {
                hd.getMa(),
                hd.getThanhToan(),
                hd.getNgayTao(),
                hd.getNv().getTen(),
                hd.getKh().getTen(),
                hd.loadTrangThaiHD()};
            model.addRow(ob);
        }
    }

    private void loadTableMonth() {
        DefaultTableModel model = (DefaultTableModel) this.tblThongKe.getModel();
        model.setRowCount(0);

        Integer month = null;
        if (rdoMonth.isSelected()) {
            Object selectedYear = cbbMonth.getSelectedItem();
            if (selectedYear instanceof Integer) {
                month = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    month = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectMonth(month);
        for (HoaDon hd : list) {
            Object[] ob = {
                hd.getMa(),
                hd.getThanhToan(),
                hd.getNgayTao(),
                hd.getNv().getTen(),
                hd.getKh().getTen(),
                hd.loadTrangThaiHD()};
            model.addRow(ob);
        }
    }

    private void updateRdoYearOrMonth() {
        rdoYear.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (rdoYear.isSelected()) {
                    cbbYear.setVisible(true);
                } else {
                    cbbYear.setSelectedIndex(0);
                    cbbYear.setVisible(false);
                }
            }
        });

        rdoMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (rdoMonth.isSelected()) {
                    cbbMonth.setVisible(true);
                } else {
                    cbbMonth.setSelectedIndex(0);
                    cbbMonth.setVisible(false);
                }
            }
        });
    }

    private void loadCbbYearOrMonth() {
        cbbYear.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                loadTableYear();
                doanhThuYear();
                tongHoaDonYear();
                tongKHYear();
            }
        });

        cbbMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                loadTableMonth();
                doanhThuMonth();
                tongHoaDonMonth();
                tongKHMonth();
            }
        });
    }

    private void doanhThu() {
        List<HoaDon> list = hdService.selectAll();
        Double sum = 0.0;
        for (int i = 0; i < list.size(); i++) {
            HoaDon hd = list.get(i);
            if (hd.getThanhToan() > 0) {
                sum = sum + hd.getThanhToan();
            }
        }

        lblDanhThu.setText(String.valueOf(sum));
    }

    private void doanhThuYear() {
        Double sum = 0.0;
        Integer year = null;
        if (rdoYear.isSelected()) {
            Object selectedYear = cbbYear.getSelectedItem();
            if (selectedYear instanceof Integer) {
                year = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    year = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectYear(year);
        for (int i = 0; i < list.size(); i++) {
            HoaDon hd = list.get(i);
            if (hd.getThanhToan() > 0) {
                sum = sum + hd.getThanhToan();
            }
        }

        lblDanhThu.setText(String.valueOf(sum));
    }

    private void doanhThuMonth() {
        Double sum = 0.0;
        Integer month = null;
        if (rdoMonth.isSelected()) {
            Object selectedYear = cbbMonth.getSelectedItem();
            if (selectedYear instanceof Integer) {
                month = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    month = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectMonth(month);
        for (int i = 0; i < list.size(); i++) {
            HoaDon hd = list.get(i);
            if (hd.getThanhToan() > 0) {
                sum = sum + hd.getThanhToan();
            }
        }

        lblDanhThu.setText(String.valueOf(sum));
    }

    private void tongHoaDon() {
        List<HoaDon> list = hdService.selectAll();
        Integer yes = 0;
        Integer no = 0;
        for (int i = 0; i < list.size(); i++) {
            HoaDon hd = list.get(i);
            if (hd.getTrangThai() == 1) {
                yes = yes + 1;
            } else if (hd.getTrangThai() == 3) {
                no = no + 1;
            }
        }

        lblHDThanhCong.setText(String.valueOf(yes));
        lblDonHuy.setText(String.valueOf(no));
    }

    private void tongHoaDonYear() {
        Integer yes = 0;
        Integer no = 0;
        Integer year = null;
        if (rdoYear.isSelected()) {
            Object selectedYear = cbbYear.getSelectedItem();
            if (selectedYear instanceof Integer) {
                year = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    year = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectYear(year);
        for (int i = 0; i < list.size(); i++) {
            HoaDon hd = list.get(i);
            if (hd.getTrangThai() == 1) {
                yes = yes + 1;
            } else if (hd.getTrangThai() == 3) {
                no = no + 1;
            }
        }

        lblHDThanhCong.setText(String.valueOf(yes));
        lblDonHuy.setText(String.valueOf(no));
    }

    private void tongHoaDonMonth() {
        Integer yes = 0;
        Integer no = 0;
        Integer month = null;
        if (rdoMonth.isSelected()) {
            Object selectedYear = cbbMonth.getSelectedItem();
            if (selectedYear instanceof Integer) {
                month = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    month = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectMonth(month);
        for (int i = 0; i < list.size(); i++) {
            HoaDon hd = list.get(i);
            if (hd.getTrangThai() == 1) {
                yes = yes + 1;
            } else if (hd.getTrangThai() == 3) {
                no = no + 1;
            }
        }

        lblHDThanhCong.setText(String.valueOf(yes));
        lblDonHuy.setText(String.valueOf(no));
    }

    private void tongKH() {
        List<HoaDon> list = hdService.selectAll();
        Set<String> liSet = new HashSet<>();

        for (HoaDon hd : list) {
            if (hd.getKh().getTen() != null) {
                liSet.add(hd.getKh().getTen());
            }
        }

        Integer tongKH = 0;
        for (int i = 0; i < liSet.size(); i++) {
            tongKH = tongKH + 1;
        }

        lblKhachMua.setText(String.valueOf(tongKH));
    }

    private void tongKHYear() {
        Integer year = null;
        if (rdoYear.isSelected()) {
            Object selectedYear = cbbYear.getSelectedItem();
            if (selectedYear instanceof Integer) {
                year = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    year = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectYear(year);
        Set<String> liSet = new HashSet<>();

        for (HoaDon hd : list) {
            if (hd.getKh().getTen() != null) {
                liSet.add(hd.getKh().getTen());
            }
        }

        Integer tongKH = 0;
        for (int i = 0; i < liSet.size(); i++) {
            tongKH = tongKH + 1;
        }

        lblKhachMua.setText(String.valueOf(tongKH));
    }

    private void tongKHMonth() {
        Integer month = null;
        if (rdoMonth.isSelected()) {
            Object selectedYear = cbbMonth.getSelectedItem();
            if (selectedYear instanceof Integer) {
                month = (Integer) selectedYear;
            } else if (selectedYear instanceof String && !((String) selectedYear).isEmpty()) {
                try {
                    month = Integer.parseInt((String) selectedYear);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi năm!");
                }
            }
        }

        List<HoaDon> list = hdService.selectMonth(month);
        Set<String> liSet = new HashSet<>();

        for (HoaDon hd : list) {
            if (hd.getKh().getTen() != null) {
                liSet.add(hd.getKh().getTen());
            }
        }

        Integer tongKH = 0;
        for (int i = 0; i < liSet.size(); i++) {
            tongKH = tongKH + 1;
        }

        lblKhachMua.setText(String.valueOf(tongKH));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblDanhThu = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblHDThanhCong = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblDonHuy = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblKhachMua = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThongKe = new javax.swing.JTable();
        rdoAll = new javax.swing.JRadioButton();
        rdoYear = new javax.swing.JRadioButton();
        rdoMonth = new javax.swing.JRadioButton();
        cbbYear = new javax.swing.JComboBox<>();
        cbbMonth = new javax.swing.JComboBox<>();

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Buy.png"))); // NOI18N
        jLabel1.setText("Doanh Thu");

        lblDanhThu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(lblDanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Bill.png"))); // NOI18N
        jLabel3.setText("Đơn Thành Công");

        lblHDThanhCong.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblHDThanhCong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(lblHDThanhCong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHDThanhCong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/CancelBill.png"))); // NOI18N
        jLabel4.setText("Đơn Đã Hủy");

        lblDonHuy.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDonHuy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(lblDonHuy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDonHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Buy.png"))); // NOI18N
        jLabel5.setText("Số Khách Mua");

        lblKhachMua.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblKhachMua.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(lblKhachMua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblKhachMua, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Hóa Đơn", "Tiền Thanh Toán", "Ngày Tạo", "Người Tạo", "Khách Hàng", "Trạng Thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblThongKe);

        buttonGroup1.add(rdoAll);
        rdoAll.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoAll.setSelected(true);
        rdoAll.setText("Tất Cả");
        rdoAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoAllMouseClicked(evt);
            }
        });

        buttonGroup1.add(rdoYear);
        rdoYear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoYear.setText("Năm");
        rdoYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoYearMouseClicked(evt);
            }
        });

        buttonGroup1.add(rdoMonth);
        rdoMonth.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoMonth.setText("Tháng");
        rdoMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoMonthMouseClicked(evt);
            }
        });

        cbbYear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbbMonth.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdoMonth)
                                .addGap(18, 18, 18)
                                .addComponent(cbbMonth, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(rdoAll)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdoYear)
                                .addGap(28, 28, 28)
                                .addComponent(cbbYear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoAll)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoYear)
                    .addComponent(cbbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoMonth)
                    .addComponent(cbbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rdoAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoAllMouseClicked
        // TODO add your handling code here:
        this.loadTableAll();
        this.doanhThu();
        this.tongHoaDon();
        this.tongKH();
    }//GEN-LAST:event_rdoAllMouseClicked

    private void rdoYearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoYearMouseClicked
        // TODO add your handling code here:
        this.loadTableYear();
        this.doanhThuYear();
        this.tongHoaDonYear();
        this.tongKHYear();
    }//GEN-LAST:event_rdoYearMouseClicked

    private void rdoMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoMonthMouseClicked
        // TODO add your handling code here:
        this.loadTableMonth();
        this.doanhThuMonth();
        this.tongHoaDonMonth();
        this.tongKHMonth();
    }//GEN-LAST:event_rdoMonthMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbbMonth;
    private javax.swing.JComboBox<String> cbbYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDanhThu;
    private javax.swing.JLabel lblDonHuy;
    private javax.swing.JLabel lblHDThanhCong;
    private javax.swing.JLabel lblKhachMua;
    private javax.swing.JRadioButton rdoAll;
    private javax.swing.JRadioButton rdoMonth;
    private javax.swing.JRadioButton rdoYear;
    private javax.swing.JTable tblThongKe;
    // End of variables declaration//GEN-END:variables
}
