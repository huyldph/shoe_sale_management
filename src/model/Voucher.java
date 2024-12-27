package model;

import java.sql.Date;

public class Voucher {

    private Integer id;
    private String ma;
    private String ten;
    private String moTa;
    private Date ngayTao;
    private Date ngBatDau;
    private Date ngKetThuc;
    private Integer soLuong;
    private Integer kieuGiam;
    private Double giaTri;
    private Integer trangThai;
    private Integer idNV;
    private NhanVien nv;

    public Voucher() {
    }

    public Voucher(Integer id, String ma, String ten, String moTa, Date ngayTao, Date ngBatDau, Date ngKetThuc, Integer soLuong, Integer kieuGiam, Double giaTri, Integer trangThai, Integer idNV, NhanVien nv) {
        this.id = id;
        this.ma = ma;
        this.ten = ten;
        this.moTa = moTa;
        this.ngayTao = ngayTao;
        this.ngBatDau = ngBatDau;
        this.ngKetThuc = ngKetThuc;
        this.soLuong = soLuong;
        this.kieuGiam = kieuGiam;
        this.giaTri = giaTri;
        this.trangThai = trangThai;
        this.idNV = idNV;
        this.nv = nv;
    }

    public Voucher(String ma) {
        this.ma = ma;
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

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgBatDau() {
        return ngBatDau;
    }

    public void setNgBatDau(Date ngBatDau) {
        this.ngBatDau = ngBatDau;
    }

    public Date getNgKetThuc() {
        return ngKetThuc;
    }

    public void setNgKetThuc(Date ngKetThuc) {
        this.ngKetThuc = ngKetThuc;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Integer getKieuGiam() {
        return kieuGiam;
    }

    public void setKieuGiam(Integer kieuGiam) {
        this.kieuGiam = kieuGiam;
    }

    public Double getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(Double giaTri) {
        this.giaTri = giaTri;
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

    public NhanVien getNv() {
        return nv;
    }

    public void setNv(NhanVien nv) {
        this.nv = nv;
    }

    public String loadTrangThai() {
        if (trangThai == 1) {
            return "Đang hoạt động";
        }
        return "Ngừng hoạt động";
    }

    public String loadKieuGiam() {
        if (kieuGiam == 1) {
            return "VNĐ";
        }
        return "%";
    }
}
