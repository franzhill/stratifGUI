package main;

import main.chargement_couches.controller.*;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelCouche;
import main.chargement_couches.model.ModelCharg;
import main.common.controller.ControllerSelectFile;
import main.common.controller.ControllerTest;
import main.common._excp.ConfigAccessException;
import main.common.gui.GuiLogOutputStream;
import main.common.tool.config.Config;
import main.stratification.controller.ControllerExecuteSqlFiles;
import main.stratification.controller.ControllerSelectSqlFiles;
import main.stratification.model.ModelStrat;
import main.utils.MyExceptionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.UIManager.setLookAndFeel;

/**
 * GUI, and also some bootstrapping elements for the pseudo MVC pattern we'll be using
 *
 * @author fhill
 */
public class Gui
{
  private JFrame       rootFrame;
  public  JPanel       rootPanel;
  private JButton      buttTest;
  private JTabbedPane  tabbedPane1;
  public  JTextArea    txtaLog;

  // Db settings

  public  JTextField   txtDbHostname;
  public  JTextField   txtDbUser;
  public  JTextField   txtDbPassword;
  public  JTextField   txtDbName;
  public  JTextField   txtDbPort;
  public  JTextField   txtDetectDep;
  private JButton      buttTestDbConnectivity;


  // Tab : Chargement couche

  private JButton      buttComputeFiles;
  public  JTable       tableDetectedFiles;
  public  JCheckBox    chbDetectFiles;
  public  JTextField   txtDetectFiles;
  private JPanel       pannel;
  public  JRadioButton rdoCoucheTopo;
  public  JRadioButton rdoCoucheAlti;
  public  JRadioButton rdoCoucheForet;
  public  JRadioButton rdoCoucheFoncier;
  public  JTextField   txtSchema;
  public  JTextField   txtTable;
  public  JTextField   txtFileExt;
  public  JTextField   txtLoadCmd;
  public  JRadioButton rdoDepDetect;
  public  JRadioButton rdoDep;
  public  JTextField   txtDep;
  public  JTextField   txtPostgresqlBinDir;
  public  JTextField   txtTempDir;
  public  JButton      buttSelectPostgresqlBinDir;
  public  JButton      buttSelectTempDir;
  public  JButton      buttGenerateScripts;
  public  JCheckBox    chbEmptyWorkDirFirst;
  private JTabbedPane  tabbedPane2;
  public  JTextField   txtSchemaTableSource;
  public  JTextField   txtNbThreads;
  private JButton      buttSelectUnzipDir;
  //public  JTextField   txtUnzipDir;
  private JButton      buttSelectFiles;
  public  JTextArea    txtaSelectedFiles;
  public  JButton      buttExecuteScripts;
  public  JProgressBar progbCouche;

  // Tab : Stratif

  private JButton      buttStratSelectFiles;
  public  JTextArea    txtaStratSelectedFiles;
  public  JRadioButton rdoStratDepTous;
  public  JRadioButton rdoStratDepSelect;
  public  JTextArea    txtaStratDepSelect;
  public  JTextField   txtStratDepPlacheholder;
  public  JButton      buttStratDo;


  /**
   * Internal logger
   */
  private static Logger logger = LoggerFactory.getLogger(Gui.class);

  /**
   * Will log in a dedicated area on the GUI, visible to the end user.
   * Is public for access from other classes in the MVC architecture
   */
  //public static Logger loggerGui;  // TODO unmake static ?
  public static org.apache.logging.log4j.Logger loggerGui;  // TODO unmake static ?

  /**
   * User input is stored in this model (for "chargement couche")
   */
  private ModelCharg modelCharg = new ModelCharg();

  /**
   * User input is stored in this model (for "stratification")
   */
  private ModelStrat modelStrat = new ModelStrat();


  /**
   * Holds the userConfig loaded from user conf file
   */
  public Config userConfig;

  /**
   * TODO once the app is packaged, decide where user conf file should be stored
   */
  private String userConfigFilePath ="./conf/conf.ini";



  /**
   * The Swing app
   *
   * Contains all the graphical elements and behaviours
   * Built with the help of IntelliJ Swing Designer
   */
  public Gui()
  {
    // Set the look and feel.
    // Note: For some reason probably due to how IntelliJ manages the construction of the Swing GUI
    // (bound to the .form) it seems applying look and feel now just fails.
    // => we'll do that later after the GUI has been displayed.
    // However for some reason we still have to call it one first time here or the external border
    // (title, exapand, close buttons) will not be displayed
    initLookAndFeel();

    // Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

    // Create and set up the window.
    rootFrame = new JFrame("Stratification");
    rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    rootFrame.setContentPane(rootPanel);

    // txtaLog.setFont(Font.decode("UTF-8")); DOESN'T WORK ...
  }


  private void showGUI()
  {   //Display the window.
    rootFrame.pack();
    rootFrame.setVisible(true);
  }


  private void initLookAndFeel()
  {
    logger.debug("setting look and feel");
    String lookNFeel =
            UIManager.getSystemLookAndFeelClassName();
    //                 UIManager.getCrossPlatformLookAndFeelClassName();
    //                 UIManager.getCrossPlatformLookAndFeelClassName();

    try
    {   setLookAndFeel(lookNFeel);
      logger.debug("Look and feel set succesfully");
    }

    catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
    {   logger.warn("Problème de chargement du thème ('look and feel'). Le thème par défaut sera utilisé. Stacktrace : \n" + ExceptionUtils.getStackTrace(e) );
    }

        /*try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //Windows Look and feel
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }*/
  }


  private void addActionListeners()
  {
    ControllerSelectCouche rdoCoucheCtrl = new ControllerSelectCouche        (this, modelCharg);

    // Make it possible to identify the radio button that has been selected
    rdoCoucheTopo              .setActionCommand(ModelCouche.TOPO);
    rdoCoucheAlti              .setActionCommand(ModelCouche.ALTI);
    rdoCoucheForet             .setActionCommand(ModelCouche.FORET);
    rdoCoucheFoncier           .setActionCommand(ModelCouche.FONCIER);

    // Attach actions
    buttSelectPostgresqlBinDir .addActionListener(new ControllerSelectFile        (this, txtPostgresqlBinDir, JFileChooser.DIRECTORIES_ONLY, false, null));
    buttSelectTempDir          .addActionListener(new ControllerSelectFile        (this, txtTempDir         , JFileChooser.DIRECTORIES_ONLY, false, null));
    buttTest                   .addActionListener(new ControllerTest              (this));
    buttTestDbConnectivity     .addActionListener(new ControllerTestDbConnectivity(this, modelCharg));
    buttSelectFiles            .addActionListener(new ControllerSelectParentFiles (this, modelCharg, txtaSelectedFiles, JFileChooser.DIRECTORIES_ONLY, true , userConfig.getProp("dir_couches")));
    buttComputeFiles           .addActionListener(new ControllerFindFiles         (this, modelCharg));
    buttGenerateScripts        .addActionListener(new ControllerGenerateScripts   (this, modelCharg));
    buttExecuteScripts         .addActionListener(new ControllerExecuteScripts    (this, modelCharg));
    rdoCoucheTopo              .addActionListener(rdoCoucheCtrl);
    rdoCoucheAlti              .addActionListener(rdoCoucheCtrl);
    rdoCoucheForet             .addActionListener(rdoCoucheCtrl);
    rdoCoucheFoncier           .addActionListener(rdoCoucheCtrl);
    //buttSelectUnzipDir         .addActionListener(new ControllerSelectFile        (this, txtUnzipDir,                  JFileChooser.DIRECTORIES_ONLY, false, null));
    buttStratSelectFiles       .addActionListener(new ControllerSelectSqlFiles(this, modelStrat, txtaStratSelectedFiles, JFileChooser.FILES_ONLY, true , userConfig.getProp("dir_strat_scripts_sql")));
    buttStratDo                .addActionListener(new ControllerExecuteSqlFiles(this, modelStrat));



    // According to selections made, some components should be disabled/selected etc.:
    chbDetectFiles.addActionListener(new AbstractAction("chbDetectFiles")
    { public void actionPerformed(ActionEvent e)
      { if (chbDetectFiles.isSelected()) {chbDetectFilesSelect(true);} else {chbDetectFilesSelect(false);}
      }
    });
    rdoDep.addActionListener(new AbstractAction("rdoDep")
    { public void actionPerformed(ActionEvent e)
      { if (rdoDep      .isSelected()) {rdoDepDetectSelect(false);}
      }
    });
    rdoDepDetect.addActionListener(new AbstractAction("rdoDepDetect")
    { public void actionPerformed(ActionEvent e)
      { if (rdoDepDetect.isSelected()) {rdoDepDetectSelect(true); }
      }
    });
    rdoStratDepTous.addActionListener(new AbstractAction("rdoStratDepTous")
    { public void actionPerformed(ActionEvent e)
      { if (rdoStratDepTous.isSelected()) {rdoStratDepTousSelect(true); }
      }
    });
    rdoStratDepSelect.addActionListener(new AbstractAction("rdoStratDepSelect")
    { public void actionPerformed(ActionEvent e)
    { if (rdoStratDepSelect.isSelected()) {rdoStratDepTousSelect(false); }
    }
    });




  }


  public static void main(String[] args)
  {
    logger.debug("");
    logger.info("");
    Gui gui = new Gui();


    //gui.initLookAndFeel();  // Note : trying to set the L&F here does not work either
    gui.showGUI();
    gui.initLookAndFeel();    // Note : it works here (with update of the tree that follows)
    SwingUtilities.updateComponentTreeUI(gui.rootFrame);
    gui.rootFrame.pack();

    gui.loadUserConf();
    gui.addActionListeners();

    logger.debug("initialising gui logger...");
    gui.initGuiLogger();
    loggerGui.trace("Testing the loggerGui àéè...");
    loggerGui.debug("Testing the loggerGui àéè...");
    loggerGui.info ("Testing the loggerGui àéè...");
    loggerGui.warn ("Testing the loggerGui àéè...");
    loggerGui.error("Testing the loggerGui àéè...");
    logger.debug("logger, debug");

    gui.initUserConfigDisplay();
  }


  /**
   * Intialise the GUI logger
   *
   * The GUI logger logs in a text area visible to the end user and may be used to display valuable information
   * (as well as possibly relevant logging messages).
   * We are using a Log4J2 dedicated appender to write inside the dedicated GUI text area, via a
   * special outpustream. The construction of such an appender may not be made via config files, it
   * has to be done programmatically (and consequently same goes for the logger) (here).
   * However we will be making it possible to store as much configs as possible in the Log4J2 config file,
   * by placing them in dummy appender and logger, in the config file, and then retrieving these configs programmatically
   * here to insert them in the real appender and logger.
   *
   * Note : we're tightly coupling to the Log4j2 logging solution here ...)
   *
   */
  private void initGuiLogger()
  {
    String GUI_LOGGER_REF = "gui_logger";  // Name used for the dummy counterparts in the config depFiles, and for the final logger

    // 1. Create the dedicated appender, and add it to the Log4J2 config
    // Get the Log4J2 context, config and stuff
    final LoggerContext context = LoggerContext.getContext(false);
    final Configuration config = context.getConfiguration();
    final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);

    // Special outpustream that redirects to the logging area
    final GuiLogOutputStream guilos = new GuiLogOutputStream(this);

    // Pattern layout for the dedicated appender
    PatternLayout layout = PatternLayout.createDefaultLayout(config);

    // Overwrite with the the layout defined in the "dummy" gui appender in conf file, if exists
    if (config.getAppenders().get(GUI_LOGGER_REF) != null) { // TODO constant
      layout = (PatternLayout) config.getAppenders().get(GUI_LOGGER_REF).getLayout();
      // TODO check if layout is really a pattern layout, display error if not
      //      or catch ClassCastException
    }

    final Appender appender = OutputStreamAppender.createAppender(layout, null, guilos, "gui_outputstream", false, true);
    appender.start();
    config.addAppender(appender);

    // 2. Add the newly created appender to the log4J2 logger we'll be dedicating to GUI logging

    LoggerConfig guiLoggerConfig =  config.getLoggers().get(GUI_LOGGER_REF);
    if (null == guiLoggerConfig )  // not declared in config file ...
    {   // Create one with some default params
      AppenderRef[] refs = new AppenderRef[] {};
      LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.INFO, GUI_LOGGER_REF,"true", refs, null, config, null );
      loggerConfig.addAppender(appender, null, null);
      config.addLogger(GUI_LOGGER_REF, loggerConfig);
      ctx.updateLoggers();
      guiLoggerConfig =  config.getLoggers().get(GUI_LOGGER_REF);

      logger.info("Le logger " + GUI_LOGGER_REF + " n'est pas déclaré dans la configuration de Log4J. Création avec des valeurs par défaut");
    }
    guiLoggerConfig.addAppender(appender, null, null);  // Note ; could do the same for level and filter as we did for the pattern layout I guess

    //loggerGui = LoggerFactory.getLogger(GUI_LOGGER_REF);
    loggerGui = org.apache.logging.log4j.LogManager.getLogger(GUI_LOGGER_REF);
  }


  /**
   * Do not use directly.
   * Use the loggerGui instead.
   * Used by the loggerGui.
   *
   * @param text
   */
  public void logInGui(String text)
  {
    logInGui(text, false);
  }


  /**
   * !! Do not use directly. !!
   * Use the loggerGui instead.
   * @used_by the loggerGui.
   *
   * @param text
   * @param addNewLine
   */
  public void logInGui(String text, boolean addNewLine)
  { final String logInGuiNewline = "\n";

    txtaLog.append( text) ;//+ (addNewLine ? logInGuiNewline : "")); //+ logInGuiNewline);
    // scrolls the text area to the end of data
    txtaLog.setCaretPosition(txtaLog.getDocument().getLength());
  }


  private void loadUserConf()
  {   // Load userConfig file
    try
    {   userConfig = new Config(userConfigFilePath);
    }
    catch (ConfigAccessException e)
    {   logger.error(String.format("Could  not load user conf file {%s}. Stack:\n {%s}", userConfigFilePath, Arrays.toString(e.getStackTrace()))); // TODO stack trace print is KO, fix it
      String usrMsg = String.format("Impossible de charger le fichier de conf {%s}. Indiquer les configurations à la main si besoin.", userConfigFilePath);
      showMessageError(usrMsg);
    }
  }


  /**
   * Initialise GUI display with config (config file)
   */
  private void initUserConfigDisplay()  // TODO factorise with ControllerSelectCouche.addActionEvent
  { logger.debug("");
    txtDbHostname          .setText(userConfig.getProp("db.hostname"));
    txtDbUser              .setText(userConfig.getProp("db.user"));
    txtDbPassword          .setText(userConfig.getProp("db.password"));
    txtDbPort              .setText(userConfig.getProp("db.port"));
    txtDbName              .setText(userConfig.getProp("db.name"));
    txtPostgresqlBinDir    .setText(userConfig.getProp("postgresql_bin_path"));
    txtTempDir             .setText(userConfig.getProp("temp_folder_path"));
    txtNbThreads           .setText(userConfig.getProp("couches.max_db_conn", "1"));
    txtStratDepPlacheholder.setText(userConfig.getProp("strat.dep_placeholder"));
    //txtUnzipDir         .setText(userConfig.getProp("rep_dezip"));
    if (! txtDetectFiles.getText().isEmpty()) { chbDetectFilesSelect(true); }  else {chbDetectFilesSelect(false); }
  }

  /**
   * Selecting or deselecting chbDetectFiles has consequences on other components
   * so we're binding their behaviours through this function
   * @param b select (true) or deselect (false)
   */
  public void chbDetectFilesSelect(boolean b)
  {
    chbDetectFiles.setSelected(b);
    txtDetectFiles.setEnabled(b);
  }


  /**
   * Selecting or deselecting rdoDepDetect has consequences on other components
   * so we're binding their behaviours through this function
   * @param b select (true) or deselect (false)
   */
  public void rdoDepDetectSelect(boolean b)
  {
    rdoDepDetect.setSelected(b);  // No need to bother about the other antagonist radio button, since they're in a group this should be handled "internally"
    txtDetectDep.setEnabled(b);
    txtDep.setEnabled(!b);
  }


  /**
   * Selecting or deselecting rdoStratDepTous has consequences on other components
   * so we're binding their behaviours through this function
   * @param b select (true) or deselect (false)
   */
  public void rdoStratDepTousSelect (boolean b)
  {
    txtaStratDepSelect.setEnabled(!b);
    //txtaStratDepSelect.setForeground(b ? Color.WHITE : Color.BLACK);  // doesn't work
  }





  /**
   *
   * @return which one of the radio buttons for couche selection is selected, null if none
   */
  public JRadioButton getRdoCoucheSelected()
  {
    if (rdoCoucheTopo   .isSelected()) return rdoCoucheTopo   ;
    if (rdoCoucheAlti   .isSelected()) return rdoCoucheAlti   ;
    if (rdoCoucheForet  .isSelected()) return rdoCoucheForet  ;
    if (rdoCoucheFoncier.isSelected()) return rdoCoucheFoncier;
    return null;
  }



  public void saveUserConfigDisplay()
  {   logger.info("Saving user config back to file...");
    try
    { userConfig.setProp("db.hostname"   , txtDbHostname.getText());
      userConfig.setProp("db.user"       , txtDbUser.getText());
      userConfig.setProp("db.password"   , txtDbPassword.getText());
      userConfig.setProp("db.port"       , txtDbPort.getText());
      userConfig.setProp("db.name"       , txtDbName.getText());
      //userConfig.setProp("rep_dezip"     , txtUnzipDir.getText());
      // TODO gérer les couches

    }
    catch (ConfigAccessException e)
    {   logger.error(String.format("Could not save to user conf file {%s}. Stack:\n {%s}", userConfigFilePath, ExceptionUtils.getStackTrace(e)));
      String usrMsg = String.format("Impossible de sauvegarder dans le fichier de conf {%s}. L'action va poursuivre mais la configuration ne sera pas enregistrée.", userConfigFilePath);
      showMessageError(usrMsg);
    }
  }


  /**
   * Display error to the user in a dialog window
   */
  public void showMessageError(String usrMsg)
  {  showMessageError(usrMsg, null);
  }

  /**
   * Display error to the user in a dialog window
   * With technical information regarding the cause exception
   * @param usrMsg
   * @param e exception that caused the problem
   */
  public void showMessageError(String usrMsg, Throwable e)
  { if (e != null)
    {   usrMsg = usrMsg +
            "\n \n" +
            "Infos techniques : \n" +
            MyExceptionUtils.getStackMessages(e);    // TODO wrap - these lines can be very long
    }
    JOptionPane.showMessageDialog(rootPanel, usrMsg, "ERREUR", JOptionPane.WARNING_MESSAGE);

    // Also display in GUI log pane
    loggerGui.error(usrMsg);
  }





  /**
   * Display info to the user in a dialog window
   */
  public void showMessageInfo(String usrMsg)
  {
    loggerGui.info(usrMsg);
    JOptionPane.showMessageDialog(rootPanel, usrMsg, "INFO", JOptionPane.INFORMATION_MESSAGE);
  }


  /**
   *
   * @return absolute path
   */
  //public String getUnzipDir()
  //{
  //  return txtUnzipDir.getText();
  //}

  public void displaySelectedFiles(List<File> selectedFiles)
  {
    // Clear display of selected depFiles
    txtaSelectedFiles.setText(null);

    // Display selected depFiles back to user
    for (File f : selectedFiles)
    {
      txtaSelectedFiles.append(f.getName() + "\n");
      logger.debug("Selected depFiles = " + f.getName());
    }

  }

  //  public void displaySelectedUnzipDir(File unzipDir)
  //{   txtUnzipDir.setText(unzipDir.getAbsolutePath());
  //}




  public void displayFoundFiles()
  {
    List<String>   cols = new ArrayList<String>();
    List<String[]> vals = new ArrayList<String[]>();
    cols.add("Département");
    cols.add("Fichier");

    // Add depFiles for display in GUI
    for (FileDep df : modelCharg.depFiles)
    { vals.add(new String[]{df.departement, df.file.getAbsolutePath()});
    }

    // Display all depFiles in GUI :
    TableModel tableModel = new DefaultTableModel(vals.toArray(new Object[][]{}), cols.toArray());
    tableDetectedFiles.setModel(tableModel);
  }

  private void createUIComponents()
  {
    // TODO: place custom component creation code here
  }
}
