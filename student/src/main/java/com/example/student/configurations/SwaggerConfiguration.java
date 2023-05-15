package com.example.student.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui/index.html --Swagger URL,  use the port where the service is running
@Configuration
// @EnableSwagger2-  Old way
public class SwaggerConfiguration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .version("v1")
                .title("XApp application API")
                .description(
                    "(NOTE: If having Swagger UI issues w/ Chrome then use Firefox instead.)")
                .license(new License().name("My License"))
                .version("v16")
                .contact(new Contact().name("Edi")));
  }

  /*  Old way to configure swagger

  ApiInfo apiInfo = new ApiInfoBuilder()
            .title("XApp application API")
            .version("v1")
            .description("(NOTE: If having Swagger UI issues w/ Chrome then use Firefox instead.)")
            .license("My License")
            .licenseUrl("http://www.rakesh.com")
            .build();

    @Bean
    public Docket docket() {
      return new Docket(DocumentationType.SWAGGER_2)
              .select()
              .apis(RequestHandlerSelectors.basePackage("com.java.fillProject"))
              .paths(PathSelectors.any())
              .build()
              .apiInfo(apiInfo);
    }*/
}
