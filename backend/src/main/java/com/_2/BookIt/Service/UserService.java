package com._2.BookIt.Service;

import com._2.BookIt.Model.User;
import com._2.BookIt.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User updateUser(String id, User updatedUser) {
        User existing = getUserById(id);
        existing.setName(updatedUser.getName());
        existing.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(existing);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
