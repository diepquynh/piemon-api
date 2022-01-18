package com.lillya.piemon.auth.user.controller;

import com.lillya.piemon.auth.exception.UserAlreadyExistException;
import com.lillya.piemon.auth.user.model.User;
import com.lillya.piemon.auth.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
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
