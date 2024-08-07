package org.makechtec.web.carpinteria.dependency_injection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.Client;
import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.web.carpinteria.components.bom.BOMDatabaseProxy;
import org.makechtec.web.carpinteria.components.client.ClientDatabaseProxy;
import org.makechtec.web.carpinteria.components.materials.MaterialTemplateDatabaseProxy;
import org.makechtec.web.carpinteria.components.product.ProductDatabaseProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerResolution {

    @Bean
    public MaterialTemplateDatabaseProxy materialDatabaseProxy() {
        return new MaterialTemplateDatabaseProxy(connectionInformation());
    }

    @Bean
    public BOMDatabaseProxy bomDatabaseProxy() {
        return new BOMDatabaseProxy(connectionInformation());
    }

    @Bean
    public ClientDatabaseProxy clientDatabaseProxy() {
        return new ClientDatabaseProxy(connectionInformation());
    }

    @Bean
    public ProductDatabaseProxy productDatabaseProxy() {
        return new ProductDatabaseProxy(connectionInformation());
    }

    @Bean
    public ConnectionInformation connectionInformation() {
        return new ConnectionInformation(
                "carpinteriauser",
                "1234",
                "localhost",
                "3306",
                "carpinteria"
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
