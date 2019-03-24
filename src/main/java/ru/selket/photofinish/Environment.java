package ru.selket.photofinish;

import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.Properties;


@Log
public class Environment {

    private static final String ENV_SERVER_NAME = "";
    private static final String ENV_OS_NAME = "os.name";

    private static final String ENV_VALUE_LOCAL = "local";
    private static final String ENV_VALUE_DEV = "dev";
    private static final String ENV_VALUE_PROD = "production";


    public static String getProfile(){
        Properties properties = System.getProperties();
        String env = null;

        if (!ENV_SERVER_NAME.isEmpty() && properties.containsKey(ENV_SERVER_NAME)){
            env = properties.getProperty(ENV_SERVER_NAME);
        }
        else {
            if (properties.containsKey(ENV_OS_NAME)){
                String os = properties.getProperty(ENV_OS_NAME);

                if (os != null && !os.isEmpty() && os.contains("Windows")){
                    env = ENV_VALUE_LOCAL;
                }
                else if(System.getProperty("profile") != null && !System.getProperty("profile").isEmpty()) {
                    env = System.getProperty("profile");
                }
                else {
                    env = ENV_VALUE_PROD;
                }
            }
        }

        if (env != null && (env.equals(ENV_VALUE_LOCAL) || env.equals(ENV_VALUE_DEV) || env.equals(ENV_VALUE_PROD)) ){
            log.info("Detected environment: " + env);
            return env;
        }

        throw new RuntimeException("Environment \"" + env + "\" is not detected!!!");
    }
}