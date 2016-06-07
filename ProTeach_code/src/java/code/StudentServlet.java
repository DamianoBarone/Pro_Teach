/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
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
@WebServlet(name = "StudentServlet", urlPatterns = {"/StudentServlet"})
public class StudentServlet extends HttpServlet {

    String studentId = "-1";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void manageCourse(Statement stmt, PrintWriter out) throws SQLException {
        String sql = "SELECT Course.idCourse, User.name,surname,Course.idTeacher,Course.name,semester,ECTS FROM (mydb.Teacher INNER JOIN mydb.Course ON Teacher.idTeacher=Course.idTeacher) INNER JOIN  User ON Teacher.idTeacher=idUser";
        ResultSet rs = stmt.executeQuery(sql);
        out.println(
                "<!DOCTYPE html> <html> <head><link href=\"style.css\" rel=\"stylesheet\">"
                + " <title>Course</title>"
                + "</head>"
                + "<body>"
                + "+<ul id=\"menu\"> \n"
                + "    <li><a href='StudentServlet?param=1'>Manage Career</a></li>\n"
                + "    <li><a href='StudentServlet?param=3'>Course</a></li>\n"
                + "    <li><a href='StudentServlet?param=2'>Personal info</a></li>\n"
                + "    <li><a href='StudentServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "  <div id=\"main\">\n"
                + "  <h1>ProTeach</h1>\n"
                + "  <h2>Available courses</h2>\n"
                + "  <form>\n"
                + "    <input type=\"search\" name=\"searchCourse\" >\n"
                + "    <input type=\"button\" name=\"searchButton\" value=\"Search\">\n"
                + "  </form>\n"
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
                    + "    <td><a href='StudentServlet?param=5&idcourse=" + rs.getString("idCourse") + "'>" + rs.getString("Course.name") + "</a></td>\n"
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

    protected void enrollcourse(Statement stmt, PrintWriter out, String idcourse) throws SQLException {
        String sql = "INSERT INTO `mydb`.`Student_has_Course` (`idCourse`, `idStudent`, `idCareer`,`status`) VALUES ('" + idcourse + "','" + studentId + "',1,0)";//CARRER PER ADESSO SEMPRE A 1 ******
        System.out.println(sql);
        stmt.executeUpdate(sql);
        out.println("<script type=\"text/javascript\"> window.alert(\"You have to wait the confirmation of the Teacher\");window.location.replace(\"StudentServlet?param=3\");</script>");

    }

    protected void showCourse(Statement stmt, PrintWriter out, String idcourse) throws SQLException {
        String sql = "SELECT Course.idCourse, User.name,surname,Course.idTeacher,Course.name,semester,ECTS FROM (mydb.Teacher INNER JOIN mydb.Course ON Teacher.idTeacher=Course.idTeacher) INNER JOIN  User ON Teacher.idTeacher=idUser where idCourse='" + idcourse + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "	<title>Course info</title>\n"
                + "	<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "</head>\n"
                + "<body>\n"
                + "<ul id=\"menu\"> \n"
                + "    <li><a href='StudentServlet?param=1'>Manage Career</a></li>\n"
                + "    <li><a href='StudentServlet?param=3'>Course</a></li>\n"
                + "    <li><a href='StudentServlet?param=2'>Personal info</a></li>\n"
                + "    <li><a href='StudentServlet?param=10'>Log out</a></li>\n"
                + "</ul> \n"
                + " <div id=\"main\">\n"
                + " 	<h1>ProTeach</h1>\n"
                + "  	<h2 id=\"course\">Course name : " + rs.getString("Course.name") + " </h2>\n"
                + "    <strong>Teacher</strong>\n"
                + "    <p id= \"teacher\"> " + rs.getString("User.surname") + "  " + rs.getString("User.name") + " </p><br>\n"
                + "    <strong>Info</strong>\n"
                + "     <p id=\"info\">Semester  " + rs.getString("semester") + " </p><br>\n"
                + "    <strong>Number of students</strong>\n");
        sql = "select count(*) from Student_has_Course where idCourse='" + idcourse + "' and status=1";
        rs = stmt.executeQuery(sql);

        rs.next();
        out.println("     <p id=\"numberStudent\">" + rs.getString("count(*)") + " </p><br>\n");
        sql = "select * from Student_has_Course where idStudent='" + studentId + "' and idCourse='" + idcourse + "'";
        rs = stmt.executeQuery(sql);
        if (!rs.next()) {

            out.println("<button onclick=\"mex()\">enroll</button>");
            out.println("<script type=\"text/javascript\"> function mex(){if (confirm('Are you sure you want enroll this course?')) {\n"
                    + "    window.location.replace(\"StudentServlet?param=6&idcourse=" + idcourse + "\");"
                    + "} else {\n"
                    + "    // Do nothing!\n"
                    + "};}</script>");
        } else {//waiting
            sql = "select * from Student_has_Course where idStudent='" + studentId + "' and idCourse='" + idcourse + "' and status=1";
            rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                out.println("You have to wait that the Teacher accept you");
            } else {

                // CORRECT TABLE FOR SEE TEST AVAILABLE IN A COURSE 
                System.out.println("function showcours non vuoto");

                out.println("    <h2>Available test</h2>\n"
                        + "    <table id=\"Available test\">\n"
                        + "      <tr>\n"
                        + "	    <th>Name</th>\n"
                        + "	    <th>Data Start</th>\n"
                        + "	    <th>Deadline</th>\n"
                        + "	    <th>Duration</th>\n"
                        + "      </tr>\n");
                sql = "select * from Test where idCourse='" + idcourse + "'  and now() between dateStart and dateFinish and idTest not in (select idTest from student_has_test where idStudent='" + studentId + "')";
                System.out.println(sql);

                rs = stmt.executeQuery(sql);
                if (!rs.next()) {
                    out.println("NO TEST AVAILABLE");
                }
                rs = stmt.executeQuery(sql);
                out.println("\n<script>\n"
                        + "	function perform(idTest){\n"
                        + "		window.location.replace(\"StudentServlet?param=7&idcourse=" + idcourse + "&idTest=\"+idTest+\"&idStudent=" + studentId + "\");\n"
                        + "	}\n"
                        + "</script>\n");
                while (rs.next()) {
                    out.println("<a href=\"StudentServlet?param=7&idcourse=" + idcourse + "&idTest=" + rs.getString("idTest") + "\">"
                            + "<tr>\n"
                            + " <th> <a href=\"StudentServlet?param=7&idcourse=" + idcourse + "&idTest=" + rs.getString("idTest") + "\">" + rs.getString("name") + "</a></th>\n"
                            + "<th>" + rs.getString("dateStart") + "</th>\n"
                            + "<th>" + rs.getString("dateFinish") + "</th>\n"
                            + "<th>" + rs.getString("duration") + "</th>\n"
                            + "</tr>\n");
                }
            }
        }
        {
            out.println("    </table>\n"
                    + "  </div>\n"
                    + "\n"
                    + "</body>\n"
                    + "</html>");
        }

    }

    protected void performTest(Statement stmt, PrintWriter out, String idcourse, String idTest) throws SQLException {
        stmt.executeUpdate("INSERT INTO `mydb`.`Student_has_Test` (`idStudent`, `idTest`) VALUES ('" + studentId + "', '" + idTest + "');", Statement.RETURN_GENERATED_KEYS);
        int StudentHasTestID = 0;
        ResultSet newStudentHasTestID = stmt.getGeneratedKeys();
        if (newStudentHasTestID.next()) {
            StudentHasTestID = newStudentHasTestID.getInt(1);
        } else {
            ; // exception!!! ***
        }

        // display course name
        String q1 = "SELECT * FROM question WHERE idTest='" + idTest + "'", q2;
        System.out.println("q1 -> " + q1);
        ResultSet rs = stmt.executeQuery(q1);

        Hashtable<String, String> el = null;
        Vector<Hashtable<String, String>> questions = new Vector<Hashtable<String, String>>();
        //int x=0;
        while (rs.next()) {
            el = new Hashtable<String, String>();
            el.put("point", rs.getString("point"));
            el.put("question", rs.getString("question"));
            el.put("type", rs.getString("type"));
            el.put("idQuestion", rs.getString("idQuestion"));
            questions.add(el);
            //x++;
        }

        //System.out.println("X vale: "+x);
        //System.out.println("questions.size() vale: "+questions.size());
        ResultSet resAns;

        out.println(" <!DOCTYPE html>\n"
                + "\n"
                + "<html>\n"
                + "  <head>\n"
                + "<link href=\"style.css\" rel=\"stylesheet\">\n"
                + "    <title>Perform Test</title>"
                + " </head>\n"
                + "<body onload=\"init()\">\n"
                + "\n"
                + "  <ul id=\"menu\"> \n"
                + "    <li><a href='StudentServlet?param=1'>Manage Career</a></li>\n"
                + "    <li><a href='StudentServlet?param=3'>Course</a></li>\n"
                + "    <li><a href='StudentServlet?param=2'>Personal info</a></li>\n"
                + "    <li><a href='StudentServlet?param=10'>Log out</a></li>\n"
                + "  </ul> \n"
                + "  \n"
                + "<div id=\"main\">\n"
                + "  <form name=\"Test\" action=\"StudentServlet\" method= \"get\">\n"
                + "  <div id=\"questions\">\n");
        String questionType;
        int i = 1, j = 1;
        while (i <= questions.size()) {
            out.println("<div id=\"q" + i + "\">"
                    + "Question " + i + "<br>Point: " + questions.get(i - 1).get("point") + "<br>\n"
                    + "     <p>" + questions.get(i - 1).get("question") + "</p>\n");
            if (questions.get(i - 1).get("type").compareTo("3") == 0) { // if open question
                out.println("<textarea name=\"r" + i + "\" rows=\"4\" cols=\"50\" maxlength=\"500\" required=\"required\" placeholder=\"Write here your answer.\"></textarea><br>\n");
            } else {
                if (questions.get(i - 1).get("type").compareTo("1") == 0) { // single choise
                    questionType = "radio";
                } else {
                    questionType = "checkbox";
                }
                q2 = "SELECT answer FROM answer WHERE idQuestion='" + questions.get(i - 1).get("idQuestion") + "'";
                System.out.println("q2 -> " + q2);

                resAns = stmt.executeQuery(q2);
                j = 1;
                while (resAns.next()) {
                    out.println("<br><input type=\"" + questionType + "\" name=\"r" + i + "_" + j + "\" value=\"" + resAns.getString("answer") + "\">" + resAns.getString("answer") + "<br>\n");
                    if (questions.get(i - 1).get("type").compareTo("2") == 0) { // change name only if multiple choise
                        j++;
                    }
                }
            }
            out.println("</div><br>");
            i++;
        }
        out.println("</div><br>");
        out.println("\n"
                + "     <input type=\"hidden\" name=\"param\" value=\"8\">\n"
                + "     <input type=\"hidden\" name=\"idCourse\" value=\"" + idcourse + "\">\n"
                + "     <input type=\"hidden\" name=\"SHTID\" value=\"" + StudentHasTestID + "\">\n"
                + "     <input type=\"submit\" name=\"savetest\" value=\"Savee test\">\n" // *** Devo inviare tutti i campi del form
                + "  </form>\n"
                + "\n"
                + "</div></body>\n"
                + "</html>");
    }

    protected void invalidAccess(PrintWriter out) {
        studentId = "-1";
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

    protected void saveTestAnswers(HttpServletRequest request, Statement stmt, PrintWriter out, String StudentHasTestID, String idcourse) throws SQLException {
        StringBuilder answersList;
        //save answers to DB
        stmt.executeUpdate("UPDATE mydb.Student_has_Test SET timeSpent=DATEDIFF(NOW(), date) WHERE idStudentHasTest='" + StudentHasTestID + "';");
        ResultSet rs = stmt.executeQuery("select idQuestion, type from question where idTest in (SELECT idTest FROM Student_has_Test WHERE idStudentHasTest='" + StudentHasTestID + "')");

        Hashtable<String, String> el = null;
        Vector<Hashtable<String, String>> questions = new Vector<Hashtable<String, String>>();
        //int x=0;
        while (rs.next()) {
            el = new Hashtable<String, String>();
            el.put("type", rs.getString("type"));
            el.put("idQuestion", rs.getString("idQuestion"));
            questions.add(el);
            //x++;
        }

        boolean onlyOpenQuestion = true;
        int i = 1, j = 1;
        while (i <= questions.size()) {
            answersList = new StringBuilder("INSERT INTO `mydb`.`Student_answer` (`idStudentHasTest`, `idQuestion`, `answer`) VALUES ");
            if (questions.get(i - 1).get("type").compareTo("3") == 0) { // if open question
                onlyOpenQuestion = false;
                answersList.append("('" + StudentHasTestID + "', '" + questions.get(i - 1).get("idQuestion") + "', '" + request.getParameter("r" + i) + "'),");
            } else {
                j = 1;
                while (request.getParameter("r" + i + "_" + j) != null) { // untill we have answer, store them
                    answersList.append("('" + StudentHasTestID + "', '" + questions.get(i - 1).get("idQuestion") + "', '" + request.getParameter("r" + i + "_" + j) + "'),");
                    j++;
                }
            }
            answersList.setCharAt(answersList.length() - 1, ';');
            System.out.println("-----------" + answersList.toString() + "-----------");
            stmt.executeUpdate(answersList.toString());
            i++;
        }
        System.out.println("+++++++++++ 1 save test answer salvate! +++++++++++");
        //partial correction
        stmt.executeUpdate("update (student_answer join question on student_answer.idQuestion=question.idQuestion) join answer on student_answer.answer=answer.answer set student_answer.correct=\"1\" where idStudentHasTest=\"" + StudentHasTestID + "\" and type!=\"3\" and question.idQuestion=answer.idQuestion and answer.correct=1");
        stmt.executeUpdate("update (student_answer join question on student_answer.idQuestion=question.idQuestion) join answer on student_answer.answer=answer.answer set student_answer.evaluatedPoints=question.point where idStudentHasTest=\"" + StudentHasTestID + "\" and type!=\"3\" and question.idQuestion=answer.idQuestion and answer.correct=1");

        System.out.println("+++++++++++ 2 save test answer salvate! +++++++++++");
        double totPoints = 0.0, studentPoints = 0.0;
        if (onlyOpenQuestion) {
            rs = stmt.executeQuery("select sum(point) as s from question where idTest in (SELECT idTest FROM student_has_test WHERE idStudentHasTest='" + StudentHasTestID + "')");
            System.out.println("select sum(point) as s from question where idTest in (SELECT idTest FROM student_has_test WHERE idStudentHasTest='" + StudentHasTestID + "')");
            rs.next();
            totPoints = Double.parseDouble(rs.getString("s"));
            System.out.println("+++++++++++ X3 save test answer salvate! +++++++++++");
            rs = stmt.executeQuery("select sum(evaluatedPoints) as p from student_answer where correct=\"1\" and idStudentHasTest='" + StudentHasTestID + "'");
            System.out.println("select sum(evaluatedPoints) as p from student_answer where correct=\"1\" and idStudentHasTest='" + StudentHasTestID + "'");
            rs.next();
            studentPoints = Double.parseDouble(rs.getString("p"));
            System.out.println("+++++++++++ X4 save test answer salvate! +++++++++++");
            long grade = Math.round(studentPoints / totPoints * 5);
            //stmt.executeUpdate("UPDATE student_has_test SET grade=\"" + grade + "\" WHERE idStudentHasTest in (select idStudentHasTest from student_answer where idStudentAnswer=\"" + request.getParameter("idSA" + (i - 1)) + "\");");
            stmt.executeUpdate("UPDATE student_has_test SET grade=\"" + grade + "\" WHERE idStudentHasTest = '" + StudentHasTestID + "'");

            System.out.println("UPDATE student_has_test SET grade=\"" + grade + "\" WHERE idStudentHasTest = '" + StudentHasTestID + "'");
        }

        //all ok, message and redirect to the course related to this test
        out.println("<script type=\"text/javascript\"> window.alert(\"Your answers have been saved.\"); window.location.replace(\"StudentServlet?param=5&idcourse=" + idcourse + "\");</script>");
    }

    protected void manageCareer(Statement stmt, PrintWriter out) throws SQLException {
        String query = "select * from (student_has_course join course on student_has_course.idCourse = course.idCourse ) join user on user.idUser=course.idTeacher where idStudent= " + studentId;
        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <title>Personal information</title>\n"
                + "  <link href=\"style.css\" rel=\"stylesheet\">\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "<ul id=\"menu\"> \n"
                + "    <li><a href='StudentServlet?param=1'>Manage Career</a></li>\n"
                + "    <li><a href='StudentServlet?param=3'>Course</a></li>\n"
                + "    <li><a href='StudentServlet?param=2'>Personal info</a></li>\n"
                + "    <li><a href='StudentServlet?param=10'>Log out</a></li>\n"
                + "</ul> \n"
                + " <div id=\"main\">\n"
                + "  <h1>ProTeach</h1>\n"
                + "  <h2>Enroled course</h2>\n"
                + "  \n"
                + " <table id=\"enroledCourse\">\n"
                + "  <tr>\n"
                + "    <th>Course name</th>\n"
                + "    <th>Teacher</th>\n"
                + "  </tr>\n");
        while (rs.next()) {

            out.println("  <tr>\n"
                    + "    <td>" + rs.getString("course.name") + "</td>\n"
                    + "    <td>" + rs.getString("user.name") + "</td>\n"
                    + "  </tr>\n");
        }
        query = "select * from (student_has_test join test on student_has_test.idTest = Test.idTest) join course on course.idCourse= test.idCourse where idStudent= " + studentId + " and grade != -1";
        System.out.println(query);
        rs = stmt.executeQuery(query);
        out.println(
                "  </table><br>\n"
                + "  <h2>Finished Test</h2>\n"
                + "  <table id=\"FinishedTest\">\n"
                + "    <tr>\n"
                + "      <th>Course name</th>\n"
                + "      <th>Test name</th>\n"
                + "      <th>Grade</th>\n"
                + "    </tr>\n"
                + "    <tr>\n");
        while (rs.next()) {
            out.println(
                    "    <td>" + rs.getString("course.name") + "</td>\n"
                    + "    <td>" + rs.getString("test.name") + "</td>\n"
                    + "    <td>" + rs.getString("grade") + "</td>\n"
                    + "    </tr>\n");
        }
        out.println(
                "  </table>\n"
                + "\n"
                + "</div>\n"
                + "    <script src=\"nav02script.js\"></script>\n"
                + "</body>\n"
                + "</html>");

    }

    protected void personalinfo(Statement stmt, PrintWriter out) throws SQLException {//works
        String query = "SELECT * FROM mydb.User INNER JOIN mydb.Student ON User.idUser=Student.idStudent WHERE  User.idUser= '" + studentId + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
            System.out.println("ERRORE ID");
        }
        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <title>Personal information</title>\n"
                + "  <link href=\"style.css\" rel=\"stylesheet\">\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "<ul id=\"menu\"> \n"
                + "    <li><a href='StudentServlet?param=1'>Manage Career</a></li>\n"
                + "    <li><a href='StudentServlet?param=3'>Course</a></li>\n"
                + "    <li><a href='StudentServlet?param=2'>Personal info</a></li>\n"
                + "    <li><a href='StudentServlet?param=10'>Log out</a></li>\n"
                + "</ul> \n"
                + "<div id=\"main\">\n"
                + "  <h1>ProTeach</h1> \n"
                + "  <h2>Personal information</h2>"
                + "<form method=\"POST\"  id=\"forminfo\" name=\"myForm\" action=\"StudentServlet\">" //FARE IN MODO DI MANDARE ALLA SERVELT PER MODIFICARE I DATE,  FARE LA UPDATE NON SO SE FARE GET OR POST
                + "      Email <input type=\"email\" name=\"email\" value=\"" + rs.getString("email") + "\"><br>\n"
                + "      Name <input type=\"text\" name=\"name\" value=\"" + rs.getString("name") + "\" ><br>\n"
                + "      Surname<input type=\"text\" name=\"surname\" value=\"" + rs.getString("surname") + "\"><br>\n"
                + "      Address<input type=\"text\" name=\"address\" value=\"" + rs.getString("address") + "\"><br>\n"
                + "      Password<input type=\"password\" name=\"password\" value=\"" + rs.getString("password") + "\"><br>\n"
                + "      <input type=\"hidden\" name=\"type\" value=\"3\">"
                + "      <input   type= \"submit\" value=\"Save change\">\n"
                + "    </form>");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;

        //  Database credentials
        String USER = "root";
        String PASS = "password";

        // JDBC driver name and database URL
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/mydb";
        if (studentId.equals("-1")) {// invalid access (you are not logged in)
            System.out.println("access withou login");
            PrintWriter out = response.getWriter();
            invalidAccess(out);
            //***** I HAVE TO PUT THE USER IN THE LOGIN PAGE
        } else {
            try {
                // Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                // Open a connection                    
                conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String sql;
                try (PrintWriter out = response.getWriter()) {
                    switch (request.getParameter("param")) {
                        //manage carrer
                        case "1":
                            manageCareer(stmt, out);
                            break;
                        //personal info
                        case "2":
                            personalinfo(stmt, out);
                            break;
                        case "3":
                            manageCourse(stmt, out);
                            break;
                        case "5": //chouse a course
                            showCourse(stmt, out, request.getParameter("idcourse"));
                            break;
                        case "6": //enroll a course
                            enrollcourse(stmt, out, request.getParameter("idcourse"));
                            break;
                        case "7": //perform a test
                            performTest(stmt, out, request.getParameter("idcourse"), request.getParameter("idTest"));
                            break;
                        case "8": //save answers of a test
                            saveTestAnswers(request, stmt, out, request.getParameter("SHTID"), request.getParameter("idCourse"));
                            break;
                        case "10"://logout
                            studentId = "-1";
                            response.sendRedirect("/ProTeach");

                            break;
                        default:
                            break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(StudentServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        // processRequest(request, response);
        Connection conn = null;
        Statement stmt = null;

        //  Database credentials
        String USER = "root";
        String PASS = "password";

        // JDBC driver name and database URL
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/mydb";

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */

                // Open a connection                    
                conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                ResultSet rs;
                String sql;
                if (request.getParameter("type").equals("1"))//register new student
                {//insert **************************************}
                    sql = "INSERT INTO `mydb`.`User`(`name`, `surname`, `email`,`address`, `type`,  `password`) VALUES ('" + request.getParameter("name") + "', '" + request.getParameter("surname") + "', '" + request.getParameter("email") + "', '" + request.getParameter("address") + "', " + 3 + ",'" + request.getParameter("password") + "')";
                    stmt.executeUpdate(sql);
                    sql = "SELECT  * FROM User WHERE email='" + request.getParameter("email") + "'";
                    rs = stmt.executeQuery(sql);
                    if (!rs.next()) {
                        System.out.println("empty");
                    }
                    sql = " INSERT INTO `mydb`.`Student`(`idStudent`, `numberCareer`,yearAtUniversity)VALUES ('" + rs.getString("idUser") + "', '" + request.getParameter("careers") + "', '" + request.getParameter("year") + "')";
                    stmt.executeUpdate(sql);

                } else if (request.getParameter("type").equals("3")) {//modify info
                    sql = "UPDATE mydb.User SET User.name='" + request.getParameter("name") + "' , User.surname='" + request.getParameter("surname") + "' , User.email='" + request.getParameter("email") + "' , User.password='" + request.getParameter("password") + "' , User.address='" + request.getParameter("address") + "' , User.surname='" + request.getParameter("surname") + "' WHERE User.idUser='" + studentId + "'";
                    stmt.executeUpdate(sql);
                    out.println("<script type=\"text/javascript\"> window.alert(\"The system have update your data\");window.location.replace(\"StudentServlet?param=2\");</script>");
                }

                //else work also without the else
                sql = "SELECT  * FROM User WHERE email='" + request.getParameter("email") + "' AND password='" + request.getParameter("password") + "'";
                rs = stmt.executeQuery(sql);

                if (rs.next() == false) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head> <meta http-equiv='refresh' content='3; url=http://localhost:8080/ProTeach/' >");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("incorrect password, you will be redirect in the home in 3 seconds");
                    out.println("</body>");
                    out.println("</html>");
                } else {
                    String type = rs.getString("type");
                    if (type.equals("1") || type.equals("2")) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<form method=\"POST\"  id=\"myForm\" name=\"myForm\" action=\"AdminServlet\">");
                        out.println("<input name=\"password\"  value=\"" + rs.getString("password") + "\"><input name=\"id\"  value=\"" + rs.getString("idUser") + "\" ><input name=\"type\"  value=\"" + type + "\" > ");
                        out.println("<input   value=\"Login\" type= \"submit\" > <input  name=\"objhidden\" type=\"hidden\" value=\"0\"></form>");
                        out.println("<script type=\"text/javascript\"> window.onload = document.getElementById(\"myForm\").submit(); </script>");

                    }

                    //}
                    //COURSE AVALAIBLE
                    studentId = rs.getString("idUser");
                    //******** personalinfo(rs.getString("idUser"),stmt,out);
                    manageCourse(stmt, out);

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(StudentServlet.class.getName()).log(Level.SEVERE, null, ex);
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
