package Service;

import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import repository.JdbcHelper;

public class NhanVienService {

    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("ID"));
                nv.setTaiKhoan(rs.getString("tai_khoan"));
                nv.setTen(rs.getString("ten"));
                nv.setDiaChi(rs.getString("dia_chi"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("email"));
                nv.setMatKhau(rs.getString("mat_khau"));
                nv.setVaiTro(rs.getInt("vai_tro"));
                nv.setTrangThai(rs.getInt("trang_thai"));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<NhanVien> getAll() {
        String sql = "select * from Taikhoan";
        return this.selectBySql(sql);
    }

    public NhanVien selectByMa(String ma) {
        String selectByMa = """
                        select * from Taikhoan
                        WHERE tai_khoan = ?
                        """;
        List<NhanVien> list = this.selectBySql(selectByMa, ma);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<NhanVien> selectPages(String keyWord) {
        String sql = """
                        select * from Taikhoan
                        WHERE tai_khoan like ?
                                or ten like ?
                                or SDT like ?
                        """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%"
        );
    }

    public List<NhanVien> selectKeword(String keyWord, int page, int limit) {
        String sql = """
                      SELECT * 
                        FROM 
                        (
                            select * from Taikhoan
                            WHERE tai_khoan like ?
                                or ten like ?
                                or SDT like ?
                     ) AS FilteredResults
                        ORDER BY ID
                        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                        """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                (page - 1) * limit,
                limit
        );
    }

    public List<NhanVien> filterPages(String keyWord, Integer vaiTro) {
        String sql = """
                        select * from Taikhoan
                        WHERE (tai_khoan like ? or ten like ? or SDT like ?)
                                and (? is null or vai_tro = ?)
                        """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                vaiTro, vaiTro
        );
    }

    public List<NhanVien> filterKeyword(String keyWord, Integer vaiTro, int page, int limit) {
        String sql = """
                      SELECT * 
                        FROM 
                        (
                            select * from Taikhoan
                            WHERE (tai_khoan like ? or ten like ? or SDT like ?)
                                    and (? is null or vai_tro = ?)
                     ) AS FilteredResults
                        ORDER BY ID
                        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                        """;
        return this.selectBySql(sql,
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                "%" + keyWord + "%%",
                vaiTro, vaiTro,
                (page - 1) * limit,
                limit
        );
    }

    public void insert(NhanVien entity) {
        String sql = """
                     INSERT INTO [dbo].[Taikhoan]
                                 ([tai_khoan]
                                 ,[mat_khau]
                                 ,[ten]
                                 ,[SDT]
                                 ,[email]
                                 ,[vai_tro]
                                 ,[trang_thai])
                           VALUES (?, ?, ?, ?, ?, ?, ?)
                     """;
        JdbcHelper.update(sql,
                entity.getTaiKhoan(),
                entity.getMatKhau(),
                entity.getTen(),
                entity.getSdt(),
                entity.getEmail(),
                entity.getVaiTro(),
                entity.getTrangThai());
    }

    public void updateNV(NhanVien entity) {
        String sql = """
                     UPDATE [dbo].[Taikhoan]
                     SET [mat_khau] = ?
                        ,[ten] = ?
                        ,[SDT] = ?
                        ,[email] = ?
                        ,[vai_tro] = ?
                        ,[trang_thai] = ?
                      WHERE tai_khoan = ?
                     """;
        JdbcHelper.update(sql,
                entity.getMatKhau(),
                entity.getTen(),
                entity.getSdt(),
                entity.getEmail(),
                entity.getVaiTro(),
                entity.getTrangThai(),
                entity.getTaiKhoan());
    }

    public void update(NhanVien entity) {
        String sql = """
                     UPDATE [dbo].[Taikhoan]
                        SET [mat_khau] = ?
                      WHERE email like ?
                     """;
        JdbcHelper.update(sql,
                entity.getMatKhau(),
                entity.getEmail());
    }
}
