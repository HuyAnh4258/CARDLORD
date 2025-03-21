package controller.servletLogic;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;


public class UnfavouriteServlet extends HttpServlet {
   
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
            out.println("<title>Servlet UnfavouriteServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UnfavouriteServlet at " + request.getContextPath () + "</h1>");
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
   private static final String COOKIE_PREFIX = "favouriteFlashcards_";

    @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

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

        String cookieName = "favouriteFlashcards_" + userId;
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

        List<String> favourites = new ArrayList<>();
        if (!favouriteList.isEmpty()) {
            String[] items = favouriteList.split(",");
            for (String item : items) {
                favourites.add(item);
            }
        }

        if (favourites.contains(flashcardSetId)) {
            favourites.remove(flashcardSetId);
        } else {
            response.getWriter().write("Flashcard set not found in favourites");
            return;
        }

        String updatedList = String.join(",", favourites);
        Cookie favCookie = new Cookie(cookieName, URLEncoder.encode(updatedList, "UTF-8"));
        favCookie.setMaxAge(60 * 60 * 24 * 7); // 7 ngày
        response.addCookie(favCookie);

        response.sendRedirect("favouriteFlashcards.jsp"); // Chuyển hướng về trang yêu thích sau khi xóa
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.jsp"); // Chuyển hướng nếu chưa đăng nhập
            return;
        }

        String flashcardId = request.getParameter("flashcardId");
        if (flashcardId == null || flashcardId.trim().isEmpty()) {
            response.sendRedirect("favourites.jsp");
            return;
        }

        String cookieName = "favouriteFlashcards_" + userId;
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

        List<String> favourites = new ArrayList<>();
        if (!favouriteList.isEmpty()) {
            String[] items = favouriteList.split(",");
            for (String item : items) {
                if (!item.equals(flashcardId)) { // Bỏ flashcard cần xóa
                    favourites.add(item);
                }
            }
        }

        // Cập nhật lại cookie
        String newFavouriteList = String.join(",", favourites);
        Cookie updatedCookie = new Cookie(cookieName, URLEncoder.encode(newFavouriteList, "UTF-8"));
        updatedCookie.setMaxAge(60 * 60 * 24 * 30); // Lưu trong 30 ngày
        response.addCookie(updatedCookie);

        response.sendRedirect("favourite.jsp"); // Quay lại trang danh sách yêu thích
    
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
