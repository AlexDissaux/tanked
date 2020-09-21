package com.andigago.tanked.controllers;

import com.andigago.tanked.models.MapRepository;
import com.andigago.tanked.models.User;
import com.andigago.tanked.models.UserRepository;
import com.andigago.tanked.util.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@Transactional
public class ProfilController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapRepository mapRepository;

    @GetMapping("/user/{username}")
    public String profilUser(@PathVariable String username, HttpServletRequest request, Model model) {
        User user = userRepository.findByUsername(username);

        if(user == null) {
            return "redirect:/";
        } else {
            User connectedUser = SessionHelper.getUser(request);
            model.addAttribute("user", user);
            model.addAttribute("connectedUser", connectedUser);
            return "profil";
        }
    }

    @PostMapping("/user/{username}")
    public String deleteMap(@PathVariable String username, HttpServletRequest request, @RequestParam int mapId, Model model) {
        User connectedUser = SessionHelper.getUser(request);

        if(connectedUser != null) {
            mapRepository.deleteByIdAndOwnerId(mapId, connectedUser.getId());
        }

        return profilUser(username, request, model);
    }

}
