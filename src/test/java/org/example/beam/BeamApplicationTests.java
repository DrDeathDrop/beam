package org.example.beam;

import org.example.beam.model.User;
import org.example.beam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeamApplicationTests {

    @Test
    void contextLoads() {
    }
}

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;



    @Test
    void testDatabaseWorks() {
        User user = new User();
        user.setName("TestUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        User saved = userRepository.save(user);

        assertTrue(userRepository.findByName("TestUser").isPresent());
    }
}

