package com.example.mtlsserver;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class ClientTest {


    public static void main(String[] args) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        // Load client keystore (contains client certificate and private key)
        KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
        ClassPathResource keystoreResource = new ClassPathResource("client/client-keystore.p12");
        try (InputStream keystoreStream = keystoreResource.getInputStream()) {
            clientKeyStore.load(keystoreStream, "password".toCharArray());
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, "password".toCharArray());

        // Load custom cacerts
        KeyStore trustStore = KeyStore.getInstance("JKS");
        ClassPathResource customTrustStore = new ClassPathResource("client/cacerts");
        try (InputStream keystoreStream = customTrustStore.getInputStream()) {
            trustStore.load(keystoreStream, "changeit".toCharArray());
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Create SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        // Create HTTP client with custom SSL context
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                                .setSslContext(sslContext)
                                .build())
                        .build())
                .build();

        // Configure RestTemplate to use the custom HTTP client
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        String response = String.valueOf(restTemplate.getForEntity("https://localhost:8080/hello", String.class));
        System.out.printf("Response: %s%n", response);


    }
}
