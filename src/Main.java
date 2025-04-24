import view.MainView;
import java.sql.Connection;
import java.sql.DriverManager;


public class Main {

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shpoopping", "root", "");
            new MainView(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

