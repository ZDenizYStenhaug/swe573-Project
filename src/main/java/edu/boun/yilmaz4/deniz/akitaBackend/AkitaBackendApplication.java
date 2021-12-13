package edu.boun.yilmaz4.deniz.akitaBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication
public class AkitaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkitaBackendApplication.class, args);
	}

}
