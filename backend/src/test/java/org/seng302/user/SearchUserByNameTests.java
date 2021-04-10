package org.seng302.user;

import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.seng302.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SearchUserByName test class - specifically for testing the searching user by name feature of the UserRepository class
 */
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
public class SearchUserByNameTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User dGAA;
    private User user;
    private User anotherUser;
    private User user2;

//    @BeforeAll
//    public void setup() throws Exception {
//        this.entityManager.clear();
//    }
//
//    @After
//    public void clear() {
//        this.entityManager.clear();
//    }


//    private User dGAA;
//    private User user;
//    private User anotherUser;
//    private User searchUser1;
//    private User searchUser2;
//    private User searchUser3;
//    private User searchUser4;
//    private User searchUser5;
//    private User searchUser6;
//    private User searchUser7;
//    private List<User> searchUsers;
//
//    @BeforeAll
//    public void setup() throws Exception {
//
//        dGAA = new User(
//                "John",
//                "Doe",
//                "S",
//                "Generic",
//                "Biography",
//                "email@email.com",
//                LocalDate.of(2020, 2, 2),
//                "0271316",
//                "325 Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.DEFAULTGLOBALAPPLICATIONADMIN);
//
//        user = new User("testfirst",
//                "testlast",
//                "testmiddle",
//                "testnick",
//                "testbiography",
//                "testemail@email.com",
//                LocalDate.of(2020, 2, 2),
//                "0271316",
//                "57 Sydney Highway, Shire of Cocos Islands, West Island, Cocos (Keeling) Islands",
//                "testpassword",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.GLOBALAPPLICATIONADMIN);
//
//        anotherUser = new User ("first",
//                "last",
//                "middle",
//                "nick",
//                "bio",
//                "example@example.com",
//                LocalDate.of(2021, 1, 1),
//                "123456789",
//                "47993 Norwood Garden, Mambere-Kadei Central African Republic, Africa",
//                "password",
//                LocalDateTime.of(LocalDate.of(2021, 1, 1),
//                        LocalTime.of(0, 0)),
//                Role.USER);
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
//        searchUsers = List.of(dGAA, user, anotherUser, searchUser1, searchUser2, searchUser3, searchUser4,
//                searchUser5, searchUser6, searchUser7);
//
//        for (User searchUser: searchUsers) {
//            entityManager.persist(searchUser);
//            entityManager.flush();
//        }
//
//    }

    /**
     * Ordering nick
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnNicknameOrderedUsers_2() throws Exception {

        this.entityManager.clear();

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
                "address",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);

        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "testaddress",
                "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);

        anotherUser = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2021, 1, 1),
                "123456789",
                "1 Example Street",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);

        user2= new User(
                "Alex",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "address",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        entityManager.persist(dGAA);
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.flush();


        this.entityManager.clear();




        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by("nickname").ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNicknames = new ArrayList<>();
//        orderedNicknames.add("Cha");
//        orderedNicknames.add("Fran");
//        orderedNicknames.add("Fran");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
//        orderedNicknames.add("Gm");
//        orderedNicknames.add("Min");
        orderedNicknames.add("nick");
//        orderedNicknames.add("S");
        orderedNicknames.add("testnick");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("", pageable);
        assertThat(userPage.getContent()).isEqualTo(0);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(6).getNickname()).isEqualTo(orderedNicknames.get(6));
        }

    }



}
