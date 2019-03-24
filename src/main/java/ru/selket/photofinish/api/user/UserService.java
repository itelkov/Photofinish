package ru.selket.photofinish.api.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User get(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public void set(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
