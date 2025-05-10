package com.example.starwars.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.starwars.service.StarWarsApiClient;
import com.fasterxml.jackson.databind.JsonNode;


@Service
public class StarWarsApiClientImpl implements StarWarsApiClient {

    private static final Logger logger = LoggerFactory.getLogger(StarWarsApiClientImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static void disableSslVerification() throws Exception {
         // Create a TrustManager that trusts all certificates
         TrustManager[] trustAllCertificates = new TrustManager[]{
             new X509TrustManager() {
                 public X509Certificate[] getAcceptedIssuers() {
                     return null;
                 }
 
                 public void checkClientTrusted(X509Certificate[] certs, String authType) {
                 }
 
                 public void checkServerTrusted(X509Certificate[] certs, String authType) {
                 }
             }
         };
 
         // Install the all-trusting trust manager
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init(null, trustAllCertificates, new java.security.SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
         // Disable hostname verification
         HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
     }
 
    @Override
    public JsonNode fetch(String endpointUrl) {
        try {
            disableSslVerification();
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            logger.info("HTTP Response: {}", responseCode);

            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    return objectMapper.readTree(response.toString());
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching data from: {}", endpointUrl, e);
        }
        return null;
    }
}
