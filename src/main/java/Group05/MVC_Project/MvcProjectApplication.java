package Group05.MVC_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="Group05.MVC_Project.controllers")
@ComponentScan(basePackages="Group05.MVC_Project.models")
@ComponentScan(basePackages="Group05.MVC_Project.utils")
@ComponentScan(basePackages="Group05.MVC_Project.repositories")
public class MvcProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcProjectApplication.class, args);
	}

}
