package es.jocasolo.competitiveeventsapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
        		.securityContexts(Arrays.asList(securityContext()))
        	    .securitySchemes(Arrays.asList(apiKey()))
        		.select()                                  
        		.apis(RequestHandlerSelectors.any())              
        		.paths(PathSelectors.any())                          
        		.build();                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
        		"Competitive Event REST API", 
        		"List of endponits available for all the functionalities developed for the project.", 
        		"1.0.0",
        		"", 
        		new Contact("Jose Carlos Solis Lojo", "", "carlos.solislojo@gmail.com"),
        		"", "", List.of());
    }
    
    private SecurityContext securityContext() { 
        return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
    } 
    
    private List<SecurityReference> defaultAuth() { 
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
        authorizationScopes[0] = authorizationScope; 
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
    }
    
    private ApiKey apiKey() { 
        return new ApiKey("JWT", "Authorization", "header"); 
    }
}

