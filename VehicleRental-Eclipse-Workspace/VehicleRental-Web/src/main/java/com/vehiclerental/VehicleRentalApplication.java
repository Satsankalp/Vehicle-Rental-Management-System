package com.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Entry point. Extends SpringBootServletInitializer so the packaged WAR can be
 * deployed to an external servlet container (Eclipse Dynamic Web Project /
 * "Run on Server") as well as run standalone via the embedded Tomcat:
 *   ./mvnw spring-boot:run   or   java -jar target/vehicle-rental-*.war
 */
@SpringBootApplication
public class VehicleRentalApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VehicleRentalApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentalApplication.class, args);
	}

}
