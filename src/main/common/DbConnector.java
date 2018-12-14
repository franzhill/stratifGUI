package main.common;

import main.ex.DbConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector
{
  private ModelDb modelDb ;

  /**
   * Connection to the PostgreSQL database
   */
  private Connection conn;


  public DbConnector(ModelDb modelDb)
  {
    this.modelDb= modelDb;
  }


  public Connection getConn() throws DbConnectionException
  {
    if (conn == null)
    {
      String url = "jdbc:postgresql://" + modelDb.hostname + ":" + modelDb.port;
      try
      {
        conn = DriverManager.getConnection(url, modelDb.user, modelDb.port);
        //logger.info("Connected to the PostgreSQL server successfully.");
      }
      catch (SQLException e)
      {
        //logger.error("Could not connect to DB : " + e.getMessage());
        throw new DbConnectionException("Could not connect to DB.", e);
      }
    }
    return conn;
  }


}
