/*
 	Name: Diego Avila
 	Course: CNT 4714 Fall 2024
 	Assignment title: Project 3 - A Two-tier Client-Server Application
 	Date: October 27th, 2024
 	
 	Class: ResultSetTableModel
 */

package development;

//A TableModel that supplies ResultSet data to a JTable.
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;
import development.ResultSetTableModel;


//ResultSet rows and columns are counted from 1 and JTable 
//rows and columns are counted from 0. When processing 
//ResultSet rows or columns for use in a JTable, it is 
//necessary to add 1 to the row or column number to manipulate
//the appropriate ResultSet column (i.e., JTable column 0 is 
//ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class ResultSetTableModel extends AbstractTableModel 
{
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	// keep track of database connection status
	private boolean connectedToDatabase = false;
	
	// New constructor that accepts a Connection object
    public ResultSetTableModel(Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        this.connectedToDatabase = true;
    }

	// get class that represents column type
	public Class getColumnClass( int column ) throws IllegalStateException
	{
		// ensure database connection is available
		if ( !connectedToDatabase ) 
			throw new IllegalStateException( "Not Connected to Database" );
		
		// determine Java class of column
		try 
		{
			String className = metaData.getColumnClassName( column + 1 );
      
			// return Class object that represents className
			return Class.forName( className );
		} // end try
		catch ( Exception exception ) 
		{
			exception.printStackTrace();
		} // end catch
   
		return Object.class; // if problems occur above, assume type Object
	} // end method getColumnClass

	// get number of columns in ResultSet
	public int getColumnCount() throws IllegalStateException
	{   
		// ensure database connection is available
		if ( !connectedToDatabase ) 
      throw new IllegalStateException( "Not Connected to Database" );

		// determine number of columns
		try 
		{
			return metaData.getColumnCount(); 
		} // end try
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		} // end catch
   
		return 0; // if problems occur above, return 0 for number of columns
	} // end method getColumnCount

	// get name of a particular column in ResultSet
	public String getColumnName( int column ) throws IllegalStateException
	{    
		// ensure database connection is available
		if ( !connectedToDatabase ) 
		throw new IllegalStateException( "Not Connected to Database" );

		// determine column name
		try 
		{
			return metaData.getColumnName( column + 1 );  
		} // end try
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		} // end catch
   
		return ""; // if problems, return empty string for column name
	} // end method getColumnName

	// return number of rows in ResultSet
	public int getRowCount() throws IllegalStateException
	{      
		// ensure database connection is available
		if ( !connectedToDatabase ) 
      throw new IllegalStateException( "Not Connected to Database" );

		return numberOfRows;
	} // end method getRowCount

	// obtain value in particular row and column
	public Object getValueAt( int row, int column ) 
   throws IllegalStateException
	{
		// ensure database connection is available
		if ( !connectedToDatabase ) 
			throw new IllegalStateException( "Not Connected to Database" );

		// obtain a value at specified ResultSet row and column
		try 
		{
		   resultSet.next();  /* fixes a bug in MySQL/Java with date format */
		   resultSet.absolute( row + 1 );
		   return resultSet.getObject( column + 1 );
		} // end try
		catch ( SQLException sqlException ) 
		{
			sqlException.printStackTrace();
		} // end catch
   
		return ""; // if problems, return empty string object
	} // end method getValueAt

	// set new database query string
	public void setQuery( String query ) 
		throws SQLException, IllegalStateException 
	{
		// ensure database connection is available
		if ( !connectedToDatabase ) 
			throw new IllegalStateException( "Not Connected to Database" );

		// specify query and execute it
		resultSet = statement.executeQuery( query );

		// obtain meta data for ResultSet
		metaData = resultSet.getMetaData();

		// determine number of rows in ResultSet
		resultSet.last();                   // move to last row
		numberOfRows = resultSet.getRow();  // get row number   
		
		// additional code will go here to handle operationslog processing
		// 1. get a connection as a project3app user to the opeationslog db
		// 2. identify user issuing query
		// 3. using the connection from step 1, send the update command
		// 4. close connection to operationslog db
		
		// Additional details of the four steps above
					// Get the metaData for the connection object
					// Extract the username from the connection object - need to know who "owns" the connection
					// NOTE: if it is theaccountant user - ignore this section of the code
				
				// Use the project3app.properties file to get a project3app "user" connection to the operationslog DB
					// Establish a connection to the operationslog DB
				// Use this connection for the updating of the operationscount table
				
				// Create prepareStatement command strings
				// Need one to find the user in the operationscount table
				// 				"select * from operationscount where login_username = ?;";
				// Need one for inserting a new user into the operationscount table
				// Need one for updating the operationscount table to increment number of queries
				
				// Create the PreparedStatement objects - one for each command string above
				// PreparedStatement "name of PreparedStatement object" = connection.prepareStatement(command string);
				
				
				// Find row in operationscount table that belongs to the user issuing the command -
				// If there is currently no row for this user, then this is their first command issued - add a new row
				// 		to the operationscount table for this user with the values: ("login-username", 1, 0).
				// If a row already exists in the operationscount table for this user, then they have issued previous commands -
				// 		increment by 1 the number of query commands issued by this user.
				// Set the username parameter for the PreparedStatement object
				// 		operationscountStatement1.setString(1, user_name);
				
				
				// Run the preparedStatement to see if the user is in the operationscount table
					// If ResultSet is empty on return - then you have a new user and need to enter a new row into operationscount table
					// 		parameter values would be (username, 1 , 0)
				// Else ResultSet contained the username - so user has issued commands before - need to update their row in the operationscount table
					// 		this user has already issued commands and is already represented in the operationscount table 
		
		try(FileInputStream dbInput = new FileInputStream("project3app.properties")){
			Properties dbProperties = new Properties();
			dbProperties.load(dbInput);
			
			String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
			String dbURL = dbProperties.getProperty("MYSQL_DB_URL");
			String dbUsername = dbProperties.getProperty("MYSQL_DB_USERNAME");
			String dbPassword = dbProperties.getProperty("MYSQL_DB_PASSWORD");
			
			// Load the database driver class
			Class.forName(dbDriver);
			
			// Establish a connection to the operationslog DB
			try(Connection logConnection = DriverManager.getConnection(dbURL, dbUsername, dbPassword)){
				// Extract the username from the connection object - need to know who "owns" the connection
				String userName = connection.getMetaData().getUserName();
				
				
				// Ignores if it is the accountant user
				if(!userName.equals("theaccountant@localhost")) {
					// Create prepared statement strings
					String findUserQuery = "SELECT * FROM operationscount WHERE login_username = ?;";
					String insertUserQuery = "INSERT INTO operationscount (login_username, num_queries, num_updates) VALUES (?, 1, 0);";
					String updateUserQuery = "UPDATE operationscount SET num_queries = num_queries + 1 WHERE login_username = ?;";

					// Create the PreparedStatement objects
					PreparedStatement findUserStatement = logConnection.prepareStatement(findUserQuery);
					PreparedStatement insertUserStatement = logConnection.prepareStatement(insertUserQuery);
					PreparedStatement updateUserStatement = logConnection.prepareStatement(updateUserQuery);
					
					// Set the username parameter for the findUserStatement
					findUserStatement.setString(1, userName);

					// Execute the findUserStatement
					ResultSet userResultSet = findUserStatement.executeQuery();

					if (!userResultSet.next()) {
						// User is not in operationscount table, insert a new row
						insertUserStatement.setString(1, userName);
						insertUserStatement.executeUpdate();
					} else {
						// User exists, update the query count
						updateUserStatement.setString(1, userName);
						updateUserStatement.executeUpdate();
					}
					
				}
	
			}
			
		}
	
		catch(IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		// notify JTable that model has changed
		fireTableStructureChanged();
	} // end method setQuery


	//set new database update-query string
	public int setUpdate( String query ) 
   throws SQLException, IllegalStateException 
	{
		int res;
		// int updateCount = statement.executeUpdate(query);
		// ensure database connection is available
		if ( !connectedToDatabase ) 
		throw new IllegalStateException( "Not Connected to Database" );

		// specify query and execute it
		res = statement.executeUpdate( query );
		
	/*
   		// obtain meta data for ResultSet
   		metaData = resultSet.getMetaData();
   		// determine number of rows in ResultSet
   		resultSet.last();                   // move to last row
   		numberOfRows = resultSet.getRow();  // get row number      
	 */    
		
		// additional code will go here to handle operationslog processing 
		// additional code to handle operationslog processing
		try (FileInputStream dbInput = new FileInputStream("project3app.properties")) {
			Properties dbProperties = new Properties();
			dbProperties.load(dbInput);

			String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
			String dbUrl = dbProperties.getProperty("MYSQL_DB_URL");
			String dbUsername = dbProperties.getProperty("MYSQL_DB_USERNAME");
			String dbPassword = dbProperties.getProperty("MYSQL_DB_PASSWORD");

			// Load the database driver class
			Class.forName(dbDriver);

			// Establish a connection to the operationslog DB
			try (Connection logConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
				// Extract the username from the current connection
				String userName = connection.getMetaData().getUserName();

				// Ignore if it is theaccountant user
				if (!userName.equals("theaccountant@localhost")) {
					// Create prepared statement strings
					String findUserQuery = "SELECT * FROM operationscount WHERE login_username = ?;";
					String insertUserQuery = "INSERT INTO operationscount (login_username, num_queries, num_updates) VALUES (?, 0, 1);";
					String updateUserQuery = "UPDATE operationscount SET num_updates = num_updates + 1 WHERE login_username = ?;";

					// Create the PreparedStatement objects
					PreparedStatement findUserStatement = logConnection.prepareStatement(findUserQuery);
					PreparedStatement insertUserStatement = logConnection.prepareStatement(insertUserQuery);
					PreparedStatement updateUserStatement = logConnection.prepareStatement(updateUserQuery);

					// Set the username parameter for the findUserStatement
					findUserStatement.setString(1, userName);
					// Execute the findUserStatement						
					ResultSet userResultSet = findUserStatement.executeQuery();

					if (!userResultSet.next()) {
						// User is not in operationscount table, insert a new row
						insertUserStatement.setString(1, userName);
						insertUserStatement.executeUpdate();
					} else {
						// User exists, update the update count
						updateUserStatement.setString(1, userName);
						updateUserStatement.executeUpdate();
					}
				}
			}
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		
		return res;
		
	} // end method setUpdate

	// close Statement and Connection               
	public void disconnectFromDatabase()            
	{              
		if ( !connectedToDatabase )                  
			return;
		// close Statement and Connection            
		else try                                          
		{                                            
			statement.close();                        
			connection.close();                       
		} // end try                                 
		catch ( SQLException sqlException )          
		{                                            
			sqlException.printStackTrace();           
		} // end catch                               
		finally  // update database connection status
		{                                            
			connectedToDatabase = false;              
		} // end finally                             
	} // end method disconnectFromDatabase          
}  // end class ResultSetTableModel

