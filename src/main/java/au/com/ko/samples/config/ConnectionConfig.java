package au.com.ko.samples.config;

import au.com.ko.samples.protocol.SensitiveValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configure a connection.
 */
public class ConnectionConfig {

    private final String host;
    private final int port;
    private final String user;
    private final SensitiveValue password;

    @JsonCreator
    public ConnectionConfig(
            @JsonProperty("host") String host,
            @JsonProperty("port") int port,
            @JsonProperty("user") String user,
            @JsonProperty("password") SensitiveValue password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * The hostname or IP address of server.
     *
     * @return The host.
     */
    public String getHost() {
        return host;
    }

    /**
     * The service port of the server.
     *
     * @return The service port.
     */
    public int getPort() {
        return port;
    }

    /**
     * The username used to authenticate to the server.
     *
     * @return The username.
     */
    public String getUser() {
        return user;
    }

    /**
     * The password used to authenticate to the server.
     *
     * @return The password.
     */
    public SensitiveValue getPassword() {
        return password;
    }

}
