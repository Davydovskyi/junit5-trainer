package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Captor
    private ArgumentCaptor<CreateUserDto> createUserDtoCaptor;

    @Test
    void loginSuccess() {
        User user = buildUser();
        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(user.getEmail(), user.getPassword());

        UserDto userDto = buildUserDto();
        doReturn(userDto).when(userMapper).map(user);

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        verify(userDao, times(1)).findByEmailAndPassword(user.getEmail(), user.getPassword());
        verify(userMapper, times(1)).map(userCaptor.capture());

        assertThat(userCaptor.getValue()).isEqualTo(user);
        assertThat(actualResult).isPresent().contains(userDto);
    }

    @Test
    void loginFailed() {
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());

        Optional<UserDto> actualResult = userService.login("dummy", "dummy");

        assertThat(actualResult).isEmpty();

        verify(userDao).findByEmailAndPassword(any(), any());
        verifyNoInteractions(userMapper);
    }

    @Test
    void create() {
        CreateUserDto createUserDto = buildCreateUserDto();
        doReturn(new ValidationResult()).when(createUserValidator).validate(createUserDto);

        User user = buildUser();
        doReturn(user).when(createUserMapper).map(createUserDto);

        UserDto userDto = buildUserDto();
        doReturn(userDto).when(userMapper).map(user);

        UserDto actualResult = userService.create(createUserDto);

        assertThat(actualResult).isEqualTo(userDto);

        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
        verify(createUserValidator).validate(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(createUserMapper).map(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(userMapper).map(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.role", "message"));
        doReturn(validationResult).when(createUserValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> userService.create(any()));

        verifyNoInteractions(userDao, userMapper, createUserMapper);
        verify(createUserValidator).validate(any());
    }

    private CreateUserDto buildCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .birthday("2015-01-01")
                .email("test@gmail.com")
                .password("123")
                .build();
    }

    private UserDto buildUserDto() {
        return UserDto.builder()
                .id(99)
                .name("Ivan")
                .birthday(LocalDate.of(2015, 1, 1))
                .email("test@gmail.com")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(99)
                .name("Ivan")
                .birthday(LocalDate.of(2015, 1, 1))
                .email("test@gmail.com")
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .build();
    }
}