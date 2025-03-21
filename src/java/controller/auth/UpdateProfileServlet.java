package controller.auth;

import java.io.PrintWriter;
import jakarta.servlet.annotation.WebServlet;
import dal.DBHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import model.AccountDAO;
import model.User;

/**
 *
 * @author Thang Nguyen
 */
@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/UpdateProfileServlet"})
public class UpdateProfileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProfileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        String newUsername = request.getParameter("username");
        String newBirthdateStr = request.getParameter("birthdate"); // Lấy ngày sinh từ form
        String newPhone = request.getParameter("phone");

        int userId = Integer.parseInt(userIdStr);

        java.sql.Date newBirthdate = null;
        try {
            newBirthdate = java.sql.Date.valueOf(newBirthdateStr); // Format yêu cầu: yyyy-MM-dd
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", "Format is invalid. Please enter follow the format yyyy-MM-dd.");
            request.getRequestDispatcher("setting.jsp").forward(request, response);
            return;
        }

        boolean duplicate = false;
        String dupMsg = "";
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.makeConnection();
            if (con != null) {
                // Kiểm tra trùng lặp username và phone (không cần kiểm tra ngày sinh vì có thể trùng)
                String sql = "SELECT COUNT(*) AS cnt FROM [user] "
                        + "WHERE (username = ? OR phone_number = ?) "
                        + "AND user_id <> ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, newUsername);
                stm.setString(2, newPhone);
                stm.setInt(3, userId);
                rs = stm.executeQuery();
                if (rs.next() && rs.getInt("cnt") > 0) {
                    duplicate = true;
                    dupMsg = "Username or phone number already exists. Cannot update.";
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            duplicate = true;
            dupMsg = "Error while checking duplicates: " + ex.getMessage();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (duplicate) {
            request.setAttribute("message", dupMsg);
            request.getRequestDispatcher("setting.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO dao = new AccountDAO();
            // Giả sử phương thức updateProfile được cập nhật để nhận thêm đối số ngày sinh thay vì email.
            boolean success = dao.updateProfile(userId, newUsername, newBirthdate, newPhone);
            if (success) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    user.setUsername(newUsername);
                    user.setDob(newBirthdate); // Cập nhật ngày sinh cho đối tượng user
                    user.setPhoneNumber(newPhone);
                    session.setAttribute("user", user);
                }
                request.setAttribute("message", "Profile updated successfully!");
            } else {
                request.setAttribute("message", "Profile update failed. Please try again.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("message", "Error: " + ex.getMessage());
        }

        request.getRequestDispatcher("setting.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}


