package org.sacumen.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.TokenDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesForceClient {

    private static final Logger log = LoggerFactory.getLogger(SalesForceClient.class);

    public TokenDTO getAccessToken(AuthInfoDTO authInfo) throws IOException, URISyntaxException {

        CloseableHttpClient client = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();

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

}
