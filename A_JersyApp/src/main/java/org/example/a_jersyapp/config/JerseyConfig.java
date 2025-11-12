package org.example.a_jersyapp.config;

import org.example.a_jersyapp.ressources.CategoryResource;
import org.example.a_jersyapp.ressources.ItemResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(CategoryResource.class);
        register(ItemResource.class);
    }
}
