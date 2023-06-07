package com.dmdev.dao;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void findAll() {
        User user1 = userDao.save(buildUser("test1@gmail.com"));
        User user2 = userDao.save(buildUser("test2@gmail.com"));
        User user3 = userDao.save(buildUser("test3@gmail.com"));

        List<User> actualResult = userDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> userIds = actualResult.stream()
                .map(User::getId)
                .toList();
        assertThat(userIds).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void findById() {
        User user = userDao.save(buildUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent().contains(user);
    }

    @Test
    void save() {
        User user = buildUser("test1@gmail.com");

        User actualResult = userDao.save(user);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findByEmailAndPassword() {
        User user = userDao.save(buildUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent().contains(user);
    }

    @Test
    void shouldNotFindByEmailAndPasswordIfUserDoesNotExist() {
        userDao.save(buildUser("test1@gmail.com"));
        userDao.save(buildUser("test2@gmail.com"));
        userDao.save(buildUser("test3@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword("dummy", "123");

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() {
        User user = userDao.save(buildUser("test1@gmail.com"));

        boolean actualResult = userDao.delete(user.getId());

        assertThat(actualResult).isTrue();
    }

    @Test
    void deleteNotExistingEntity() {
        userDao.save(buildUser("test1@gmail.com"));

        boolean actualResult = userDao.delete(100500);

        assertThat(actualResult).isFalse();
    }

    @Test
    void update() {
        User user = userDao.save(buildUser("test1@gmail.com"));
        user.setName("Ivan-updated");
        user.setPassword("123-updated");

        userDao.update(user);

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent().contains(user);
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
}