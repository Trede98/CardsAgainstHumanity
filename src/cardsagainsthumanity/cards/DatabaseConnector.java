package cardsagainsthumanity.cards;

import java.sql.*;

/**
 * Created by Giovanni on 07/05/16.
 */
public class DatabaseConnector {

    private Connection connection;
	private ResultSet tableList;
    private Statement statement;

    public DatabaseConnector(String path, String username, String password) {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path, username, password);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            tableList = connection.getMetaData().getTables(null, null, "%", null);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
	
	public ResultSet executeQuery(String sql) throws SQLException {
            return statement.executeQuery(sql);
    }

    public void noReturnQuery(String sql) throws SQLException {
        statement.execute(sql);
    }

    public void close() throws SQLException {
        connection.close();
    }
	
}
