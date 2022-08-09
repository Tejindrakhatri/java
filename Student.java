/* Group Project Servlets
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
 * @author Error_404_Name_Not_Found
 */
public class Student extends HttpServlet {

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
            try {
                String sql = ("SELECT * FROM foodlist");
                rs = pstmt.executeQuery(sql);

                String str = styleTable() + "<table border=1><tr><th>Product Type</th><th>Quantity</th><th>Weight</th>"
                        + "<th>Exp Date</th><th>Notes</th></tr>";
                while (rs.next()) {
                    str += "<tr><td>" + rs.getString(2) + "</td>" + "<td>" + rs.getString(3) + "</td>" + "<td>" + rs.getString(4)
                            + "</td>" + "<td>" + rs.getString(5) + "</td>" + "<td>" + rs.getString(6) + "</td></tr>";
                }
                str += "</table>";

                out.print(str);
                out.close();

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
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the
     * servlet handles the GET request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Setup the table form that where the data would be displayed
     *
     * @return String
     */
    protected String styleTable() {
        return " <style type=\"text/css\">\n"
                + "                    table{\n"
                + "                        border-collapse: collapse;\n"
                + "                        width: 100%;\n"
                + "                        color: #d96459;\n"
                + "                        font-family: monospace;\n"
                + "                        font-size: 25px;\n"
                + "                        text-align: Left;\n }\n"
                + "                    th{\n"
                + "                        background-color: #d96459;\n"
                + "                        color: white;          \n"
                + "                    }\n"
                + "                    td{\n"
                + "                      paddin: 10px  \n"
                + "                    }\n"
                + "                    \n"
                + "                    </style>";
    }

    /**
     * Called to load the JDBC Drivers, establish a connection with the database
     * And create a statement
     */
    public void initializeJdbc() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/monthlypantry?useSSL=false", "root", "Hich");
            pstmt = con.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
