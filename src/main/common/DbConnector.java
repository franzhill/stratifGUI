package main.common;

import main.common.model.ModelDb;
import main.common._excp.DbConnectionException;

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
      String url = "jdbc:postgresql://" + modelDb.hostname + ":" + modelDb.port + "/" + modelDb.name;
      try
      {
        conn = DriverManager.getConnection(url, modelDb.user, modelDb.password);
        //logger.info("Connected to the PostgreSQL server successfully.");
      }
      catch (SQLException e)
      {
        //logger.error("Could not connect to DB : " + e.getMessage());
        throw new DbConnectionException(String.format("Could not connect to DB (url = %s)", url ), e);
      }
    }
    return conn;
  }

}
