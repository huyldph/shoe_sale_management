package Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import model.Voucher;
import repository.JdbcHelper;

public class VoucherService {

    public void insert(Voucher entity) {
        String sql = """
                     INSERT INTO [dbo].[Voucher]
                                           ([ten]
                                           ,[mo_ta]
                                           ,[ngay_tao]
                                           ,[ngay_bat_dau]
                                           ,[ngay_ket_thuc]
                                           ,[so_luong]
                                           ,[kieu_giam]
                                           ,[gia_tri]
                                           ,[trang_thai]
                                           ,[tai_khoan_id])
                          VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                     """;
        JdbcHelper.update(sql,
                entity.getTen(),
                entity.getMoTa(),
                entity.getNgayTao(),
                entity.getNgBatDau(),
                entity.getNgKetThuc(),
                entity.getSoLuong(),
                entity.getKieuGiam(),
                entity.getGiaTri(),
                entity.getTrangThai(),
                entity.getIdNV());
    }

    public void update(Voucher entity) {
        String sql = """
                     UPDATE [dbo].[Voucher]
                        SET ten = ?
                           ,mo_ta = ?
                           ,ngay_tao = ?
                           ,[ngay_bat_dau] = ?
                           ,[ngay_ket_thuc] = ?
                           ,[so_luong] = ?
                           ,[kieu_giam] = ?
                           ,[gia_tri] = ?
                           ,[trang_thai] = ?
                           ,[tai_khoan_id] = ?
                      WHERE ma like ?
                     """;
        JdbcHelper.update(sql,
                entity.getTen(),
                entity.getMoTa(),
                entity.getNgayTao(),
                entity.getNgBatDau(),
                entity.getNgKetThuc(),
                entity.getSoLuong(),
                entity.getKieuGiam(),
                entity.getGiaTri(),
                entity.getTrangThai(),
                entity.getIdNV(),
                entity.getMa());
    }

    public void updateSL(Voucher entity) {
        String sql = """
                     UPDATE [dbo].[Voucher]
                        SET [so_luong] = ?
                           ,[trang_thai] = ?
                      WHERE ma like ?
                     """;
        JdbcHelper.update(sql,
                entity.getSoLuong(),
                entity.getTrangThai(),
                entity.getMa());
    }

    public void delete(String ma) {
        String sql = """
                     delete from KMCT
                     where Ma = ?
                     """;
        JdbcHelper.update(sql, ma);
    }

    protected List<Voucher> selectBySql(String sql, Object... args) {
        List<Voucher> list = new ArrayList<>();

        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                Voucher vcct = new Voucher();
                vcct.setId(rs.getInt("ID"));
                vcct.setMa(rs.getString("ma"));
                vcct.setTen(rs.getString("ten"));
                vcct.setMoTa(rs.getString("mo_ta"));
                vcct.setNgayTao(rs.getDate("ngay_tao"));
                vcct.setSoLuong(rs.getInt("so_luong"));
                vcct.setTrangThai(rs.getInt("trang_thai"));
                vcct.setKieuGiam(rs.getInt("kieu_giam"));
                vcct.setGiaTri(rs.getDouble("gia_tri"));
                vcct.setNgBatDau(rs.getDate("ngay_bat_dau"));
                vcct.setNgKetThuc(rs.getDate("ngay_ket_thuc"));
                vcct.setNv(new NhanVien(rs.getString("TenNV")));

                list.add(vcct);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Voucher selectByMa(String maVCCT) {
        String sql = """
                    SELECT dbo.Voucher.ID, 
                            dbo.Voucher.ma, 
                            dbo.Voucher.ten, 
                            dbo.Voucher.mo_ta, 
                            dbo.Voucher.ngay_tao, 
                            dbo.Voucher.ngay_bat_dau, 
                            dbo.Voucher.ngay_ket_thuc, 
                            dbo.Voucher.so_luong, 
                            dbo.Voucher.kieu_giam, 
                            dbo.Voucher.gia_tri, 
                            dbo.Voucher.trang_thai, 
                            dbo.Taikhoan.ten AS TenNV
                     FROM dbo.Taikhoan 
                     INNER JOIN dbo.Voucher ON dbo.Taikhoan.ID = dbo.Voucher.tai_khoan_id
                     where dbo.Voucher.ma like ?
                     """;
        List<Voucher> list = this.selectBySql(sql, "%" + maVCCT + "%");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<Voucher> selectAll() {
        String sql = """
                    SELECT dbo.Voucher.ID, 
                            dbo.Voucher.ma, 
                            dbo.Voucher.ten, 
                            dbo.Voucher.mo_ta, 
                            dbo.Voucher.ngay_tao, 
                            dbo.Voucher.ngay_bat_dau, 
                            dbo.Voucher.ngay_ket_thuc, 
                            dbo.Voucher.so_luong, 
                            dbo.Voucher.kieu_giam, 
                            dbo.Voucher.gia_tri, 
                            dbo.Voucher.trang_thai, 
                            dbo.Taikhoan.ten AS TenNV
                     FROM dbo.Taikhoan 
                     INNER JOIN dbo.Voucher ON dbo.Taikhoan.ID = dbo.Voucher.tai_khoan_id
                     """;
        return this.selectBySql(sql);
    }

    public List<Voucher> selectByKeyWord(String keyWord) {
        String sql = """
                            SELECT dbo.Voucher.ID, 
                                    dbo.Voucher.ma, 
                                    dbo.Voucher.ten, 
                                    dbo.Voucher.mo_ta, 
                                    dbo.Voucher.ngay_tao, 
                                    dbo.Voucher.ngay_bat_dau, 
                                    dbo.Voucher.ngay_ket_thuc, 
                                    dbo.Voucher.so_luong, 
                                    dbo.Voucher.kieu_giam, 
                                    dbo.Voucher.gia_tri, 
                                    dbo.Voucher.trang_thai, 
                                    dbo.Taikhoan.ten AS TenNV
                             FROM dbo.Taikhoan 
                             INNER JOIN dbo.Voucher ON dbo.Taikhoan.ID = dbo.Voucher.tai_khoan_id
                     WHERE dbo.Voucher.ma like ?
                            OR dbo.Voucher.ten like ?
                            OR dbo.Taikhoan.ten like ?
                     """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%"
        );
    }

    public List<Voucher> searchKeyWord(String keyWord, int pages, int limit) {
        String sql = """
                     SELECT * 
                     FROM 
                     (
                           SELECT dbo.Voucher.ID, 
                                    dbo.Voucher.ma, 
                                    dbo.Voucher.ten, 
                                    dbo.Voucher.mo_ta, 
                                    dbo.Voucher.ngay_tao, 
                                    dbo.Voucher.ngay_bat_dau, 
                                    dbo.Voucher.ngay_ket_thuc, 
                                    dbo.Voucher.so_luong, 
                                    dbo.Voucher.kieu_giam, 
                                    dbo.Voucher.gia_tri, 
                                    dbo.Voucher.trang_thai, 
                                    dbo.Taikhoan.ten AS TenNV
                             FROM dbo.Taikhoan 
                             INNER JOIN dbo.Voucher ON dbo.Taikhoan.ID = dbo.Voucher.tai_khoan_id
                            WHERE dbo.Voucher.ma like ?
                                   OR dbo.Voucher.ten like ?
                                   OR dbo.Taikhoan.ten like ?
                     ) AS FilteredResults
                     ORDER BY ID
                     OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                     """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                (pages - 1) * limit, limit);
    }

    public List<Voucher> FilterPage(String keyWord, Integer thang, Integer nam, Integer trangThai, Integer kieuGiam) {
        String sql = """
                            SELECT dbo.Voucher.ID, 
                                dbo.Voucher.ma, 
                                dbo.Voucher.ten, 
                                dbo.Voucher.mo_ta, 
                                dbo.Voucher.ngay_tao, 
                                dbo.Voucher.ngay_bat_dau, 
                                dbo.Voucher.ngay_ket_thuc, 
                                dbo.Voucher.so_luong, 
                                dbo.Voucher.kieu_giam, 
                                dbo.Voucher.gia_tri, 
                                dbo.Voucher.trang_thai, 
                                dbo.Taikhoan.ten AS TenNV
                         FROM dbo.Taikhoan 
                         INNER JOIN dbo.Voucher ON dbo.Taikhoan.ID = dbo.Voucher.tai_khoan_id
                        WHERE (dbo.Voucher.ma like ? OR dbo.Voucher.ten like ? OR dbo.Taikhoan.ten like ?)
                           and year(dbo.Voucher.ngay_tao) = ?
                           and month(dbo.Voucher.ngay_tao) = ?
                           and dbo.Voucher.trang_thai = ?
                           and dbo.Voucher.kieu_giam = ?
                     """;
        return this.selectBySql(sql,
                "%" + keyWord + "%",
                "%" + keyWord + "%",
                "%" + keyWord + "%",
                nam,
                thang,
                trangThai,
                kieuGiam
        );
    }

    public List<Voucher> FilterData(String keyWord, Integer thang, Integer nam, Integer trangThai, Integer kieuGiam, int pages, int limit) {
        String sql = """
                     SELECT * 
                     FROM 
                     (
                         SELECT dbo.Voucher.ID, 
                                dbo.Voucher.ma, 
                                dbo.Voucher.ten, 
                                dbo.Voucher.mo_ta, 
                                dbo.Voucher.ngay_tao, 
                                dbo.Voucher.ngay_bat_dau, 
                                dbo.Voucher.ngay_ket_thuc, 
                                dbo.Voucher.so_luong, 
                                dbo.Voucher.kieu_giam, 
                                dbo.Voucher.gia_tri, 
                                dbo.Voucher.trang_thai, 
                                dbo.Taikhoan.ten AS TenNV
                         FROM dbo.Taikhoan 
                         INNER JOIN dbo.Voucher ON dbo.Taikhoan.ID = dbo.Voucher.tai_khoan_id
                        WHERE (dbo.Voucher.ma like ? OR dbo.Voucher.ten like ? OR dbo.Taikhoan.ten like ?)
                            and year(dbo.Voucher.ngay_tao) = ?
                            and month(dbo.Voucher.ngay_tao) = ?
                            and dbo.Voucher.trang_thai = ?
                            and dbo.Voucher.kieu_giam = ?
                     ) AS FilteredResults
                     ORDER BY ID
                     OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                     """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                nam,
                thang,
                trangThai,
                kieuGiam,
                (pages - 1) * limit, limit);
    }
}
