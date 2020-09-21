package com.andigago.tanked.controllers;

import com.andigago.tanked.models.UserRepository;
import com.andigago.tanked.models.User;
import com.andigago.tanked.util.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created by eisti on 26/11/18.
 */
@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return (SessionHelper.isConnected(request)) ? "redirect:/" : "login";
    }

    @PostMapping("/login")
    public String onFormSubmit(HttpServletRequest request, Model model) {
        if(request.getParameter("login") != null) {
            User user = getUser(request.getParameter("username"), request.getParameter("password"));
            if(user != null) {
                SessionHelper.setUser(request,user);

                if(request.getSession().getAttribute("tankName") == null)
                    request.getSession().setAttribute("tankName",user.getUsername());
                return "redirect:/";
            } else {
                model.addAttribute("error","User not found");
            }
        } else if(request.getParameter("register") != null) {
            User userPotential = getUsername(request.getParameter("username"));
            if (userPotential == null) {
                if(Objects.equals(request.getParameter("password"), request.getParameter("password2"))) {
                    createUser(request.getParameter("username"), request.getParameter("password"));
                } else {
                    model.addAttribute("error", "Impossible de s'enregistrer");
                }
            } else {
                model.addAttribute("error", "Nom d'utilisateur déjà existant");
            }

        }

        return "login";
    }

    private User getUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    private User getUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void createUser(String username, String password) {
        User user = new User(username, password);
        userRepository.save(user);
    }
}
