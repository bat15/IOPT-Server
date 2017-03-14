/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.server;

import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author Павел
 */
public class Settings {
    
    public static final Logger LOG = Logger.getLogger(Settings.class.getName());
    
    public static final String DB_USER = "db_user";
    public static final String DB_PASSWORD = "db_passfile";
    public static final String DB_HOST = "db_host";
    public static final String DB_PORT = "db_port";
    public static final String DB_NAME = "db_name";

    
    public static final String USERS_TABLE_NAME = "user";
    public static final String SNAPSHOTS_TABLE_NAME = "snapshot";
    public static final String DASHBOARDS_TABLE_NAME = "dashboard";
    public static final String APPKEYS_TABLE_NAME = "appkey";
    public static final String COOKIES_TABLE_NAME = "cookie";
    
    public static final String MODELS_TABLE_NAME = "model";
    public static final String OBJECTS_TABLE_NAME = "object";
    public static final String PROPERTIES_TABLE_NAME = "property";
    public static final String SCRIPTS_TABLE_NAME = "script";
    
    

    public static final String[] SETTING_NAMES = {
        DB_USER, DB_PASSWORD, DB_HOST, DB_NAME, DB_PORT
    };   
    
    public static void validate(Properties prop) {
        if (prop == null) {
            throw new IllegalArgumentException("Properties are null");
        }
        for (String name : SETTING_NAMES) {
            if (!prop.containsKey(name)) {
                throw new IllegalArgumentException("Not found property '" + name + "'");
            }
        }
        System.out.println("##### Properties are checked. Everything is ok!");
    }
        
}
