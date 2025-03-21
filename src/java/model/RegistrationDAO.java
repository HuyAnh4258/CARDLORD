package model;

import dal.DBHelper;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RegistrationDAO implements Serializable {

    // Method to check duplicate entries
    public String checkDuplicate(String username, String email, String phone_number) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Check username
                String checkUserSql = "SELECT username FROM [user] WHERE username = ?";
                stm = con.prepareStatement(checkUserSql);
                stm.setString(1, username);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return "Username already exists!";
                }
                rs.close();
                stm.close();

                // Check email
                String checkEmailSql = "SELECT email FROM [user] WHERE email = ?";
                stm = con.prepareStatement(checkEmailSql);
                stm.setString(1, email);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return "Email already exists!";
                }
                rs.close();
                stm.close();

                // Check phone number
                String checkPhoneSql = "SELECT phone_number FROM [user] WHERE phone_number = ?";
                stm = con.prepareStatement(checkPhoneSql);
                stm.setString(1, phone_number);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return "Phone number already exists!";
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
        return null; // No duplicate found
    }

//    public boolean Register(String username, String password, String email, String phone_number, Date dob)
//            throws SQLException, ClassNotFoundException {
//        Connection con = null;
//        PreparedStatement stm = null;
//
//        try {
//            con = DBHelper.makeConnection();
//            if (con != null) {
//                String insertSql = "INSERT INTO [user] (username, email, phone_number, [password], dob) "
//                        + "VALUES (?, ?, ?, ?, ?)";
//                stm = con.prepareStatement(insertSql);
//                stm.setString(1, username);
//                stm.setString(2, email);
//                stm.setString(3, phone_number);
//                stm.setString(4, password);
//                stm.setDate(5, new java.sql.Date(dob.getTime()));
//
//                int row = stm.executeUpdate();
//                return row > 0;
//            }
//        } finally {
//            if (stm != null) {
//                stm.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        }
//        return false;
//    }
//    
//    
    
    
    
    /// bổ sung thêm tạo ví tiền cho người dùng
    public boolean Register(String username, String password, String email, String phone_number, Date dob)
        throws SQLException, ClassNotFoundException {
    Connection con = null;
    PreparedStatement stm = null;
    ResultSet rs = null;

    try {
        con = DBHelper.makeConnection();
        if (con != null) {
            // Đầu tiên, thêm người dùng vào bảng [user]
            String insertSql = "INSERT INTO [user] (username, email, phone_number, [password], dob) "
                    + "VALUES (?, ?, ?, ?, ?)";
            stm = con.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);  // Lấy ID vừa được tạo
            stm.setString(1, username);
            stm.setString(2, email);
            stm.setString(3, phone_number);
            stm.setString(4, password);
            stm.setDate(5, new java.sql.Date(dob.getTime()));

            int row = stm.executeUpdate();

            // Nếu người dùng được đăng ký thành công, lấy user_id và thêm vào bảng user_wallet
            if (row > 0) {
                // Lấy ID của người dùng mới từ kết quả
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);

                    // Thêm ví tiền cho người dùng mới với số dư là 0
                    String insertWalletSql = "INSERT INTO user_wallet (user_id, balance) VALUES (?, ?)";
                    stm = con.prepareStatement(insertWalletSql);
                    stm.setInt(1, userId);
                    stm.setFloat(2, 0);  // Số dư là 0
                    int walletRow = stm.executeUpdate();
                    return walletRow > 0;
                }
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
    return false;
}

    
}
