package org.sacumen.demo.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.RecordDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;

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
    private RecordDTO record;

    @Mock
    private TokenDTO token;

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

        when(record.getId()).thenReturn(recordID);
        when(token.getAccessToken()).thenReturn("test");

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

        InputStream inJson = AuthInfoDTO.class.getResourceAsStream("/auth.json");
        AuthInfoDTO authInfo = new ObjectMapper().readValue(inJson, AuthInfoDTO.class);
        TokenDTO token = salesForceClient.getAccessToken(authInfo);

        assertEquals("access_token", "00D2w000002iBes!AQkAQKuarCasF21YGlBfgaGkgYTKWErlI7qT4EvVKX4kSN66DrFCgdijTmKoGD_C2yYTX4XnTKWjCekscUkUywIWSi_XEiei", token.getAccessToken());

    }




}