package au.com.ko.samples.prov;

import au.com.ko.samples.config.ConnectionConfig;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Support for loading the configuration from a resource.
 */
public class ConfigManager {

    private URL configSrc;
    private ConnectionConfig config;

    private static final ConfigManager INSTANCE = new ConfigManager();
    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());
    private static final String CONFIG_FILE_KEY = ConnectionConfig.class.getSimpleName();
    private static final Pattern URL_PATTERN = Pattern.compile("[a-z]{2,20}://.*", Pattern.CASE_INSENSITIVE);

    private ConfigManager() {
    }

    public static ConnectionConfig getConfig() {
        return INSTANCE.getConfiguration();
    }

    private synchronized URL getConfigSrc() {
        if (configSrc == null) {

            // ------------------- option 1 read the config file location using JNDI -------------------
            /*
                The cofig file can be defined in web.xml:

                <env-entry>
                    <env-entry-name>ConnectionConfig</env-entry-name>
                    <env-entry-type>java.lang.String</env-entry-type>
                    <env-entry-value>d:\configs\ConnectionConfig.json</env-entry-value>
                </env-entry>

                Or can be simulated for unit test by using MockInitialContextFactory class.
            */

            try {
                InitialContext context = new InitialContext();
                Object ent = context.lookup("java:comp/env/" + CONFIG_FILE_KEY);
                try {
                    String s;
                    if (ent != null && (s = ent.toString().trim()).length() > 0) {
                        if (URL_PATTERN.matcher(s).matches()) {
                            configSrc = new URL(s);
                        } else {
                            File file = new File(s);
                            if (!file.canRead()) {
                                LOGGER.log(Level.WARNING, "File for " + CONFIG_FILE_KEY + " not readable at " + file.getAbsolutePath());
                            }
                            configSrc = file.toURI().toURL();
                        }
                        LOGGER.log(Level.FINE, "Configuration parameter '"
                                + CONFIG_FILE_KEY + "' in java:comp/env resolved to '" + configSrc + "'");
                        return configSrc;
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Provided configuration parameter '"
                            + CONFIG_FILE_KEY + "' in 'java:comp/env" + CONFIG_FILE_KEY + "' but not valid file path or URL."
                            + e.getClass().getName() + " - " + e.getMessage(), e);

                }
            } catch (NamingException e) {
                LOGGER.log(Level.FINE, "Configuration parameter '"
                        + CONFIG_FILE_KEY + "' not found in 'java:comp/env'"
                        + e.getClass().getName() + " - " + e.getMessage());
            }

            // ------------------- option 2 read the config file location from system properties supplied at runtime -------------------
            // Can be supplied in runtime as -DConnectionConfig=D:/ConnectionConfig.json
            String confName = System.getProperty(CONFIG_FILE_KEY);
            if (confName != null) {
                try {
                    if (URL_PATTERN.matcher(confName).matches()) {
                        configSrc = new URL(confName);
                    } else {
                        File file = new File(confName);
                        if (!file.canRead()) {
                            LOGGER.log(Level.WARNING, "File for " + CONFIG_FILE_KEY + " not readable at " + file.getAbsolutePath());
                        }
                        configSrc = file.toURI().toURL();
                    }
                    LOGGER.log(Level.FINE, "System property parameter '"
                            + CONFIG_FILE_KEY + "' in resolved to '" + configSrc + "'");
                    return configSrc;
                } catch (MalformedURLException e) {
                    LOGGER.log(Level.FINE, "Configuration file '"
                            + confName + "' provided in system property, can not be loaded."
                            + e.getClass().getName() + " - " + e.getMessage());
                }
            }

            // ------------------- option 3 read the config file location from path -------------------
            String resName = "/" + CONFIG_FILE_KEY.replace('.', '/') + ".json";
            if (new File(ConfigManager.class.getResource(resName).getFile()).canRead()) {
                LOGGER.log(Level.FINE, "Config file is loading from class path '"+ configSrc + "'.");
                configSrc = ConfigManager.class.getResource(resName);
                return configSrc;
            }

            // ------------------- option 4 read the config file location from build directory -------------------
            confName = "build/resources/main/" + ConnectionConfig.class.getSimpleName() + ".json";
            File confFile = new File(confName);

            if (confFile.canRead()) {
                try {
                    configSrc = confFile.toURI().toURL();
                    LOGGER.log(Level.FINE, "Config file is loading from build directory '"+ configSrc + "'.");
                    return configSrc;
                } catch (MalformedURLException e) {
                    LOGGER.log(Level.WARNING, "Invalid URL from " + confFile, e);
                }
            } else {
                LOGGER.log(Level.INFO, "No config found at " + confFile.getAbsolutePath());
            }

        }
        return configSrc;
    }

    private synchronized ConnectionConfig getConfiguration() {
        URL src;
        if (config == null && (src = getConfigSrc()) != null) {
            ObjectMapper om = new ObjectMapper();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            om.configure(SerializationFeature.INDENT_OUTPUT, true);
            om.registerModule(SensitiveValueModule.masking());
            try {
                try (InputStream in = src.openStream()) {
                    config = om.readValue(in, ConnectionConfig.class);
                    LOGGER.log(Level.INFO, "Service Engine configuration loaded from " + src);
                    LOGGER.log(Level.INFO, "loaded configuration is: " + om.writeValueAsString(config));
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Unable to load configuration from " + src, e);
            }
        }
        return config;
    }
}
