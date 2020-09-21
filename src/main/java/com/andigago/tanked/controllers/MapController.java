package com.andigago.tanked.controllers;

import com.andigago.tanked.models.Map;
import com.andigago.tanked.models.MapRepository;
import com.andigago.tanked.models.User;
import com.andigago.tanked.util.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

import static com.andigago.tanked.util.SessionHelper.isConnected;

@Controller
public class MapController {

    @Autowired
    private MapRepository mapRepository;

    @GetMapping("/create-map")
    public String createMap(Model model, HttpServletRequest request) {
        boolean connected = isConnected(request);
        model.addAttribute("isConnected", connected);
        if (connected) {
            User user = (User) request.getSession().getAttribute("user");
            String username = user.getUsername();
            model.addAttribute("username", username);
        }
        return "map";
    }

    @GetMapping("/edit-map/{mapId}")
    public String editMap(@PathVariable int mapId, Model model, HttpServletRequest request) {
        User user = SessionHelper.getUser(request);

        if(user == null) {
            return "redirect:/";
        } else {
            Map map = mapRepository.findByIdAndOwnerId(mapId, user.getId());
            if(map == null) {
                return "redirect:/";
            } else {
                model.addAttribute("map", map);
                return "map";
            }
        }
    }
}
