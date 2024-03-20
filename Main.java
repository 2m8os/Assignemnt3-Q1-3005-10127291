import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args)
    {
        //use your own url, user and password
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        String user = "postgres";
        String password = "4125Ltjt";

        try
        {
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            Connection con = DriverManager.getConnection(url, user, password);
            if (con != null) {
                System.out.println("Connected to PostgreSQL successfully!");
            } else {
                System.out.println("Failed to establish connection.");
            }

            //creating and populating the table
            createTable(con);
            populateDB(con);

            //testing all the functions
            System.out.println("Initial Values:");
            displayAllStudents(con);

            //testing addStudents()
            System.out.println("Adding Newt, Stue, newt.stue@fake.com, 2024-03-19:");
            addStudent(con, "Newt", "Stue", "newt.stue@fake.com", "2024-03-19");
            displayAllStudents(con);
            System.out.println("Done!");

            //testing updateStudentEmail()
            System.out.println("Updated email for student with ID 1 to new@email.com");
            updateStudentEmail(con, 1, "new@email.com");
            displayAllStudents(con);
            System.out.println("Done!");

            //testing deleteStudent()
            System.out.println("Deleting student with ID 2:");
            deleteStudent(con, 2);
            displayAllStudents(con);
            System.out.println("Done!");

            con.close();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //FUNCTIONS

    //takes a connection con
    //Creates the students table if it does not exist
    public static void createTable(Connection con)
    {
        //sql statement
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "student_id SERIAL PRIMARY KEY," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "enrollment_date DATE)";

        try
        {
            Statement stmt = con.createStatement(); // Execute SQL query
            stmt.execute(sql);

            // Close resources
            stmt.close();
        }
        catch(Exception e){System.out.println("createTable," + e);}

    }

    //takes a connection con
    //inserts all initial students into table
    public static void populateDB(Connection con)
    {
        //initial students
        String[] initialStudents = {
                "('John', 'Doe', 'john.doe@example.com', '2023-09-01')",
                "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01')",
                "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02')"
        };

        //sql statement
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES ";

        try
        {
            //execute query
            Statement stmt = con.createStatement();
            for (String students : initialStudents)
            {
                stmt.addBatch(sql + students);
            }
            stmt.executeBatch();

            //Close resources
            stmt.close();
        }
        catch(Exception e){System.out.println("populateDB," + e);}
    }

    //takes a connection con
    //Displays all current students in table
    public static void displayAllStudents(Connection con)
    {
        // the sql statement
        String sql = "SELECT * FROM students";

        try
        {
            Statement stmt = con.createStatement(); // Create statement
            ResultSet rs = stmt.executeQuery(sql); // execute and save query results
            //int test = 1;

            //outputs query results into terminal
            while(rs.next())
            {
                //System.out.println(test);
                //test++;
                System.out.println(rs.getInt("student_id") + ", " +
                        rs.getString("first_name") + " " +
                        rs.getString("last_name") + ", " +
                        rs.getString("email") + ", " +
                        rs.getString("enrollment_date"));
            }

            // Close resources
            rs.close();
            stmt.close();

        }
        catch(Exception e){System.out.println("displayAllStudents," + e);}
    }

    //takes a connection con, and strings for first name, last name, email and enrollement date
    //inserts that student with that information into students table
    public static void addStudent(Connection con, String firstName, String lastName, String email, String enrollmentDate)
    {
        //sql statement for adding students
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (";
        try
        {
            //adding on to the sql statement
            sql = sql + firstName + ", ";
            sql = sql + lastName + ", ";
            sql = sql + email + ", ";
            sql = sql + enrollmentDate + ")";

            //executing query
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);

            //Close resources
            stmt.close();
        }
        catch(Exception e){System.out.println("addStudent," + e);}
    }

    //takes a connection con, int for studentId,  string email
    //Updates the email of the student with that student id in students table
    public static void updateStudentEmail(Connection con, int studentId, String newEmail)
    {
        //sql statement
        String sql = "UPDATE students SET email = ";// ? WHERE student_id = ?";
        try
        {
            //add email and student id
            sql = sql + newEmail + " WHERE student_id = ";
            sql = sql + studentId;

            //execute query
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);

            //Close resources
            stmt.close();
        }
        catch(Exception e){System.out.println("updateStudentEmail," + e);}
    }

    //takes a connection con, and int for student id
    //deletes student with id in student table
    public static void deleteStudent(Connection con, int studentId)
    {
        //start the sql statement
        String sql = "DELETE FROM students WHERE student_id = ";
        try
        {
            //add student id
            sql = sql + studentId;

            //execute query
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);

            //Close resources
            stmt.close();
        }
        catch(Exception e){System.out.println("deleteStudent," + e);}
    }
}

