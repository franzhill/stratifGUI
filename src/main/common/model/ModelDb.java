package main.common.model;

import lombok.Getter;
import main.utils.MyStringUtils;

public class ModelDb
{
  @Getter public String user;
  @Getter public String password;
  @Getter public String hostname;
  @Getter public String port;
  @Getter public String name;  // TODO Add

  public ModelDb(String hostname, String port, String user, String password, String name)
  {
    this.hostname= hostname;
    this.port    = port;
    this.user    = user;
    this.password= password;
    this.name    = name;
  }

  /**
   * True when some info is missing
   * @return
   */
  public boolean isIncomplete()
  {
    return  MyStringUtils.isNullOrEmpty(user)     ||
            MyStringUtils.isNullOrEmpty(password) ||
            MyStringUtils.isNullOrEmpty(hostname) ||
            MyStringUtils.isNullOrEmpty(port)     ||
            MyStringUtils.isNullOrEmpty(name);
  }
/*

  String url = "jdbc:postgresql://" + txtDbHostname.getText();
            try {
  conn = DriverManager.getConnection(url, txtDbUser.getText(), txtDbPassword.getText());
  logger.info("Connected to the PostgreSQL server successfully.");
} catch (
  SQLException e) {
  //logger.error("Could not connect to DB : " + e.getMessage());
  throw new DbConnectionException("Could not connect to DB.", e);
}*/
}
