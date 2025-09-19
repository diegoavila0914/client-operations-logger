/*
 	Name: Diego Avila
 	Course: CNT 4714 Fall 2024
 	Assignment title: Project 3 - A Specialized Accountant Application
 	Date: November 3rd, 2024
 	
 	Class: TheAccountantAppFall2024
 */


package development;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TheAccountantAppFall2024 extends JPanel {
	
	//Class Variables
		private JButton connectButton, disconnectButton, clearCommandButton, executeButton, clearWindowButton, 
		closeAppButton;
		private JLabel commandLabel, dbInfoLabel, jdbcLabel1, jdbcLabel2, userLabel, passwordLabel;
		private JTextArea textCommand;
		private JComboBox dbUrlPropertiesDropdown, userPropertiesDropdown;
		private JTextField userText;
		private JPasswordField passwordText;
		private JLabel statusLabel, windowLabel;
		private ResultSetTableModel tableModel;
		private Connection connect;
		private TableModel Empty;
		private JTable resultTable;
		private JScrollPane resultScrollPane;
		
		public TheAccountantAppFall2024() 
		{
			setLayout(null);
			setPreferredSize(new Dimension(1000, 600));
			
			//Constructs GUI Interface
			
			// Construct GUI components 
			
			// DB URL Properties Label
	        JLabel dbUrlLabel = new JLabel("DB URL Properties");
	        dbUrlLabel.setFont(new Font("Arial", Font.BOLD, 12));
	        dbUrlLabel.setBounds(20, 0, 200, 25);
	        add(dbUrlLabel);

	        // Operations log Label for DB URL Properties
	        JLabel dbUrlValueLabel = new JLabel("operationslog.properties");
	        dbUrlValueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	        dbUrlValueLabel.setBounds(150, 20, 200, 25);
	        add(dbUrlValueLabel);

	        // User Properties Label
	        JLabel userPropertiesLabel = new JLabel("User Properties");
	        userPropertiesLabel.setFont(new Font("Arial", Font.BOLD, 12));
	        userPropertiesLabel.setBounds(20, 40, 200, 25);
	        add(userPropertiesLabel);
	        
	        // The Accountant Label for User Properties
	        JLabel userPropertiesValueLabel = new JLabel("theaccountant.properties");
	        userPropertiesValueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	        userPropertiesValueLabel.setBounds(150, 60, 200, 25);
	        add(userPropertiesValueLabel);

	        // Username Field
	        JLabel userLabel = new JLabel("Username");
	        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
	        userLabel.setBounds(20, 100, 100, 25);
	        add(userLabel);

	        userText = new JTextField();
	        userText.setBounds(120, 100, 200, 25);
	        add(userText);

	        // Password Field
	        JLabel passwordLabel = new JLabel("Password");
	        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
	        passwordLabel.setBounds(20, 140, 100, 25);
	        add(passwordLabel);

	        passwordText = new JPasswordField();
	        passwordText.setBounds(120, 140, 200, 25);
	        add(passwordText);
			
			// Define Buttons - 6 buttons total
	        
	        // Connect Button
	        connectButton = new JButton("Connect to Database");
	        connectButton.setFont(new Font("Arial", Font.BOLD, 12));
	        connectButton.setBackground(Color.BLUE);
	        connectButton.setForeground(Color.WHITE);
	        connectButton.setBorderPainted(false);
	        connectButton.setOpaque(true);
	        connectButton.setBounds(20, 180, 200, 30);
	        add(connectButton);

	        // Disconnect Button
	        disconnectButton = new JButton("Disconnect from Database");
	        disconnectButton.setFont(new Font("Arial", Font.BOLD, 12));
	        disconnectButton.setBackground(Color.RED);
	        disconnectButton.setForeground(Color.BLACK);
	        disconnectButton.setBorderPainted(false);
	        disconnectButton.setOpaque(true);
	        disconnectButton.setEnabled(false);
	        disconnectButton.setBounds(240, 180, 200, 30);
	        add(disconnectButton);
	        
	        // Command Label
	        commandLabel = new JLabel("Enter An SQL Command");
	        commandLabel.setFont(new Font("Arial", Font.BOLD, 14));
	        commandLabel.setForeground(Color.BLUE);
	        commandLabel.setBounds(450, 20, 200, 25);
	        add(commandLabel);

	        // SQL Command Text Area
	        textCommand = new JTextArea();
	        textCommand.setBounds(450, 50, 500, 100);
	        textCommand.setLineWrap(true);
	        textCommand.setWrapStyleWord(true);
	        add(textCommand);

	        // Clear SQL Command Button
	        clearCommandButton = new JButton("Clear SQL Command");
	        clearCommandButton.setFont(new Font("Arial", Font.BOLD, 12));
	        clearCommandButton.setForeground(Color.BLACK);
	        clearCommandButton.setBackground(Color.YELLOW);
	        clearCommandButton.setBorderPainted(false);
	        clearCommandButton.setOpaque(true);
	        clearCommandButton.setBounds(450, 160, 200, 30);
	        add(clearCommandButton);
	        
	        // Execute SQL Command Button
	        executeButton = new JButton("Execute SQL Command");
	        executeButton.setFont(new Font("Arial", Font.BOLD, 12));
	        executeButton.setForeground(Color.BLACK);
	        executeButton.setBackground(Color.GREEN);
	        executeButton.setBorderPainted(false);
	        executeButton.setOpaque(true);
	        executeButton.setBounds(670, 160, 200, 30);
	        add(executeButton);
	        
	        // Status Label for Connection
	        statusLabel = new JLabel("NO CONNECTION ESTABLISHED", JLabel.CENTER);
	        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
	        statusLabel.setForeground(Color.RED);
	        statusLabel.setBackground(Color.BLACK);
	        statusLabel.setOpaque(true);
	        statusLabel.setBounds(20, 220, 930, 25);
	        add(statusLabel);

	        // SQL Execution Result Window Label
	        windowLabel = new JLabel("SQL Execution Result Window");
	        windowLabel.setFont(new Font("Arial", Font.BOLD, 14));
	        windowLabel.setForeground(Color.BLUE);
	        windowLabel.setBounds(20, 250, 300, 25);
	        add(windowLabel);
	        
	        // Result Table
	        resultTable = new JTable(new DefaultTableModel());
	        resultTable.setShowGrid(true);
	        resultTable.setGridColor(Color.BLACK);
	        resultScrollPane = new JScrollPane(resultTable);
	        resultScrollPane.setBounds(20, 280, 930, 200);
	        add(resultScrollPane);

	        // Clear Result Window Button
	        clearWindowButton = new JButton("Clear Result Window");
	        clearWindowButton.setFont(new Font("Arial", Font.BOLD, 12));
	        clearWindowButton.setForeground(Color.BLACK);
	        clearWindowButton.setBackground(Color.YELLOW);
	        clearWindowButton.setBorderPainted(false);
	        clearWindowButton.setOpaque(true);
	        clearWindowButton.setBounds(20, 500, 200, 30);
	        add(clearWindowButton);

	        // Close Application Button
	        closeAppButton = new JButton("Close Application");
	        closeAppButton.setFont(new Font("Arial", Font.BOLD, 12));
	        closeAppButton.setBackground(Color.RED);
	        closeAppButton.setForeground(Color.BLACK);
	        closeAppButton.setBorderPainted(false);
	        closeAppButton.setOpaque(true);
	        closeAppButton.setBounds(750, 500, 200, 30);
	        add(closeAppButton);
	        
	        // Define Labels 
	        commandLabel = new JLabel();
	        commandLabel.setFont(new Font("Arial", Font.BOLD, 14));
	        commandLabel.setForeground(Color.BLUE);
	        commandLabel.setText("Enter An SQL Command");
			
	        /**************************************************
	          Register Action Listeners and Event Handlers 
	         **************************************************/
	        
	        /**************************************************
	          "Connect" Button
	        **************************************************/
	        
	        // Connect Button Event Handler
	        connectButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	                try {
	                    // if already connected, close previous connection
	                    if (connect != null) {
	                        connect.close();
	                    }
	                    // display status message when not connected
	                    statusLabel.setText("No Connection Now");
	                    	                	               
	                    // Load the properties files
	                    Properties dbProperties = new Properties();
	                    Properties userProperties = new Properties();

	                    try (FileInputStream dbInput = new FileInputStream(new File("operationslog.properties"));
	                            FileInputStream userInput = new FileInputStream(new File("theaccountant.properties"))){
	                    	dbProperties.load(dbInput);
	                    	userProperties.load(userInput);
	                    	
	                    	// Extract database driver, URL, and credentials
	                    	String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
	                    	String dbUrl = dbProperties.getProperty("MYSQL_DB_URL");
	                    	String username = userProperties.getProperty("MYSQL_DB_USERNAME");
	                    	String password = userProperties.getProperty("MYSQL_DB_PASSWORD");
	                    	
	                    	// Load the database driver class 
	                    	Class.forName(dbDriver);
	                    	
	                    	// Match username and password input fields
	                    	String inputUsername = userText.getText();
	                    	String inputPassword = String.valueOf(passwordText.getPassword());

	                       boolean userCredentialsOK = username.equals(inputUsername) && password.equals(inputPassword);

	                        if (userCredentialsOK) {
	                            // set DataSource parameter values
	                            // get connection
	                        	connect = DriverManager.getConnection(dbUrl, username, password);
	                            // update connection status
	                            statusLabel.setText("CONNECTED TO: " + dbUrl);
	                            statusLabel.setForeground(Color.YELLOW);
	                            statusLabel.setBackground(Color.BLACK);
	                            statusLabel.setOpaque(true);
	                            disconnectButton.setEnabled(true);
	                            
	                            // Instantiate tableModel with the established connection
	                            tableModel = new ResultSetTableModel(connect);
	                        } else {
	                            // indicate no connection
	                            statusLabel.setText("No Connection - Credentials Mismatch");
	                            statusLabel.setForeground(Color.RED);
	                        }

	                    } catch (SQLException e) {
	                        JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
	                    }
	                    catch (IOException e) {
	                        JOptionPane.showMessageDialog(null, e.getMessage(), "Properties File error", JOptionPane.ERROR_MESSAGE);
	                    }
	                    catch (ClassNotFoundException e) {
	                    	JOptionPane.showMessageDialog(null, e.getMessage(), "Driver Class Not Found", JOptionPane.ERROR_MESSAGE);
	                    }

	                }  catch (SQLException e) {
	                    JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });
	        
	        /**************************************************
	           "Disconnect from DB" Button
	         **************************************************/
	        disconnectButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent event) {        		
	        		try {
	        			// if a connection exists, close it
	        			if(connect != null && !connect.isClosed()) {
	        				connect.close();
	        				connect = null;
	        				//Update connection status 
	        				statusLabel.setText("No Connection Now");
	        				statusLabel.setForeground(Color.RED);
	        				
	        				// enable the connect button
	        				connectButton.setEnabled(true);
	        				
	        				// Clear the results displayed in the window
	        				resultTable.setModel(new DefaultTableModel());
	                		// Clear the input command area
	        				textCommand.setText("");
	        				
	        			}
	        			
	        		} //end try
	        		
	        		catch(SQLException e) {
	        			JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
	        		} //end catch
	        		
	        	}
	        	
	        }
	        );
	        
	        /**************************************************
	        "Clear Query" Button
	        **************************************************/
	        clearCommandButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent event) {
	        		// Clear the text displayed in the query window
	        		textCommand.setText("");
	        		
	        	}
	        	
	        }
	        		
	        );
	       
	        /**************************************************
	        "Execute" Button
	         **************************************************/
	        executeButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent event) {
	        		try {
	        			// activate result table
	        			resultScrollPane.setVisible(true);
	        			// set scrolling
	        			resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        			resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        			// create TableModel object for results
	        			
	        			// If select statement is used (a query), use executeQuery() from the ResultSetTableModel class
	        			// All other command types will use executeUpdate() from the ResultSetTableModel class
	        			// New Window will pop up with message for user that does not have permission to issue the command 
	        			
	        			if(textCommand.getText().toUpperCase().contains("SELECT")) {
	        				// helper class executes query statement 
	        				tableModel.setQuery(textCommand.getText());
	        				// convert ResultSet object into JTable object
	        				resultTable.setModel(tableModel);
	        				
	        			}
	        			else {
	        				// helper class executes update statement 
	        				int updateCount = tableModel.setUpdate(textCommand.getText());
	        				// if return value is 0, then update was successful, oops otherwise
	        				if(updateCount >= 0) {
	        					JOptionPane.showMessageDialog(null, "Update executed successfully", "Update Success", JOptionPane.INFORMATION_MESSAGE);
	        				}
	        				else {
	        					JOptionPane.showMessageDialog(null, "Error executing update", "Update Failed", JOptionPane.ERROR_MESSAGE);
	        				}
	        				
	        			}
	        			
	        		}
	        		
	        		// catch database error
	        		catch(SQLException e){
	        			JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
	        			
	        		}	        

	        	}
	   
	        }       		
	        		
	        );
	       
	        /**************************************************
	        "Clear Result Window" Button
	      **************************************************/
	        clearWindowButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent event) {
	        		// Clears the result displayed in the window
	                resultTable.setModel(new DefaultTableModel());
	        	}
	        }
	        
	        );
	       
	        /**************************************************
	        "Close Application" Button
	      **************************************************/
	        closeAppButton.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent event) {
	    		 // Shuts down the application
	             System.exit(0);
	    		 
	    	 }
	    	    
	       }
	       );     
	        
		} // end constructor method
		
	public static void main(String[] args) {
		// new JFrame
		JFrame frame = new JFrame("Specialized Accountant Application - Fall 2024");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//create GUI instance 
		frame.getContentPane().add(new TheAccountantAppFall2024());
		frame.pack();
		frame.setLocationRelativeTo(null);
		// show GUI
		frame.setVisible(true); 

	}

}
