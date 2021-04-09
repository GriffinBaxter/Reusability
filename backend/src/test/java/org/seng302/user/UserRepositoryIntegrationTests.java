package org.seng302.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.Address.Address;
import org.seng302.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository test class.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
public class UserRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Optional<User> foundUser;
    private List<UserPayload> foundUserPayloadList;

    private static Address address;

    @BeforeAll
    public static void before() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
    }

    /**
     * Tests that a (correct) user is returned when calling findByEmail() with an existing email
     */
    @Test
    public void whenFindByExistingEmail_thenReturnUser() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUser = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(foundUser.isPresent()).isTrue();

        assertThat(user.getId()).isEqualTo(foundUser.get().getId());
        assertThat(user.getEmail()).isEqualTo(foundUser.get().getEmail());
        assertThat(user.getFirstName()).isEqualTo(foundUser.get().getFirstName());
        assertThat(user.getMiddleName()).isEqualTo(foundUser.get().getMiddleName());
        assertThat(user.getLastName()).isEqualTo(foundUser.get().getLastName());
        assertThat(user.getNickname()).isEqualTo(foundUser.get().getNickname());
        assertThat(user.getBio()).isEqualTo(foundUser.get().getBio());
        assertThat(user.getDateOfBirth()).isEqualTo(foundUser.get().getDateOfBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(foundUser.get().getPhoneNumber());
        assertThat(user.getHomeAddress().toString()).isEqualTo(foundUser.get().getHomeAddress().toString());
        assertThat(user.getCreated()).isEqualTo(foundUser.get().getCreated());
        assertThat(user.getPassword()).isEqualTo(foundUser.get().getPassword());
    }

    /**
     * Tests that no user is returned when calling findByEmail() with a non-existing email
     */
    @Test
    public void whenFindByNonExistingEmail_thenDontReturnUser() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUser = userRepository.findByEmail("test@email.com");

        // then
        assertThat(foundUser.isEmpty()).isTrue();
    }

    /**
     * Tests that a (correct) user is returned when calling findById() with an existing id
     */
    @Test
    public void whenFindByExistingId_thenReturnUser() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUser = userRepository.findById(user.getId());

        // then
        assertThat(foundUser.isPresent()).isTrue();

        assertThat(user.getId()).isEqualTo(foundUser.get().getId());
        assertThat(user.getEmail()).isEqualTo(foundUser.get().getEmail());
        assertThat(user.getFirstName()).isEqualTo(foundUser.get().getFirstName());
        assertThat(user.getMiddleName()).isEqualTo(foundUser.get().getMiddleName());
        assertThat(user.getLastName()).isEqualTo(foundUser.get().getLastName());
        assertThat(user.getNickname()).isEqualTo(foundUser.get().getNickname());
        assertThat(user.getBio()).isEqualTo(foundUser.get().getBio());
        assertThat(user.getDateOfBirth()).isEqualTo(foundUser.get().getDateOfBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(foundUser.get().getPhoneNumber());
        assertThat(user.getHomeAddress().toString()).isEqualTo(foundUser.get().getHomeAddress().toString());
        assertThat(user.getCreated()).isEqualTo(foundUser.get().getCreated());
        assertThat(user.getPassword()).isEqualTo(foundUser.get().getPassword());
    }

    /**
     * Tests that no user is returned when calling findById() with a non-existing id
     */
    @Test
    public void whenFindByNonExistingId_thenDontReturnUser() {
        // when
        foundUser = userRepository.findById(0);

        // then
        assertThat(foundUser.isEmpty()).isTrue();
    }

    /**
     * Tests that a (correct) User payload is returned when calling
     * findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase() with an existing first, middle and last
     * name.
     */
    @Test
    public void whenFindByExistingFirstMiddleLastIgnoreCase_thenReturnUserPayload() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUserPayloadList = UserPayload.toUserPayload(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(
                "TESTFIRST", "TESTMIDDLE", "TESTLAST"
        ));

        // then
        assertThat(foundUserPayloadList.size() == 1).isTrue();
        assertThat(user.getId()).isEqualTo(foundUserPayloadList.get(0).getId());
        assertThat(user.getEmail()).isEqualTo(foundUserPayloadList.get(0).getEmail());
        assertThat(user.getFirstName()).isEqualTo(foundUserPayloadList.get(0).getFirstName());
        assertThat(user.getMiddleName()).isEqualTo(foundUserPayloadList.get(0).getMiddleName());
        assertThat(user.getLastName()).isEqualTo(foundUserPayloadList.get(0).getLastName());
        assertThat(user.getNickname()).isEqualTo(foundUserPayloadList.get(0).getNickname());
        assertThat(user.getBio()).isEqualTo(foundUserPayloadList.get(0).getBio());
        assertThat(user.getDateOfBirth()).isEqualTo(foundUserPayloadList.get(0).getDateOfBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(foundUserPayloadList.get(0).getPhoneNumber());
        assertThat(user.getHomeAddress().toString()).isEqualTo(foundUserPayloadList.get(0).getHomeAddress().toString());
        assertThat(user.getCreated()).isEqualTo(foundUserPayloadList.get(0).getCreated());
    }

    /**
     * Tests that no User payload is returned when calling
     * findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase() with a non-existing first, middle and
     * last name.
     */
    @Test
    public void whenFindByNonExistingFirstMiddleLast_thenDontReturnUserPayload() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUserPayloadList = UserPayload.toUserPayload(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(
                "not first", "not middle", "not last"
        ));

        // then
        assertThat(foundUserPayloadList.isEmpty()).isTrue();
    }

    /**
     * Tests that a (correct) User payload is returned when calling findByFirstNameIgnoreCaseAndLastNameIgnoreCase()
     * with an existing first and last name.
     */
    @Test
    public void whenFindByExistingFirstLastIgnoreCase_thenReturnUserPayload() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUserPayloadList = UserPayload.toUserPayload(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("TESTFIRST", "TESTLAST"));

        // then
        assertThat(foundUserPayloadList.size() == 1).isTrue();
        assertThat(user.getId()).isEqualTo(foundUserPayloadList.get(0).getId());
        assertThat(user.getEmail()).isEqualTo(foundUserPayloadList.get(0).getEmail());
        assertThat(user.getFirstName()).isEqualTo(foundUserPayloadList.get(0).getFirstName());
        assertThat(user.getMiddleName()).isEqualTo(foundUserPayloadList.get(0).getMiddleName());
        assertThat(user.getLastName()).isEqualTo(foundUserPayloadList.get(0).getLastName());
        assertThat(user.getNickname()).isEqualTo(foundUserPayloadList.get(0).getNickname());
        assertThat(user.getBio()).isEqualTo(foundUserPayloadList.get(0).getBio());
        assertThat(user.getDateOfBirth()).isEqualTo(foundUserPayloadList.get(0).getDateOfBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(foundUserPayloadList.get(0).getPhoneNumber());
        assertThat(user.getHomeAddress().toString()).isEqualTo(foundUserPayloadList.get(0).getHomeAddress().toString());
        assertThat(user.getCreated()).isEqualTo(foundUserPayloadList.get(0).getCreated());
    }

    /**
     * Tests that no User payload is returned when calling findByFirstNameIgnoreCaseAndLastNameIgnoreCase() with a
     * non-existing first and last name.
     */
    @Test
    public void whenFindByNonExistingFirstLast_thenDontReturnUserPayload() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUserPayloadList = UserPayload.toUserPayload(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("not first", "not last"));

        // then
        assertThat(foundUserPayloadList.isEmpty()).isTrue();
    }

    /**
     * Tests that a (correct) User payload is returned when calling findByNicknameIgnoreCase(),
     * findByFirstNameIgnoreCase(), findByLastNameIgnoreCase() and findByMiddleNameIgnoreCase() with an existing
     * nickname, first, last and middle name respectively.
     */
    @Test
    public void whenFindByExistingNicknameOrFirstOrLastOrMiddleIgnoreCase_thenReturnUserPayload() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        List<UserPayload> foundUserPayloadListNickname = UserPayload.toUserPayload(userRepository.findByNicknameIgnoreCase("TESTNICK"));
        List<UserPayload> foundUserPayloadListFirst = UserPayload.toUserPayload(userRepository.findByFirstNameIgnoreCase("TESTFIRST"));
        List<UserPayload> foundUserPayloadListLast = UserPayload.toUserPayload(userRepository.findByLastNameIgnoreCase("TESTLAST"));
        List<UserPayload> foundUserPayloadListMiddle = UserPayload.toUserPayload(userRepository.findByMiddleNameIgnoreCase("TESTMIDDLE"));
        List<UserPayload> foundUserPayloads = List.of(
                foundUserPayloadListNickname.get(0),
                foundUserPayloadListFirst.get(0),
                foundUserPayloadListLast.get(0),
                foundUserPayloadListMiddle.get(0)
        );

        // then
        assertThat(foundUserPayloadListNickname.size() == 1).isTrue();
        assertThat(foundUserPayloadListFirst.size() == 1).isTrue();
        assertThat(foundUserPayloadListLast.size() == 1).isTrue();
        assertThat(foundUserPayloadListMiddle.size() == 1).isTrue();

        for (UserPayload foundUserPayload: foundUserPayloads) {
            assertThat(user.getId()).isEqualTo(foundUserPayload.getId());
            assertThat(user.getEmail()).isEqualTo(foundUserPayload.getEmail());
            assertThat(user.getFirstName()).isEqualTo(foundUserPayload.getFirstName());
            assertThat(user.getMiddleName()).isEqualTo(foundUserPayload.getMiddleName());
            assertThat(user.getLastName()).isEqualTo(foundUserPayload.getLastName());
            assertThat(user.getNickname()).isEqualTo(foundUserPayload.getNickname());
            assertThat(user.getBio()).isEqualTo(foundUserPayload.getBio());
            assertThat(user.getDateOfBirth()).isEqualTo(foundUserPayload.getDateOfBirth());
            assertThat(user.getPhoneNumber()).isEqualTo(foundUserPayload.getPhoneNumber());
            assertThat(user.getHomeAddress().toString()).isEqualTo(foundUserPayload.getHomeAddress().toString());
            assertThat(user.getCreated()).isEqualTo(foundUserPayload.getCreated());
        }
    }

    /**
     * Tests that no User payload is returned when calling findByNicknameIgnoreCase(), findByFirstNameIgnoreCase(),
     * findByLastNameIgnoreCase() and findByMiddleNameIgnoreCase() with a non-existing nickname, first, last and middle
     * name respectively.
     */
    @Test
    public void whenFindByNonExistingNicknameOrFirstOrLastOrMiddle_thenDontReturnUserPayload() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", address, "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        List<UserPayload> foundUserPayloadListNickname = UserPayload.toUserPayload(userRepository.findByNicknameIgnoreCase("not nickname"));
        List<UserPayload> foundUserPayloadListFirst = UserPayload.toUserPayload(userRepository.findByFirstNameIgnoreCase("not first"));
        List<UserPayload> foundUserPayloadListLast = UserPayload.toUserPayload(userRepository.findByLastNameIgnoreCase("not last"));
        List<UserPayload> foundUserPayloadListMiddle = UserPayload.toUserPayload(userRepository.findByMiddleNameIgnoreCase("not middle"));

        // then
        assertThat(foundUserPayloadListNickname.isEmpty()).isTrue();
        assertThat(foundUserPayloadListFirst.isEmpty()).isTrue();
        assertThat(foundUserPayloadListLast.isEmpty()).isTrue();
        assertThat(foundUserPayloadListMiddle.isEmpty()).isTrue();
    }
}
