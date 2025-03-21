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
import java.util.ArrayList;
import model.AccountDAO;
import model.User;
import utils.ParamsValid;
import java.sql.SQLException;
import java.sql.Date;
import model.FlashcardSet;
import model.flashcardsetDAO;

/**
 *
 * @author Phan Sơn
 */
public class adminServlet extends HttpServlet {

    /**
     * Get all User.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String getFS = request.getParameter("getFS");
        String getUser = request.getParameter("getUser");

        if (!ParamsValid.isEmpty(getFS)) {
            this.getFS(request, response);
        } else if (!ParamsValid.isEmpty(getUser)) {
            this.getUser(request, response);
        }
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AccountDAO dao = new AccountDAO();
        try {
            ArrayList<User> list = dao.getAllUsers();
            // 2. Set it to request
            request.setAttribute("userList", list);
            // 3. Dispatch back 
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("Error", e.getMessage());
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);
        }
    }

    private void getFS(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        flashcardsetDAO dao = new flashcardsetDAO();
        try {
            ArrayList<FlashcardSet> list = (ArrayList<FlashcardSet>) dao.getAllFlashSet();
            // 2. Set it to request
            request.setAttribute("fsList", list);
            // 3. Dispatch back 
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("Error", e.getMessage());
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);
        }
    }

    /**
     * Create new User.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String methodOverride = request.getParameter("_method");
        if (methodOverride != null) {
            if ("put".equalsIgnoreCase(methodOverride)) {
                doPut(request, response);
                return;
            } else if ("delete".equalsIgnoreCase(methodOverride)) {
                doDelete(request, response);
                return;
            }
        }

        AccountDAO dao = new AccountDAO();

        // Get user input from form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone_number = request.getParameter("phone_number");
        String code = request.getParameter("verificationCode");
        String Sdob = request.getParameter("dob");

        Date dob = Date.valueOf(Sdob);
        // Register new user
        try {
            if (dao.register(username, email, phone_number, password, code, dob)) {
                request.setAttribute("Log", "Create success");
                request.getRequestDispatcher("adminDash.jsp").forward(request, response);

            } else {
                throw new SQLException("Duplicated.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("Error", e.getMessage());
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);

        }
    }

    /**
     * Update User.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String updateFS = request.getParameter("updateFS");
        String updateUser = request.getParameter("updateUser");

        if (!ParamsValid.isEmpty(updateFS)) {
            this.updateFS(request, response);
        } else if (!ParamsValid.isEmpty(updateUser)) {
            this.updateUser(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Create user DAO
        AccountDAO dao = new AccountDAO();
        try {
            // 3. Else get other params and create new user
            String userIdParam = request.getParameter("userId");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String vipStatusParam = request.getParameter("vipStatus");
            String vipTypeParam = request.getParameter("vipType");
            String vipSDateParam = request.getParameter("vipStartDate");
            String vipEDateParam = request.getParameter("vipEndDate");
            String roleParam = request.getParameter("role");
            String dobParam = request.getParameter("dob");
            String verificationCode = request.getParameter("verificationCode");
            String isVerificationParam = request.getParameter("isVerification");

            User upInfo = new User();

            if (!ParamsValid.isEmpty(userIdParam)) {
                upInfo.setUserId(Integer.parseInt(userIdParam));
            } else {
                request.setAttribute("Error", "No user ID found");
                request.getRequestDispatcher("adminDash.jsp").forward(request, response);
            }

            upInfo.setUsername(ParamsValid.isEmpty(username) ? null : username);
            upInfo.setPassword(ParamsValid.isEmpty(password) ? null : password);
            upInfo.setEmail(ParamsValid.isEmpty(email) ? null : email);
            upInfo.setPhoneNumber(ParamsValid.isEmpty(phoneNumber) ? null : phoneNumber);
            upInfo.setVipStatus(vipStatusParam.equals("Yes"));
            if (vipStatusParam.equals("Yes")) {
                upInfo.setVipType(ParamsValid.isEmpty(vipTypeParam) ? null : vipTypeParam);
                upInfo.setVipStartDate(Date.valueOf(vipSDateParam));
                upInfo.setVipEndDate(Date.valueOf(vipEDateParam));
            }
            upInfo.setRole(roleParam.equals("admin"));
            upInfo.setDob(ParamsValid.isEmpty(dobParam) ? null : Date.valueOf(dobParam));
            upInfo.setVerificationCode(ParamsValid.isEmpty(verificationCode) ? null : verificationCode);
            upInfo.setIsVerification(isVerificationParam.equals("true"));

            dao.updateUser(upInfo);
            request.setAttribute("Log", "Update User Success");
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("Error", e.getMessage());
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);

        }
    }

    private void updateFS(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        flashcardsetDAO fsDAO = new flashcardsetDAO();

        try {
            String idParam = request.getParameter("fsId");
            String nameParam = request.getParameter("name_of_flashcardset");
            String statusParam = request.getParameter("status");
            String scoreParam = request.getParameter("score");
            String tagsParam = request.getParameter("tags");
            String visiParam = request.getParameter("visibility");

            int id = -1;
            if (ParamsValid.isEmpty(idParam)) {
                request.setAttribute("Error", "No user ID found");
                request.getRequestDispatcher("adminDash.jsp").forward(request, response);
                return;
            } else {
                id = Integer.parseInt(idParam);
            }
            double score = Double.parseDouble(scoreParam);

            fsDAO.updateFlashcardSet(id, nameParam, tagsParam, visiParam, statusParam, score);
            request.setAttribute("Log", "Update Flash set Success");
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("Error", e.getMessage());
            request.getRequestDispatcher("adminDash.jsp").forward(request, response);

        }
    }

    /**
     * Delete User
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deleteU = req.getParameter("deleteUser");
        String deleteFS = req.getParameter("deleteFS");
        if (!ParamsValid.isEmpty(deleteU)) {
            this.deleteUser(req, resp);
        } else if (!ParamsValid.isEmpty(deleteFS)) {
            this.deleteFS(req, resp);
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AccountDAO dao = new AccountDAO();
        String user_id = req.getParameter("deleteUser");
        try {
            dao.deleteUser(Integer.parseInt(user_id));
            req.setAttribute("Log", "Delete User Success");
            req.getRequestDispatcher("adminDash.jsp").forward(req, resp);
        } catch (SQLException | ClassNotFoundException e) {
            req.setAttribute("Error", e.getMessage());
            req.getRequestDispatcher("adminDash.jsp").forward(req, resp);

        }
    }

    private void deleteFS(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        flashcardsetDAO dao = new flashcardsetDAO();
        String fs_id = req.getParameter("deleteFS");
        try {
            dao.deleteFlashcardset(Integer.parseInt(fs_id));
            req.setAttribute("Log", "Delete Flashcard Set Success");
            req.getRequestDispatcher("adminDash.jsp").forward(req, resp);
        } catch (SQLException | ClassNotFoundException e) {
            req.setAttribute("Error", e.getMessage());
            req.getRequestDispatcher("adminDash.jsp").forward(req, resp);

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
