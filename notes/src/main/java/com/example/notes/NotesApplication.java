package com.example.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesApplication.class, args);
	}

}
// FILENAME: NotesApplication.java
// this is the main class of the application, it is responsible for starting the application. It is annotated with @SpringBootApplication,
// which is a convenience annotation that adds all of the following:
// @Configuration: Tags the class as a source of bean definitions for the application context.
// @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings
// @ComponentScan: Tells Spring to look for other components, configurations, and services in the com/example/notes package, allowing it to find the controllers.
// The main method uses SpringApplication.run() to launch the application.
// This method sets up the default configuration, starts the Spring application context,
// and performs a class path scan.