/* Group Project  Servlets
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

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Tejindra
 */
public class AddItemWithCookie extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html";

    // Use a prepared statement to store a student into the database
    private PreparedStatement pstmt;

    /**
     * Initialize variables
     *
     * @throws javax.servlet.ServletException Defines a general exception a
     * servlet can throw when it encounters difficulty.
     */
    public void init() throws ServletException {
        initializeJdbc();
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Obtain data from the form
        String itemName = request.getParameter("itemName");
        String quantity = request.getParameter("quantity");
        String weight = request.getParameter("weight");
        String expirationDate = request.getParameter("expirationDate");

        if (itemName.length() == 0) {
            out.println("You have to enter a poduct");
        } else {
            // Create cookies and send cookies to browsers
            Cookie cookieItemName = new Cookie("itemName", itemName);

            response.addCookie(cookieItemName);
            Cookie cookieQuantity = new Cookie("quantity", quantity);
            response.addCookie(cookieQuantity);

            Cookie cookieWeight = new Cookie("weight", weight);
            response.addCookie(cookieWeight);
            Cookie cookieExpirationDate = new Cookie("expirationDate", expirationDate);
            response.addCookie(cookieExpirationDate);

            // Ask for confirmation
            out.println("You Entered the following product");
            out.println("<p>Item Name: " + itemName);
            out.println("<br>Quantity: " + quantity);
            out.println("<br>Weight: " + weight);
            out.println("<br>Expiration Date: " + expirationDate);

            // Set the action to process added items
            out.println("<p><form method=\"post\" action="
                    + "AddItemWithCookie>");
            out.println("<p><input type=\"submit\" value=\"Confirm\" >");
            out.println("</form>");
        }

        out.close(); // Close stream
    }

    /**
     * Called by the server to allow a servlet to handle a POST request.
     *
     * @param request object that contains the request the client has made of
     * the servlet
     * @param response object that contains the response the servlet sends to
     * the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if the request for the POST could not be handled
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();

        String itemName = "";
        String quantity = "";
        String weight = "";
        String expirationDate = "";

        // Read the cookies
        Cookie[] cookies = request.getCookies();

        // Get cookie values
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("itemName")) {
                itemName = cookies[i].getValue();
            } else if (cookies[i].getName().equals("quantity")) {
                quantity = cookies[i].getValue();
            } else if (cookies[i].getName().equals("weight")) {
                weight = cookies[i].getValue();
            } else if (cookies[i].getName().equals("expirationDate")) {
                expirationDate = cookies[i].getValue();
            }
        }

        try {
            storeProduct(itemName, quantity, weight, expirationDate);

            out.println(itemName + " is now added in the database");

            out.close(); // Close stream
        } catch (SQLException ex) {
            out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Called to load the JDBC Drivers, establish a connection with the database
     * And create a statement
     */
    private void initializeJdbc() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // Establish a connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/monthlyPantry?useSSL=false", "root", "Hich");

            // Create a Statement
            pstmt = conn.prepareStatement("insert into foodlist "
                    + "(ProductName, Quantity, Weight, ExpDate"
                    + ") values (?, ?, ?, ?)");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Store the entered items into the database *
     *
     * @param itemName Name of the Product
     * @param quantity Quantity of the product
     * @param weight Weight of the product
     * @param expirationDate Expiration Date of the product
     *
     * @throws SQLException An exception that provides information on a database
     * access error or other errors.
     */
    private void storeProduct(String itemName, String quantity,
            String weight, String expirationDate) throws SQLException {
        pstmt.setString(1, itemName);
        pstmt.setString(2, quantity);
        pstmt.setString(3, weight);
        pstmt.setString(4, expirationDate);
        pstmt.executeUpdate();
    }
}
