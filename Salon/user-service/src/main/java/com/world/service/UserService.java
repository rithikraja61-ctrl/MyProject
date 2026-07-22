package com.world.service;

import com.world.exception.UserException;
import com.world.model.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {
    public User createUser(User user);
    public List<User> getUsers();
    public User getUserById(Long id) throws UserException;
    public User updateUser(User user,Long id) throws UserException;
    public String deleteUser(Long id) throws UserException;
}
