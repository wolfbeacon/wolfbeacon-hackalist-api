package com.wolfbeacon.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SecuredPingTest {

    @RequestMapping("/api/v1/secured-ping")
    public String getHackathons(HttpServletRequest request, HttpServletResponse response) {
        return "This is a secured endpoint. You have a valid token!";
    }
}