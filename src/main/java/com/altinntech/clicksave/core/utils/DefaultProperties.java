package com.altinntech.clicksave.core.utils;

import com.altinntech.clicksave.interfaces.Observer;
import com.altinntech.clicksave.interfaces.PropertyEnvironment;
import lombok.Getter;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DefaultProperties} class provides default properties for configuring the application.
 * It retrieves properties from the environment and provides default values if not specified.
 *
 * @author Fyodor Plotnikov
 */

@Getter
public class DefaultProperties {

    private final List<Observer> observers = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String initialConnectionsPoolSize;
    private String connectionsPoolSizeRefillThreshold;
    private String maxConnectionPoolSize;
    private String allowConnectionsPoolExpansion;
    private String rootPackageToScan;
    private String testEnv;
    private String batchSaveRate;
    private String threadManagerMaxProcessors;
    private String threadManagerMaxQueueSize;

    public static DefaultProperties fromPropertyFile() {
        PropertyReader propertyReader = PropertyReader.getInstance();
        return getProperties(propertyReader);
    }

    public static DefaultProperties fromEnvironment(Environment environment) {
        SpringEnvironment springEnvironment = new SpringEnvironment(environment);
        return getProperties(springEnvironment);
    }

    private static DefaultProperties getProperties(PropertyEnvironment propertyEnvironment) {
        DefaultProperties defaultProperties = new DefaultProperties();
        defaultProperties.url = propertyEnvironment.getProperty("clicksave.connection.datasource.url", "");
        defaultProperties.username = propertyEnvironment.getProperty("clicksave.connection.datasource.username");
        defaultProperties.password = propertyEnvironment.getProperty("clicksave.connection.datasource.password");
        defaultProperties.initialConnectionsPoolSize = propertyEnvironment.getProperty("clicksave.connection.pool.initial-size", "40");
        defaultProperties.connectionsPoolSizeRefillThreshold = propertyEnvironment.getProperty("clicksave.connection.pool.refill-threshold", "5");
        defaultProperties.maxConnectionPoolSize = propertyEnvironment.getProperty("clicksave.connection.pool.max-size", "50");
        defaultProperties.allowConnectionsPoolExpansion = propertyEnvironment.getProperty("clicksave.connection.pool.allow-expansion", "true");
        defaultProperties.rootPackageToScan = propertyEnvironment.getProperty("clicksave.core.root-package", "");
        defaultProperties.testEnv = propertyEnvironment.getProperty("clicksave.test-env", "false");
        defaultProperties.batchSaveRate = propertyEnvironment.getProperty("clicksave.core.batch-save-rate", "1200");
        defaultProperties.threadManagerMaxProcessors = propertyEnvironment.getProperty("clicksave.core.thread-manager.max-processors", "0");
        defaultProperties.threadManagerMaxQueueSize = propertyEnvironment.getProperty("clicksave.core.core.thread-manager.max-queue-size", "1000");
        return defaultProperties;
    }

    public boolean validate() {
        return !url.isEmpty() && password != null && username != null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws SQLException {
        this.url = url;
        notifyObservers();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws SQLException {
        this.username = username;
        notifyObservers();
    }

    public void setPassword(String password) throws SQLException {
        this.password = password;
        notifyObservers();
    }

    public String getPassword() {
        return this.password;
    }

    public void setInitialConnectionsPoolSize(String initialConnectionsPoolSize) {
        this.initialConnectionsPoolSize = initialConnectionsPoolSize;
    }

    public String getInitialConnectionsPoolSize() {
        return initialConnectionsPoolSize;
    }

    public String getConnectionsPoolSizeRefillThreshold() {
        return connectionsPoolSizeRefillThreshold;
    }

    public void setConnectionsPoolSizeRefillThreshold(String connectionsPoolSizeRefillThreshold) {
        this.connectionsPoolSizeRefillThreshold = connectionsPoolSizeRefillThreshold;
    }

    public String getMaxConnectionPoolSize() {
        return maxConnectionPoolSize;
    }

    public void setMaxConnectionPoolSize(String maxConnectionPoolSize) {
        this.maxConnectionPoolSize = maxConnectionPoolSize;
    }

    public String getAllowConnectionsPoolExpansion() {
        return allowConnectionsPoolExpansion;
    }

    public void setAllowConnectionsPoolExpansion(String allowConnectionsPoolExpansion) {
        this.allowConnectionsPoolExpansion = allowConnectionsPoolExpansion;
    }

    public String getRootPackageToScan() {
        return rootPackageToScan;
    }

    public void setRootPackageToScan(String rootPackageToScan) {
        this.rootPackageToScan = rootPackageToScan;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() throws SQLException {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
