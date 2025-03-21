/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.VnPay;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PaymentDao;

public class DepositServlet extends HttpServlet {

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
            out.println("<title>Servlet DepositServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DepositServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private final String DISPLAY_PAGE = "DisplayMoney.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy số tiền và thời gian thanh toán từ request
        String amountString = request.getParameter("vnp_Amount");
        String date = request.getParameter("vnp_PayDate");
        String courseName = request.getParameter("vnp_OrderInfo");

        // Chuyển đổi amount từ String sang long và chia cho 100 để có giá trị thực tế
        float amount = 0;
        if (amountString != null && !amountString.isEmpty()) {
            try {
                try {
                    long amountLong = Long.parseLong(amountString); // Chuyển từ String sang long
                    amount = amountLong / 100f; // Chia cho 100 và ép kiểu sang float
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Xử lý trường hợp số tiền không hợp lệ
                }
                
// Lấy session từ request
HttpSession session = request.getSession();

// Lấy giá trị user_id từ session và kiểm tra kiểu
Object userIdObject = session.getAttribute("user_id");

if (userIdObject != null) {
    // Nếu user_id là Integer, bạn có thể ép kiểu thành int
    int userId = 0;
    if (userIdObject instanceof Integer) {
        userId = (Integer) userIdObject;
    } else if (userIdObject instanceof String) {
        // Nếu là String, chuyển sang int
        try {
            userId = Integer.parseInt((String) userIdObject);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid user ID format.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
    }

    // Tiến hành thanh toán
    PaymentDao pay = new PaymentDao();
    pay.makePayment(userId, amount, courseName);

} else {
    request.setAttribute("errorMessage", "Session time out, please login again !");
    request.getRequestDispatcher("login.jsp").forward(request, response);
}

// Định dạng thời gian từ "yyyyMMddHHmmss" thành "yyyy-MM-dd HH:mm:ss"
DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
String formattedDateTime = dateTime.format(outputFormatter);

response.sendRedirect("/PRJ_SE1938/checkout?amount=" + amount + "&courseName=" + courseName + "&formattedDateTime=" + formattedDateTime);

            } catch (SQLException ex) {
                Logger.getLogger(DepositServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DepositServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
