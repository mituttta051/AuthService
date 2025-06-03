package cybercooker.authservice.controller;

import cybercooker.authservice.dto.UserDTO;
import cybercooker.authservice.mapper.UserMapper;
import cybercooker.authservice.request.UserCreateRequest;
import cybercooker.authservice.request.UserUpdateRequest;
import cybercooker.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        UUID uuid = UUID.fromString(userId);
        UserDTO user = userService.getUserById(uuid);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateRequest user) {
        UserDTO createdUser = userService.createUser(UserMapper.INSTANCE.toUser(user));
        return ResponseEntity.status(201).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest user) {
        UserDTO updatedUser = userService.updateUser(userId, UserMapper.INSTANCE.toUser(user));
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
