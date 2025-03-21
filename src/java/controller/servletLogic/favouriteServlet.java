/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.servletLogic;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="favouriteServlet", urlPatterns={"/favourite"})
public class favouriteServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet favouriteServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet favouriteServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  private static final String COOKIE_PREFIX = "favouriteFlashcards_"; // Prefix theo userId

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        // Lấy userId từ session (sau khi đăng nhập)
        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        if (userId == null) {
            response.getWriter().write("You need to log in first");
            return;
        }

        String flashcardSetId = request.getParameter("id");
        if (flashcardSetId == null || flashcardSetId.isEmpty()) {
            response.getWriter().write("Invalid Flashcard ID");
            return;
        }

        // Lấy Cookie theo userId
        String cookieName = COOKIE_PREFIX + userId;
        Cookie[] cookies = request.getCookies();
        String favouriteList = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    favouriteList = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    break;
                }
            }
        }

        // Chuyển danh sách thành List
        List<String> favourites = new ArrayList<>();
        if (!favouriteList.isEmpty()) {
            String[] items = favouriteList.split(",");
            for (String item : items) {
                favourites.add(item);
            }
        }

        // Kiểm tra nếu flashcard đã tồn tại trong danh sách
        if (!favourites.contains(flashcardSetId)) {
            favourites.add(flashcardSetId);
        } else {
            response.getWriter().write("Set already in favourites");
            return;
        }

        // Lưu lại vào Cookie theo userId
        String updatedList = String.join(",", favourites);
        Cookie favCookie = new Cookie(cookieName, URLEncoder.encode(updatedList, "UTF-8"));
        favCookie.setMaxAge(60 * 60 * 24 * 7); // 7 ngày
        response.addCookie(favCookie);

        response.getWriter().write("Added the set to your favourites collection");
    }


    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
