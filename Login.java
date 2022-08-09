/* Group Project 2 Servlets
 *  By: Error_404_Name_Not_Found
*   Date: 11/19/2018
*
* Using the code models from this chapter, create a web form that manages a portion of the data management in your project -
* for example user resources, user login, or user scores.  The web form should have Session-based cookies, and store and retrieve 
* information from a server-side database.  
* User interface is important. The user should not have to re-enter database information, but that information can change.
* Session information should last no longer than 6 hours.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hicham
 */
public class Login extends HttpServlet {

    private Statement pstmt;
    private ResultSet rs;

    public void init() throws ServletException {
        initializeJdbc();
    }

    /**
     * Called by the server to allow a servlet to process a request.
     *
     * @param request object that contains the request the client has made of
     * the servlet
     * @param response object that contains the response the servlet sends to
     * the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the
     * servlet handles the GET request
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String userName = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                String sql = ("SELECT * FROM students WHERE username='" + userName + "' AND password='" + password + "'");
                rs = pstmt.executeQuery(sql);

                String choose = request.getParameter("choose");
                if (rs.next()) {
                    if ("Student".equals(choose)) {
                        response.sendRedirect("studentPage.html");
                    } else {
                        response.sendRedirect("AddItemWithCookie.html");
                    }
                } else {
                    out.println("Wrong user");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Called by the server to allow a servlet to handle a GET request.
     *
     * @param request object that contains the request the client has made of
     * the servlet
     * @param response object that contains the response the servlet sends to
     * the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the
     * servlet handles the GET request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Called by the server to allow a servlet to handle a POST request.
     *
     * @param request object that contains the request the client has made of
     * the servlet
     * @param response object that contains the response the servlet sends to
     * the client
     * @throws ServletException if the request for the POST could not be handled
     * @throws IOException if an input or output error is detected when the
     * servlet handles the GET request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Called to load the JDBC Drivers, establish a connection with the database
     * And create a statement
     */
    public void initializeJdbc() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdatabase?useSSL=false", "root", "Hich");
            pstmt = con.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
