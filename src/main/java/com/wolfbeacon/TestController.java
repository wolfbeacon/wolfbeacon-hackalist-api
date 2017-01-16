package com.wolfbeacon;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = "/test")
    public String ping() {
        return "Hello. My name is Ralph. I created WolfBeacon. I am cool. Oh wait, you don't put real data in test...";
    }

}