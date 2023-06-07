package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void shouldPassValidation() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .birthday("2015-01-01")
                .email("test@gmail.com")
                .password("123")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertTrue(actualResult.isValid());
    }

    @Test
    void invalidBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .birthday("2015-01-01 45")
                .email("test@gmail.com")
                .password("123")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void invalidGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .gender("invalid")
                .role(Role.USER.name())
                .birthday("2015-01-01")
                .email("test@gmail.com")
                .password("123")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void invalidRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .gender(Gender.MALE.name())
                .role("invalid")
                .birthday("2015-01-01")
                .email("test@gmail.com")
                .password("123")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.role");
    }

    @Test
    void invalidRoleGenderBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .gender("invalid")
                .role("invalid")
                .birthday("2015-01-01 45")
                .email("test@gmail.com")
                .password("123")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(3);
        List<String> errorsCode = actualResult.getErrors().stream().
                map(Error::getCode)
                .toList();
        assertThat(errorsCode).containsExactlyInAnyOrder("invalid.role", "invalid.gender", "invalid.birthday");
    }
}