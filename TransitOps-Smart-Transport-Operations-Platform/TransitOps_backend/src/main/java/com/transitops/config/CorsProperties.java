package com.transitops.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

	private String allowedOrigins = "http://localhost:5173,http://127.0.0.1:5173";
}
