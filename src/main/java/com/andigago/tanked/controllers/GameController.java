package com.andigago.tanked.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GameController {
    @GetMapping("/game/{gameId}")
    public String gameRoom(@PathVariable String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "game";
    }
}
