package com.world.service.impl;

import com.world.exception.UserException;
import com.world.model.User;
import com.world.repository.UserRepository;
import com.world.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isPresent())
        {
            return otp.get();
        }
        throw new UserException("User Id Not Found"+id);
    }

    @Override
    public User updateUser(User user, Long id) throws UserException {

        Optional<User> otp=userRepository.findById(id);
        if(otp.isEmpty())
        {
            throw new UserException("User Id Not Found");
        }
        User existingUser=otp.get();
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setFullName(user.getFullName());
        return userRepository.save(existingUser);
    }

    @Override
    public String deleteUser(Long id) throws UserException {

        Optional<User> otp=userRepository.findById(id);
        if(otp.isEmpty())
        {
            throw new UserException("User Id Not Found"+id);
        }
        userRepository.deleteById(otp.get().getId());
        return "User Deleted Successfully";
    }
}
