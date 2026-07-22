package com.transitops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.transitops.config.CorsProperties;
import com.transitops.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({ JwtProperties.class, CorsProperties.class })
public class TransitOpsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransitOpsBackendApplication.class, args);
	}

}
