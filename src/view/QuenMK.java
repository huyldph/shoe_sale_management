package view;

import Service.NhanVienService;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

import model.NhanVien;
import repository.Validated;

public class QuenMK extends javax.swing.JDialog {

    private int ghiNho;
    private final NhanVienService service = new NhanVienService();
    private List<NhanVien> list = service.getAll();
    private boolean emailFound = false;

    public QuenMK(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);

        //Nhập code
        lblCode.setVisible(false);
        lblGachChan.setVisible(false);
        txtCode.setVisible(false);
        btnOke.setVisible(false);

        //Nhập pass
        lblPass.setVisible(false);
        lblGachChan1.setVisible(false);
        pwdMK.setVisible(false);

        lblXnPass.setVisible(false);
        lblGachChan2.setVisible(false);
        pwdXNMK.setVisible(false);

        btnXacNhan.setVisible(false);
    }

    private void guiMail() {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", 587);

            String username = "ledinhhuy26032004@gmail.com";
            String password = "nhytxypjlhlirshb";

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            String from = "ledinhhuy26032004@gmail.com";
            String to = txtToMail.getText();

            for (NhanVien nv : list) {
                if (to.equals(nv.getEmail())) {
                    emailFound = true;
                    break;
                }
            }

            if (!emailFound) {
                JOptionPane.showMessageDialog(this, "Email chưa được đăng ký!");
                return;
            }


            if (to.trim().isEmpty() || !Validated.isEmail(txtToMail.getText())) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập vào Mail để nhận mã!");
                return;
            }
            String subject = "Resteting code";

            Random random = new Random();
            int randomNumber = 10000 + random.nextInt(90000);
            ghiNho = randomNumber;
            String body = "Your code is " + Integer.toString(randomNumber);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            MimeBodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(body, "text/html; charset=utf-8");

            msg.setSubject(subject);
            msg.setText(body);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(contentPart);
            msg.setContent(multipart);

            Transport.send(msg);
            JOptionPane.showMessageDialog(this, "Gửi mail thành công");

            lblCode.setVisible(true);
            lblGachChan.setVisible(true);
            txtCode.setVisible(true);
            btnOke.setVisible(true);
        } catch (MessagingException e) {
            System.out.println("Gửi email thất bại. Lỗi: " + e.getMessage());
        }
    }

    private void checkCode() {
        if (txtCode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập vào mã!");
            return;
        }
        if (Integer.parseInt(txtCode.getText()) != ghiNho) {
            JOptionPane.showMessageDialog(this, "Mã code không đúng vui lòng nhập lại!");
            return;
        }

        lblPass.setVisible(true);
        lblGachChan1.setVisible(true);
        pwdMK.setVisible(true);

        lblXnPass.setVisible(true);
        lblGachChan2.setVisible(true);
        pwdXNMK.setVisible(true);

        btnXacNhan.setVisible(true);
    }

    private void xacNhan() {
        NhanVien nv = new NhanVien();

        String matKhau = new String(pwdMK.getPassword());
        String xacNhanMatKhau = new String(pwdXNMK.getPassword());

        if (!Validated.checkEmpty(matKhau, xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập vào mật khẩu!");
            return;
        }
        if (!matKhau.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp!");
            return;
        }

        nv.setMatKhau(matKhau);
        nv.setEmail(txtToMail.getText());

        try {
            service.update(nv);
            JOptionPane.showMessageDialog(this, "Cập nhập mật khẩu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cập nhập mật khẩu thất bại!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtToMail = new javax.swing.JTextField();
        btnSendMail = new javax.swing.JButton();
        lblCode = new javax.swing.JLabel();
        lblGachChan = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        btnOke = new javax.swing.JButton();
        lblPass = new javax.swing.JLabel();
        lblGachChan1 = new javax.swing.JLabel();
        pwdMK = new javax.swing.JPasswordField();
        lblXnPass = new javax.swing.JLabel();
        pwdXNMK = new javax.swing.JPasswordField();
        lblGachChan2 = new javax.swing.JLabel();
        btnXacNhan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Quên Mật Khẩu");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/mail.png"))); // NOI18N

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setOpaque(true);

        txtToMail.setBorder(null);
        txtToMail.setOpaque(false);
        txtToMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToMailActionPerformed(evt);
            }
        });

        btnSendMail.setBackground(new java.awt.Color(255, 255, 255));
        btnSendMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Send.png"))); // NOI18N
        btnSendMail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnSendMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMailActionPerformed(evt);
            }
        });

        lblCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Code.png"))); // NOI18N

        lblGachChan.setBackground(new java.awt.Color(0, 0, 0));
        lblGachChan.setOpaque(true);

        txtCode.setBorder(null);
        txtCode.setOpaque(false);
        txtCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodeActionPerformed(evt);
            }
        });

        btnOke.setBackground(new java.awt.Color(255, 255, 255));
        btnOke.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Ok.png"))); // NOI18N
        btnOke.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnOke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkeActionPerformed(evt);
            }
        });

        lblPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Security.png"))); // NOI18N

        lblGachChan1.setBackground(new java.awt.Color(0, 0, 0));
        lblGachChan1.setOpaque(true);

        pwdMK.setBorder(null);
        pwdMK.setOpaque(false);

        lblXnPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Security.png"))); // NOI18N

        pwdXNMK.setBorder(null);
        pwdXNMK.setOpaque(false);

        lblGachChan2.setBackground(new java.awt.Color(0, 0, 0));
        lblGachChan2.setOpaque(true);

        btnXacNhan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXacNhan.setText("Xác Nhận");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(87, 87, 87)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                                        .addComponent(txtToMail))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSendMail, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblCode)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblGachChan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnOke, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblPass)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblGachChan1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                                        .addComponent(pwdMK)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(btnXacNhan)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(lblXnPass)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(lblGachChan2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(pwdXNMK, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(37, 37, 37)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnSendMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtToMail))
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnOke, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(lblGachChan, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblGachChan1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pwdMK))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblXnPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblGachChan2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pwdXNMK, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnXacNhan)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtToMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToMailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToMailActionPerformed

    private void btnSendMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMailActionPerformed
        // TODO add your handling code here:
        this.guiMail();
    }//GEN-LAST:event_btnSendMailActionPerformed

    private void txtCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodeActionPerformed

    private void btnOkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkeActionPerformed
        // TODO add your handling code here:
        this.checkCode();
    }//GEN-LAST:event_btnOkeActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        // TODO add your handling code here:
        this.xacNhan();
    }//GEN-LAST:event_btnXacNhanActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuenMK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuenMK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuenMK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuenMK.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuenMK dialog = new QuenMK(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOke;
    private javax.swing.JButton btnSendMail;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblGachChan;
    private javax.swing.JLabel lblGachChan1;
    private javax.swing.JLabel lblGachChan2;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblXnPass;
    private javax.swing.JPasswordField pwdMK;
    private javax.swing.JPasswordField pwdXNMK;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtToMail;
    // End of variables declaration//GEN-END:variables
}
