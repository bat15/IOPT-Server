/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.server;

import java.util.Set;
import javax.ws.rs.core.Application;


//bat15.restful.requests.RestfulAppConfig
/**
 *
 * @author Павел
 */
@javax.ws.rs.ApplicationPath("service")
public class RestfulAppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    
    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(bat15.iot.rest.resources.AuthResource.class);
        resources.add(bat15.iot.rest.resources.ModelsResource.class);
        resources.add(bat15.iot.rest.resources.SyncResource.class);
        resources.add(bat15.iot.rest.resources.TestResource.class);
    }
    
}