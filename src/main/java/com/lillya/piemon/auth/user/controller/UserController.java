package com.lillya.piemon.auth.user.controller;

import com.lillya.piemon.auth.exception.UserAlreadyExistException;
import com.lillya.piemon.auth.user.data.UserDAO;
import com.lillya.piemon.auth.user.model.User;
import com.lillya.piemon.auth.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDAO userDAO;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService, UserDAO userDAO) {
        this.userDetailsService = userDetailsService;
        this.userDAO = userDAO;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity getUserInfo() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDAO.findByUsername(username);

        return ResponseEntity.ok().body(user);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody User user) {
        User ret = null;
        try {
            ret = userDetailsService.registerUser(user);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.ok().body(e.getMessage());
        }

        return ResponseEntity.ok().body(ret);
    }
}
