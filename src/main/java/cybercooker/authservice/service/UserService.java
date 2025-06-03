package cybercooker.authservice.service;

import cybercooker.authservice.dto.UserDTO;
import cybercooker.authservice.entity.User;

import java.util.UUID;

public interface UserService {

    UserDTO getUserById(UUID userId);

    UserDTO getUserByUsername(String username);

    UserDTO createUser(User user);

    UserDTO updateUser(UUID userId, User user);

    void deleteUserById(UUID userId);
}
