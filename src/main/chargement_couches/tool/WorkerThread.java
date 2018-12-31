package main.chargement_couches.tool;

import main.common.tool.SysCommand;

public class WorkerThread implements Runnable
{

  private SysCommand syscommand;

  public WorkerThread(SysCommand command)
  {
    this.syscommand=command;
  }

  @Override
  public void run()
  {
    System.out.println(Thread.currentThread().getName()+" Start. Command = "+syscommand);
    processCommand();
    System.out.println(Thread.currentThread().getName()+" End.");
  }


  private void processCommand()
  {
    try
    {
      Thread.sleep(1000);
      int retVal = syscommand.execute();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public String toString()
  {
    return "Thread executing syscommand : " + this.syscommand.toString();
  }
}

