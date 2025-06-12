package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.security.CustomPasswordEncoder;
import com.itic.intranet.services.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EntityHelper entityHelper;

    @Mock
    private CustomPasswordEncoder customPasswordEncoder;

    @Mock
    private LogService logService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {

        role = new Role();
        role.setId(1L);
        role.setRoleType(RoleType.STUDENT);

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("encodedPassword")
                .role(role)
                .active(true)
                .build();

        userRequestDto = new UserRequestDto();
        userRequestDto.setId(1L);
        userRequestDto.setFirstName("John");
        userRequestDto.setLastName("Doe");
        userRequestDto.setEmail("john.doe@example.com");
        userRequestDto.setUsername("johnDoe");
        userRequestDto.setPassword("Password123!");
        userRequestDto.setRoleType(RoleType.STUDENT);

        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .roleType(RoleType.STUDENT)
                .active(true)
                .build();
    }

    @Test
    void testGetAllUsers_shouldReturnUserList() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(userResponseDto);

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(logService).info(eq("SYSTEM"), eq("GET_ALL_USERS"), anyString(), anyMap());
    }

    @Test
    void testGetActiveUsers_shouldReturnActiveUsers() {
        List<User> users = List.of(user);
        when(userRepository.findByActive(true)).thenReturn(users);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(userResponseDto);

        List<UserResponseDto> result = userService.getAllActiveUsers();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(logService).info(eq("SYSTEM"), eq("GET_ALL_ACTIVE_USERS"), anyString(), anyMap());
    }

    @Test
    void testGetUserById_shouldReturnUser() {
        when(entityHelper.getUser(1L)).thenReturn(user);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.getUserById(1L);

        assertEquals("John", result.getFirstName());
        verify(logService).info(eq("SYSTEM"), eq("GET_USER"), anyString(), anyMap());
    }

    @Test
    void testSearchUser_shouldReturnMatchingUsers() {
        List<User> users = List.of(user);
        when(userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("jo", "jo")).thenReturn(users);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(userResponseDto);

        List<UserResponseDto> result = userService.searchUser("jo");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(logService).info(eq("SYSTEM"), eq("SEARCH_USER"), anyString(), anyMap());
    }

    @Test
    void testSearchUser_shouldThrowExceptionIfKeywordIsEmpty() {
        assertThrows(BadRequestException.class, () -> userService.searchUser(" "));
    }

    @Test
    void testCreateUser_shouldCreateUserSuccessfully() {
        when(entityHelper.getRoleRoleType(RoleType.STUDENT)).thenReturn(role);
        when(userMapper.convertDtoToEntity(userRequestDto, role)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.createUser(userRequestDto);

        assertEquals("John", result.getFirstName());
        verify(logService).info(eq("SYSTEM"), eq("CREATE_USER"), anyString(), anyMap());
    }

    @Test
    void testUpdateUser_shouldUpdateSuccessfully() {
        UserRequestDto updatedDto = new UserRequestDto();
        updatedDto.setFirstName("Jane");
        updatedDto.setLastName("Doe");
        updatedDto.setEmail("jane.doe@example.com");
        updatedDto.setUsername("janeDoe");
        updatedDto.setPassword("newPassword1!");
        updatedDto.setRoleType(RoleType.STUDENT);

        when(entityHelper.getUser(1L)).thenReturn(user);
        when(entityHelper.getRoleRoleType(RoleType.STUDENT)).thenReturn(role);
        when(customPasswordEncoder.encode("newPassword1!")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convertEntityToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.updateUser(1L, updatedDto);

        assertNotNull(result);
        verify(userMapper).updateEntityFromDto(updatedDto, user, role);
        verify(logService).info(eq("SYSTEM"), eq("UPDATE_USER"), anyString(), anyMap());
    }

    @Test
    void testDeactivateUser_shouldSetUserInactive() {
        when(entityHelper.getActiveUser(1L)).thenReturn(user);
        user.setActive(true);

        userService.deactivateUser(1L);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
        verify(logService).info(eq("SYSTEM"), eq("DEACTIVATE_USER"), anyString(), anyMap());
    }

    @Test
    void testPermanentlyDeleteUser_shouldDeleteUser() {
        when(entityHelper.getUser(1L)).thenReturn(user);

        userService.permanentlyDeleteUser(1L);

        verify(userRepository).delete(user);
        verify(logService).info(eq("SYSTEM"), eq("DELETE_USER"), anyString(), anyMap());
    }
}
