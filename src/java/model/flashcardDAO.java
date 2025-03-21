/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mysql.cj.xdevapi.PreparableStatement;
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
public class flashcardDAO {

    //create
    public boolean insertFlashcard(int flashcard_set_id, String term, String flashcard_definition) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            //b1 connect database
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO flashcard(flashcard_set_id, term, flashcard_definition) VALUES (?, ?, ?)";
                //b2 preparing sql statement
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                stm.setString(2, term);
                stm.setString(3, flashcard_definition);
                int row = stm.executeUpdate();
                //b3 check if execute successfully
                if (row > 0) {
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

    //read
    public List<FlashCard> getFlashcard(int flashcard_set_id) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<FlashCard> list = new ArrayList<>();
        //connect to database
        try {
            con = DBHelper.makeConnection();
            //check if having connect
            if (con != null) {
                String sql = "Select * from flashcard where flashcard_set_id = ? ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_set_id);
                rs = stm.executeQuery();
                //while for add the new flasncardset to the list
                while (rs.next()) {
                    FlashCard newCard = new FlashCard(rs.getInt("flashcard_id"), rs.getInt("flashcard_set_id"), rs.getNString("term"), rs.getNString("flashcard_definition"));
                    list.add(newCard);
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

    //update
//    public boolean updateFlashcard(int flashcard_id, String term, String flashcard_definition) throws SQLException {
//        Connection con = null;
//        PreparedStatement stm = null;
//        try {
//            con = DBHelper.makeConnection();
//            if (con != null) {
//                String sql = "Update flashcard "
//                        + "Set term = ?, flashcard_definition = ? "
//                        + "Where flashcard_id = ? ";
//                stm = con.prepareStatement(sql);
//                stm.setString(1, term);
//                stm.setString(2, flashcard_definition);
//                stm.setInt(3, flashcard_id);
//                int row = stm.executeUpdate();
//                if (row > 0) {
//                    return true;
//                }
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            System.out.println(e);
//        } finally {
//            if (stm != null) {
//                stm.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//            return false;
//        }
//    }
        public boolean updateFlashcard(int flashcardId, String field, String value) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Xây dựng câu lệnh SQL động để cập nhật field cụ thể
                String sql = "UPDATE flashcard SET " + field + " = ? WHERE flashcard_id = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, value);
                stm.setInt(2, flashcardId);

                int rowsUpdated = stm.executeUpdate();
                return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false; // Trả về false nếu có lỗi
    }


    //delete
    public boolean deleteFlashcard(int flashcard_id) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            //b1: connect database
            con = DBHelper.makeConnection();
            //b2: if having connection => preparing for statement
            if (con != null) {
                String sql = "Delete FROM flashcard Where flashcard_id = ? ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcard_id);
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

    //other
    public int getOwnerId(int flashcardSetId) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int ownerId = -1;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT user_id FROM flashcard_set WHERE flashcard_set_id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, flashcardSetId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    ownerId = rs.getInt("user_id");
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
        return ownerId;
    }

    public int getOwnerByFlashcardId(int flashcardId) throws SQLException, ClassNotFoundException {
        Connection con = DBHelper.makeConnection();
        if (con != null) {
            String sql = "SELECT fs.user_id FROM flashcards f JOIN flashcard_set fs ON f.flashcard_set_id = fs.flashcard_set_id WHERE f.flashcard_id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, flashcardId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            con.close();
        }
        return -1;
    }

    public int getFlashcardSetId(int flashcardId) throws SQLException, ClassNotFoundException {
        Connection con = DBHelper.makeConnection();
        if (con != null) {
            String sql = "SELECT flashcard_set_id FROM flashcards WHERE flashcard_id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, flashcardId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("flashcard_set_id");
            }
            con.close();
        }
        return -1;
    }

    
}

//    public static void main(String[] args) throws SQLException {
//        flashcardDAO dao = new flashcardDAO();
//        List<FlashCard> a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//        dao.deleteFlashcard(3);
//        a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//    }
//    public static void main(String[] args) throws SQLException {
//        flashcardDAO dao = new flashcardDAO();
//        List<FlashCard> a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//        dao.deleteFlashcard(3);
//        a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//    }
//    public static void main(String[] args) throws SQLException {
//        flashcardDAO dao = new flashcardDAO();
//        List<FlashCard> a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//        dao.deleteFlashcard(3);
//        a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//    }
//    public static void main(String[] args) throws SQLException {
//        flashcardDAO dao = new flashcardDAO();
//        List<FlashCard> a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//        dao.deleteFlashcard(3);
//        a = dao.getFlashcard(3);
//        for (FlashCard flashCard : a) {
//            System.out.println(flashCard.getTerm() + " | " + flashCard.getFlashcard_definition());
//        }
//    }

