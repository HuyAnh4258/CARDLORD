package model;

import dal.DBHelper;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.mail.MessagingException;
import java.util.ArrayList;
import model.User;
import utils.Helper;
import utils.SendEmail;

public class AccountDAO implements Serializable {

    // Login
    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Sửa câu SQL để sử dụng collation case-sensitive cho username và password
                String sql = "SELECT username FROM [user] WHERE username = ? COLLATE SQL_Latin1_General_CP1_CS_AS AND password = ? COLLATE SQL_Latin1_General_CP1_CS_AS";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);  // Sử dụng username nhập vào
                stm.setString(2, password);  // Sử dụng mật khẩu nhập vào
                rs = stm.executeQuery();
                if (rs.next()) {
                    return true;  // Nếu tìm thấy tài khoản, trả về true
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
        return false;  // Nếu không tìm thấy tài khoản, trả về false
    }

    public boolean register(String username, String email, String phoneNumber, String password, String verifyCode, Date dob) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            con.setAutoCommit(false);
            if (con != null) {
                // Check if username, email or phone already exists in the database
                String checkSQL = "SELECT username, email, phone_number FROM [user] WHERE username = ? OR email = ? OR phone_number = ?";
                stm = con.prepareStatement(checkSQL);
                stm.setString(1, username);
                stm.setString(2, email);
                stm.setString(3, phoneNumber);
                rs = stm.executeQuery();
                if (rs.next()) {
                    // If any of the fields (username, email, or phone_number) is already registered, return false
                    return false;
                }

                // If not, proceed with inserting the new user
                String insertSQL = "INSERT INTO [user] (username, email, phone_number, [password], verificationCode, dob) VALUES (?, ?, ?, ?, ?, ?)";
                stm = con.prepareStatement(insertSQL);
                stm.setString(1, username);
                stm.setString(2, email);
                stm.setString(3, phoneNumber);
                stm.setString(4, password);
                stm.setString(5, verifyCode);
                stm.setDate(6, dob);  // Chèn giá trị dob vào database
                int rowsAffected = stm.executeUpdate();
                if (rowsAffected != 1) {
                    con.rollback();
                    throw new SQLException("Create than 1. Roll back");
                } else {
                    con.commit();
                    return true;
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

    // Forgot Password (Generate a verification code and send it to the email)
    public boolean forgotPassword(String email) throws SQLException, ClassNotFoundException, MessagingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Check if email exists in the database
                String checkEmailSQL = "SELECT email FROM [user] WHERE email = ?";
                stm = con.prepareStatement(checkEmailSQL);
                stm.setString(1, email);
                rs = stm.executeQuery();
                if (rs.next()) {
                    // Email exists, generate a random verification code
                    String Verifycode = Helper.generateCode(6);

                    // Update the user record with the generated verification code
                    String updateSQL = "UPDATE [user] SET verificationCode = ? WHERE email = ?";
                    stm = con.prepareStatement(updateSQL);
                    stm.setString(1, Verifycode);
                    stm.setString(2, email);
                    int rowsAffected = stm.executeUpdate();
                    if (rowsAffected > 0) {
                        // Send the verification code to the user's email (simulated here, implement email sending logic as needed)
                        SendEmail.SentEmailForgotPass(email, Verifycode);

                        return true;
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

    // Helper method to generate a random verification code
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999);
        return String.format("%06d", code); // Return a 6-digit string
    }

    // Helper method to simulate sending the verification code to the user's email
    private void sendVerificationCodeToEmail(String email, String verificationCode) {
        // You would implement your email sending logic here.
        // For now, we just simulate the action by printing the verification code.
        System.out.println("Verification code sent to " + email + ": " + verificationCode);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Kiểm tra xem người dùng có tồn tại và mật khẩu cũ có đúng không
                String checkSQL = "SELECT password FROM [user] WHERE username = ? AND password = ?";
                stm = con.prepareStatement(checkSQL);
                stm.setString(1, username);
                stm.setString(2, oldPassword);
                rs = stm.executeQuery();

                if (rs.next()) {
                    // Nếu mật khẩu cũ đúng, tiến hành thay đổi mật khẩu mới
                    String updateSQL = "UPDATE [user] SET password = ? WHERE username = ?";
                    stm = con.prepareStatement(updateSQL);
                    stm.setString(1, newPassword);
                    stm.setString(2, username);
                    int rowsAffected = stm.executeUpdate();
                    return rowsAffected > 0; // Nếu có ít nhất 1 dòng bị ảnh hưởng, tức là cập nhật thành công
                } else {
                    // Mật khẩu cũ không đúng
                    return false;
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

    public boolean updatePasswordByUsername(String username, String newPassword) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Truy vấn SQL để cập nhật mật khẩu mới cho người dùng theo username
                String updateSQL = "UPDATE [user] SET password = ? WHERE username = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setString(1, newPassword);
                stm.setString(2, username);
                int rowsAffected = stm.executeUpdate();
                return rowsAffected > 0; // Nếu có ít nhất 1 dòng bị ảnh hưởng, tức là cập nhật thành công
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false; // Trả về false nếu không có dòng nào bị ảnh hưởng (nghĩa là người dùng không tồn tại)
    }

    public boolean updatePasswordByEmail(String email, String newPassword) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Truy vấn SQL để cập nhật mật khẩu mới cho người dùng theo username
                String updateSQL = "UPDATE [user] SET password = ? WHERE email = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setString(1, newPassword);
                stm.setString(2, email);
                int rowsAffected = stm.executeUpdate();
                return rowsAffected > 0; // Nếu có ít nhất 1 dòng bị ảnh hưởng, tức là cập nhật thành công
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false; // Trả về false nếu không có dòng nào bị ảnh hưởng (nghĩa là người dùng không tồn tại)
    }

    public boolean verifyCode(String email, String inputCode) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Kiểm tra mã xác minh và email cùng lúc
                String updateSQL = "UPDATE [user] SET verificationCode = NULL, isVerification = 1 "
                        + "WHERE email = ? AND CAST(verificationCode AS NVARCHAR(MAX)) = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setString(1, email);
                stm.setString(2, inputCode);

                int rowsAffected = stm.executeUpdate();

                // Nếu có bản ghi bị ảnh hưởng, có nghĩa là mã xác minh đúng và đã được cập nhật
                return rowsAffected > 0;
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false; // Trả về false nếu không có bản ghi nào bị ảnh hưởng
    }

    public boolean setVerificationCode(String username, String verificationCode) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Kiểm tra người dùng qua username và cập nhật verificationCode
                String updateSQL = "UPDATE [user] SET verificationCode = ? WHERE username = ?";
                stm = con.prepareStatement(updateSQL);
                stm.setString(1, verificationCode);
                stm.setString(2, username);

                int rowsAffected = stm.executeUpdate();
                return rowsAffected > 0; // Nếu có bản ghi bị ảnh hưởng, tức là cập nhật thành công
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false; // Trả về false nếu không có bản ghi nào bị ảnh hưởng
    }

    public User getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        User user = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT * FROM [user] WHERE username = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);
                rs = stm.executeQuery();

                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setPassword(rs.getString("password"));
                    user.setVipStatus(rs.getBoolean("vip_status"));
                    user.setVipType(rs.getString("vip_type"));
                    user.setVipStartDate(rs.getDate("vip_start_date"));
                    user.setVipEndDate(rs.getDate("vip_end_date"));
                    user.setRole(rs.getBoolean("role"));
                    user.setDob(rs.getDate("dob"));
                    user.setVerificationCode(rs.getString("verificationCode"));
                    user.setIsVerification(rs.getBoolean("isVerification"));
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

        return user;
    }

    public int getUserID(String username, String password)
            throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int userId = -1;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT user_id FROM [user] WHERE username = ? AND password = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);
                stm.setString(2, password);

                rs = stm.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("user_id");
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
        return userId;
    }

    public boolean updateProfile(int userId, String username, java.sql.Date birthdate, String phone)
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE [user] SET username = ?, dob = ?, phone_number = ? WHERE user_id = ?";
        try (Connection con = DBHelper.makeConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, username);
            stm.setDate(2, birthdate);
            stm.setString(3, phone);
            stm.setInt(4, userId);
            return stm.executeUpdate() > 0;
        }
    }

    public boolean vipStatus(String username) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                String sql = "SELECT vip_status FROM [user] WHERE [username] = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);
                rs = stm.executeQuery();
                if (rs.next()) {
                    int status = rs.getInt("vip_status");
                    return status == 1;
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

    // =====================================================================================
    public ArrayList<User> getAllUsers()
            throws SQLException, ClassNotFoundException {
        ArrayList<User> result = new ArrayList();
        String stm = "SELECT * FROM [user]";

        try (Connection conn = DBHelper.makeConnection(); PreparedStatement ps = conn.prepareStatement(stm)) {

            ResultSet rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                result.add(this.mapUser(rs));
            }
        }

        return result;
    }

    public User getUserById(int userId) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        User user = null;

        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Truy vấn thông tin người dùng theo user_id
                String sql = "SELECT * FROM [user] WHERE user_id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, userId);  // Thêm user_id vào câu truy vấn

                rs = stm.executeQuery();
                if (rs.next()) {
                    // Nếu tìm thấy người dùng, tạo đối tượng User và gán giá trị
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setPassword(rs.getString("password"));
                    user.setVipStatus(rs.getBoolean("vip_status"));
                    user.setVipType(rs.getString("vip_type"));
                    user.setVipStartDate(rs.getDate("vip_start_date"));
                    user.setVipEndDate(rs.getDate("vip_end_date"));
                    user.setTimesCreate(rs.getInt("timesCreate"));
                    user.setRole(rs.getBoolean("role"));
                    user.setDob(rs.getDate("dob"));
                    user.setVerificationCode(rs.getString("verificationCode"));
                    user.setIsVerification(rs.getBoolean("isVerification"));
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

        return user;  // Trả về đối tượng User hoặc null nếu không tìm thấy
    }

    public void updateUser(User user) throws SQLException, ClassNotFoundException {
        StringBuilder stm = new StringBuilder("UPDATE [user] SET ");
        ArrayList<Object> params = new ArrayList<>();

        if (user.getUsername() != null) {
            stm.append("username = ?, ");
            params.add(user.getUsername());
        }
        if (user.getEmail() != null) {
            stm.append("email = ?, ");
            params.add(user.getEmail());
        }
        if (user.getPhoneNumber() != null) {
            stm.append("phone_number = ?, ");
            params.add(user.getPhoneNumber());
        }
        if (user.getPassword() != null) {
            stm.append("[password] = ?, ");
            params.add(user.getPassword());
        }
        if (user.getVipType() != null) {
            stm.append("vip_type = ?, ");
            params.add(user.getVipType());
        }
        if (user.getVipStartDate() != null) {
            stm.append("vip_start_date = ?, ");
            params.add(user.getVipStartDate());
        }
        if (user.getVipEndDate() != null) {
            stm.append("vip_end_date = ?, ");
            params.add(user.getVipEndDate());
        }
        stm.append("vip_status = ?, ");
        params.add(user.isVipStatus() ? 1 : 0); // BIT → INT (0 hoặc 1)

        stm.append("timesCreate = ?, ");
        params.add(user.getTimesCreate());

        stm.append("[role] = ?, ");
        params.add(user.isRole() ? 1 : 0); // BIT → INT (0 hoặc 1)

        if (user.getDob() != null) {
            stm.append("dob = ?, ");
            params.add(user.getDob());
        }
        if (user.getVerificationCode() != null) {
            stm.append("verificationCode = ?, ");
            params.add(user.getVerificationCode());
        }
        stm.append("isVerification = ?, ");
        params.add(user.isIsVerification() ? 1 : 0); // BIT → INT (0 hoặc 1)

        // Loại bỏ dấu phẩy cuối cùng
        stm.setLength(stm.length() - 2);

        // Thêm điều kiện WHERE
        stm.append(" WHERE user_id = ?");
        params.add(user.getUserId());

        // Kết nối DB và thực thi PreparedStatement
        try (Connection conn = DBHelper.makeConnection(); PreparedStatement ps = conn.prepareStatement(stm.toString())) {
            conn.setAutoCommit(false);

            // Gán tham số cho PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            int updatedRows = ps.executeUpdate();

            if (updatedRows < 1) {
                conn.rollback();
                throw new SQLException("Error: No rows updated.");
            } else if (updatedRows > 1) {
                conn.rollback();
                throw new SQLException("Error: More than one row updated.");
            } else {
                conn.commit();
            }
        }
    }

    public void deleteUser(int userId)
            throws SQLException, ClassNotFoundException {

        String stm = "DELETE FROM [user] WHERE user_id = ?";

        try (Connection conn = DBHelper.makeConnection(); PreparedStatement ps = conn.prepareStatement(stm)) {
            conn.setAutoCommit(false);

            ps.setInt(1, userId);

            int deletedRows = ps.executeUpdate();

            if (deletedRows == 0) {
                throw new SQLException("Error. Delete none");
            } else if (deletedRows > 1) {
                throw new SQLException("Error. Deleted more than 1");
            } else {
                conn.commit();
            }
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("password"),
                rs.getBoolean("vip_status"), // BIT -> boolean
                rs.getString("vip_type"), // NVARCHAR -> String
                rs.getDate("vip_start_date"), // DATETIME -> java.sql.Date
                rs.getDate("vip_end_date"), // DATETIME -> java.sql.Date
                rs.getInt("timesCreate"), // INT -> int
                rs.getBoolean("role"), // BIT -> boolean
                rs.getDate("dob"), // DATE -> java.sql.Date
                rs.getString("verificationCode"), // TEXT -> String
                rs.getBoolean("isVerification") // BIT -> boolean
        );
    }

}
