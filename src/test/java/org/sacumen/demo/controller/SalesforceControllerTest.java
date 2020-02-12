package org.sacumen.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.sacumen.demo.service.SalesForceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(SalesforceController.class)
public class SalesforceControllerTest {

    private MockMvc mvc;

    @Mock
    private SalesForceClient salesForceClient;

    @InjectMocks
    private SalesforceController salesforceController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(salesforceController).build();
    }


    @Test
    public void testGetToken() throws Exception {

        InputStream inJson = AuthInfoDTO.class.getResourceAsStream("/auth.json");
        AuthInfoDTO authInfo = new ObjectMapper().readValue(inJson, AuthInfoDTO.class);
        TokenDTO token = new TokenDTO();
        token.setAccessToken("TestTokenString");

        when(salesForceClient.getAccessToken(authInfo)).thenReturn(token);

        mvc.perform( MockMvcRequestBuilders
                .post("/services/oauth2/token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").value("TestTokenString"));

        verify(salesForceClient, times(1)).getAccessToken(authInfo);
        verifyNoMoreInteractions(salesForceClient);

    }

    @Test
    public void testGetTokenWithException() throws Exception {

        InputStream inJson = AuthInfoDTO.class.getResourceAsStream("/auth.json");
        AuthInfoDTO authInfo = new ObjectMapper().readValue(inJson, AuthInfoDTO.class);

        when(salesForceClient.getAccessToken(authInfo)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> salesforceController.getToken());
        verify(salesForceClient, times(1)).getAccessToken(authInfo);
        verifyNoMoreInteractions(salesForceClient);

    }

}