package br.com.magazine;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MagazineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagazineApplication.class, args);
		
		PropertyConfigurator.configure(MagazineApplication.class.getClassLoader()
				.getResourceAsStream("log4j.properties"));
		
	}

}

