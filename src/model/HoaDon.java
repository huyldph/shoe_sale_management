package model;

import java.sql.Date;

public class HoaDon {

    private Integer id;
    private String ma;
    private Date ngayTao;
    private Double tongGia;
    private Double thanhToan;
    private Integer trangThai;
    private Integer idNV;
    private Integer idKH;
    private Integer idVC;
    private NhanVien nv;
    private KhachHang kh;
    private Voucher vc;

    public HoaDon() {
    }

    public HoaDon(Integer id, String ma, Date ngayTao, Double tongGia, Double thanhToan, Integer trangThai, Integer idNV, Integer idKH, Integer idVC, NhanVien nv, KhachHang kh, Voucher vc) {
        this.id = id;
        this.ma = ma;
        this.ngayTao = ngayTao;
        this.tongGia = tongGia;
        this.thanhToan = thanhToan;
        this.trangThai = trangThai;
        this.idNV = idNV;
        this.idKH = idKH;
        this.idVC = idVC;
        this.nv = nv;
        this.kh = kh;
        this.vc = vc;
    }

    public HoaDon(String ma) {
        this.ma = ma;
    }

    public HoaDon(NhanVien nv) {
        this.nv = nv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Double getTongGia() {
        return tongGia;
    }

    public void setTongGia(Double tongGia) {
        this.tongGia = tongGia;
    }

    public Double getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(Double thanhToan) {
        this.thanhToan = thanhToan;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getIdNV() {
        return idNV;
    }

    public void setIdNV(Integer idNV) {
        this.idNV = idNV;
    }

    public Integer getIdKH() {
        return idKH;
    }

    public void setIdKH(Integer idKH) {
        this.idKH = idKH;
    }

    public NhanVien getNv() {
        return nv;
    }

    public void setNv(NhanVien nv) {
        this.nv = nv;
    }

    public KhachHang getKh() {
        return kh;
    }

    public void setKh(KhachHang kh) {
        this.kh = kh;
    }

    public Integer getIdVC() {
        return idVC;
    }

    public void setIdVC(Integer idVC) {
        this.idVC = idVC;
    }

    public Voucher getVc() {
        return vc;
    }

    public void setVc(Voucher vc) {
        this.vc = vc;
    }

    public String loadTrangThaiHD() {
        if (trangThai == 1) {
            return "Đã thanh toán";
        } else if (trangThai == 2) {
            return "Chờ thanh toán";
        } else {
            return "Đã hủy";
        }
    }
}
