package controller.Statistics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.UserActivityDAO;
import model.UserActivity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class StatisticsUserServlet extends HttpServlet {

    // Phương thức xử lý yêu cầu
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // Lấy session từ request
       HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");  // Lấy user_id từ session


        // Kiểm tra nếu user_id là null (người dùng chưa đăng nhập)
        if (userId == null) {
            // Nếu user_id là null, người dùng chưa đăng nhập, chuyển hướng về trang login
            response.sendRedirect("login.jsp");
            return;
        }

        // Khởi tạo đối tượng DAO để lấy thông tin thống kê
        UserActivityDAO userActivityDAO = new UserActivityDAO();
        
        try {
            // Lấy tổng thời gian học của người dùng
            float totalHours = userActivityDAO.getTotalHours(userId);
            // Lấy streak học (chuỗi ngày học liên tiếp)
            int streak = userActivityDAO.getStreak(userId);
            // Lấy thông tin thời gian học theo các ngày
            List<UserActivity> activities = userActivityDAO.getTotalHoursByDate(userId);

            // Truyền các thông tin thống kê vào request để hiển thị trên JSP
            request.setAttribute("totalHours", totalHours); 
                        request.setAttribute("totalMinute", totalHours*60);

            request.setAttribute("streak", streak);
            request.setAttribute("activities", activities);  // Danh sách các ngày học và tổng thời gian học trong ngày

            // Chuyển tiếp đến trang thống kê (statisticsUser.jsp hoặc một trang khác để hiển thị)
            request.getRequestDispatcher("statisticsUser.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error occurred while fetching statistics.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Statistics Servlet for User Activity";
    }
}
