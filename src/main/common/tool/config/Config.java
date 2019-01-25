package main.common.tool.config;

import main.common._excp.ConfigAccessException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read and save to a ini-style configuration file
 *
 * @author fhill
 */
public class Config
{
    private static Logger logger = LoggerFactory.getLogger(Config.class);

    /**
     * Our handle on the Properties-style (ini) configuration file
     * (lets us load from & save to the file)
     */
    private FileBasedConfigurationBuilder<PropertiesConfiguration> builder;
    /*private CombinedConfigurationBuilder builder;*/

    /**
     * Holds our configurations (key- value pairs)
     */
    private Configuration config;

/*
    public void Config(String filePath) throws ConfigAccessException
    {
        try
        {
            Configurations configs = new Configurations();
            builder = configs.combinedBuilder(filePath);
            config = builder.getConfiguration();
        }
        catch (ConfigurationException cex)
        {   String msg = String.format("Could not load configuration file path: [%s]", filePath);
            logger.error(msg);
            throw new ConfigAccessException(msg, cex);
        }

    }
*/

    /**
     *
     * @param filePath
     *          From Apache Commons Configuration :
     *          "If you do not specify an absolute path, the file will be searched automatically in the following locations:
     *            -in the current directory
     *            -in the user home directory
     *            -in the classpath"
     * @throws ConfigAccessException conf file not found or loading pb otherwise
     */
    public Config(String filePath) throws ConfigAccessException
    {
        try
        {   Configurations configs = new Configurations();
            // obtain the configuration
            builder = configs.propertiesBuilder(filePath);
            config  = builder.getConfiguration();
        }
        catch (ConfigurationException cex)
        {   String msg = String.format("Could not load configuration file path: [%s]", filePath);
            logger.error(msg);
            throw new ConfigAccessException(msg, cex);
        }
    }





    /**
     * Get a configuration property
     * @param name name of property (key)
     * @return value of property, null if property is not found
     */
    public String getProp(String name)
    {
        return config.getString(name);
    }


    public String getProp(String name, String default_val)
    {
        return (config.getString(name) != null) ? config.getString(name) : default_val;
    }


    /**
     * Set a property.
     * Backing conf file is saved.
     * @param name
     * @param value
     * @throws ConfigAccessException
     */
    public void setProp(String name, String value) throws ConfigAccessException
    {
        config.setProperty(name, value);
        // Save file every time we change a property. Not optimized, but hey that will do.
        try {
            builder.save();
        }
        catch (ConfigurationException cex)
        {   String msg = String.format("Could not save to configuration file path: [%s]", builder.getFileHandler().getFile().getAbsolutePath());  // Not tested, but can't see why this wouldn't give us the original file path back...
            logger.error(msg);
            throw new ConfigAccessException(msg, cex);
        }
    }


}
