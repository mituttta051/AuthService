package cybercooker.authservice.service;

import cybercooker.authservice.entity.User;
import cybercooker.authservice.exception.AlreadyExistsException;
import cybercooker.authservice.exception.NotFoundException;
import cybercooker.authservice.exception.details.DatabaseDetails;
import cybercooker.authservice.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(new DatabaseDetails("User with id " + userId + " not found")));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new DatabaseDetails("User with username " + username + " not found")));
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyExistsException(new DatabaseDetails("User with username " + user.getUsername() + " already exists"));
        }
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        user.setHashedPassword(BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UUID userId, User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() &&
                !userRepository.findByUsername(user.getUsername()).get().getId().equals(userId)) {
            throw new AlreadyExistsException(new DatabaseDetails("User with username " + user.getUsername() + " already exists"));
        }
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setHashedPassword(BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt()));
                    existingUser.setEmail(user.getEmail());
                    existingUser.setName(user.getName());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new NotFoundException(new DatabaseDetails("User with id " + userId + " not found")));
    }

    @Override
    public void deleteUserById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(new DatabaseDetails("User with id " + userId + " not found"));
        }
        userRepository.deleteById(userId);

    }
}
