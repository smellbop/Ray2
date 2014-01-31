package org.jboss.as.quickstarts.helloworld;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JdbcStuff {
	
	private static Connection ccmdbConnection = null; 
	
	static String theQuery = "SELECT  DISTINCT siteid,"
        +" (SELECT  count(*) FROM    ticket WHERE   internalpriority = 1 AND     status in ('INPROG','PENDING','NEW','QUEUED') and class = 'SR' and     siteid = t.siteid) p1,"
        +" (SELECT  count(*) FROM    ticket WHERE   internalpriority = 2 AND     status in ('INPROG','PENDING','NEW','QUEUED') and     class = 'SR' and     siteid = t.siteid) p2,"
        +" (SELECT  count(*) FROM    ticket WHERE   internalpriority = 3 AND     status in ('INPROG','PENDING','NEW','QUEUED') and     class = 'SR' and     siteid = t.siteid) p3,"
        +" (SELECT  count(*) FROM    ticket WHERE   internalpriority = 4 AND     status in ('INPROG','PENDING','NEW','QUEUED')  and     class = 'SR' and     siteid = t.siteid) p4,"
        +" (SELECT  count(*) FROM    ticket WHERE   internalpriority = 5 AND     status in ('INPROG','PENDING','NEW','QUEUED') and     class = 'SR' and     siteid = t.siteid ) p5,"
        +" (SELECT  count(*) FROM    ticket WHERE   internalpriority = 6 AND     status in ('INPROG','PENDING','NEW','QUEUED')  and     class = 'SR' and     siteid = t.siteid) p6"
        +" FROM    ticket t WHERE   t.siteid in (select siteid from ticket where siteid = t.siteid and status in ('INPROG','PENDING','NEW','QUEUED')  and     class = 'SR' and siteid <> 'DLNG')"
        ;
	
	private static Connection getCcmdbConnection() throws SQLException {
		if (!driverCheck()){
			
		}
		
		// if the connection is null or closed then ...
		if (JdbcStuff.ccmdbConnection == null || JdbcStuff.ccmdbConnection.isClosed()) {
			
			// open a new connection
			try {
				JdbcStuff.ccmdbConnection = DriverManager.getConnection("jdbc:db2://i7ccmdb.bpdconsulting.co.uk:50005/CCMDB","maximo","XXX");
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} 
		}
		
		// return the handle to the connection
		return JdbcStuff.ccmdbConnection;
	}

	private static Boolean driverCheck(){
			try{
				Class.forName("com.ibm.db2.jcc.DB2Driver");
				return true;
			} catch (ClassNotFoundException e) {
				System.out.println("Please include Classpath  Where your DB2 Driver is located");
				 e.printStackTrace();
				 return false;
			}
	}
	
	static ResultSet  getResults(String statement) {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			
			}
			
		
			 catch (ClassNotFoundException e) {
			 System.out.println("Please include Classpath  Where your DB2 Driver is located");
			 e.printStackTrace();
			 return null;
			 }
			System.out.println("DB2 driver is loaded successfully");
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rset=null;
			boolean found=false;
			try {
			conn = DriverManager.getConnection("jdbc:db2://i7ccmdb.bpdconsulting.co.uk:50005/CCMDB","maximo","XXX");
			    if (conn != null)
			{
			   System.out.println("DB2 Database Connected");
			}
			   else
			{
			      System.out.println("Db2 connection Failed ");
			 }
			 pstmt=conn.prepareStatement(statement);
			rset=pstmt.executeQuery();
			  if(rset!=null)
			{

			 while(rset.next())
			{
			 found=true;
			 return rset;
			}
			}
			if (found ==false)
			{
			System.out.println("No Information Found");
			}
			} catch (SQLException e) {
			System.out.println("DB2 Database connection Failed");
			e.printStackTrace();
			return null;
			}
			
			return null;
	}
	
	static String getTable() throws SQLException{
		StringBuffer sb = new StringBuffer();
		
		ResultSet rset = getResults(theQuery);
		ResultSetMetaData md = rset.getMetaData();
		int count = md.getColumnCount();
		
		sb.append("<table border=1><tr>");
		for (int i=1; i<=count; i++){
			sb.append("<th>");
			sb.append(md.getColumnLabel(i));
			sb.append("</th>");
		}
		sb.append("</tr>");
		while (rset.next()){
			sb.append("<tr>");
			for (int i=1; i<=count; i++){
				sb.append("<td>");
				sb.append(rset.getString(i));
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
		
	}
	
}
