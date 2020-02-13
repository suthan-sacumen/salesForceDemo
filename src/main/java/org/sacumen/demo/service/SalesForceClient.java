package org.sacumen.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.EventLogDTO;
import org.sacumen.demo.dto.RecordDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Component
public class SalesForceClient {

    private static final Logger log = LoggerFactory.getLogger(SalesForceClient.class);

    private CloseableHttpClient client = HttpClients.createDefault();

    @Autowired
    private String salesForceAppURL;

    private ObjectMapper mapper = new ObjectMapper();

    public TokenDTO getAccessToken(AuthInfoDTO authInfo) throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder(salesForceAppURL);
        builder.setPath("/services/oauth2/token");
        builder.setParameter("grant_type", authInfo.getGrant_type());
        builder.setParameter("client_id", authInfo.getClient_id());
        builder.setParameter("client_secret", authInfo.getClient_secret());
        builder.setParameter("username", authInfo.getUsername());
        builder.setParameter("password", authInfo.getPassword());

        HttpPost post = new HttpPost(builder.build());

        HttpResponse queryResponse = client.execute(post);

        TokenDTO token = mapper.readValue(queryResponse.getEntity().getContent(), TokenDTO.class);

        log.info("New Token SuccessFully addded {} ", token);

        return token;
    }

    public EventLogDTO getEventLog(TokenDTO token) throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder(salesForceAppURL);
        builder.setPath("/services/data/v47.0/query");
        builder.setParameter("q", "SELECT Id , EventType from EventLogFile where EventType ='Login'");

        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + token.getAccessToken());

        HttpResponse queryResponse = client.execute(get);

        EventLogDTO eventLog = mapper.readValue(queryResponse.getEntity().getContent(), EventLogDTO.class);

        log.info("Event Log retrieved {} ", eventLog);

        return eventLog;
    }

    public String getEventLogFileById(TokenDTO token, RecordDTO record) throws URISyntaxException, IOException {
        String eventLogFile;

        URIBuilder builder = new URIBuilder(salesForceAppURL);
        builder.setPath("/services/data/v47.0/sobjects/EventLogFile/" + record.getId() + "/LogFile");
        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + token.getAccessToken());
        HttpResponse queryResponse = client.execute(get);

        eventLogFile = IOUtils.toString(queryResponse.getEntity().getContent(), StandardCharsets.UTF_8.name());

        log.info("Get Events File SuccessFully {}", eventLogFile);

        return eventLogFile;
    }

    public String getSalesForceAppURL() {
        return salesForceAppURL;
    }

    public void setSalesForceAppURL(String salesForceAppURL) {
        this.salesForceAppURL = salesForceAppURL;
    }
}
