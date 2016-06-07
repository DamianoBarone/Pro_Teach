/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Damiano
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends HttpServlet {

    String idTeacher = "-1";
    String idAdmin = "-1";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void confirm_student(Statement stmt, PrintWriter out, String idcourse, String idstudent) throws SQLException {
        //The action after the teacher accept the student
        String query = " UPDATE Student_has_Course SET status=1 Where idCourse='" + idcourse + "' and idStudent='" + idstudent + "'";
        stmt.executeUpdate(query);
        out.println("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<script type=\"text/javascript\"> window.location.replace(\"AdminServlet?param=4&idcourse=" + idcourse + "\")</script></head></html>");

    }

    protected void personalinfo(Statement stmt, PrintWriter out) throws SQLException {
        //information of admin or Teacher
        String query;
        ResultSet rs;
        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <title>Personal information</title>\n"
                + "  <link href=\"style.css\" rel=\"stylesheet\">\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "<ul id=\"menu\"> \n");
        if (!idAdmin.equals("-1")) { //admin
            query = "SELECT * FROM mydb.User INNER JOIN mydb.Administration_personnel ON User.idUser=idAdministrationPersonnel WHERE  User.idUser= '" + idAdmin + "'";
            out.println(
                    "    <li><a href='AdminServlet?param=1'>Manage User</a></li> \n"
                    + "    <li><a href='AdminServlet?param=2'>Manage Course</a></li> \n"
                    + "<li><a href='#'>Personal info</a></li>\n"
                    + "<li><a href='index.html'>Log out</a></li>\n");
        } else if (!idTeacher.equals("-1"))//teacher
        {
            query = "SELECT * FROM mydb.User INNER JOIN mydb.Teacher ON User.idUser=idTeacher WHERE  User.idUser= '" + idTeacher + "'";
            out.println(
                    "    <li><a href='AdminServlet?param=1'>Manage Course</a></li>\n"
                    + "    <li><a href='AdminServlet?param=2'>Personal info</a></li>\n"
                    + "    <li><a href='index.html'>Log out</a></li>\n");

        } else {
            return;// LOGIN ILLEGAL
        }
        rs = stmt.executeQuery(query);
        rs.next();
        out.println("</ul> \n" + "<div id=\"main\">\n"
                + "  <h1>ProTeach</h1> \n"
                + "  <h2>Personal information</h2>"
                + "<form method=\"POST\"  id=\"forminfo\" name=\"myForm\" action=\"AdminServlet\">" //FORM WITH INFO
                + "      Email <input type=\"email\" name=\"email\" value=\"" + rs.getString("email") + "\"><br>\n"
                + "      Name <input type=\"text\" name=\"name\" value=\"" + rs.getString("name") + "\" ><br>\n"
                + "      Surname<input type=\"text\" name=\"surname\" value=\"" + rs.getString("surname") + "\"><br>\n"
                + "      Address<input type=\"text\" name=\"address\" value=\"" + rs.getString("address") + "\"><br>\n"
                + "      Password<input type=\"password\" name=\"password\" value=\"" + rs.getString("password") + "\"><br>\n"
                + "      <input type=\"hidden\" name=\"objhidden\" value=\"3\">"
                + "      <input   type= \"submit\" value=\"Save change\">\n"
                + "    </form>");

    }

    protected void manageCourse(Statement stmt, PrintWriter out) throws SQLException { //e' la stessa di student servlet si puo mettere in un altro file
        //LIST OF COURSE FOR THE ADMIN
        String sql = "SELECT Course.idCourse, User.name,surname,Course.idTeacher,Course.name,semester,ECTS FROM (mydb.Teacher INNER JOIN mydb.Course ON Teacher.idTeacher=Course.idTeacher) INNER JOIN  User ON Teacher.idTeacher=idUser";
        ResultSet rs = stmt.executeQuery(sql);
        out.println(
                "<!DOCTYPE html> <html> <head><link href=\"style.css\" rel=\"stylesheet\">"
                + " <title>Course</title>"
                + "</head>"
                + "<body>"
                + "<ul id=\"menu\"> \n"
                + "    <li><a href='AdminServlet?param=1'>Manage User</a></li> \n"
                + "    <li><a href='#'> Manage Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">\n"
                + "  <h1>ProTeach</h1>\n"
                + "  <h2> courses</h2>\n"
        );
        out.println("<button onclick=\"mex()\">Add Course</button>");
        out.println("<script type=\"text/javascript\"> function mex(){window.location.replace(\"AdminServlet?param=6\")} "
                + "</script>"
                + " <table>\n"
                + "  <tr>\n"
                + "    <th>Course name</th>\n"
                + "    <th>semester</th>\n"
                + "    <th>ECTS</th>\n"
                + "    <th>Assiged Teacher</th>\n"
                + "  </tr>\n");
        while (rs.next()) {
            out.println(
                    "  <tr>\n"
                    + "    <td>" + rs.getString("Course.name") + "</td>\n"
                    + "    <td>" + rs.getString("semester") + "</td>\n"
                    + "    <td>" + rs.getString("ECTS") + "</td>\n"
                    + "    <td>" + rs.getString("User.name") + " " + rs.getString("surname") + "</td>\n"
                    + "  </tr>\n");
        }
        out.println("</table> \n"
                + "</div>\n"
                + "</body>\n"
                + "</html>");

    }

    protected void manage_user(PrintWriter out, Statement stmt) throws SQLException {
        //list of the user for admin
        String query = "SELECT * FROM User";
        ResultSet rs = stmt.executeQuery(query);
        out.println("<!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title></title>"
                + " </head>\n"
                + "  <body>\n"
                + "\n"
                + "  <ul id=\"menu\"> \n"
                + "    <li><a href='#'>Manage User</a></li> \n"
                + "    <li><a href='AdminServlet?param=2'>Manage Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">");
        out.println("<button onclick=\"mex()\">Add Teacher</button>"
                + " <table>\n"
                + "  <tr>\n"
                + "    <th> Name</th>\n"
                + "    <th>Surname </th>\n"
                + "    <th>Email</th>\n"
                + "    <th>Type</th>\n"
                + "  </tr>\n");
        while (rs.next()) {//list of User
            out.println(
                    "  <tr>\n"
                    + "<td>" + rs.getString("name") + "</td>\n"
                    + "<td>" + rs.getString("surname") + "</td>\n"
                    + "<td>" + rs.getString("email") + "</td>\n");
            switch (rs.getString("type")) {
                // admin
                case "1":
                    out.println("<td> Admin </td>\n");
                    break;
                case "2":
                    out.println("<td> Teacher </td>\n");
                    break;
                default:
                    out.println("<td> Student </td>\n");
                    break;
            }
            out.println("  </tr>\n");
        }
        out.println("</table> \n");
        out.println("<script type=\"text/javascript\"> function mex(){window.location.replace(\"AdminServlet?param=5\")} "
                + "</script>"
                + "</form>"
                + "</div>\n"
                + "</body>\n"
                + "</html>");

    }

    protected void addCourse(Statement stmt, PrintWriter out) throws SQLException {
        //page for add a course
        out.println("<!DOCTYPE html> <html> <head><link href=\"style.css\" rel=\"stylesheet\">"
                + " <title>Course</title>"
                + "</head>"
                + "<body>"
                + "<ul id=\"menu\"> \n"
                + "    <li><a href='AdminServlet?param=1'>Manage User</a></li> \n"
                + "    <li><a href='AdminServlet?param=2'>Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">\n"
                + "  <h1>ProTeach</h1>\n"
                + "    <h2>Create new course</h2>\n"
                + "     <form id=\"myForm\"  action=\"AdminServlet\" method= \"post\">\n"
                + "    	<label >Name</label>\n"
                + "    	<input type=\"text\" name=\"courseName\"><br>\n"
                + "    	<label >ECTS</label>\n"
                + "    	<input type=\"text\" name=\"ECTS\"><br>\n"
                + "    	<label >Semester</label>\n"
                + "    	<select name=\"semester\" >\n"
                + "			<option value=\"1\">1</option>\n"
                + "			<option value=\"2\">2</option>\n"
                + "			<option value=\"3\">3</option>\n"
                + "			<option value=\"4\">4</option>\n"
                + "			<option value=\"5\">5</option>\n"
                + "			<option value=\"6\">6</option>\n"
                + "			<option value=\"7\">7</option>\n"
                + "			<option value=\"8\">8</option>\n"
                + "			<option value=\"9\">9</option>\n"
                + "			<option value=\"10\">10</option>\n"
                + "			<option value=\"11\">11</option>\n"
                + "			<option value=\"12\">12</option>\n"
                + "		</select><br>\n"
                + "    	<label >Status</label>\n"
                + "    	<select name=\"status\" >\n"
                + "			<option value=\"true\">active</option>\n"
                + "			<option value=\"false\">inactive</option>\n"
                + "		</select><br>\n"
                + "		<label>Asigned teacher</label>\n"
                + "		<select name=\"teacherid\">\n");
        String sql = "SELECT * FROM mydb.User join Teacher ON idUser=idTeacher";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {//list of student
            out.println("<option value=\"" + rs.getString("idTeacher") + " \">" + rs.getString("name") + " " + rs.getString("surname") + "</option>");  /// I WAIT FOR Page
        }

        out.println("		</select><br>\n"
                + "		<input type=\"submit\" name=\"Add\" value=\"Add\">\n"
                + "  <input  name=\"objhidden\" type=\"hidden\" value=\"2\"  >"
                + "    </form>\n"
                + "\n"
                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>");
    }

    protected void addTeacher(Statement stmt, PrintWriter out) throws SQLException {
        //page for add teacher
        out.println("<!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title></title>"
                + " </head>\n"
                + "  <body>\n"
                + "\n"
                + "  <ul id=\"menu\"> \n"
                + "    <li><a href='AdminServlet?param=1'>Manage User</a></li> \n"
                + "    <li><a href='AdminServlet?param=2'>Manage Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">"
                + "<h1>ProTeach</h1>\n"
                + "    <h2>Add teacher</h2>\n"
                + "     <form id=\"myForm\"  action=\"AdminServlet\" method= \"post\">\n"
                + "      Email <input type=\"email\" name=\"email\" placeholder=\"your email\"><br>\n"
                + "      Name <input type=\"text\" name=\"name\" placeholder=\"Name\"><br>\n"
                + "      Surname<input type=\"text\" name=\"surname\" placeholder=\"surname\"><br>\n"
                + "      Password<input type=\"password\" name=\"password\" placeholder=\"password\"><br>\n"
                + "      Confirm password<input type=\"password\" name=\"confirmPass\" placeholder=\"password\"><br>\n"
                + "      Address<input type=\"text\" name=\"address\" placeholder=\"address\"><br>\n"
                + "      Salary<input type=\"text\" name=\"salary\" placeholder=\"salary\"><br>\n"
                + "      Position<input type=\"text\" name=\"position\" placeholder=\"position\"><br>\n"
                + "  <input  name=\"objhidden\" type=\"hidden\" value=\"1\"  >"
                + "      <input type=\"button\" value=\"Save\" onclick=\"ValidateUser(this.form)\">\n"
                + "	  \n"
                + "    </form>\n"
                + "	</div>\n"
                + "	<script>\n"
                + "	var ck_password =new RegExp (/^[A-Za-z0-9-]/);\n"
                + "	var ck_name =new RegExp (/^[A-Za-z0-9-]/);\n"
                + "	var ck_email = new RegExp (/^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$/i)\n"
                + "	function ValidateUser(form) {\n"
                + "	\n"
                + "	var email = form.email.value;\n"
                + "    var password = form.password.value;\n"
                + "	var name = form.name.value;\n"
                + "	var password = form.password.value;\n"
                + "	var surname = form.surname.value;\n"
                + "	var confirmPass = form.confirmPass.value;\n"
                + "	 if (email==\"\")\n"
                + "    {\n"
                + "     alert(\"email name must be filled out\");\n"
                + "     return ;\n"
                + "     \n"
                + "    }else if (!ck_email.test(email)) {\n"
                + "          alert (\"You must enter a valid email  address.\");\n"
                + "          return ;\n"
                + "     }\n"
                + "\n"
                + "	 \n"
                + "	 \n"
                + "\n"
                + "	 if (validateName(name) && validateName(surname) && validatePass(password) &&validatePass(confirmPass))\n"
                + "		 document.getElementById(\"myForm\").submit();"
                + "	 }\n"
                + "	 \n"
                + "	 \n"
                + "	 function validatePass(pass){\n"
                + "	 \n"
                + "		if (pass.length=='') {\n"
                + "          alert (\"You must enter a password\"); \n"
                + "          return false;\n"
                + "     }\n"
                + "	 else if (!ck_password.test(pass)) {\n"
                + "          alert (\"You must enter a valid password\");\n"
                + "          return false;\n"
                + "     }\n"
                + "     return true;\n"
                + "	 }\n"
                + "	 \n"
                + "	 function validateName(name){\n"
                + "		\n"
                + "	 if (name==\"\")\n"
                + "    {\n"
                + "		alert(\"Surname and Name must be filled out\");\n"
                + "     	 return false;\n"
                + "    }\n"
                + "    else if (name.length > 45)\n"
                + "    {\n"
                + "        alert(\"Surname and Name cannot be more than 45 characters\");\n"
                + "         return false;\n"
                + "       \n"
                + "    }else if (!ck_name.test(name)) {\n"
                + "	\n"
                + "	  alert(\"You must enter a valid  name\");\n"
                + "	   return false;\n"
                + "	  }\n"
                + "	 return true;\n"
                + "	 }\n"
                + "	\n"
                + "	</script>\n"
                + "	<script src=\"script.js\"></script> \n"
                + "	\n"
                + "	<script>\n"
                + "		function cancel(){\n"
                + "			window.location.href=\"#\";\n"
                + "		}\n"
                + "	</script>\n"
                + "</body>\n"
                + "</html> ");
    }

    protected void course_teacher(PrintWriter out, Statement stmt) throws SQLException {
        //the list of the course of the teacher        
        String query = "SELECT * FROM Course  where idTeacher = " + idTeacher;
        ResultSet rs = stmt.executeQuery(query);

        out.println(
                " <!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title></title>"
                + " </head>\n"
                + "  <body>\n"
                + "\n"
                + "  <ul id=\"menu\"> \n"
                + "    <li><a href='AdminServlet?param=1'>Manage Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">"
                + " <table>\n"
                + "  <tr>\n"
                + "    <th>Course name</th>\n"
                + "    <th>semester</th>\n"
                + "    <th>ECTS</th>\n"
                + "  </tr>\n");
        while (rs.next()) {//list of courses 
            out.println(
                    "  <tr>\n"
                    + "    <td><a href='AdminServlet?param=4&" + "idcourse=" + rs.getString("idCourse") + "'</a>" + rs.getString("name") + "</td>\n"
                    + "    <td>" + rs.getString("semester") + "</td>\n"
                    + "    <td>" + rs.getString("ECTS") + "</td>\n"
                    + "  </tr>\n");
        }
        out.println(
                "</table> \n"
                + "</div>\n"
                + "</body>\n"
                + "</html>");
    }

    protected void teacher_choose_course(PrintWriter out, Statement stmt, String idcourse) throws SQLException {
        //this page is after the teacher chose one course from his home page

        String query = "SELECT * FROM Course WHERE idCourse=" + idcourse; //studen enroll
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
            //empty
            System.out.println("query empty teacher_choose_course");
            return;
        }
        out.println(" <!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title></title>"
                + " </head>\n"
                + "  <body>\n"
                + "\n"
                + "  <ul id=\"menu\"> \n"
                + "    <li><a href='AdminServlet?param=1'>Manage Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">"
                + "    <h1>ProTeach</h1>\n"
                + "    <h2 id=\"courseName\">Course name : " + rs.getString("Course.name") + " </h2>\n"
                + "  <strong>Enrolled student</strong>\n"
                + "  <table id=\"enrolledStudent\">\n"
                + "  <tr>\n"
                + "    <th>Student name</th>\n"
                + "    <th>ID</th>\n"
                + "    <th>email</th>\n"
                + "    </tr>\n");
        query = "SELECT * FROM (mydb.Student_has_Course join User ON idStudent=idUser)join Course ON Course.idCourse= Student_has_Course.idCourse WHERE Course.idCourse='" + idcourse + "' and status=1"; //studen enroll

        rs = stmt.executeQuery(query);
        while (rs.next()) {//list of student
            out.println(rs.getString("name")
                    + "  <tr>\n"
                    + "    <td>" + rs.getString("User.name") + " " + rs.getString("surname") + " </td>\n"
                    + "    <td>" + rs.getString("User.idUser") + "</td>\n"
                    + "    <td>" + rs.getString("email") + "</td></tr>\n");
        }
        query = "SELECT * FROM (mydb.Student_has_Course join User ON idStudent=idUser)join Course ON Course.idCourse= Student_has_Course.idCourse WHERE Course.idCourse='" + idcourse + "' and status=0"; //studen enroll
        rs = stmt.executeQuery(query);
        out.println("  </table><br> <strong>Student need approval</strong>\n"
                + " <table id=\"studentApproval\">\n"
                + "  <tr>\n"
                + "    <th>Student name</th>\n"
                + "    <th>ID</th>\n"
                + "    <th>email</th>\n"
                + "    <th>confirm</th>\n"
                + "    </tr>\n");
        while (rs.next()) {//list of student need approval
            out.println(rs.getString("name")
                    + "  <tr>\n"
                    + "    <td>" + rs.getString("User.name") + " " + rs.getString("surname") + " </td>\n"
                    + "    <td>" + rs.getString("User.idUser") + "</td>\n"
                    + "    <td>" + rs.getString("email") + "</td>\n"
                    + "    <td><a href='AdminServlet?param=5&idCourse=" + rs.getString("Course.idCourse") + "&idUser=" + rs.getString("User.idUser") + "'> Accept </a></td></tr>\n");

        }
        out.println(
                "  </table><br>\n"
                + "\n"
                + "  <strong>List of test</strong>\n"
                + "  <table id=\"listest\">\n"
                + "  <tr>\n");
        query = "SELECT * FROM mydb.Test WHERE idCourse='" + idcourse + "'";
        rs = stmt.executeQuery(query);

        out.println(
                "    <th>Name</th>\n"
                + "    <th>Data start</th>\n"
                + "    <th>Data finish</th>\n"
                + "    </tr>\n"
                + "  <tr>\n");
        while (rs.next()) {
            out.println(
                    "    <td>" + rs.getString("name") + "</td>\n"
                    + "    <td>" + rs.getString("dateStart") + "</td>\n"
                    + "    <td>" + rs.getString("dateFinish") + "</td>\n"
                    + "  </tr>\n");
        }

        out.println("  </table><br>\n"
                + "  <strong>Test to be evaluated</strong>\n"
                + "  <table id=\"evaluateTest\">\n"
                + "  <tr>\n");
        query = "SELECT * FROM (Test join student_has_test on Test.idTest=student_has_test.idTest) join user on student_has_test.idStudent=user.idUser WHERE idCourse='" + idcourse + "' and grade=-1";
        rs = stmt.executeQuery(query);

        out.println(
                "    <th>Test name</th>\n"
                + "    <th>Student name</th>\n"
                + "    </tr>\n");
        while (rs.next()) {
            out.println("<tr>\n"
                    + "    <td><a href='AdminServlet?param=7&idSHT=" + rs.getString("idStudentHasTest") + "'>" + rs.getString("Test.name") + "</a></td>\n"
                    + "    <td>" + rs.getString("user.name") + "</td>\n"
                    + "  </tr>\n");
        }

        out.println("  </table><br>\n"
                + "\n"
                + "<script type=\"text/javascript\"> "
                + "function newTest() {"
                + "window.location.replace(\"AdminServlet?param=6&idcourse=" + idcourse + "\");"
                + "}"
                + "</script>\n"
                + "  	<input type=\"button\" name=\"createst\" value=\"Create new test\" onclick=\"newTest()\">\n"
                + "</body>\n"
                + "</html>");
    }

    protected void showTestAnswer(PrintWriter out, Statement stmt, String idSHT) throws SQLException {
        // displai course name
        ResultSet rs = stmt.executeQuery("SELECT * FROM student_answer join question on student_answer.idQuestion=question.idQuestion WHERE type='3' and idStudentHasTest='" + idSHT + "'");
        //rs.getString("name")
        out.println("<form id=\"evaluationForm\"  action=\"AdminServlet\" method= \"get\">\n");
        int i = 0;
        while (rs.next()) {
            i++;
            out.println("Question " + i + "<br>"
                    + "<p>" + rs.getString("question") + "<p>\n"
                    + "<p>" + rs.getString("answer") + "</p>\n"
                    + "<label>Max points: </label>" + rs.getString("point") + "<br>\n"
                    + "<label>Assigned point: </label><input name=\"p" + i + "\" type=\"number\" value=\"0\" min=\"0\" max=\"" + rs.getString("point") + "\" required=\"required\"><br><br>\n"
                    + "<input type=\"hidden\" name=\"idSA" + i + "\" value=\"" + rs.getString("idStudentAnswer") + "\">\n");
        }
        out.println("<input type=\"hidden\" name=\"param\" value=\"8\">\n"
                + "<input type=\"submit\" value=\"Save result\">\n"
                + "</form>");
    }

    protected void saveEvaluatedAnswer(HttpServletRequest request, PrintWriter out, Statement stmt) throws SQLException {
        int i = 1;
        double totPoints = 0.0, studentPoints = 0.0;
        while (request.getParameter("idSA" + i) != null) {
            stmt.executeUpdate("UPDATE student_answer SET correct=\"" + ((Double.parseDouble(request.getParameter("p" + i)) > 0.0) ? "1" : "0") + "\", evaluatedPoints=\"" + request.getParameter("p" + i) + "\" WHERE idStudentanswer='" + request.getParameter("idSA" + i) + "';");
            i++;
        }
        ResultSet rs = stmt.executeQuery("select sum(point) as s from question where idTest in (SELECT idTest FROM student_answer join question on student_answer.idQuestion=question.idQuestion WHERE idStudentAnswer='" + request.getParameter("idSA" + (i - 1)) + "')");
        rs.next();
        totPoints = Double.parseDouble(rs.getString("s"));
        rs = stmt.executeQuery("select sum(evaluatedPoints) as p from student_answer where correct=\"1\" and idStudentHasTest in (select idStudentHasTest from student_answer where idStudentAnswer=\"" + request.getParameter("idSA" + (i - 1)) + "\")");
        rs.next();
        studentPoints = Double.parseDouble(rs.getString("p"));

        long grade = Math.round(studentPoints / totPoints * 5);
        stmt.executeUpdate("UPDATE student_has_test SET grade=\"" + grade + "\" WHERE idStudentHasTest in (select idStudentHasTest from student_answer where idStudentAnswer=\"" + request.getParameter("idSA" + (i - 1)) + "\");");

        rs = stmt.executeQuery("select idCourse from test where idTest in (select idTest from student_has_test where idStudentHasTest in (select idStudentHasTest from student_answer where idStudentAnswer=\"" + request.getParameter("idSA" + (i - 1)) + "\"))");
        rs.next();
        out.println("<script type=\"text/javascript\"> window.alert(\"Result: " + grade + ", saved.\"); window.location.replace(\"AdminServlet?param=4&idcourse=" + rs.getString("idCourse") + "\");</script>");
    }

    protected void createTest(PrintWriter out, Statement stmt, String idcourse) throws SQLException {
        // displai course name
        ResultSet rs = stmt.executeQuery("SELECT * FROM Course WHERE Course.idCourse='" + idcourse + "'");
        //rs.getString("name")
        rs.next();
        out.println(" <!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title>Create Test for " + rs.getString("name") + "</title>"
                + " </head>\n"
                + "<script type=\"text/javascript\"> "
                + "var numQuestion=1;"
                + "function init()"
                + "{numQuestion=1;}"
                + "function appendQuestion()\n"
                + "{\n"
                + "numQuestion+=1;\n"
                + "var myDiv = document.getElementById(\"questions\");\n"
                + "var newDiv = document.createElement(\"div\");\n"
                + "newDiv.setAttribute(\"id\", \"q\"+numQuestion);\n"
                + "var radio1=document.createElement(\"input\");\n"
                + "radio1.setAttribute(\"type\", \"radio\");\n"
                + "radio1.setAttribute(\"name\", \"openchoice\"+numQuestion);\n"
                + "radio1.setAttribute(\"value\", \"open\");\n"
                + "var radio2=document.createElement(\"input\");\n"
                + "radio2.setAttribute(\"type\", \"radio\");\n"
                + "radio2.setAttribute(\"name\", \"openchoice\"+numQuestion);\n"
                + "radio2.setAttribute(\"value\", \"choice\");\n"
                + "var point = document.createElement(\"input\");\n"
                + "point.setAttribute(\"name\", \"point\"+numQuestion);\n"
                + "point.setAttribute(\"type\", \"text\");\n"
                + "point.setAttribute(\"maxlength\", \"10\");\n"
                + "point.setAttribute(\"size\", \"4\");\n"
                + "var text=document.createElement(\"textarea\");\n"
                + "text.setAttribute(\"name\", \"text\"+numQuestion);\n"
                + "text.setAttribute(\"maxlength\", \"100\");\n"
                + "text.setAttribute(\"rows\", \"5\");\n"
                + "text.setAttribute(\"cols\", \"50\");\n"
                + "var button=document.createElement(\"input\");\n"
                + "button.setAttribute(\"type\", \"button\");\n"
                + "button.setAttribute(\"name\", \"appendAnswer\"+numQuestion);\n"
                + "button.setAttribute(\"value\", \"Add answer\");\n"
                + "button.setAttribute(\"onclick\", \"appendAnswer(\"+numQuestion+\", 0)\");\n"
                + "newDiv.appendChild(radio1)\n"
                + "newDiv.appendChild(document.createTextNode(\"Open question\"))\n"
                + "newDiv.appendChild(radio2)\n"
                + "newDiv.appendChild(document.createTextNode(\"Single/multiple choice\"))\n"
                + "newDiv.appendChild(document.createElement(\"br\"))\n"
                + "newDiv.appendChild(document.createTextNode(\"Point: \"))\n"
                + "newDiv.appendChild(point)\n"
                + "newDiv.appendChild(document.createElement(\"br\"))\n"
                + "newDiv.appendChild(document.createTextNode(\"Question: \"))\n"
                + "newDiv.appendChild(document.createElement(\"br\"))\n"
                + "newDiv.appendChild(text)\n"
                + "newDiv.appendChild(button)\n"
                + "newDiv.appendChild(document.createElement(\"br\"))\n"
                + "myDiv.appendChild(newDiv);\n"
                + "}\n"
                + "function appendAnswer(divID, numAnswer)\n"
                + "{\n"
                + "numAnswer+=1;"
                + "var updateButton=document.getElementsByName(\"appendAnswer\"+divID)[0];\n"
                + "updateButton.removeAttribute(\"onclick\");\n"
                + "updateButton.setAttribute(\"onclick\", \"appendAnswer(\"+divID+\", \"+numAnswer+\")\");\n"
                + "var myDiv = document.getElementById(\"q\"+divID);\n"
                + "var newA = document.createElement(\"input\");\n"
                + "newA.setAttribute(\"name\", \"a\"+numQuestion+\"_\"+numAnswer);\n"
                + "newA.setAttribute(\"type\", \"text\");\n"
                + "newA.setAttribute(\"maxlength\", \"500\");\n"
                + "newA.setAttribute(\"size\", \"50\");\n"
                + "var cb=document.createElement(\"input\");\n"
                + "cb.setAttribute(\"type\", \"checkbox\");\n"
                + "cb.setAttribute(\"name\", \"correctAnswer\"+numQuestion+\"_\"+numAnswer);\n"
                + "cb.setAttribute(\"value\", \"correct\");\n"
                + "myDiv.appendChild(newA)\n"
                + "myDiv.appendChild(cb)\n"
                + "myDiv.appendChild(document.createTextNode(\"Correct Answer\"));\n"
                + "myDiv.appendChild(document.createElement(\"br\"))\n"
                + "}\n"
                + "</script>\n"
                + "<body onload=\"init()\">\n"
                + "\n"
                + "  <ul id=\"menu\"> \n"
                + "    <li><a href='AdminServlet?param=1'>Manage Course</a></li>\n"
                + "    <li><a href='AdminServlet?param=3'>Personal info</a></li>\n"
                + "    <li><a href='AdminServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "                  <div id=\"main\">\n\n"
                + "  <form name=\"newTest\" action=\"AdminServlet\" method= \"get\">\n"
                + "      <label>Name of the test:</label><input type=\"text\" name=\"testName\" maxlength=\"45\" size=\"15\"><br>\n"
                + "      <label>Start time for the test:</label><input type=\"time\" name=\"startTime\" maxlength=\"\" size=\"4\"><br>\n" // *** time è un testo normale!
                + "      <label>End time for the test:</label><input type=\"time\" name=\"finishTime\" maxlength=\"\" size=\"4\"><br>\n" // *** time è un testo normale!
                + "      <label>Total time to complete the test (minutes):</label><input type=\"text\" name=\"duration\" maxlength=\"10\" size=\"3\"><br>\n"
                + "  <div id=\"questions\">\n"
                + "     <div id=\"q1\">\n"
                + "     <input type=\"radio\" name=\"openchoice1\" value=\"open\">Open question\n"
                + "     <input type=\"radio\" name=\"openchoice1\" value=\"choice\" checked>Single/multiple choice<br>\n"
                + "     Point: <input type=\"text\" name=\"point1\" maxlength=\"10\" size=\"4\"><br>\n"
                + "     Question: <br><textarea name=\"text1\" maxlength=\"100\" rows=\"5\" cols=\"50\"></textarea>\n"
                + "     <input type=\"button\" name=\"appendAnswer1\" value=\"Add answer\" onclick=\"appendAnswer(1, 0)\"><br>\n"
                + "     </div>\n"
                + "     </div>\n"
                + "  </div>\n");
        out.println(
                "\n"
                //+ "  <form name=\"newTest\" action=\"AdminServlet\" method= \"get\">\n"
                + "      <input type=\"hidden\" name=\"param\" value=\"9\">"
                //+ "      <input type=\"hidden\" name=\"param\" value=\"9\">"
                + "      <input type=\"hidden\" name=\"idcourse\" value=\"" + idcourse + "\">"
                + "  	<input type=\"submit\" name=\"savetest\" value=\"Savee test\">\n" // *** Devo inviare tutti i campi del form
                //+ "  	<input type=\"button\" name=\"createst\" value=\"Create test\" onclick=\"#\">\n"
                + "  </form>\n"
                + "  	<input type=\"button\" name=\"appendQuestion\" value=\"Add question\" onclick=\"appendQuestion()\">\n"
                + "\n"
                + "</body>\n"
                + "</html>");
    }

    protected void saveNewTest(HttpServletRequest request, PrintWriter out, Statement stmt) throws SQLException {
        final int MAXQUESTIONPERTEST = 50;
        final int MAXPOINTPERQUESTION = 10;
        final int MAXANSWERS = 10;
        //save test to DB

        int numForm = Integer.parseInt(request.getParameter("param"));
        if (numForm > MAXQUESTIONPERTEST) {
            numForm = MAXQUESTIONPERTEST;
        }

        double points;
        int numAnswers;
        int IDQuestion = 0, IDTest = 0;
        ResultSet newID;
        StringBuilder answers;

        stmt.executeUpdate("INSERT INTO `mydb`.`Test` (`name`, `idCourse`, `dateStart`, `dateFinish`, `duration`) VALUES ('" + request.getParameter("testName") + "', '" + request.getParameter("idcourse") + "', '" + request.getParameter("startTime") + "', '" + request.getParameter("finishTime") + "', '" + request.getParameter("duration") + "');", Statement.RETURN_GENERATED_KEYS);
        //get ID of the new added test
        newID = stmt.getGeneratedKeys();
        if (newID.next()) {
            IDTest = newID.getInt(1);
        } else {
            ; // exception!!! ***
        }
        int type = 0, correctAnswer, totalCorrectAnswre, i = 1;
        //for (int i=1;i<=numForm;i++) {
        while (request.getParameter("point" + i) != null) {
            points = Double.parseDouble(request.getParameter("point" + i));
            if (points > MAXPOINTPERQUESTION) {
                points = MAXPOINTPERQUESTION;
            }

            //update question
            if (request.getParameter("openchoice" + i).compareTo("open") == 0) {
                type = 3;
            } else {
                type = 1;
            }
            stmt.executeUpdate("INSERT INTO `mydb`.`Question` (`idTest`, `question`, `type`, `point`) VALUES ('" + IDTest + "', '" + request.getParameter("text" + i) + "', '" + type + "', '" + points + "');", Statement.RETURN_GENERATED_KEYS);
            //get ID of the new added question
            newID = stmt.getGeneratedKeys();
            if (newID.next()) {
                IDQuestion = newID.getInt(1);
            } else {
                ; // exception!!! ***
            }

            answers = new StringBuilder("INSERT INTO `mydb`.`Answer` (`idQuestion`, `answer`, `correct`) VALUES ");
            int j;
            totalCorrectAnswre = 0;
            if (type == 3) {
                answers.append("('" + IDQuestion + "', '', '1');"); // request.getParameter("a"+i+"_1")
            } else {
                j = 1;
                while (request.getParameter("a" + i + "_" + j) != null) {
                    if (j > MAXANSWERS) {
                        break;
                    }
                    correctAnswer = (request.getParameter("correctAnswer" + i + "_" + j) != null) ? 1 : 0;
                    totalCorrectAnswre += correctAnswer;
                    answers.append("('" + IDQuestion + "', '" + request.getParameter("a" + i + "_" + j) + "', '" + correctAnswer + "'),");
                    j++;
                }
                answers.setCharAt(answers.length() - 1, ';');
            }
            stmt.executeUpdate(answers.toString());

            // update question type if needed
            if (totalCorrectAnswre > 1) // multiple choice question
            {
                stmt.executeUpdate("update Question set type=2 where idQuestion=" + IDQuestion);
            }

            //all ok, message and redirect to course
            out.println("<script type=\"text/javascript\"> window.alert(\"New test saved.\");window.location.replace(\"AdminServlet?param=4&idcourse=" + request.getParameter("idcourse") + "\");</script>");
            i++;
        }
    }

    protected void invalidAccess(PrintWriter out) {
        idTeacher = "-1";
        idAdmin = "-1";

        out.println("<!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title></title><meta http-equiv='refresh' content='3; url=http://localhost:8080/ProTeach/' />");
        out.println("</head>");
        out.println("<body><div id=\"main\">");

        out.println("invalid access,  you will be redirect in the home in 3 seconds");
        out.println("</div></body>");
        out.println("</html>");
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
        //processRequest(request, response);
        Connection conn = null;
        Statement stmt = null;

        //  Database credentials
        String USER = "root";
        String PASS = "password";

        // JDBC driver name and database URL
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/mydb";
        if (idTeacher.equals("-1") && idAdmin.equals("-1")) {// invalid access (non ti sei loggato)
            System.out.println("access withou login");
            //***** I HAVE TO PUT THE USER IN THE LOGIN PAGE
            PrintWriter out = response.getWriter();
            invalidAccess(out);
            //***** I HAVE TO PUT THE
        } else {
            try {
                // Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                // Open a connection                    
                conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String sql;
                try (PrintWriter out = response.getWriter()) {
                    if (!idAdmin.equals("-1")) { //ADMIN
                        switch (request.getParameter("param")) {
                            case "1"://Course
                                manage_user(out, stmt);
                                break;
                            case "2"://Course
                                manageCourse(stmt, out);
                                break;
                            case "3"://personalinfo
                                personalinfo(stmt, out);
                                break;
                            case "5"://add teacher
                                addTeacher(stmt, out);
                                break;
                            case "6"://add course
                                addCourse(stmt, out);
                                break;
                            case "10"://logout
                                idAdmin = "-1";
                                response.sendRedirect("/ProTeach");

                                break;
                            default:
                                break;

                        }
                    } else //Teacher
                    {
                        System.out.println("teacher");
                        if (!idTeacher.equals("-1")) {
                            System.out.println(request.getParameter("param"));
                            switch (request.getParameter("param")) {
                                case "1"://Course
                                    course_teacher(out, stmt);
                                    break;
                                case "3"://personalinfo
                                    personalinfo(stmt, out);
                                    break;
                                case "4":
                                    //course info
                                    teacher_choose_course(out, stmt, request.getParameter("idcourse"));
                                    break;
                                case "5":
                                    //confirm student
                                    confirm_student(stmt, out, request.getParameter("idCourse"), request.getParameter("idUser"));
                                    break;
                                case "6"://create test
                                    createTest(out, stmt, request.getParameter("idcourse"));
                                    break;
                                case "7"://showTestAnswer
                                    showTestAnswer(out, stmt, request.getParameter("idSHT"));
                                    break;
                                case "8"://saveEvaluatedAnswer
                                    saveEvaluatedAnswer(request, out, stmt);
                                    break;
                                case "9"://save new test
                                    saveNewTest(request, out, stmt);
                                    break;
                                case "10"://logout
                                    idTeacher = "-1";
                                    response.sendRedirect("/ProTeach");

                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        //  processRequest(request, response);

        try (PrintWriter out = response.getWriter()) {

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs;
            String sql;
            //  Database credentials
            String USER = "root";
            String PASS = "password";

            // JDBC driver name and database URL
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            String DB_URL = "jdbc:mysql://localhost:3306/mydb";

            try {
                // Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                System.out.println("1");
                // Open a connection
                conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                switch (request.getParameter("objhidden")) {
                    case "1":
                        //after form add teacher
                        sql = "INSERT INTO `mydb`.`User`(`name`, `surname`, `email`,`address`, `type`,  `password`) VALUES ('" + request.getParameter("name") + "', '" + request.getParameter("surname") + "', '" + request.getParameter("email") + "', '" + request.getParameter("address") + "', " + 2 + ",'" + request.getParameter("password") + "')";
                        stmt.executeUpdate(sql);
                        sql = "SELECT  * FROM User WHERE email='" + request.getParameter("email") + "'";
                        rs = stmt.executeQuery(sql);
                        if (!rs.next()) {
                            System.out.println("empty");
                            return;
                        }
                        sql = " INSERT INTO `mydb`.`Teacher`(`idTeacher`,`salary`, `position`)VALUES ('" + rs.getString("idUser") + "', '" + request.getParameter("salary") + "', '" + request.getParameter("position") + "')";
                        stmt.executeUpdate(sql);
                        out.println("<script type=\"text/javascript\"> window.alert(\"You have added the teacher\");window.location.replace(\"AdminServlet?param=1\");</script>");
                        break;
                    case "2":
                        // add course
                        sql = "INSERT INTO `mydb`.`Course` (`idTeacher`, `name`,`semester`,`ECTS`) VALUES('" + request.getParameter("teacherid") + "', '" + request.getParameter("courseName") + "', '" + request.getParameter("semester") + "', '" + request.getParameter("ECTS") + "')";
                        stmt.executeUpdate(sql);
                        out.println("<script type=\"text/javascript\"> window.alert(\"You have added the Course\");window.location.replace(\"AdminServlet?param=1\");</script>");
                        break;
                    case "4"://save test
                        saveNewTest(request, out, stmt);
                        break;
                    case "3":
                        sql = "UPDATE mydb.User SET User.name='" + request.getParameter("name") + "' , User.surname='" + request.getParameter("surname") + "' , User.email='" + request.getParameter("email") + "' , User.password='" + request.getParameter("password") + "' , User.address='" + request.getParameter("address") + "' , User.surname='" + request.getParameter("surname") + "' WHERE User.idUser='" + idAdmin + "'";
                        stmt.executeUpdate(sql);
                        out.println("<script type=\"text/javascript\"> window.alert(\"The system have update your data\");window.location.replace(\"AdminServlet?param=3\");</script>");
                        break;
                    default:
                        //0
                        sql = "SELECT  * FROM User WHERE idUser='" + request.getParameter("id") + "' AND password='" + request.getParameter("password") + "'";
                        rs = stmt.executeQuery(sql);
                        if (!rs.next()) {
                            invalidAccess(out); //redirect in a new page

                        } else {

                            if (request.getParameter("type").equals("1")) //ADMIN
                            {

                                idAdmin = request.getParameter("id");
                                manage_user(out, stmt);
                            } else //Assignement values
                            {

                                //TEACHER
                                idTeacher = request.getParameter("id");
                                course_teacher(out, stmt);
                            }

                            // Clean-up environment
                            rs.close();
                            stmt.close();
                            conn.close();

                        }
                        break;
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

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
