package feedback_app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {
	
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/feedback_db";
    private static final String JDBC_USER = "root";       // Database username
    private static final String JDBC_PASSWORD = "rohitjha"; // Database password

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		// form data: Get karna hoga
		
			String email = req.getParameter("email");
			String phone = req.getParameter("phone");
			String feedbackMessage = req.getParameter("feedback_message");
		
		
		// form data: process
	
			// database connectivity
			Connection conn = null;
	        PreparedStatement pstmt = null;
	        
	        try {
	        	// Load JDBC driver
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            // Establish database connection
	            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

	            // SQL query to insert data
	            String sql = "INSERT INTO feedback (email, phone, feedbackMessage) VALUES (?, ?, ?)";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, email);
	            pstmt.setString(2, phone);
	            pstmt.setString(3, feedbackMessage);
	            
	         // Execute query
	            int rowsInserted = pstmt.executeUpdate();
	            if (rowsInserted > 0) {
	            	writer.println("<h2>Thank you, " + email + ", for your feedback!</h2>");
	            } else {
	            	writer.println("<h2>Failed to submit feedback. Please try again.</h2>");
	            }
	        }
	        catch (ClassNotFoundException e) {
	        	writer.println("<h2>Error: JDBC Driver not found!</h2>");
	            e.printStackTrace();
	        }catch (SQLException e) {
	        	writer.println("<h2>Database Error: " + e.getMessage() + "</h2>");
	            e.printStackTrace();
	        }finally {
	            // Close resources
	            try {
	                if (pstmt != null) pstmt.close();
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
			
			// data save
		
		// response dena hoga
	   //     resp.setContentType("text/html");
	   //		PrintWriter writer = resp.getWriter();
		
		
		writer.println("<h1>Feedback servlet working</h1>");
		writer.println("""
				<h2> Your details:</h2>
				<h3> Email Address %s </h3>
				<h3> Phone Number %s </h3>
				<h3> Feedback Message %s </h3>
				
				""".formatted(email,phone,feedbackMessage));
		
		
	}

	
	
}
