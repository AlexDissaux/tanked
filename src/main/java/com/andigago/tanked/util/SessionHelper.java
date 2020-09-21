package com.andigago.tanked.util;

import com.andigago.tanked.models.User;

import javax.servlet.http.HttpServletRequest;

public class SessionHelper {

    public static boolean isConnected(HttpServletRequest request) {
        return (getUser(request) != null);
    }

    public static User getUser(HttpServletRequest request) {
         return (User) request.getSession().getAttribute("user");
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }
}
