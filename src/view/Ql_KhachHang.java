package view;

import Service.KhachHangService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.KhachHang;
import repository.Validated;

public class Ql_KhachHang extends javax.swing.JPanel {

    private final KhachHangService service = new KhachHangService();
    private int row = -1;
    private int pages = 1;
    private final int limit = 10;
    private int numberOfPages = 0;
    private int check;

    public Ql_KhachHang() {
        initComponents();
        this.loadSearch();
        this.fillTable();
    }

    private void getPages(List<KhachHang> list) {
        if (list.size() % limit == 0) {
            numberOfPages = list.size() / limit;
        } else {
            numberOfPages = (list.size() / limit) + 1;
        }

        lblPages.setText("1");
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        model.setRowCount(0);

        String keywod = txtTimKiem.getText();
        List<KhachHang> listPage = service.getPages(keywod);
        this.getPages(listPage);

        List<KhachHang> list = service.selectSearch(keywod, pages, limit);
        for (KhachHang khachHang : list) {
            model.addRow(new Object[]{
                khachHang.getMa(),
                khachHang.getTen(),
                khachHang.getSdt()
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

    private void firstPage() {
        pages = 1;
        this.fillTable();

        lblPages.setText("1");
    }

    private void prevPage() {
        if (pages > 1) {
            pages--;
            this.fillTable();

            lblPages.setText("" + pages);
        }
    }

    private void nextPage() {
        if (pages < numberOfPages) {
            pages++;
            this.fillTable();

            lblPages.setText("" + pages);
        }
    }

    private void lastPage() {
        pages = numberOfPages;
        this.fillTable();

        lblPages.setText("" + pages);
    }

    private void setDataForm(KhachHang kh) {
        txtSDT.setText(kh.getSdt());
        txtTen.setText(kh.getTen());
    }

    private KhachHang getDataform() {
        if (!Validated.checkEmpty(txtTen.getText(),
                txtSDT.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống form!");
            return null;
        }

        if (!Validated.isPhoneNumber(txtSDT.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số điện thoại!");
            return null;
        }

        KhachHang kh = new KhachHang();

        kh.setTen(txtTen.getText());
        kh.setSdt(txtSDT.getText());

        return kh;
    }

    private void insert() {
        check = JOptionPane.showConfirmDialog(this, "Bạn thực sự muốn thêm nhân viên này?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        KhachHang khachHang = this.getDataform();
        try {
            if (khachHang == null) {
                return;
            }
            service.insert(khachHang);
            this.fillTable();
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thêm dữ liệu thất bại!");
        }
    }

    private void update() {
        check = JOptionPane.showConfirmDialog(this, "Bạn thực sự muốn sửa khách hàng này?");
        if (check != JOptionPane.YES_OPTION) {
            return;
        }

        KhachHang khachHang = this.getDataform();
        try {
            if (khachHang == null) {
                return;
            }

            String ma = (String) tblKhachHang.getValueAt(row, 0);
            khachHang.setMa(ma);
            service.update(khachHang);
            this.fillTable();
            JOptionPane.showMessageDialog(this, "Sửa dữ liệu thành công !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sửa dữ liệu thất bại!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnFirst_Product = new javax.swing.JButton();
        btnPrev_Product = new javax.swing.JButton();
        lblPages = new javax.swing.JLabel();
        btnNext_Product = new javax.swing.JButton();
        btnLast_Product = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Quản Lý Khách Hàng");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Tên Kh:");

        txtTen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("SĐT:");

        txtSDT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Xem thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblKhachHang);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Tìm kiếm:");

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnFirst_Product)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPrev_Product)
                                .addGap(10, 10, 10)
                                .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNext_Product)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLast_Product))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnFirst_Product)
                        .addComponent(btnPrev_Product))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNext_Product)
                        .addComponent(btnLast_Product))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblPages, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(394, 394, 394)
                .addComponent(jLabel1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 928, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseClicked
        // TODO add your handling code here:
        this.row = tblKhachHang.getSelectedRow();
        String ma = (String) tblKhachHang.getValueAt(row, 0);
        KhachHang nv = service.selectByMa(ma);

        this.setDataForm(nv);
    }//GEN-LAST:event_tblKhachHangMouseClicked

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

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed

        List<KhachHang> list = service.selectAll();
        for (KhachHang kh : list) {
            if (txtSDT.getText().equals(kh.getSdt())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại!");
                return;
            }
        }

        this.insert();
    }//GEN-LAST:event_btnThemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst_Product;
    private javax.swing.JButton btnLast_Product;
    private javax.swing.JButton btnNext_Product;
    private javax.swing.JButton btnPrev_Product;
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPages;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
