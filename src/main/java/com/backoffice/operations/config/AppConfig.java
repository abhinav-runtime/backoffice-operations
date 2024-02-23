package com.backoffice.operations.config;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.io.IOException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import javax.net.ssl.HttpsURLConnection;


@Configuration
public class AppConfig {

	@Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
	
//	@Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

	private ClientHttpRequestFactory clientHttpRequestFactory() {
	    return new CustomSimpleClientHttpRequestFactory();
	}

	private static class CustomSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	    @Override
	    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
	        if (connection instanceof HttpsURLConnection) {
	            configureSslContext((HttpsURLConnection) connection);
	        }
	        super.prepareConnection(connection, httpMethod);
	    }
	}

	private static void configureSslContext(HttpsURLConnection connection) {
	    try {
	        // Load the SSL certificate from the file system
	        Resource resource = new ClassPathResource("aibtstactmq.alizislamic.download.crt");
	        CertificateFactory cf = CertificateFactory.getInstance("X.509");
	        Certificate ca;
	        try (InputStream caInput = resource.getInputStream()) {
	            ca = cf.generateCertificate(caInput);
	        }

	        // Create a KeyStore containing the trusted SSL certificate
	        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        keyStore.load(null, null);
	        keyStore.setCertificateEntry("ca", ca);

	        // Create a TrustManager that trusts the SSL certificate in the KeyStore
	        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        tmf.init(keyStore);
	        TrustManager[] trustManagers = tmf.getTrustManagers();

	        // Create an SSLContext that uses the TrustManager
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(null, trustManagers, null);

	        // Configure the SSL socket factory for the connection
	        connection.setSSLSocketFactory(sslContext.getSocketFactory());
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to configure SSL context", e);
	    }
	}
}

