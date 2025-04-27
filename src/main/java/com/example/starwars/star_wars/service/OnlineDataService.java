package com.example.starwars.star_wars.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.starwars.star_wars.Constants;
import com.example.starwars.star_wars.model.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.ipc.http.HttpSender.Response;


@Service
public class OnlineDataService {
    @Autowired
    private RestTemplate restTemplate;
    Logger logger = Logger.getLogger("OnlineDataService.class");


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

    public SearchResult fetchData(String type, String name) {
        try {
            // // Construct the URL with parameters
            String httpsUrl = "https://swapi.dev/api/"+type+"/?search="+name+"&format=json";
            logger.info("Service Class URL : "+ httpsUrl);
            disableSslVerification();
            URL url = new URL(httpsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            int responseCode = connection.getResponseCode();
            logger.info("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {  // 200 OK
            // Read the response body
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String jsonResponse = response.toString();
                // Convert JSON response to Java object using Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonResponse);

                int count = rootNode.path("count").asInt();
                JsonNode resultsNode = rootNode.path("results");

                if (resultsNode.isArray() && resultsNode.size() > 0) {
                    JsonNode starshipNode = resultsNode.get(0);  // Assuming we are only interested in the first result
                    String nameField = starshipNode.path("name").asText();
                    List<String> films = List.of(starshipNode.path("films").get(0).asText());  // Assuming the first film URL

                    // Create a SearchResult object
                    SearchResult result = new SearchResult(type, nameField, films.get(0)); // Only map first film URL
                    result.setCount(count);

                    return result;
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Request failed, Response Code: " + responseCode);
        }
   
            
        } catch (Exception e) {
            // Handle exceptions (e.g., connection issues, invalid API response)
            e.printStackTrace();
        }

        return new SearchResult(type, name, "No Films Found");
    }


}
