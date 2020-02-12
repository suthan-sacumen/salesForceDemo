package org.sacumen.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sacumen.demo.dto.AuthInfoDTO;
import org.sacumen.demo.dto.EventLogDTO;
import org.sacumen.demo.dto.TokenDTO;
import org.sacumen.demo.service.SalesForceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@RestController
public class SalesforceController {

    private static final Logger log = LoggerFactory.getLogger(SalesforceController.class);

    @Autowired
    private SalesForceClient salesForceClient;

    @RequestMapping(value = "/services/oauth2/token", method = RequestMethod.POST)
    @ResponseBody
    public TokenDTO getToken() throws IOException, URISyntaxException {

        InputStream inJson = AuthInfoDTO.class.getResourceAsStream("/auth.json");
        AuthInfoDTO authInfo = new ObjectMapper().readValue(inJson, AuthInfoDTO.class);
        TokenDTO token = salesForceClient.getAccessToken(authInfo);
        return token;
    }

    @RequestMapping(value = "/services/data/EventLogFile", method = RequestMethod.GET)
    @ResponseBody
    public String getEventLogFile() throws IOException, URISyntaxException {

        InputStream inJson = AuthInfoDTO.class.getResourceAsStream("/auth.json");
        AuthInfoDTO authInfo = new ObjectMapper().readValue(inJson, AuthInfoDTO.class);
        TokenDTO token = salesForceClient.getAccessToken(authInfo);

        EventLogDTO eventLog = salesForceClient.getEventLog(token);

        log.info("Sending File Response...");
        return salesForceClient.getEventLogFileById(token, eventLog.getRecords().get(0));

    }

}