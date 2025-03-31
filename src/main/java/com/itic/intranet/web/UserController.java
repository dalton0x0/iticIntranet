package com.itic.intranet.web;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.models.User;
import com.itic.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v9/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsers(keyword);
        return users.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestDto userDto) {
        User user = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userDto) {
        if(userDto == null) {
            return ResponseEntity.badRequest().build();
        }
        User updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> permanentlyDeleteUser(@PathVariable Long id) {
        userService.permanentlyDeleteUser(id);
        return ResponseEntity.ok().build();
    }
}