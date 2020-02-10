package org.sacumen.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.EventLogDTO;
import org.sacumen.demo.dto.RecordDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SalesForceClient {

    private static final Logger log = LoggerFactory.getLogger(SalesForceClient.class);

    private CloseableHttpClient client;
    private ObjectMapper mapper = new ObjectMapper();

    public TokenDTO getAccessToken(AuthInfoDTO authInfo) throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder("https://ap15.salesforce.com");
        builder.setPath("/services/oauth2/token");

        HttpPost post = new HttpPost(builder.build());
        List<BasicNameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", authInfo.getGrant_type()));
        urlParameters.add(new BasicNameValuePair("client_id", authInfo.getClient_id()));
        urlParameters.add(new BasicNameValuePair("client_secret", authInfo.getClient_secret()));
        urlParameters.add(new BasicNameValuePair("username", authInfo.getUsername()));
        urlParameters.add(new BasicNameValuePair("password", authInfo.getPassword()));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse queryResponse = client.execute(post);

        TokenDTO token = mapper.readValue(queryResponse.getEntity().getContent(), TokenDTO.class);

        log.info("New Token SuccessFully addded {} ", token);

        return token;
    }

    public EventLogDTO getEventLog(TokenDTO token) throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder("https://ap15.salesforce.com");
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

        URIBuilder builder = new URIBuilder("https://ap15.salesforce.com");
        builder.setPath("/services/data/v47.0/sobjects/EventLogFile/"+record.getId()+"/LogFile");
        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + token.getAccessToken());
        HttpResponse queryResponse = client.execute(get);

        eventLogFile = convertInputStreamToString(queryResponse.getEntity().getContent());

        log.info("Get Events File SuccessFully {}", eventLogFile);

        return eventLogFile;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());

    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }
}
