/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Statistics;

import com.google.gson.Gson;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Statistics.FlashcardStatistics;
import model.Statistics.UserAgeStatistics;
import model.Statistics.VipStatistics;
import model.StatisticsDAO;

@WebServlet("/statistics")
public class StatisticsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StatisticsServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StatisticsServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StatisticsDAO statisticsDAO = new StatisticsDAO();
        try {
            // Lấy dữ liệu thống kê
            List<FlashcardStatistics> top15Flashcards = statisticsDAO.getTop15Flashcards();
            List<UserAgeStatistics> userAgeStats = statisticsDAO.getUserAgeStatistics();
            List<VipStatistics> vipStats = statisticsDAO.getUserVipCount();

            // Chuyển dữ liệu thành JSON bằng Gson
            Gson gson = new Gson();
            String top15FlashcardsJSON = gson.toJson(top15Flashcards);
            String userAgeStatsJSON = gson.toJson(userAgeStats);
            String vipStatsJSON = gson.toJson(vipStats);

            // Truyền dữ liệu JSON vào request
            request.setAttribute("top15FlashcardsJSON", top15FlashcardsJSON);
            request.setAttribute("userAgeStatsJSON", userAgeStatsJSON);
            request.setAttribute("vipStatsJSON", vipStatsJSON);
            
            request.setAttribute("top15Flashcards", top15Flashcards);
            request.setAttribute("userAgeStats", userAgeStats);
            request.setAttribute("vipStats", vipStats);

            // Forward đến JSP để hiển thị
            request.getRequestDispatcher("statistics.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
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
