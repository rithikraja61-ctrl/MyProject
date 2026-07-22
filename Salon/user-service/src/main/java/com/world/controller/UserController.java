package com.world.controller;

import com.world.exception.UserException;
import com.world.model.User;
import com.world.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user)
    {
        User createdUser=userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers()
    {
        List<User> users=userService.getUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long id) throws UserException {
       User user=userService.getUserById(id);
       return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @PutMapping("/api/users/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user,@PathVariable("userId") Long id) throws UserException
    {
        User updatedUser=userService.updateUser(user,id);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }
    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long id) throws UserException
    {
         String message=userService.deleteUser(id);
         return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }
}
