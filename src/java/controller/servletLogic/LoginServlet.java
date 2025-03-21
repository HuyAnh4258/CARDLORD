package controller.servletLogic;

import model.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import model.User;
import model.UserActivityDAO;
import utils.Helper;
import utils.SendEmail;

public class LoginServlet extends HttpServlet {

    private final String INVALID_PAGE = "login.jsp";
    private final String SEARCH_PAGE = "dashboard.jsp";
    private final String VERIFY_PAGE = "verify.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = INVALID_PAGE;

        String button = request.getParameter("btAction");

        // Check if the login button is pressed
        try {
            if (button.equals("Log in")) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                int user_id = -1;

                String captcha = request.getParameter("captcha");
                String captcha_txt = (String) request.getSession().getAttribute("captcha");

                // Validate CAPTCHA
                if (captcha_txt == null || !captcha_txt.equals(captcha)) {
                    request.setAttribute("errorMessage", "Invalid CAPTCHA. Please try again.");
                    request.getRequestDispatcher(INVALID_PAGE).forward(request, response);
                    return;
                }

                AccountDAO dao = new AccountDAO();
                boolean result = dao.checkLogin(username, password);

                // If login is successful
                if (result) {
                    User user = dao.getUserByUsername(username);

                    // If the account is verified
                    if (user.isIsVerification()) {
                        HttpSession session = request.getSession();
                        user_id = dao.getUserID(username, password);
                        session.setAttribute("user", user);
                        session.setAttribute("user_id", user_id);
                        session.setAttribute("user_role", user.isRole());
                        
                         UserActivityDAO activityDAO = new UserActivityDAO();
                    activityDAO.addStartTime(user_id);
                        
                        request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                        // If the account is not verified
                    } else {
                        url = VERIFY_PAGE;
                        request.setAttribute("email", user.getEmail());

                        String Verifycode = Helper.generateCode(6);
                        dao.setVerificationCode(username, Verifycode);
                        SendEmail.sendConfirmationEmail(user.getEmail(), Verifycode);
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
// If login fails
                } else {
                    request.setAttribute("errorMessage", "Incorrect username or password!");
                    request.getRequestDispatcher(INVALID_PAGE).forward(request, response);
                }
            }

            // Handle SQL exceptions
        } catch (SQLException ex) {
            ex.printStackTrace();

            // Handle ClassNotFound exceptions
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();

            // Ensure the response is redirected and resources are closed
        } finally {
            out.close();
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
        return "Short description";
    }

}
