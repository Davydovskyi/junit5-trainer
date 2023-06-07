package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void map() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .birthday("2015-01-01")
                .email("test@gmail.com")
                .password("123")
                .build();


        User actualResult = mapper.map(dto);

        User expectedResult = User.builder()
                .name("Ivan")
                .birthday(LocalDate.of(2015, 1, 1))
                .email("test@gmail.com")
                .password("123")
                .gender(Gender.MALE)
                .role(Role.USER)
                .build();

        assertEquals(expectedResult, actualResult);
    }
}