package com.andigago.tanked.controllers.rest;

import com.andigago.tanked.models.Map;
import com.andigago.tanked.models.MapRepository;
import com.andigago.tanked.models.User;
import com.andigago.tanked.util.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SaveMapController {
    @Autowired
    private MapRepository mapRepository;

    @PostMapping("/save-map")
    public int saveMap(HttpServletRequest request, @RequestBody Map map ) {
        User user = SessionHelper.getUser(request);
        if(user != null) {
            map.setOwner(user);
        }
        Map res = mapRepository.save(map);
        return res.getId();
    }

}
