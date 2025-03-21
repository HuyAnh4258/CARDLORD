/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import dal.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daizl
 */
public class flashcardsetDAO {

    public List<FlashcardSet> getAllFlashSet()
            throws SQLException, ClassNotFoundException {
        List<FlashcardSet> result = new ArrayList();
        String stm = "SELECT * FROM [flashcard_set]";

        try (Connection conn = DBHelper.makeConnection(); PreparedStatement ps = conn.prepareStatement(stm)) {

            ResultSet rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                FlashcardSet row = new FlashcardSet(
                        rs.getInt("flashcard_set_id"),
                        rs.getTimestamp("time_created"),
                        rs.getInt("user_id"),
                        rs.getString("name_of_flashcardset"),
                        rs.getString("status"),
                        rs.getFloat("score"),
                        rs.getString("tags"),
                        rs.getString("visibility"),
                        rs.getInt("view")
                );
                result.add(row);
            }
        }

        return result;
    }

    public boolean updateFlashcardSet(int flashcard_set_id, String name_of_flashcardset, String tags, String visibility, String status, double score)
            throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "UPDATE flashcard_set "
                        + "SET name_of_flashcardset = ?, tags = ?, visibility = ?, status = ?, score = ? "
                        + "WHERE flashcard_set_id = ?";

                stm = con.prepareStatement(sql);
                stm.setString(1, name_of_flashcardset);
                stm.setString(2, tags);
                stm.setString(3, visibility);
                stm.setString(4, status);
                stm.setDouble(5, score);
                stm.setInt(6, flashcard_set_id);

                int row = stm.executeUpdate();
                return row > 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return false;
    }

    // ==========================================================
    public List<FlashcardSet> getFlashcardSets(int page, int pageSize, String searchQuery, String sortBy, boolean ascending) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<FlashcardSet> flashcardSets = new ArrayList<>();

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Tạo câu lệnh SQL với phân trang, tìm kiếm và sắp xếp
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM flashcard_set WHERE name_of_flashcardset LIKE ? OR status LIKE ? OR tags LIKE ? OR visibility LIKE ? ");

                // Thêm sắp xếp theo các cột và hướng
                if ("view".equalsIgnoreCase(sortBy)) {
                    sql.append("ORDER BY [view] ");
                } else if ("time_created".equalsIgnoreCase(sortBy)) {
                    sql.append("ORDER BY time_created ");
                } else {
                    sql.append("ORDER BY flashcard_set_id ");
                }

                sql.append(ascending ? "ASC " : "DESC ");

                // Thêm phân trang
                sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

                stm = con.prepareStatement(sql.toString());

                // Set giá trị cho các tham số
                stm.setString(1, "%" + searchQuery + "%"); // tìm kiếm trong name_of_flashcardset
                stm.setString(2, "%" + searchQuery + "%"); // tìm kiếm trong status
                stm.setString(3, "%" + searchQuery + "%"); // tìm kiếm trong tags
                stm.setString(4, "%" + searchQuery + "%"); // tìm kiếm trong visibility

                // Phân trang
                stm.setInt(5, (page - 1) * pageSize); // OFFSET
                stm.setInt(6, pageSize);              // FETCH NEXT

                rs = stm.executeQuery();

                // Lấy kết quả và thêm vào danh sách
                while (rs.next()) {
                    FlashcardSet flashcardSet = new FlashcardSet(
                            rs.getInt("flashcard_set_id"),
                            rs.getTimestamp("time_created"),
                            rs.getInt("user_id"),
                            rs.getString("name_of_flashcardset"),
                            rs.getString("status"),
                            rs.getFloat("score"),
                            rs.getString("tags"),
                            rs.getString("visibility"),
                            rs.getInt("view")
                    );

                    flashcardSets.add(flashcardSet);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return flashcardSets;
    }

    //read
    public List<FlashcardSet> getallfromOwner(int user_id) throws SQLException {
        return getallfromOwner(user_id, "");
    }

    // Lấy flashcard set của chủ sở hữu với tùy chọn sắp xếp
    public List<FlashcardSet> getallfromOwner(int user_id, String sort) throws SQLException {
        List<FlashcardSet> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT * FROM flashcard_set WHERE user_id = ?";
                if (sort != null && !sort.isEmpty()) {
                    if ("date_desc".equals(sort)) {
                        sql += " ORDER BY time_created DESC";
                    } else if ("date_asc".equals(sort)) {
                        sql += " ORDER BY time_created ASC";
                    } else if ("view_desc".equals(sort)) {
                        sql += " ORDER BY [view] DESC";
                    } else if ("view_asc".equals(sort)) {
                        sql += " ORDER BY [view] ASC";
                    }
                }
                stm = con.prepareStatement(sql);
                stm.setInt(1, user_id);
                rs = stm.executeQuery();
                while (rs.next()) {
                    FlashcardSet flashcardSet = new FlashcardSet(
                            rs.getInt("flashcard_set_id"),
                            rs.getTimestamp("time_created"),
                            rs.getInt("user_id"),
                            rs.getString("name_of_flashcardset"),
                            rs.getString("status"),
                            rs.getFloat("score"),
                            rs.getString("tags"),
                            rs.getString("visibility"),
                            rs.getInt("view")
                    );
                    list.add(flashcardSet);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return list;
    }

    public int getTotalFlashcardSets(String searchQuery) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int total = 0;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(*) FROM flashcard_set WHERE name_of_flashcardset LIKE ? OR status LIKE ? OR tags LIKE ? OR visibility LIKE ?";
                stm = con.prepareStatement(sql);

                // Set giá trị cho các tham số
                stm.setString(1, "%" + searchQuery + "%");
                stm.setString(2, "%" + searchQuery + "%");
                stm.setString(3, "%" + searchQuery + "%");
                stm.setString(4, "%" + searchQuery + "%");

                rs = stm.executeQuery();

                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return total;
    }

    // Lấy flashcard set công khai không sắp xếp (mặc định)
    public List<FlashcardSet> getallPublic() throws SQLException {
        return getallPublic("");
    }

    // Lấy flashcard set công khai với tùy chọn sắp xếp
    public List<FlashcardSet> getallPublic(String sort) throws SQLException {
        List<FlashcardSet> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT * FROM flashcard_set WHERE visibility = ?";
                if (sort != null && !sort.isEmpty()) {
                    if ("date_desc".equals(sort)) {
                        sql += " ORDER BY time_created DESC";
                    } else if ("date_asc".equals(sort)) {
                        sql += " ORDER BY time_created ASC";
                    } else if ("view_desc".equals(sort)) {
                        sql += " ORDER BY [view] DESC";
                    } else if ("view_asc".equals(sort)) {
                        sql += " ORDER BY [view] ASC";
                    }
                }
                stm = con.prepareStatement(sql);
                stm.setString(1, "Everyone");
                rs = stm.executeQuery();
                while (rs.next()) {
                    FlashcardSet flashcardSet = new FlashcardSet(
                            rs.getInt("flashcard_set_id"),
                            rs.getTimestamp("time_created"),
                            rs.getInt("user_id"),
                            rs.getString("name_of_flashcardset"),
                            rs.getString("status"),
                            rs.getFloat("score"),
                            rs.getString("tags"),
                            rs.getString("visibility"),
                            rs.getInt("view")
                    );
                    list.add(flashcardSet);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return list;
    }

    //getallFSset
    public List<FlashcardSet> searchFCset(String searchStr) throws SQLException {
        List<FlashcardSet> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            //b1: connect database
            con = DBHelper.makeConnection();
            //b2:if have connect =>prepare for sql statement
            if (con != null) {
                String sql = "Select * from flashcard_set where CAST(flashcard_set_id AS varchar) LIKE ? or name_of_flashcardset LIKE ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + searchStr + "%");
                stm.setString(2, "%" + searchStr + "%");
                //b3: excute the statement
                rs = stm.executeQuery();
                //b4: while for add the new category to the list
                while (rs.next()) {
                    FlashcardSet flashcardSet = new FlashcardSet(
                            rs.getInt("flashcard_set_id"),
                            rs.getTimestamp("time_created"),
                            rs.getInt("user_id"),
                            rs.getString("name_of_flashcardset"),
                            rs.getString("status"),
                            rs.getFloat("score"),
                            rs.getString("tags"),
                            rs.getString("visibility"),
                            rs.getInt("view")
                    );
                    list.add(flashcardSet);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return list;
    }

    public String getFlashcardName(int flashcard_set_id) {
        String flashcard_set_name = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT name_of_flashcardset FROM flashcard_set WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    flashcard_set_name = rs.getString("name_of_flashcardset");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return flashcard_set_name;
    }

    public boolean updateTimesCreate(int userId) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Cập nhật timesCreate, mỗi lần gọi hàm sẽ tăng lên 1
                String updateSQL = "UPDATE [user] SET timesCreate = timesCreate + 1 WHERE user_id = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setInt(1, userId);  // Thêm user_id vào câu truy vấn

                int rowsAffected = stm.executeUpdate();
                return rowsAffected > 0;  // Trả về true nếu có ít nhất 1 dòng bị ảnh hưởng (nghĩa là cập nhật thành công)
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;  // Trả về false nếu không có dòng nào bị ảnh hưởng
    }

//create
    public boolean insertFlashcardset(int user_id, String name_of_flashcardset, String tags, String visibility) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            //b1 connect database
            con = DBHelper.makeConnection();
            //b2: if having connection => preparing for statement
            if (con != null) {
                String sql = "INSERT INTO flashcard_set (user_id, name_of_flashcardset, tags, visibility) VALUES (?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setInt(1, user_id);
                stm.setString(2, name_of_flashcardset);
                stm.setString(3, tags);
                stm.setString(4, visibility);
                int row = stm.executeUpdate();
                //check if rs have more than 1 row => stm working
                if (row > 0) {
                    updateTimesCreate(user_id);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    //delete
    public boolean deleteFlashcardset(int flashcard_set_id)
            throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            //b1: connect database
            con = DBHelper.makeConnection();
            //b2: if having connection => preparing for statement
            if (con != null) {
                String sql = "Delete FROM flashcard_set Where flashcard_set_id = ? ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                //b3: excute query
                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
        }
        return false;
    }

    //update
    public boolean updateCategory(int flashcard_set_id, String name_of_flashcardset, String tags, String visibility) {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            //b1 connect database
            con = DBHelper.makeConnection();
            //b2 if having connect => preparing for stament
            if (con != null) {
                String sql = "Update flashcard_set "
                        + "SET [name_of_flashcardset] = ?, [tags] = ?,visibility = ?  "
                        + "WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, name_of_flashcardset);
                stm.setString(2, tags);
                stm.setString(3, visibility);
                stm.setInt(4, flashcard_set_id);
                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public void incrementView(int flashcard_set_id) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "UPDATE flashcard_set SET [view] = [view] + 1 WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                stm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean updateStatus(String status, int flashcard_set_id) {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            //b1 connect database
            con = DBHelper.makeConnection();
            //b2 if having connect => preparing for stament
            if (con != null) {
                String sql = "Update flashcard_set "
                        + "SET [status] = ? "
                        + "WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, status);
                stm.setInt(2, flashcard_set_id);
                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public FlashcardSet getFlashcardSetById(int flashcard_set_id) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //b1: connect database
            con = DBHelper.makeConnection();
            //b2 if having connect => preparing for stament
            if (con != null) {
                String sql = "Select * from flashcard_set where flashcard_set_id = ? ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    FlashcardSet c = new FlashcardSet(
                            rs.getInt("flashcard_set_id"),
                            rs.getTimestamp("time_created"),
                            rs.getInt("user_id"),
                            rs.getString("name_of_flashcardset"),
                            rs.getString("status"),
                            rs.getFloat("score"),
                            rs.getString("tags"),
                            rs.getString("visibility"),
                            rs.getInt("view")
                    );
                    return c;

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return null;
    }

    public int getUserIdByFlashcardSetId(int flashcard_set_id) throws SQLException {
        int user_id = -1;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT user_id FROM flashcard_set WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    user_id = rs.getInt("user_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return user_id;
    }

    public String getVisibilityByFlashcardSetId(int flashcard_set_id) throws SQLException {
        String visibility = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT visibility FROM flashcard_set WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    visibility = rs.getString("visibility");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return visibility;
    }
}
