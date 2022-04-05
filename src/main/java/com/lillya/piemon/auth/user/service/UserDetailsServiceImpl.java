package com.lillya.piemon.auth.user.service;

import com.lillya.piemon.auth.authorities.data.RoleRepository;
import com.lillya.piemon.auth.exception.UserAlreadyExistException;
import com.lillya.piemon.auth.user.data.UserDAO;
import com.lillya.piemon.auth.user.model.User;
import com.lillya.piemon.auth.user.model.UserDetailsImpl;
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

    @Autowired
    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserDAO userDAO) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);

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
}
