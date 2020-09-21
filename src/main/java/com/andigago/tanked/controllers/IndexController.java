package com.andigago.tanked.controllers;

import com.andigago.tanked.models.User;
import com.andigago.tanked.util.SessionHelper;
import com.andigago.tanked.websockets.objects.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        User user = SessionHelper.getUser(request);
        boolean isConnected = user != null;

        if (isConnected) {
            String username = user.getUsername();
            model.addAttribute("username", username);
        }

        String tankName = (String) request.getSession().getAttribute("tankName");
        if(tankName == null) {
            tankName = "Guest";
        }

        model.addAttribute("tankName", tankName);
        model.addAttribute("games", gameRepository.getGames());
        model.addAttribute("isConnected", isConnected);

        return "index";

    }


}
