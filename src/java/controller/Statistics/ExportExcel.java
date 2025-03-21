/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Statistics;

import java.io.IOException;
import java.io.PrintWriter;
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
import java.util.List;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import model.Statistics.FlashcardStatistics;
import model.Statistics.UserAgeStatistics;
import model.Statistics.VipStatistics;
import model.StatisticsDAO;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExportExcel extends HttpServlet {

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
            out.println("<title>Servlet ExportExcel</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExportExcel at " + request.getContextPath() + "</h1>");
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

        StatisticsDAO statisticsDAO = new StatisticsDAO();
        try {
            // Fetch statistics
            List<FlashcardStatistics> top15Flashcards = statisticsDAO.getTop15Flashcards();
            List<UserAgeStatistics> userAgeStats = statisticsDAO.getUserAgeStatistics();
            List<VipStatistics> vipStats = statisticsDAO.getUserVipCount();

            // Create Excel report and send as response
            createExcelReport(top15Flashcards, userAgeStats, vipStats, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }

    }

    private void createExcelReport(List<FlashcardStatistics> top15Flashcards,
            List<UserAgeStatistics> userAgeStats,
            List<VipStatistics> vipStats,
            HttpServletResponse response) throws IOException {

        // Create workbook and sheets
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet flashcardSheet = workbook.createSheet("Top 15 Flashcards");
        XSSFSheet ageSheet = workbook.createSheet("User Age Stats");
        XSSFSheet vipSheet = workbook.createSheet("VIP Stats");

        // Create Flashcard sheet
        int rowNum = 0;
        XSSFRow flashcardHeader = flashcardSheet.createRow(rowNum++);
        flashcardHeader.createCell(0).setCellValue("Flashcard Set ID");
        flashcardHeader.createCell(1).setCellValue("Name");
        flashcardHeader.createCell(2).setCellValue("View Count");

        for (FlashcardStatistics flashcard : top15Flashcards) {
            XSSFRow row = flashcardSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(flashcard.getFlashcardSetId());
            row.createCell(1).setCellValue(flashcard.getName());
            row.createCell(2).setCellValue(flashcard.getViewCount());
        }

        // Create User Age sheet
        rowNum = 0;
        XSSFRow ageHeader = ageSheet.createRow(rowNum++);
        ageHeader.createCell(0).setCellValue("Age");
        ageHeader.createCell(1).setCellValue("User Count");

        for (UserAgeStatistics userAgeStat : userAgeStats) {
            XSSFRow row = ageSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(userAgeStat.getAge());
            row.createCell(1).setCellValue(userAgeStat.getCount());
        }

        // Create VIP Stats sheet
        rowNum = 0;
        XSSFRow vipHeader = vipSheet.createRow(rowNum++);
        vipHeader.createCell(0).setCellValue("VIP Type");
        vipHeader.createCell(1).setCellValue("Count");

        for (VipStatistics vipStat : vipStats) {
            XSSFRow row = vipSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(vipStat.getVipType());
            row.createCell(1).setCellValue(vipStat.getCount());
        }

        // Set the response content type to Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=StatisticsReport.xlsx");

        // Write the workbook to the output stream
        workbook.write(response.getOutputStream());

        // Close resources
        workbook.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
