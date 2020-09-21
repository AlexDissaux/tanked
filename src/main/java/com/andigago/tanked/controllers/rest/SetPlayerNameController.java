package com.andigago.tanked.controllers.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SetPlayerNameController {
    @PostMapping("/set-tank-name")
    public String setPlayerName(HttpServletRequest request, @RequestParam("tankName") String tankName) {
        request.getSession().setAttribute("tankName", tankName);
        return "ok";
    }
}
