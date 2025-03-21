/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.servletLogic;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FlashcardSet;
import model.flashcardsetDAO;

/**
 *
 * @author daizl
 */
public class updateFlashcardsetServlet extends HttpServlet {

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
            out.println("<title>Servlet updateFlashcardSet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet updateFlashcardSet at " + request.getContextPath() + "</h1>");
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
        String raw_id = request.getParameter("id");
        flashcardsetDAO dao = new flashcardsetDAO();

        try {
            int id = Integer.parseInt(raw_id);
            FlashcardSet flashcardSet = dao.getFlashcardSetById(id);

            if (flashcardSet != null) {
                request.setAttribute("flashcardSet", flashcardSet);
                request.getRequestDispatcher("updateFlashcardset.jsp").forward(request, response);
            } else {
//                request.setAttribute("message", "Flashcard set không tồn tại.");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        // Lấy thông tin từ request
        String id_raw = request.getParameter("flashcard_set_id");
        String name = request.getParameter("name_of_flashcardset");
        String status = request.getParameter("status");
        String score_raw = request.getParameter("score");
        String tags = request.getParameter("tags");
        String visibility = request.getParameter("visibility");

        try {
            int id = Integer.parseInt(id_raw);
            double score = Double.parseDouble(score_raw);

            // Kiểm tra độ dài tên flashcard set
            if (name.length() > 200) {
                request.setAttribute("message", "Flashcard set name must not exceed 200 characters");
                request.getRequestDispatcher("updateFlashcardset.jsp").forward(request, response);
            }

            // Kiểm tra độ dài của tags
            if (tags.length() > 200) {
                request.setAttribute("message", "Tag set name must not exceed 200 characters");
                request.getRequestDispatcher("updateFlashcardset.jsp").forward(request, response);
            }

            // Kiểm tra tính hợp lệ của status và visibility
            if (!status.matches("New|Learning|Known|Forgotten")) {
                throw new IllegalArgumentException("Invalid status value");
            }
            if (!visibility.matches("Everyone|Only me")) {
                throw new IllegalArgumentException("Invalid visibility value");
            }

            flashcardsetDAO dao = new flashcardsetDAO();
            boolean isUpdated = dao.updateCategory(id, name, tags, visibility);

            if (isUpdated) {
                request.setAttribute("message", "Update successful");
            } else {
                request.setAttribute("message", "Update failed");
            }
            request.getRequestDispatcher("library.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid number format: " + e.getMessage());
            request.getRequestDispatcher("library.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", "Validation error: " + e.getMessage());
            request.getRequestDispatcher("library.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("library.jsp").forward(request, response);
        }
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
