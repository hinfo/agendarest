package br.com.magazine.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket magazineApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.magazine"))
				.paths(regex("/api.*"))
				.build()
				.apiInfo(metaInfo());
	}
	
	private ApiInfo metaInfo() {
		ApiInfo apiInfo = new ApiInfo(//null, null, null, null, null, null, null);
				"Magazine Luiza API REST",
				"API REST para Agendamento de salas.",
				"1.0",
				"Terms of Service",
				new Contact("Henrique Antunes", "https://henriqueantunes.herokuap.com", "hinfo@gmx.com"),
				"Apache Licence Version 2.0",
				"https://www.apache.org/licence.html", new ArrayList<VendorExtension>()
				);
		return apiInfo;
	}
}
