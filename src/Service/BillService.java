package Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;
import model.KhachHang;
import model.NhanVien;
import model.Voucher;
import repository.JdbcHelper;

public class BillService {

    public void insert(HoaDon entity) {
        String sql = """
                     INSERT INTO [dbo].[HoaDon]
                                ([ngay_tao]
                                ,[tong_gia]
                                ,[thanhToan]
                                ,[trang_thai]
                                ,[tai_khoan_id]
                                ,[khach_hang_id]
                                ,[ID_Voucher])
                          VALUES(?, ?, ?, ?, ?, ?, ?)
                     """;
        JdbcHelper.update(sql,
                entity.getNgayTao(),
                entity.getTongGia(),
                entity.getThanhToan(),
                entity.getTrangThai(),
                entity.getIdNV(),
                entity.getIdKH(),
                entity.getIdVC());
    }

    public void update(HoaDon entity) {
        String sql = """
                     UPDATE [dbo].[HoaDon]
                        SET [tong_gia] = ?
                           ,[thanhToan] = ?
                           ,[trang_thai] = ?
                           ,[khach_hang_id] = ?
                           ,[ID_Voucher] = ?
                      WHERE ID = ?
                     """;
        JdbcHelper.update(sql,
                entity.getTongGia(),
                entity.getThanhToan(),
                entity.getTrangThai(),
                entity.getIdKH(),
                entity.getIdVC(),
                entity.getId());
    }

    public HoaDon selectById(Integer id) {
        String sql = """
                     SELECT 
                         hd.ID, 
                         hd.ma_hoa_don As Ma, 
                         nv.ten AS TenNV,
                         hd.ngay_tao AS NgayTao, 
                         hd.tong_gia AS TongTien, 
                         hd.trang_thai AS TrangThai
                     FROM 
                         dbo.HoaDon hd
                     JOIN 
                         dbo.TaiKhoan nv ON hd.tai_khoan_id = nv.ID
                     WHERE hd.ID = ?
                     """;
        List<HoaDon> list = this.selectBySql(sql, id);

        if (list == null) {
            return null;
        }
        return list.get(0);
    }

    public HoaDon selectByMa(String ma) {
        String sql = """
                     SELECT 
                            hd.ID, 
                            hd.ma_hoa_don AS Ma, 
                            nv.ten AS TenNV,
                            hd.ngay_tao AS NgayTao, 
                            hd.tong_gia AS TongTien, 
                            hd.trang_thai AS TrangThai
                        FROM 
                            dbo.HoaDon hd
                        JOIN 
                            dbo.TaiKhoan nv ON hd.tai_khoan_id = nv.ID
                     WHERE hd.ma_hoa_don LIKE ?
                     """;
        List<HoaDon> list = this.selectBySql(sql, "%" + ma + "%");

        if (list == null) {
            return null;
        }
        return list.get(0);
    }

    public List<HoaDon> selectAllHD() {
        String sql = """
                     SELECT 
                         dbo.HoaDon.ma_hoa_don, 
                         dbo.HoaDon.ngay_tao, 
                         dbo.HoaDon.tong_gia, 
                         dbo.HoaDon.thanhToan, 
                         dbo.Taikhoan.ten AS ten_tai_khoan, 
                         CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.ten ELSE NULL END AS ten_khach_hang, 
                         CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.SDT ELSE NULL END AS SDT, 
                         CASE WHEN dbo.HoaDon.ID_Voucher IS NOT NULL THEN dbo.Voucher.ma ELSE NULL END AS ma_voucher, 
                         dbo.HoaDon.trang_thai
                     FROM 
                         dbo.HoaDon 
                     INNER JOIN 
                         dbo.Taikhoan ON dbo.HoaDon.tai_khoan_id = dbo.Taikhoan.ID 
                     LEFT JOIN 
                         dbo.KhachHang ON dbo.HoaDon.khach_hang_id = dbo.KhachHang.ID 
                     LEFT JOIN 
                         dbo.Voucher ON dbo.HoaDon.ID_Voucher = dbo.Voucher.ID
                     where dbo.HoaDon.trang_thai = 1 or dbo.HoaDon.trang_thai = 3
                     """;
        return this.selectBySqlAll(sql);
    }

    public List<HoaDon> selectAll() {
        String sql = """
                     SELECT 
                         dbo.HoaDon.ma_hoa_don, 
                         dbo.HoaDon.ngay_tao, 
                         dbo.HoaDon.tong_gia, 
                         dbo.HoaDon.thanhToan, 
                         dbo.Taikhoan.ten AS ten_tai_khoan, 
                         CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.ten ELSE NULL END AS ten_khach_hang, 
                         CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.SDT ELSE NULL END AS SDT, 
                         CASE WHEN dbo.HoaDon.ID_Voucher IS NOT NULL THEN dbo.Voucher.ma ELSE NULL END AS ma_voucher, 
                         dbo.HoaDon.trang_thai
                     FROM 
                         dbo.HoaDon 
                     INNER JOIN 
                         dbo.Taikhoan ON dbo.HoaDon.tai_khoan_id = dbo.Taikhoan.ID 
                     LEFT JOIN 
                         dbo.KhachHang ON dbo.HoaDon.khach_hang_id = dbo.KhachHang.ID 
                     LEFT JOIN 
                         dbo.Voucher ON dbo.HoaDon.ID_Voucher = dbo.Voucher.ID
                     """;
        return this.selectBySqlAll(sql);
    }

    public List<HoaDon> selectSearch(String keyWord) {
        String sql = """
                     SELECT 
                         dbo.HoaDon.ma_hoa_don, 
                         dbo.HoaDon.ngay_tao, 
                         dbo.HoaDon.tong_gia, 
                         dbo.HoaDon.thanhToan, 
                         dbo.Taikhoan.ten AS ten_tai_khoan, 
                         CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.ten ELSE NULL END AS ten_khach_hang, 
                         CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.SDT ELSE NULL END AS SDT, 
                         CASE WHEN dbo.HoaDon.ID_Voucher IS NOT NULL THEN dbo.Voucher.ma ELSE NULL END AS ma_voucher, 
                         dbo.HoaDon.trang_thai
                     FROM 
                         dbo.HoaDon 
                     INNER JOIN 
                         dbo.Taikhoan ON dbo.HoaDon.tai_khoan_id = dbo.Taikhoan.ID 
                     LEFT  JOIN 
                         dbo.KhachHang ON dbo.HoaDon.khach_hang_id = dbo.KhachHang.ID 
                     LEFT JOIN 
                             dbo.Voucher ON dbo.HoaDon.ID_Voucher = dbo.Voucher.ID
                     Where dbo.HoaDon.ma_hoa_don like ?
                        or dbo.Taikhoan.ten like ?
                        or dbo.KhachHang.ten like ?
                        or dbo.KhachHang.SDT like ?
                     """;
        return this.selectBySqlAll(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%"
        );
    }

    public List<HoaDon> selectFilter(String keyWord, Integer trangThai, Double tongTien, Integer thang, Integer nam) {
        String sql = """
                 SELECT 
                     dbo.HoaDon.ma_hoa_don, 
                     dbo.HoaDon.ngay_tao, 
                     dbo.HoaDon.tong_gia, 
                     dbo.HoaDon.thanhToan, 
                     dbo.Taikhoan.ten AS ten_tai_khoan, 
                     CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.ten ELSE NULL END AS ten_khach_hang, 
                     CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.SDT ELSE NULL END AS SDT, 
                     CASE WHEN dbo.HoaDon.ID_Voucher IS NOT NULL THEN dbo.Voucher.ma ELSE NULL END AS ma_voucher, 
                     dbo.HoaDon.trang_thai
                 FROM 
                     dbo.HoaDon 
                 INNER JOIN 
                     dbo.Taikhoan ON dbo.HoaDon.tai_khoan_id = dbo.Taikhoan.ID 
                 LEFT JOIN 
                     dbo.KhachHang ON dbo.HoaDon.khach_hang_id = dbo.KhachHang.ID 
                 LEFT JOIN 
                     dbo.Voucher ON dbo.HoaDon.ID_Voucher = dbo.Voucher.ID
                 Where (dbo.HoaDon.ma_hoa_don like ?
                    or dbo.Taikhoan.ten like ?
                    or dbo.KhachHang.ten like ?
                    or dbo.KhachHang.SDT like ?)
                 and (? IS NULL OR dbo.HoaDon.trang_thai = ?)
                 and (? IS NULL OR dbo.HoaDon.tong_gia = ?)
                 and (? IS NULL OR Month(dbo.HoaDon.ngay_tao) = ?)
                 and (? IS NULL OR year(dbo.HoaDon.ngay_tao) = ?)
                 """;

        return this.selectBySqlAll(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                trangThai, trangThai,
                tongTien, tongTien,
                thang, thang,
                nam, nam
        );
    }

    public List<HoaDon> selectYear(Integer year) {
        String sql = """
                 SELECT 
                     dbo.HoaDon.ma_hoa_don, 
                     dbo.HoaDon.ngay_tao, 
                     dbo.HoaDon.tong_gia, 
                     dbo.HoaDon.thanhToan, 
                     dbo.Taikhoan.ten AS ten_tai_khoan, 
                     CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.ten ELSE NULL END AS ten_khach_hang, 
                     CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.SDT ELSE NULL END AS SDT, 
                     CASE WHEN dbo.HoaDon.ID_Voucher IS NOT NULL THEN dbo.Voucher.ma ELSE NULL END AS ma_voucher, 
                     dbo.HoaDon.trang_thai
                 FROM 
                     dbo.HoaDon 
                 INNER JOIN 
                     dbo.Taikhoan ON dbo.HoaDon.tai_khoan_id = dbo.Taikhoan.ID 
                 LEFT JOIN 
                     dbo.KhachHang ON dbo.HoaDon.khach_hang_id = dbo.KhachHang.ID 
                 LEFT JOIN 
                     dbo.Voucher ON dbo.HoaDon.ID_Voucher = dbo.Voucher.ID
                 WHERE YEAR(dbo.HoaDon.ngay_tao) = ?
                         and (dbo.HoaDon.trang_thai = 1 or dbo.HoaDon.trang_thai = 3)
                 """;
        return this.selectBySqlAll(sql, year);
    }

    public List<HoaDon> selectMonth(Integer month) {
        String sql = """
                 SELECT 
                     dbo.HoaDon.ma_hoa_don, 
                     dbo.HoaDon.ngay_tao, 
                     dbo.HoaDon.tong_gia, 
                     dbo.HoaDon.thanhToan, 
                     dbo.Taikhoan.ten AS ten_tai_khoan, 
                     CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.ten ELSE NULL END AS ten_khach_hang, 
                     CASE WHEN dbo.HoaDon.khach_hang_id IS NOT NULL THEN dbo.KhachHang.SDT ELSE NULL END AS SDT, 
                     CASE WHEN dbo.HoaDon.ID_Voucher IS NOT NULL THEN dbo.Voucher.ma ELSE NULL END AS ma_voucher, 
                     dbo.HoaDon.trang_thai
                 FROM 
                     dbo.HoaDon 
                 INNER JOIN 
                     dbo.Taikhoan ON dbo.HoaDon.tai_khoan_id = dbo.Taikhoan.ID 
                 LEFT JOIN 
                     dbo.KhachHang ON dbo.HoaDon.khach_hang_id = dbo.KhachHang.ID 
                 LEFT JOIN 
                     dbo.Voucher ON dbo.HoaDon.ID_Voucher = dbo.Voucher.ID
                 WHERE Month(dbo.HoaDon.ngay_tao) = ?
                         and (dbo.HoaDon.trang_thai = 1 or dbo.HoaDon.trang_thai = 3)
                 """;
        return this.selectBySqlAll(sql, month);
    }

    protected List<HoaDon> selectBySqlAll(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();

        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                HoaDon hd = new HoaDon();

                hd.setMa(rs.getString("ma_hoa_don"));
                hd.setNgayTao(rs.getDate("ngay_tao"));
                hd.setTongGia(rs.getDouble("tong_gia"));
                hd.setThanhToan(rs.getDouble("thanhToan"));
                hd.setTrangThai(rs.getInt("trang_thai"));
                hd.setNv(new NhanVien(rs.getString("ten_tai_khoan")));
                hd.setKh(new KhachHang(rs.getString("ten_khach_hang"), rs.getString("SDT")));
                hd.setVc(new Voucher(rs.getString("ma_voucher")));
                list.add(hd);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    protected List<HoaDon> selectBySql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();

        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                HoaDon hd = new HoaDon();

                hd.setId(rs.getInt("ID"));
                hd.setMa(rs.getString("Ma"));
                hd.setNgayTao(rs.getDate("NgayTao"));
                hd.setTongGia(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getInt("TrangThai"));
                hd.setNv(new NhanVien(rs.getString("TenNV")));

                list.add(hd);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public List<HoaDon> selectByStatus() {
        String sql = """
                     SELECT 
                            hd.ID, 
                            hd.ma_hoa_don AS Ma, 
                            nv.ten AS TenNV,
                            hd.ngay_tao AS NgayTao, 
                            hd.tong_gia AS TongTien, 
                            hd.trang_thai AS TrangThai
                        FROM 
                            dbo.HoaDon hd
                        JOIN 
                            dbo.TaiKhoan nv ON hd.tai_khoan_id = nv.ID
                     WHERE hd.trang_thai = 2
                     """;
        return this.selectBySql(sql);
    }

}
