package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPERTIES = new Properties();
    private static final String BASE_URL = "baseUrl";
    private static final String API_KEY = "apiKey";
    private static final String WIREMOCK_URL = "wireMockUrl";


    static {
        try (InputStream input = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException("config.properties not found");
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private Config() {
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String baseUrl() {
        return getProperty(BASE_URL);
    }

    public static String apiKey() {
        return getProperty(API_KEY);
    }

    public static String wireMockUrl() {
        return getProperty(WIREMOCK_URL);
    }
}
