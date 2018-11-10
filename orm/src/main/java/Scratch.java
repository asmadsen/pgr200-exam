

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Scratch {
    public static void main(String[] args) throws SQLException, ParseException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "postgres");
        con.setSchema("conference_server");

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(args[0]);
        System.out.println(date.toString());
    }

}
