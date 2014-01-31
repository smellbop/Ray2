package org.jboss.as.quickstarts.helloworld;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A simple CDI service which is able to say hello to someone
 * 
 * @author Pete Muir
 * 
 */
public class HelloService {
	String createHelloMessage() throws SQLException {
		return JdbcStuff.getTable();
	}

}
