import java.io.IOException;
import java.sql.*;

public class TestConnection {
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "root";
    public static final String URL = "jdbc:mysql://localhost:3306/mysql";
    public static Statement statement;
    public static Connection connection;

    static {
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        try {
            statement = connection.createStatement();
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS shop (id int auto_increment primary key, name varchar(30) not null, quantity varchar(30) not null, price varchar(30) not null)");
        statement.executeUpdate("INSERT INTO shop (name, quantity, price) VALUES ('RTX 4060', '5', '299')");

        ResultSet resultSet = statement.executeQuery("SELECT * FROM shop");

        while (resultSet.next()) {
            System.out.println(
                    new StringBuilder()
                            .append(resultSet.getString(1))
                            .append(resultSet.getString(2))
                            .append(resultSet.getString(3))
                            .append(resultSet.getString(4))
            );
        }
    }
}
