package Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;
import repository.JdbcHelper;

public class KhachHangService {

    public void insert(KhachHang entity) {
        String sql = """
                     INSERT INTO [dbo].[KhachHang]
                                ([ten]
                                ,[SDT])
                          VALUES (?, ?)
                     """;
        JdbcHelper.update(sql,
                entity.getTen(),
                entity.getSdt());
    }

    public void update(KhachHang entity) {
        String sql = """
                     UPDATE [dbo].[KhachHang]
                        SET [ten] = ?
                           ,[SDT] = ?
                      WHERE ma_khach_hang = ?
                     """;

        JdbcHelper.update(sql,
                entity.getTen(),
                entity.getSdt(),
                entity.getMa());
    }

    public void delete(Integer id) {
        String sql = """
                     DELETE FROM [dbo].[KhachHang]
                           WHERE ID = ?
                     """;
        JdbcHelper.update(sql, id);
    }

    public KhachHang selectById(Integer id) {
        String sql = "SELECT * FROM KhachHang WHERE ID = ?";

        List<KhachHang> list = this.selectBySql(sql, id);
        if (list == null) {
            return null;
        }

        return list.get(0);
    }

    public KhachHang selectBySDT(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE SDT like ?";

        List<KhachHang> list = this.selectBySql(sql, "%" + sdt + "%");
        if (list == null) {
            return null;
        }

        return list.get(0);
    }

    public KhachHang selectByMa(String ma) {
        String sql = "SELECT * FROM KhachHang WHERE ma_khach_hang like ?";

        List<KhachHang> list = this.selectBySql(sql, "%" + ma + "%");
        if (list == null) {
            return null;
        }

        return list.get(0);
    }

    public List<KhachHang> selectAll() {
        String sql = "SELECT * FROM KhachHang";

        return this.selectBySql(sql);
    }

    protected List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();

        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getInt("ID"));
                kh.setMa(rs.getString("ma_khach_hang"));
                kh.setTen(rs.getString("ten"));
                kh.setSdt(rs.getString("SDT"));

                list.add(kh);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public List<KhachHang> getPages(String keyword) {
        String sql = """
                     SELECT * FROM KhachHang
                     where ma_khach_hang like ?
                            or ten like ?
                            or SDT like ?
                     """;

        return this.selectBySql(sql,
                "%" + keyword + "%%",
                "%" + keyword + "%%",
                "%" + keyword + "%%"
        );
    }

    public List<KhachHang> selestPages(int pages, int limit) {
        String sql = """
                     SELECT * 
                     FROM 
                     (
                         SELECT * FROM KhachHang
                     ) AS FilteredResults
                     ORDER BY ID
                     OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                     """;
        return this.selectBySql(sql, (pages - 1) * limit, limit);
    }

    public List<KhachHang> selectSearch(String keyword, int pages, int limit) {
        String sql = """
                     SELECT * 
                     FROM 
                     (
                         SELECT * FROM KhachHang
                        where ma_khach_hang like ?
                               or ten like ?
                               or SDT like ?
                     ) AS FilteredResults
                     ORDER BY ID
                     OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                     """;
        return this.selectBySql(sql,
                "%" + keyword + "%%",
                "%" + keyword + "%%",
                "%" + keyword + "%%",
                (pages - 1) * limit, limit);
    }
}
