/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.VnPay;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import model.User;
import model.UserActivityDAO;

@WebServlet(name = "VipPageServlet", urlPatterns = {"/vip"})
public class VipPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            // User not logged in, redirect to login page
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Fetch the latest user data from database to ensure we have up-to-date VIP status
        UserActivityDAO userDAO = new UserActivityDAO();
        User user = userDAO.getUserById(currentUser.getUserId());
        
        if (user.isVipStatus()) {
            // User already has VIP status
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String vipStartDate = (user.getVipStartDate() != null) ? 
                    dateFormat.format(user.getVipStartDate()) : "N/A";
            String vipEndDate = (user.getVipEndDate() != null) ? 
                    dateFormat.format(user.getVipEndDate()) : "N/A";
            
            request.setAttribute("vipStatus", true);
            request.setAttribute("vipType", user.getVipType());
            request.setAttribute("vipStartDate", vipStartDate);
            request.setAttribute("vipEndDate", vipEndDate);
        } else {
            // User does not have VIP status
            request.setAttribute("vipStatus", false);
        }
        
        request.getRequestDispatcher("vip.jsp").forward(request, response);
    }
}

