package cybercooker.authservice.service;

import cybercooker.authservice.dto.UserDTO;
import cybercooker.authservice.entity.User;
import cybercooker.authservice.exception.AlreadyExistsException;
import cybercooker.authservice.exception.NotFoundException;
import cybercooker.authservice.exception.details.DatabaseDetails;
import cybercooker.authservice.mapper.UserMapper;
import cybercooker.authservice.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserMapper userMapper = UserMapper.INSTANCE;
    @Autowired
    private UserRepository userRepository;
    
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(new DatabaseDetails("User with id " + userId + " not found")));
        return userMapper.toUserDTO(user);
    }
    
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new DatabaseDetails("User with username " + username + " not found")));
        return userMapper.toUserDTO(user);
    }
    
    public UserDTO createUser(User user) {
        createCheck(user);
        user.setHashedPassword(BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }
    
    public UserDTO updateUser(User user) {
        updateCheck(user);
        User updatedUser = userRepository.findById(user.getId())
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setHashedPassword(BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt()));
                    existingUser.setEmail(user.getEmail());
                    existingUser.setName(user.getName());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new NotFoundException(new DatabaseDetails("User with id " + user.getId() + " not found")));
        return userMapper.toUserDTO(updatedUser);
    }
    
    public void deleteUserById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(new DatabaseDetails("User with id " + userId + " not found"));
        }
        userRepository.deleteById(userId);
    }

    private void createCheck(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new AlreadyExistsException(new DatabaseDetails("Username is already in use"));
                });

        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new AlreadyExistsException(new DatabaseDetails("Email is already in use"));
                });
    }

    private void updateCheck(User user) {
        userRepository.findByUsername(user.getUsername())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> {
                    throw new AlreadyExistsException(new DatabaseDetails("Username is already in use"));
                });

        userRepository.findByEmail(user.getEmail())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> {
                    throw new AlreadyExistsException(new DatabaseDetails("Email is already in use"));
                });
    }


}