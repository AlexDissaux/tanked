package com.andigago.tanked.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by eisti on 18/12/18.
 */

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String login(HttpServletRequest request) {
        String page;
        if (request.getSession().getAttribute("user") == null)
            page = "redirect:/";
        else
            page = "logout";
        HttpSession session = request.getSession();
        session.invalidate();
        return (page);

    }
}
