package main;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * @deprecated
 */

@Plugin(name = "GuiAppender", category = "Core", elementType = "appender")
public class GuiAppender extends AbstractAppender
{
    protected Gui gui;

    protected GuiAppender(String name, Gui gui, Filter filter)
    {   super(name, filter, null);
        System.out.println("GuiAppender");
        this.gui = gui;
    }

    @PluginFactory
    public static GuiAppender createAppender(@PluginAttribute("name") String name,
                                             @PluginAttribute("gui") Gui gui,
                                             @PluginElement("Filter") Filter filter)
    {   System.out.println("createAppender()");
        if (name == null) {
            LOGGER.error("No name provided for GuiAppender");
            return null;
        }
        return new GuiAppender(name, gui, filter);
    }

    @Override
    public void append(LogEvent event)
    {   gui.logInGui(event.toString());
    }
}
