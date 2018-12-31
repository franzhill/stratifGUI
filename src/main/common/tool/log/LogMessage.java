package main.common.tool.log;

import lombok.Getter;
import org.apache.logging.log4j.Level;

public class LogMessage
{
  @Getter private Level level;
  @Getter private String message;


  public LogMessage(Level level, String message)
  { this.level = level;
    this.message = message;
  }
}
