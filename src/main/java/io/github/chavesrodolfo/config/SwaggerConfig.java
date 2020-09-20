package io.github.chavesrodolfo.config;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
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
	public Docket apiVersionDoc() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("api/v1").select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/v1/**")).build().apiInfo(apiInfo())
			//	.securityContexts(Arrays.asList(actuatorSecurityContext(), apiSecurityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Money Box REST API")
				.description("\"Documentation of Money Box API\"").version("1.0.0")
				.license("MIT License").licenseUrl("https://opensource.org/licenses/MIT")
				.contact(new Contact("Rodolfo", "http://github.com/chavesrodolfo", "chavesrodolfo@gmail.com")).build();
	}

	private SecurityContext actuatorSecurityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.ant("/actuator/**")).build();
	}

	private SecurityContext apiSecurityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.ant("/api/v1/**")).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

}