package com.lillya.piemon.auth.user.service;

import com.lillya.piemon.auth.authorities.data.RoleRepository;
import com.lillya.piemon.auth.exception.UserAlreadyExistException;
import com.lillya.piemon.auth.user.data.UserDAO;
import com.lillya.piemon.auth.user.model.User;
import com.lillya.piemon.auth.user.model.UserDetailsImpl;
import com.lillya.piemon.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserDAO userDAO;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                                  UserDAO userDAO, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDAO = userDAO;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user;

        if (username.contains("@"))
            user = userDAO.findByEmail(username);
        else
            user = userDAO.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Could not find user");

        return new UserDetailsImpl(user);
    }

    public void registerUser(User user) {
        boolean usernameExist = userDAO.findByUsername(user.getUsername()) != null;
        if (usernameExist)
            throw new UserAlreadyExistException("Username exist!");

        boolean emailExist = userDAO.findByEmail(user.getEmail()) != null;
        if (emailExist)
            throw new UserAlreadyExistException("Email exist!");

        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userDAO.save(user);
    }

    public String updateUser(User newUser) {
        // This is never null
        User oldUser = userDAO.findById(newUser.getId()).get();

        if (newUser.getUsername() != null) {
            boolean usernameExist = userDAO.findByUsername(newUser.getUsername()) != null;
            if (usernameExist)
                throw new UserAlreadyExistException("Username exist!");
            oldUser.setUsername(newUser.getUsername());
        }

        if (newUser.getEmail() != null) {
            boolean emailExist = userDAO.findByEmail(newUser.getEmail()) != null;
            if (emailExist)
                throw new UserAlreadyExistException("Email exist!");
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getUid() != null) {
            boolean uidExist = userDAO.findByUid(newUser.getUid()) != null;
            if (uidExist)
                throw new UserAlreadyExistException("UID exist!");
            oldUser.setUid(newUser.getUid());
        }

        if (newUser.getPassword() != null)
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userDAO.save(oldUser);
        UserDetailsImpl userDetails = new UserDetailsImpl(oldUser);
        return jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }
}
