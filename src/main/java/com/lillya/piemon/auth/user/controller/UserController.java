package com.lillya.piemon.auth.user.controller;

import com.lillya.piemon.auth.exception.UserAlreadyExistException;
import com.lillya.piemon.auth.user.data.UserDAO;
import com.lillya.piemon.auth.user.model.User;
import com.lillya.piemon.auth.user.service.OAuth2UserServiceImpl;
import com.lillya.piemon.auth.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDAO userDAO;

    @Autowired
    public UserController(OAuth2UserServiceImpl oAuth2UserService, UserDetailsServiceImpl userDetailsService, UserDAO userDAO) {
        this.oAuth2UserService = oAuth2UserService;
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
        try {
            userDetailsService.registerUser(user);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @RequestMapping(value = "/login/oauth", method = RequestMethod.POST)
    public ResponseEntity oAuthLogin(@RequestParam("token") String idToken)
            throws GeneralSecurityException, IOException {
        String newToken = oAuth2UserService.processToken(idToken);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newToken)
                .body(null);
    }
}
