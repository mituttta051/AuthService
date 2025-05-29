package cybercooker.authservice.service;

import cybercooker.authservice.entity.User;

import java.util.UUID;

public interface UserService {

    User getUserById(UUID userId);

    User getUserByUsername(String username);

    User createUser(User user);

    User updateUser(UUID userId, User user);

    void deleteUserById(UUID userId);
}
