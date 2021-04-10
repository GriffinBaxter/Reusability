package org.seng302.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.seng302.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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


    private User dGAA;
    private User user;
    private User anotherUser;
    private User searchUser1;
    private User searchUser2;
    private User searchUser3;
    private User searchUser4;
    private User searchUser5;
    private User searchUser6;
    private User searchUser7;
    private List<User> searchUsers;


    public void setup() throws Exception {



    }


    /**
     * Ordering nick
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnNicknameOrderedUsers() throws Exception {
        // given
        dGAA = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "325 Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        entityManager.persist(dGAA);
        entityManager.flush();

        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "57 Sydney Highway, Shire of Cocos Islands, West Island, Cocos (Keeling) Islands",
                "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        entityManager.persist(user);
        entityManager.flush();

        anotherUser = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2021, 1, 1),
                "123456789",
                "47993 Norwood Garden, Mambere-Kadei Central African Republic, Africa",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        entityManager.persist(anotherUser);
        entityManager.flush();


//        //test users for searching for user by name
//
//        searchUser1= new User(
//                "Alex",
//                "Doe",
//                "S",
//                "Generic",
//                "Biography",
//                "test@email.com",
//                LocalDate.of(2020, 2, 2),
//                "0271316",
//                "129 Mastic Trail, Frank Sound, Cayman Islands, Caribbean, North America",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//
//        searchUser2 = new User(
//                "Chad",
//                "Taylor",
//                "S",
//                "Cha",
//                "Biography123",
//                "chad.taylor@example.com",
//                LocalDate.of(2008, 2, 2),
//                "0271316678",
//                "80416 Jon Loop, Shaanxi, China",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//        searchUser3 = new User(
//                "Naomi",
//                "Wilson",
//                "I",
//                "Gm",
//                "Biography",
//                "naomi.wilson@example.com",
//                LocalDate.of(2000, 2, 2),
//                "0271316",
//                "9205 Monique Vista, Bururi, Bigomogomo, Africa",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//        searchUser4 = new User(
//                "Seth",
//                "Murphy",
//                "Tea",
//                "S",
//                "Biography",
//                "seth.murphy@example.com",
//                LocalDate.of(2008, 2, 2),
//                "027188316",
//                "240 Bernhard Run, Southland, New Zealand",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//        searchUser5 = new User(
//                "Minttu",
//                "Wainio",
//                "A",
//                "Min",
//                "Biography",
//                "minttu.wainio@example.com",
//                LocalDate.of(2020, 2, 2),
//                "0271316",
//                "186 Simpsons Road, Ashburton, Canterbury, New Zealand",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//        searchUser6 = new User(
//                "Francisca",
//                "Benitez",
//                "T",
//                "Fran",
//                "Biography",
//                "francisca.benitez@example.com",
//                LocalDate.of(2020, 2, 2),
//                "0271316",
//                "14798 Terry Highway, Queenstown-Lakes District, New Zealand",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//        searchUser7 = new User(
//                "Francisca",
//                "Bznitez",
//                "T",
//                "Fran",
//                "Biography",
//                "francisca.benitez@example.com",
//                LocalDate.of(2020, 2, 2),
//                "0271316",
//                "3396 Bertram Parkway, Central Otago, New Zealand",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.USER);
//
//
//        searchUsers = List.of(dGAA, user, anotherUser, searchUser1, searchUser2, searchUser3, searchUser4,
//                searchUser5, searchUser6, searchUser7);

//        for (User searchUser: searchUsers) {
//            entityManager.persist(searchUser);
//            entityManager.flush();
//        }

        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by("nickname").ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNicknames = new ArrayList<>();
        orderedNicknames.add("Cha");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Gm");
        orderedNicknames.add("Min");
        orderedNicknames.add("nick");
        orderedNicknames.add("S");
        orderedNicknames.add("testnick");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("", pageable);
        assertThat(userPage.getContent()).isEqualTo(0);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(6).getNickname()).isEqualTo(orderedNicknames.get(6));
        }

    }


    /**
     * Tests that a (correct) user is returned when calling findByEmail() with an existing email
     */
    @Test
    public void whenFindByExistingEmail_thenReturnUser() throws Exception {
        // given
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
                "0271316", "testaddress", "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER);

        entityManager.persist(user);
        entityManager.flush();

        // when
        foundUser = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(foundUser.isPresent()).isTrue();


        assertThat(userRepository.findAllByFirstNameContains("")).isEqualTo(0);


        assertThat(user.getId()).isEqualTo(foundUser.get().getId());
        assertThat(user.getEmail()).isEqualTo(foundUser.get().getEmail());
        assertThat(user.getFirstName()).isEqualTo(foundUser.get().getFirstName());
        assertThat(user.getMiddleName()).isEqualTo(foundUser.get().getMiddleName());
        assertThat(user.getLastName()).isEqualTo(foundUser.get().getLastName());
        assertThat(user.getNickname()).isEqualTo(foundUser.get().getNickname());
        assertThat(user.getBio()).isEqualTo(foundUser.get().getBio());
        assertThat(user.getDateOfBirth()).isEqualTo(foundUser.get().getDateOfBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(foundUser.get().getPhoneNumber());
        assertThat(user.getHomeAddress()).isEqualTo(foundUser.get().getHomeAddress());
        assertThat(user.getCreated()).isEqualTo(foundUser.get().getCreated());
        assertThat(user.getPassword()).isEqualTo(foundUser.get().getPassword());
    }
//
//    /**
//     * Tests that no user is returned when calling findByEmail() with a non-existing email
//     */
//    @Test
//    public void whenFindByNonExistingEmail_thenDontReturnUser() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
//                Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        foundUser = userRepository.findByEmail("test@email.com");
//
//        // then
//        assertThat(foundUser.isEmpty()).isTrue();
//    }
//
//    /**
//     * Tests that a (correct) user is returned when calling findById() with an existing id
//     */
//    @Test
//    public void whenFindByExistingId_thenReturnUser() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
//                Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        foundUser = userRepository.findById(user.getId());
//
//        // then
//        assertThat(foundUser.isPresent()).isTrue();
//
//        assertThat(user.getId()).isEqualTo(foundUser.get().getId());
//        assertThat(user.getEmail()).isEqualTo(foundUser.get().getEmail());
//        assertThat(user.getFirstName()).isEqualTo(foundUser.get().getFirstName());
//        assertThat(user.getMiddleName()).isEqualTo(foundUser.get().getMiddleName());
//        assertThat(user.getLastName()).isEqualTo(foundUser.get().getLastName());
//        assertThat(user.getNickname()).isEqualTo(foundUser.get().getNickname());
//        assertThat(user.getBio()).isEqualTo(foundUser.get().getBio());
//        assertThat(user.getDateOfBirth()).isEqualTo(foundUser.get().getDateOfBirth());
//        assertThat(user.getPhoneNumber()).isEqualTo(foundUser.get().getPhoneNumber());
//        assertThat(user.getHomeAddress()).isEqualTo(foundUser.get().getHomeAddress());
//        assertThat(user.getCreated()).isEqualTo(foundUser.get().getCreated());
//        assertThat(user.getPassword()).isEqualTo(foundUser.get().getPassword());
//    }
//
//    /**
//     * Tests that no user is returned when calling findById() with a non-existing id
//     */
//    @Test
//    public void whenFindByNonExistingId_thenDontReturnUser() {
//        // when
//        foundUser = userRepository.findById(0);
//
//        // then
//        assertThat(foundUser.isEmpty()).isTrue();
//    }
//
//    /**
//     * Tests that a (correct) User payload is returned when calling
//     * findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase() with an existing first, middle and last
//     * name.
//     */
//    @Test
//    public void whenFindByExistingFirstMiddleLastIgnoreCase_thenReturnUserPayload() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        foundUserPayloadList = userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(
//                "TESTFIRST", "TESTMIDDLE", "TESTLAST"
//        );
//
//        // then
//        assertThat(foundUserPayloadList.size() == 1).isTrue();
//        assertThat(user.getId()).isEqualTo(foundUserPayloadList.get(0).getId());
//        assertThat(user.getEmail()).isEqualTo(foundUserPayloadList.get(0).getEmail());
//        assertThat(user.getFirstName()).isEqualTo(foundUserPayloadList.get(0).getFirstName());
//        assertThat(user.getMiddleName()).isEqualTo(foundUserPayloadList.get(0).getMiddleName());
//        assertThat(user.getLastName()).isEqualTo(foundUserPayloadList.get(0).getLastName());
//        assertThat(user.getNickname()).isEqualTo(foundUserPayloadList.get(0).getNickname());
//        assertThat(user.getBio()).isEqualTo(foundUserPayloadList.get(0).getBio());
//        assertThat(user.getDateOfBirth()).isEqualTo(foundUserPayloadList.get(0).getDateOfBirth());
//        assertThat(user.getPhoneNumber()).isEqualTo(foundUserPayloadList.get(0).getPhoneNumber());
//        assertThat(user.getHomeAddress()).isEqualTo(foundUserPayloadList.get(0).getHomeAddress());
//        assertThat(user.getCreated()).isEqualTo(foundUserPayloadList.get(0).getCreated());
//    }
//
//    /**
//     * Tests that no User payload is returned when calling
//     * findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase() with a non-existing first, middle and
//     * last name.
//     */
//    @Test
//    public void whenFindByNonExistingFirstMiddleLast_thenDontReturnUserPayload() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        foundUserPayloadList = userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(
//                "not first", "not middle", "not last"
//        );
//
//        // then
//        assertThat(foundUserPayloadList.isEmpty()).isTrue();
//    }
//
//    /**
//     * Tests that a (correct) User payload is returned when calling findByFirstNameIgnoreCaseAndLastNameIgnoreCase()
//     * with an existing first and last name.
//     */
//    @Test
//    public void whenFindByExistingFirstLastIgnoreCase_thenReturnUserPayload() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        foundUserPayloadList = userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("TESTFIRST", "TESTLAST");
//
//        // then
//        assertThat(foundUserPayloadList.size() == 1).isTrue();
//        assertThat(user.getId()).isEqualTo(foundUserPayloadList.get(0).getId());
//        assertThat(user.getEmail()).isEqualTo(foundUserPayloadList.get(0).getEmail());
//        assertThat(user.getFirstName()).isEqualTo(foundUserPayloadList.get(0).getFirstName());
//        assertThat(user.getMiddleName()).isEqualTo(foundUserPayloadList.get(0).getMiddleName());
//        assertThat(user.getLastName()).isEqualTo(foundUserPayloadList.get(0).getLastName());
//        assertThat(user.getNickname()).isEqualTo(foundUserPayloadList.get(0).getNickname());
//        assertThat(user.getBio()).isEqualTo(foundUserPayloadList.get(0).getBio());
//        assertThat(user.getDateOfBirth()).isEqualTo(foundUserPayloadList.get(0).getDateOfBirth());
//        assertThat(user.getPhoneNumber()).isEqualTo(foundUserPayloadList.get(0).getPhoneNumber());
//        assertThat(user.getHomeAddress()).isEqualTo(foundUserPayloadList.get(0).getHomeAddress());
//        assertThat(user.getCreated()).isEqualTo(foundUserPayloadList.get(0).getCreated());
//    }
//
//    /**
//     * Tests that no User payload is returned when calling findByFirstNameIgnoreCaseAndLastNameIgnoreCase() with a
//     * non-existing first and last name.
//     */
//    @Test
//    public void whenFindByNonExistingFirstLast_thenDontReturnUserPayload() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        foundUserPayloadList = userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("not first", "not last");
//
//        // then
//        assertThat(foundUserPayloadList.isEmpty()).isTrue();
//    }
//
//    /**
//     * Tests that a (correct) User payload is returned when calling findByNicknameIgnoreCase(),
//     * findByFirstNameIgnoreCase(), findByLastNameIgnoreCase() and findByMiddleNameIgnoreCase() with an existing
//     * nickname, first, last and middle name respectively.
//     */
//    @Test
//    public void whenFindByExistingNicknameOrFirstOrLastOrMiddleIgnoreCase_thenReturnUserPayload() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        List<UserPayload> foundUserPayloadListNickname = userRepository.findByNicknameIgnoreCase("TESTNICK");
//        List<UserPayload> foundUserPayloadListFirst = userRepository.findByFirstNameIgnoreCase("TESTFIRST");
//        List<UserPayload> foundUserPayloadListLast = userRepository.findByLastNameIgnoreCase("TESTLAST");
//        List<UserPayload> foundUserPayloadListMiddle = userRepository.findByMiddleNameIgnoreCase("TESTMIDDLE");
//        List<UserPayload> foundUserPayloads = List.of(
//                foundUserPayloadListNickname.get(0),
//                foundUserPayloadListFirst.get(0),
//                foundUserPayloadListLast.get(0),
//                foundUserPayloadListMiddle.get(0)
//        );
//
//        // then
//        assertThat(foundUserPayloadListNickname.size() == 1).isTrue();
//        assertThat(foundUserPayloadListFirst.size() == 1).isTrue();
//        assertThat(foundUserPayloadListLast.size() == 1).isTrue();
//        assertThat(foundUserPayloadListMiddle.size() == 1).isTrue();
//
//        for (UserPayload foundUserPayload: foundUserPayloads) {
//            assertThat(user.getId()).isEqualTo(foundUserPayload.getId());
//            assertThat(user.getEmail()).isEqualTo(foundUserPayload.getEmail());
//            assertThat(user.getFirstName()).isEqualTo(foundUserPayload.getFirstName());
//            assertThat(user.getMiddleName()).isEqualTo(foundUserPayload.getMiddleName());
//            assertThat(user.getLastName()).isEqualTo(foundUserPayload.getLastName());
//            assertThat(user.getNickname()).isEqualTo(foundUserPayload.getNickname());
//            assertThat(user.getBio()).isEqualTo(foundUserPayload.getBio());
//            assertThat(user.getDateOfBirth()).isEqualTo(foundUserPayload.getDateOfBirth());
//            assertThat(user.getPhoneNumber()).isEqualTo(foundUserPayload.getPhoneNumber());
//            assertThat(user.getHomeAddress()).isEqualTo(foundUserPayload.getHomeAddress());
//            assertThat(user.getCreated()).isEqualTo(foundUserPayload.getCreated());
//        }
//    }
//
//    /**
//     * Tests that no User payload is returned when calling findByNicknameIgnoreCase(), findByFirstNameIgnoreCase(),
//     * findByLastNameIgnoreCase() and findByMiddleNameIgnoreCase() with a non-existing nickname, first, last and middle
//     * name respectively.
//     */
//    @Test
//    public void whenFindByNonExistingNicknameOrFirstOrLastOrMiddle_thenDontReturnUserPayload() throws Exception {
//        // given
//        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
//                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2),
//                "0271316", "testaddress", "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), Role.USER);
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        // when
//        List<UserPayload> foundUserPayloadListNickname = userRepository.findByNicknameIgnoreCase("not nickname");
//        List<UserPayload> foundUserPayloadListFirst = userRepository.findByFirstNameIgnoreCase("not first");
//        List<UserPayload> foundUserPayloadListLast = userRepository.findByLastNameIgnoreCase("not last");
//        List<UserPayload> foundUserPayloadListMiddle = userRepository.findByMiddleNameIgnoreCase("not middle");
//
//        // then
//        assertThat(foundUserPayloadListNickname.isEmpty()).isTrue();
//        assertThat(foundUserPayloadListFirst.isEmpty()).isTrue();
//        assertThat(foundUserPayloadListLast.isEmpty()).isTrue();
//        assertThat(foundUserPayloadListMiddle.isEmpty()).isTrue();
//    }
}
