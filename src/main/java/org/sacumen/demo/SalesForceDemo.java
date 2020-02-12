package org.sacumen.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClients;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.EventLogDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.sacumen.demo.service.SalesForceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@SpringBootApplication
public class SalesForceDemo {

    @Bean
    public  String salesForceAppURL() {
        return "https://ap15.salesforce.com";
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        //SpringApplication.run(SalesForceDemo.class, args);

        InputStream inJson = AuthInfoDTO.class.getResourceAsStream("/auth.json");
        AuthInfoDTO authInfo = new ObjectMapper().readValue(inJson, AuthInfoDTO.class);

        SalesForceClient salesForceClient = new SalesForceClient();
        salesForceClient.setSalesForceAppURL("https://ap15.salesforce.com");

        TokenDTO token = salesForceClient.getAccessToken(authInfo);

        EventLogDTO eventLog = salesForceClient.getEventLog(token);

        salesForceClient.getEventLogFileById(token, eventLog.getRecords().get(0));

    }


}