package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceIT extends IntegrationTestBase {

    @Spy
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                CreateUserValidator.getInstance(),
                userDao,
                CreateUserMapper.getInstance(),
                UserMapper.getInstance()
        );
    }

    @Test
    void login() {
        User user = userDao.save(buildUser("test1@gmail.com"));

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void create() {
        CreateUserDto createUserDto = buildCreateUserDto();

        UserDto actualResult = userService.create(createUserDto);

        assertThat(actualResult.getId()).isNotNull();
    }

    private User buildUser(String email) {
        return User.builder()
                .name("Ivan")
                .birthday(LocalDate.of(2015, 1, 1))
                .email(email)
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .build();
    }

    private CreateUserDto buildCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .birthday("2015-01-01")
                .email("test1@gmail.com")
                .password("123")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .build();
    }
}