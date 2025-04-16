/**
Develop web applications using Servlets and JSP for user input handling, database integration.
Easy Level: Write a servlet to accept user credentials through an HTML form and display a personalized welcome message if the login is successful. Medium Level:
Create a servlet integrated with JDBC to display a list of employees from a database. Include a search form to fetch employee details by ID. Hard Level: Develop a JSP-based student portal
. Include a form for entering attendance details and save them to the database using a servlet.
*/

//HTML
<form action="LoginServlet" method="post">
    Username: <input type="text" name="username"><br>
    Password: <input type="password" name="password"><br>
    <input type="submit" value="Login">
</form>

//JAVA
  import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if("admin".equals(username) && "password123".equals(password)) {
            out.println("<h2>Welcome, " + username + "!</h2>");
        } else {
            out.println("<h2>Login Failed. Try again.</h2>");
        }
    }
}

//SQL
CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    department VARCHAR(50)
);
//SERVLET JDBC
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "password");
            
            String empId = request.getParameter("id");
            PreparedStatement ps;
            
            if (empId != null) {
                ps = con.prepareStatement("SELECT * FROM employees WHERE id=?");
                ps.setInt(1, Integer.parseInt(empId));
            } else {
                ps = con.prepareStatement("SELECT * FROM employees");
            }
            
            ResultSet rs = ps.executeQuery();
            
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("department") + "</td></tr>");
            }
            out.println("</table>");
            
            con.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
//JSP
<form action="AttendanceServlet" method="post">
    Student ID: <input type="text" name="studentId"><br>
    Date: <input type="date" name="date"><br>
    Status: <select name="status">
        <option value="Present">Present</option>
        <option value="Absent">Absent</option>
    </select><br>
    <input type="submit" value="Submit Attendance">
</form>
  
//JAVA Attendance
  import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String studentId = request.getParameter("studentId");
        String date = request.getParameter("date");
        String status = request.getParameter("status");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "password");
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO attendance (studentId, date, status) VALUES (?, ?, ?)");
            ps.setString(1, studentId);
            ps.setString(2, date);
            ps.setString(3, status);
            
            int result = ps.executeUpdate();
            if(result > 0) {
                out.println("<h3>Attendance Saved Successfully!</h3>");
            } else {
                out.println("<h3>Failed to Save Attendance.</h3>");
            }
            
            con.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
