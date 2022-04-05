package com.lillya.piemon.auth.user.data;

import com.lillya.piemon.auth.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "userDAO")
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUid(Integer uid);
}
