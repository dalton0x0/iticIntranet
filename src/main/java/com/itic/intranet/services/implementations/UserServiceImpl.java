package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Role;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public ApiResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        return ApiResponse.builder()
                .message("Liste des utilisateurs")
                .response(users.stream().map(this::convertToDto).toList())
                .build();
    }

    @Override
    public ApiResponse getAllActiveUsers() {
        List<User> activeUsers = userRepository.findByActive(true);
        return ApiResponse.builder()
                .message("Liste des utilisateurs actifs")
                .response(activeUsers.stream().map(this::convertToDto).toList())
                .build();
    }

    @Override
    public ApiResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        return ApiResponse.builder()
                .message("Utilisateur trouvé")
                .response(convertToDto(user))
                .build();
    }

    @Override
    public ApiResponse searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Recherche incorrecte");
        }

        List<User> users = userRepository
                .findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(keyword, keyword);

        return ApiResponse.builder()
                .message("Résultats de la recherche")
                .response(users.stream().map(this::convertToDto).toList())
                .build();
    }

    @Override
    public ApiResponse createUser(UserRequestDto userDto) {
        validateUserRequest(userDto);
        checkUniqueConstraints(userDto);

        User user = new User();
        mapDtoToEntity(userDto, user);
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return ApiResponse.builder()
                .message("Utilisateur créé avec succès")
                .response(convertToDto(savedUser))
                .build();
    }

    @Override
    public ApiResponse updateUser(Long id, UserRequestDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        validateUserRequest(userDto);
        mapDtoToEntity(userDto, user);

        User updatedUser = userRepository.save(user);
        return ApiResponse.builder()
                .message("Utilisateur mis à jour")
                .response(convertToDto(updatedUser))
                .build();
    }

    @Override
    public ApiResponse deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        if (!user.isActive()) {
            throw new BadRequestException("L'utilisateur est déjà désactivé");
        }

        user.setActive(false);
        userRepository.save(user);
        return ApiResponse.builder()
                .message("Utilisateur désactivé avec succès")
                .response(convertToDto(user))
                .build();
    }

    @Override
    public ApiResponse permanentlyDeleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé");
        }

        userRepository.deleteById(id);
        return ApiResponse.builder()
                .message("Utilisateur supprimé définitivement")
                .build();
    }

    private void validateUserRequest(UserRequestDto dto) {
        if (dto.getFirstname() == null || dto.getFirstname().trim().isEmpty()) {
            throw new BadRequestException("Le prénom est obligatoire");
        }
        if (dto.getLastname() == null || dto.getLastname().trim().isEmpty()) {
            throw new BadRequestException("Le nom est obligatoire");
        }
        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new BadRequestException("Email invalide");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Le nom d'utilisateur est obligatoire");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new BadRequestException("Le mot de passe doit contenir au moins 6 caractères");
        }
        if (dto.getRoleType() == null) {
            throw new BadRequestException("Le rôle est obligatoire");
        }
    }

    private void checkUniqueConstraints(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Cet email est déjà utilisé");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Ce nom d'utilisateur est déjà pris");
        }
    }

    private void mapDtoToEntity(UserRequestDto dto, User user) {
        user.setFirstname(dto.getFirstname().trim());
        user.setLastname(dto.getLastname().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());

        Role role = roleRepository.findByRoleType(dto.getRoleType())
                .orElseThrow(() -> new BadRequestException("Rôle invalide"));
        user.setRole(role);
    }

    private UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .username(user.getUsername())
                .roleType(user.getRole().getRoleType())
                .active(user.isActive())
                .build();
    }
}