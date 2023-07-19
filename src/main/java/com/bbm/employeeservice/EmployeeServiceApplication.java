package com.bbm.employeeservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition (
		info = @Info(
				title = "Employee Management System - API",
				description = "Uma Api para gestão de funcionários numa empresa",
				version = "1.0",
				contact = @Contact(
						name = "Belmiro Mungoi",
						url = "https://github.com/BelmiroMungoi",
						email = "belmiromungoi@gmail.com"
				),
				license = @License(
						name = "Apache License 2.0",
						url = "https://www.apache.org/licenses/"
				)
		)
)
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
