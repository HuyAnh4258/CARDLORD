package model;

import dal.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDao {

    // Phương thức nạp tiền (Deposit)
    public boolean depositMoney(int userId, float amount) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Cập nhật số dư ví tiền của người dùng
                String updateBalanceSQL = "UPDATE user_wallet SET balance = balance + ? WHERE user_id = ?";
                stm = con.prepareStatement(updateBalanceSQL);
                stm.setFloat(1, amount);
                stm.setInt(2, userId);

                int rowsAffected = stm.executeUpdate();

                // Nếu cập nhật thành công, thêm giao dịch vào bảng transactions
                if (rowsAffected > 0) {
                    addTransaction(con, userId, "Deposit", amount);
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

    // Phương thức rút tiền (Withdraw)
    public boolean withdrawMoney(int userId, float amount) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Kiểm tra số dư ví có đủ để rút không
                String checkBalanceSQL = "SELECT balance FROM user_wallet WHERE user_id = ?";
                stm = con.prepareStatement(checkBalanceSQL);
                stm.setInt(1, userId);
                rs = stm.executeQuery();

                if (rs.next()) {
                    float balance = rs.getFloat("balance");

                    // Nếu số dư đủ, thực hiện rút tiền
                    if (balance >= amount) {
                        // Cập nhật số dư ví tiền sau khi rút
                        String updateBalanceSQL = "UPDATE user_wallet SET balance = balance - ? WHERE user_id = ?";
                        stm = con.prepareStatement(updateBalanceSQL);
                        stm.setFloat(1, amount);
                        stm.setInt(2, userId);
                        int rowsAffected = stm.executeUpdate();

                        // Nếu cập nhật thành công, thêm giao dịch vào bảng transactions
                        if (rowsAffected > 0) {
                            addTransaction(con, userId, "Withdraw", amount);
                            return true;
                        }
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
    
    

    public boolean makePaymentFromBalance(int userId, float amount, String vipType) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Kiểm tra số dư ví của người dùng
                String checkBalanceSQL = "SELECT balance FROM user_wallet WHERE user_id = ?";
                stm = con.prepareStatement(checkBalanceSQL);
                stm.setInt(1, userId);
                rs = stm.executeQuery();

                if (rs.next()) {
                    float currentBalance = rs.getFloat("balance");

                    // Kiểm tra xem số dư có đủ để thanh toán không
                    if (currentBalance >= amount) {
                        // Cập nhật số dư ví tiền sau khi thanh toán
                        String updateBalanceSQL = "UPDATE user_wallet SET balance = balance - ? WHERE user_id = ?";
                        stm = con.prepareStatement(updateBalanceSQL);
                        stm.setFloat(1, amount);
                        stm.setInt(2, userId);

                        int rowsAffected = stm.executeUpdate();

                        // Nếu cập nhật số dư ví thành công, thêm giao dịch vào bảng transactions
                        if (rowsAffected > 0) {
                            // Thêm giao dịch vào bảng transactions với loại "Payment"
                            addTransaction(con, userId, "Payment", amount);

                            // Cập nhật trạng thái VIP và thời gian bắt đầu và kết thúc VIP
                            updateVipStatus(con, userId, vipType);

                            return true;
                        }
                    } else {
                        // Số dư không đủ
                        System.out.println("Insufficient balance for the payment.");
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
        return false; // Trả về false nếu không thể thực hiện thanh toán
    }

    
  public boolean makePayment(int userId, float amount, String vipType) throws SQLException, ClassNotFoundException {
    Connection con = null;
    PreparedStatement stm = null;
    try {
        con = DBHelper.makeConnection();
        if (con != null) {
            // Thêm giao dịch vào bảng transactions với loại "Payment"
            addTransaction(con, userId, "Payment", amount);

            // Cập nhật trạng thái VIP và thời gian bắt đầu và kết thúc VIP
            updateVipStatus(con, userId, vipType);

            return true;
        }
    } finally {
        if (stm != null) {
            stm.close();
        }
        if (con != null) {
            con.close();
        }
    }
    return false; // Trả về false nếu không thể thực hiện thanh toán
}

    // Thêm giao dịch vào bảng transactions
    private void addTransaction(Connection con, int userId, String transactionType, float amount) throws SQLException {
        PreparedStatement stm = null;
        try {
            String insertTransactionSQL = "INSERT INTO transactions (wallet_id, transaction_type, amount) "
                    + "SELECT wallet_id, ?, ? FROM user_wallet WHERE user_id = ?";
            stm = con.prepareStatement(insertTransactionSQL);
            stm.setString(1, transactionType);
            stm.setFloat(2, amount);
            stm.setInt(3, userId);
            stm.executeUpdate();
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
    }

    private void updateVipStatus(Connection con, int userId, String vipType) throws SQLException {
        PreparedStatement stm = null;
        try {
            // Lấy ngày hiện tại
            java.util.Date currentDate = new java.util.Date();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(currentDate);

           // Tính toán thời gian kết thúc dựa trên loại VIP
        if (vipType.equalsIgnoreCase("vip1")) {
            cal.add(java.util.Calendar.MONTH, 1); // Thêm 1 tháng
        } else if (vipType.equalsIgnoreCase("vip2")) {
            cal.add(java.util.Calendar.MONTH, 3); // Thêm 3 tháng
        } else if (vipType.equalsIgnoreCase("vip3")) {
            cal.add(java.util.Calendar.MONTH, 6); // Thêm 6 tháng
        }


            // Lấy thời gian kết thúc
            java.util.Date vipEndDate = cal.getTime();

            // Cập nhật trạng thái VIP, loại VIP, và thời gian bắt đầu và kết thúc
            String updateVipSQL = "UPDATE [user] SET vip_status = 1, vip_type = ?, vip_start_date = ?, vip_end_date = ? WHERE user_id = ?";
            stm = con.prepareStatement(updateVipSQL);
            stm.setString(1, vipType);
            stm.setDate(2, new java.sql.Date(currentDate.getTime())); // Chuyển đổi Date sang java.sql.Date
            stm.setDate(3, new java.sql.Date(vipEndDate.getTime()));   // Chuyển đổi Date sang java.sql.Date
            stm.setInt(4, userId);

            stm.executeUpdate();
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        PaymentDao d = new PaymentDao();

        System.out.println(d.makePayment(5, 10000, "vip1"));
    }

    
    
}
