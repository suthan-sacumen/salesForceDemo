package org.sacumen.demo.service;


import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.sacumen.demo.SalesForceDemo;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.EventLogDTO;
import org.sacumen.demo.dto.RecordDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.springframework.test.util.ReflectionTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class SalesForceClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(
            wireMockConfig().dynamicPort()
                    .notifier(new ConsoleNotifier(true))
    );

    @Mock
    private CloseableHttpResponse queryResponse;

    @Mock
    private HttpEntity entity;

    @InjectMocks
    private SalesForceClient salesForceClient;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        ReflectionTestUtils.setField(salesForceClient, "salesForceAppURL", "http://localhost:" + wireMockRule.port());
    }

    @Test
    public void testErrorResponse() throws Exception {

        String recordID = "0AT2w00000C49E4GAJ";
        RecordDTO record = new RecordDTO();
        record.setId(recordID);

        TokenDTO token = new TokenDTO();
        token.setAccessToken("test");

        wireMockRule.stubFor(get(urlPathMatching("/services/data/v47.0/sobjects/EventLogFile/" + recordID + "/LogFile"))
                .willReturn(aResponse().withStatus(404).withBody("error")));
        salesForceClient.getEventLogFileById(token, record);
    }

    @Test
    public void testResponse() throws Exception {

        String shortResponse = "{" +
                "    \"access_token\": \"00D2w000002iBes!AQkAQKuarCasF21YGlBfgaGkgYTKWErlI7qT4EvVKX4kSN66DrFCgdijTmKoGD_C2yYTX4XnTKWjCekscUkUywIWSi_XEiei\"," +
                "    \"instance_url\": \"https://ap16.salesforce.com\"," +
                "    \"id\": \"https://login.salesforce.com/id/00D2w000002iBesEAE/0052w000001kcevAAA\"," +
                "    \"token_type\": \"Bearer\"," +
                "    \"issued_at\": \"1581320198245\"," +
                "    \"signature\": \"jJAmzIYOyBRZH4O5FUf4BXMsrfIWIb7MqRAt/RwCpwY=\"" +
                "}";

        wireMockRule.stubFor(post(urlPathMatching("/services/oauth2/token"))
                .willReturn(aResponse().withStatus(200).withBody(shortResponse)));

        AuthInfoDTO authInfo = new AuthInfoDTO();
        TokenDTO token = salesForceClient.getAccessToken(authInfo);

        assertEquals("access_token", "00D2w000002iBes!AQkAQKuarCasF21YGlBfgaGkgYTKWErlI7qT4EvVKX4kSN66DrFCgdijTmKoGD_C2yYTX4XnTKWjCekscUkUywIWSi_XEiei", token.getAccessToken());

    }


    @Test
    public void testgetEventLog() throws Exception {
        CloseableHttpClient client = Mockito.mock(CloseableHttpClient.class);
        ReflectionTestUtils.setField(salesForceClient, "client", client);
        TokenDTO token = new TokenDTO();
        token.setAccessToken("test");
        when(entity.getContent()).thenReturn(SalesForceDemo.class.getResourceAsStream("/eventQuerry.json"));
        when(queryResponse.getEntity()).thenReturn(entity);
        when(client.execute(ArgumentMatchers.any())).thenReturn(queryResponse);

        EventLogDTO eventlog = salesForceClient.getEventLog(token);
        assertEquals("Total Size", 1, eventlog.getTotalSize());
    }



}