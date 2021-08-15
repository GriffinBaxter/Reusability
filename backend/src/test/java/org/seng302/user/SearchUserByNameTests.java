package org.seng302.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.model.Address;
import org.seng302.Main;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SearchUserByName test class - specifically for testing the searching user by name feature of the UserRepository class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class SearchUserByNameTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

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
    private User searchUser8;
    private User searchUser9;
    private User searchUser10;
    private List<User> searchUsers;
    private Address address1;
    private Address address2;
    private Address address3;
    private Address address4;
    private Address address5;
    private Address address6;
    private Address address7;
    private Address addressDGAA;
    private Address addressUser;
    private Address addressAnotherUser;

    /**
     * Creates and inserts all users for testing.
     * Ideally this would be BeforeAll.
     * BeforeEach works but will replace all users before each test. Only functional difference when testing is that they will have new IDs.
     * @throws Exception Any exception.
     */
    @BeforeEach
    void setup() throws Exception {

        addressDGAA = new Address("325", "Citlalli Track", "New Lois", "Heard Island and McDonald Islands", "HM", "Antarctica", "Pingu");
        entityManager.persist(addressDGAA);
        entityManager.flush();

        dGAA = new User(
                "Johnny",
                "Doe",
                "Pete",
                "Aldeniz",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                addressDGAA,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);

        addressUser = new Address("57", "Sydney Highway", "Shire of Cocos Islands", "West Island", "Cocos (Keeling) Islands", "9732", "Sydney");
        entityManager.persist(addressUser);
        entityManager.flush();

        user = new User("testfirst",
                "Dentri",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                addressUser,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);

        addressAnotherUser = new Address("47993", "Norwood Garden", "Mambere-Kadei", "Central African Republic", "Africa", "3428", "Norwood");

        entityManager.persist(addressAnotherUser);
        entityManager.flush();

        anotherUser = new User ("Caedence",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                addressAnotherUser,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);

        address1 = new Address("129", "Mastic Trail", "Frank Sound Cayman Islands", "Caribbean", "North America", "3442", "Pirate Cove");
        entityManager.persist(address1);
        entityManager.flush();

        searchUser1= new User(
                "Alex",
                "Hine",
                "Ben",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        address2 = new Address("80416", "Rodney Street", "Jon Loop", "Shaanxi", "China", "2113", "Barryville");
        entityManager.persist(address2);
        entityManager.flush();

        searchUser2 = new User(
                "Crad",
                "Taylor",
                "Barth",
                "Cra",
                "Biography123",
                "chad.taylor@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316678",
                address2,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        address3 = new Address("9205", "Monique Vista", "Bururi", "Bigomogomo", "Africa", "1000", "Buri");
        entityManager.persist(address3);
        entityManager.flush();

        searchUser3 = new User(
                "Naomi",
                "Wilson",
                "I",
                "Gm",
                "Biography",
                "naomi.wilson@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address3,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        address4 = new Address("240", "Newson Street", "Bernhard Run", "Southland", "New Zealand", "2839", "Ilam");
        entityManager.persist(address4);
        entityManager.flush();

        searchUser4 = new User(
                "Seti",
                "Rodger",
                "Tea",
                "Murphy",
                "Biography",
                "seth.murphy@example.com",
                LocalDate.of(2000, 2, 2),
                "027188316",
                address4,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        address5 = new Address("186", "Simpsons Road",  "Ashburton", "Canterbury", "New Zealand", "2828", "Ilam");

        entityManager.persist(address5);
        entityManager.flush();

        searchUser5 = new User(
                "Minttu",
                "Rine",
                "A",
                "Min",
                "Biography",
                "minttu.wainio@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address5,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        address6 = new Address("14798", "Terry Highway", "Queenstown-Lakes", "District", "New Zealand", "2982", "Frankton");
        entityManager.persist(address6);
        entityManager.flush();

        searchUser6 = new User(
                "Francisca",
                "Benitez",
                "T",
                "Fran",
                "Biography",
                "francisca.benitez@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address6,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        address7 = new Address("3396", "Bertram Parkway", "Central", "Central Otago", "New Zealand", "1111", "Wanaka");

        entityManager.persist(address7);
        entityManager.flush();

        searchUser7 = new User(
                "Francisca",
                "Bznitez",
                "Denali",
                "Fran",
                "Biography",
                "francisca.benitez@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address7,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser8 = new User(
                "Alex",
                "Hine",
                "Toal",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser9 = new User(
                "Alex",
                "Hasdsadine",
                "Ben",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser10 = new User(
                "Alex",
                "Hine",
                "Ben",
                "Generic",
                "Biography",
                "testaa@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUsers = List.of(dGAA, user, anotherUser, searchUser1, searchUser2, searchUser3, searchUser4,
                searchUser5, searchUser6, searchUser7, searchUser8, searchUser9, searchUser10);

        for (User searchUser: searchUsers) {
            entityManager.persist(searchUser);

        }
        entityManager.flush();

    }

    /**
     * Tests that the findAll method will order users by nickname in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllUsers_thenReturnNicknameOrderedUsersAscending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.asc("nickname").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNicknames = new ArrayList<>();
        orderedNicknames.add("Aldeniz");
        orderedNicknames.add("Cra");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Gm");
        orderedNicknames.add("Min");
        orderedNicknames.add("Murphy");
        orderedNicknames.add("nick");
        orderedNicknames.add("testnick");

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getNickname()).isEqualTo(orderedNicknames.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by nickname in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnNicknameOrderedUsersAscending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.asc("nickname").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedNicknames = new ArrayList<>();
        orderedNicknames.add("Aldeniz");
        orderedNicknames.add("Cra");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Gm");
        orderedNicknames.add("Min");
        orderedNicknames.add("Murphy");
        orderedNicknames.add("nick");
        orderedNicknames.add("testnick");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getNickname()).isEqualTo(orderedNicknames.get(i));
        }
    }

    /**
     * Tests that the findAll method will order users by nickname in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllUsers_thenReturnNicknameOrderedUsersDescending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.desc("nickname").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedNicknames = new ArrayList<>();
        orderedNicknames.add("testnick");
        orderedNicknames.add("nick");
        orderedNicknames.add("Murphy");
        orderedNicknames.add("Min");
        orderedNicknames.add("Gm");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Cra");
        orderedNicknames.add("Aldeniz");

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getNickname()).isEqualTo(orderedNicknames.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by nickname in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnNicknameOrderedUsersDescending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.desc("nickname").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedNicknames = new ArrayList<>();
        orderedNicknames.add("nick");
        orderedNicknames.add("Murphy");
        orderedNicknames.add("Min");
        orderedNicknames.add("Gm");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Generic");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Cra");
        orderedNicknames.add("Aldeniz");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getNickname()).isEqualTo(orderedNicknames.get(i));
        }
    }

    /**
     * Tests that the findAll method will order users by email in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllUsers_thenReturnEmailOrderedUsersAscending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.asc("email").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedEmail = new ArrayList<>();
        orderedEmail.add("chad.taylor@example.com");
        orderedEmail.add("email@email.com");
        orderedEmail.add("example@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("minttu.wainio@example.com");
        orderedEmail.add("naomi.wilson@example.com");
        orderedEmail.add("seth.murphy@example.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("testaa@email.com");
        orderedEmail.add("testemail@email.com");

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getEmail()).isEqualTo(orderedEmail.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by email in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnEmailOrderedUsersAscending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.asc("email").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedEmail = new ArrayList<>();
        orderedEmail.add("chad.taylor@example.com");
        orderedEmail.add("email@email.com");
        orderedEmail.add("example@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("minttu.wainio@example.com");
        orderedEmail.add("naomi.wilson@example.com");
        orderedEmail.add("seth.murphy@example.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("testaa@email.com");
        orderedEmail.add("testemail@email.com");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getEmail()).isEqualTo(orderedEmail.get(i));
        }
    }

    /**
     * Tests that the findAll method will order users by email in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllUsers_thenReturnEmailOrderedUsersDescending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.desc("email").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedEmail = new ArrayList<>();
        orderedEmail.add("testemail@email.com");
        orderedEmail.add("testaa@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("seth.murphy@example.com");
        orderedEmail.add("naomi.wilson@example.com");
        orderedEmail.add("minttu.wainio@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("example@example.com");
        orderedEmail.add("email@email.com");
        orderedEmail.add("chad.taylor@example.com");

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getEmail()).isEqualTo(orderedEmail.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by email in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnEmailOrderedUsersDescending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.desc("email").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedEmail = new ArrayList<>();
        orderedEmail.add("testaa@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("test@email.com");
        orderedEmail.add("seth.murphy@example.com");
        orderedEmail.add("naomi.wilson@example.com");
        orderedEmail.add("minttu.wainio@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("francisca.benitez@example.com");
        orderedEmail.add("example@example.com");
        orderedEmail.add("email@email.com");
        orderedEmail.add("chad.taylor@example.com");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getEmail()).isEqualTo(orderedEmail.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by address in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnAddressOrderedUsersAscending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.asc("homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.asc("homeAddress.region").ignoreCase()).and(Sort.by(Sort.Order.asc("homeAddress.country").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase())));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedAddress = new ArrayList<>();
        orderedAddress.add("186, Simpsons Road, Ilam, Ashburton, Canterbury, New Zealand, 2828");
        orderedAddress.add("240, Newson Street, Ilam, Bernhard Run, Southland, New Zealand, 2839");
        orderedAddress.add("9205, Monique Vista, Buri, Bururi, Bigomogomo, Africa, 1000");
        orderedAddress.add("3396, Bertram Parkway, Wanaka, Central, Central Otago, New Zealand, 1111");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("80416, Rodney Street, Barryville, Jon Loop, Shaanxi, China, 2113");
        orderedAddress.add("47993, Norwood Garden, Norwood, Mambere-Kadei, Central African Republic, Africa, 3428");
        orderedAddress.add("325, Citlalli Track, Pingu, New Lois, Heard Island and McDonald Islands, HM, Antarctica");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getHomeAddress().toOneLineString()).isEqualTo(orderedAddress.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames will order users by address in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnAddressOrderedUsersDescending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
        Sort sortBy = Sort.by(Sort.Order.desc("homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.desc("homeAddress.region").ignoreCase()).and(Sort.by(Sort.Order.desc("homeAddress.country").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase())));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedAddress = new ArrayList<>();
        orderedAddress.add("14798, Terry Highway, Frankton, Queenstown-Lakes, District, New Zealand, 2982");
        orderedAddress.add("325, Citlalli Track, Pingu, New Lois, Heard Island and McDonald Islands, HM, Antarctica");
        orderedAddress.add("47993, Norwood Garden, Norwood, Mambere-Kadei, Central African Republic, Africa, 3428");
        orderedAddress.add("80416, Rodney Street, Barryville, Jon Loop, Shaanxi, China, 2113");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("129, Mastic Trail, Pirate Cove, Frank Sound Cayman Islands, Caribbean, North America, 3442");
        orderedAddress.add("3396, Bertram Parkway, Wanaka, Central, Central Otago, New Zealand, 1111");
        orderedAddress.add("9205, Monique Vista, Buri, Bururi, Bigomogomo, Africa, 1000");
        orderedAddress.add("240, Newson Street, Ilam, Bernhard Run, Southland, New Zealand, 2839");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getHomeAddress().toOneLineString()).isEqualTo(orderedAddress.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by their full name (first+middle+last) in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFullNameOrderedUsersAscending() {
        // given
        int pageNo = 0;
        int pageSize = 14;
        Sort sortBy = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedFullName = new ArrayList<>();
        orderedFullName.add("Alex Ben Hasdsadine");
        orderedFullName.add("Alex Ben Hine");
        orderedFullName.add("Alex Ben Hine");
        orderedFullName.add("Alex Toal Hine");
        orderedFullName.add("Caedence middle last");
        orderedFullName.add("Crad Barth Taylor");
        orderedFullName.add("Francisca Denali Bznitez");
        orderedFullName.add("Francisca T Benitez");
        orderedFullName.add("Johnny Pete Doe");
        orderedFullName.add("Minttu A Rine");
        orderedFullName.add("Naomi I Wilson");
        orderedFullName.add("Seti Tea Rodger");
        orderedFullName.add("testfirst testmiddle Dentri");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            String fullName = userPage.getContent().get(i).getFirstName() + " " + userPage.getContent().get(i).getMiddleName() + " " + userPage.getContent().get(i).getLastName();
            assertThat(fullName).isEqualTo(orderedFullName.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will order users by their full name (first+middle+last) in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFullNameOrderedUsersDescending() {
        // given
        int pageNo = 0;
        int pageSize = 14;
        Sort sortBy = Sort.by(Sort.Order.desc("firstName").ignoreCase()).and(Sort.by(Sort.Order.desc("middleName").ignoreCase())).and(Sort.by(Sort.Order.desc("lastName").ignoreCase())).and(Sort.by(Sort.Order.desc("email").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        ArrayList<String> orderedFullName = new ArrayList<>();
        orderedFullName.add("Seti Tea Rodger");
        orderedFullName.add("Naomi I Wilson");
        orderedFullName.add("Minttu A Rine");
        orderedFullName.add("Johnny Pete Doe");
        orderedFullName.add("Francisca T Benitez");
        orderedFullName.add("Francisca Denali Bznitez");
        orderedFullName.add("Crad Barth Taylor");
        orderedFullName.add("Caedence middle last");
        orderedFullName.add("Alex Toal Hine");
        orderedFullName.add("Alex Ben Hine");
        orderedFullName.add("Alex Ben Hine");
        orderedFullName.add("Alex Ben Hasdsadine");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            String fullName = userPage.getContent().get(i).getFirstName() + " " + userPage.getContent().get(i).getMiddleName() + " " + userPage.getContent().get(i).getLastName();
            assertThat(fullName).isEqualTo(orderedFullName.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will return paginated results correctly when the page is not full with users.
     */
    @Test
    void whenFindAllUsers_thenReturnPageHalfFull() {
        // given
        int pageNo = 0;
        // Page size 20 means page will be half full with the default 13 users inserted
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(13);
        for (int i = 0; i < searchUsers.size(); i++) {
            assertThat(userPage.getContent().get(i)).isEqualTo(searchUsers.get(i));
        }
    }

    /**
     * Tests that the findAllUsersByNames method will return paginated results correctly when the page is not full with users.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnPageHalfFull() {
        // given
        int pageNo = 0;
        // Page size 20 means page will be half full with the default 13 users inserted
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(12);
        List<User> resultUsers = List.of(dGAA, anotherUser, searchUser1, searchUser2, searchUser3, searchUser4,
                searchUser5, searchUser6, searchUser7, searchUser8, searchUser9, searchUser10);
        for (int i = 0; i < resultUsers.size(); i++) {
            assertThat(userPage.getContent().get(i)).isEqualTo(resultUsers.get(i));
        }
    }

    /**
     * Tests that the findAll method will return pages other than the first one with correct users.
     */
    @Test
    void whenFindAllUsers_thenReturnPagesFromTwoOnward() {
        // given
        int pageSize = 2;

        // when
        Page<User> userPage2 = userRepository.findAll(PageRequest.of(1, pageSize));
        Page<User> userPage3 = userRepository.findAll(PageRequest.of(2, pageSize));
        Page<User> userPage4 = userRepository.findAll(PageRequest.of(3, pageSize));
        Page<User> userPage5 = userRepository.findAll(PageRequest.of(4, pageSize));

        // then
        assertThat(userPage2.getTotalPages()).isEqualTo(7);
        assertThat(userPage2.getContent().get(0)).isEqualTo(searchUsers.get(2));
        assertThat(userPage2.getContent().get(1)).isEqualTo(searchUsers.get(3));
        assertThat(userPage3.getContent().get(0)).isEqualTo(searchUsers.get(4));
        assertThat(userPage3.getContent().get(1)).isEqualTo(searchUsers.get(5));
        assertThat(userPage4.getContent().get(0)).isEqualTo(searchUsers.get(6));
        assertThat(userPage4.getContent().get(1)).isEqualTo(searchUsers.get(7));
        assertThat(userPage5.getContent().get(0)).isEqualTo(searchUsers.get(8));
        assertThat(userPage5.getContent().get(1)).isEqualTo(searchUsers.get(9));
    }

    /**
     * Tests that the findAllUsersByNames method will return pages other than the first one with correct users.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnPagesFromTwoOnward() {
        // given
        int pageSize = 2;

        List<String> names = new ArrayList<>();
        names.add("a"); // searchQuery is "a"

        // when
        Page<User> userPage2 = userRepository.findAllUsersByNames(names, PageRequest.of(1, pageSize));
        Page<User> userPage3 = userRepository.findAllUsersByNames(names, PageRequest.of(2, pageSize));
        Page<User> userPage4 = userRepository.findAllUsersByNames(names, PageRequest.of(3, pageSize));
        Page<User> userPage5 = userRepository.findAllUsersByNames(names, PageRequest.of(4, pageSize));

        // then
        assertThat(userPage2.getTotalPages()).isEqualTo(6);
        assertThat(userPage2.getContent().get(0)).isEqualTo(searchUsers.get(3));
        assertThat(userPage2.getContent().get(1)).isEqualTo(searchUsers.get(4));
        assertThat(userPage3.getContent().get(0)).isEqualTo(searchUsers.get(5));
        assertThat(userPage3.getContent().get(1)).isEqualTo(searchUsers.get(6));
        assertThat(userPage4.getContent().get(0)).isEqualTo(searchUsers.get(7));
        assertThat(userPage4.getContent().get(1)).isEqualTo(searchUsers.get(8));
        assertThat(userPage5.getContent().get(0)).isEqualTo(searchUsers.get(9));
        assertThat(userPage5.getContent().get(1)).isEqualTo(searchUsers.get(10));
    }

    /**
     * Tests that the search functionality will return an empty page when given a filter value
     * that does not match anything in the database.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnEmptyPage() {
        // given
        int pageNo = 0;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("ThisValueDoesNotExist");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isZero();
        assertThat(userPage.getTotalPages()).isZero();

    }

    /**
     * Tests that the search functionality will return the page correctly when the page is full.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFullPage() {
        // given
        int pageNo = 0;
        // Page size 8 means tested page will be full as there are 13 total values
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        assertThat(userPage.getTotalPages()).isEqualTo(2);
        assertThat(userPage.getSize()).isEqualTo(8);
        for (int i = 0; i < userPage.getSize(); i++) {
            assertThat(userPage.getContent().get(i)).isEqualTo(searchUsers.get(i));
        }

    }

    /**
     * Tests that the search functionality ordering works across pages, not just within a single page.
     *  I.e. That data is ordered 'globally' from all results in the database,
     *      not just the few values that are returned are correctly ordered.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnGloballyOrderedUsers() {
        // given
        int pageNo = 3;
        int pageSize = 3;
        Sort sortBy = Sort.by(Sort.Order.asc("nickname").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        assertThat(userPage.getTotalPages()).isEqualTo(5);
        assertThat(userPage.getSize()).isEqualTo(3);
        assertThat(userPage.getContent().get(0).getNickname()).isEqualTo("Min");
        assertThat(userPage.getContent().get(1).getNickname()).isEqualTo("Murphy");
        assertThat(userPage.getContent().get(2).getNickname()).isEqualTo("nick");

    }



    /**
     * Tests that the filter functionality will give all Users when the search value is the empty string.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredNoMatch() {

        //given
        int pageNo = 0;
        int pageSize = 11;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("nothing");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isZero();
    }

    /**
     * Tests that the filter functionality will give all Users when the search value is the empty string.
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredNoSearchValue() {

        //given
        int pageNo = 0;
        int pageSize = 11;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAll(pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(13);
    }


    /**
     * Tests that the filter functionality will filter users by a given letter that appears in any of their first, middle, last
     * or nick names.
     * This particular test tests that all Users which contain an "H" or "h" in their first, middle, last or nick names
     * are returned when the search value is "h".
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredSingleLetter() {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("h");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getSize()).isEqualTo(5);
        assertThat(userPage.getContent().get(0).getFirstName()).isEqualTo("Johnny");
        assertThat(userPage.getContent().get(1).getLastName()).isEqualTo("Hine");
        assertThat(userPage.getContent().get(2).getMiddleName()).isEqualTo("Barth");
        assertThat(userPage.getContent().get(3).getNickname()).isEqualTo("Murphy");
        assertThat(userPage.getContent().get(4).getLastName()).isEqualTo("Hine");

    }

    /**
     * Tests that the filter functionality will filter users by a given substring that appears in any of their first, middle, last
     * or nick names.
     * This particular test tests that all Users which contain "den", regardless of letter case, in their first, middle, last or nick names
     * are returned when the search value is "den".
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredSubstring() {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("den");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(4);
        assertThat(userPage.getContent().get(0).getNickname()).isEqualTo("Aldeniz");
        assertThat(userPage.getContent().get(1).getLastName()).isEqualTo("Dentri");
        assertThat(userPage.getContent().get(2).getFirstName()).isEqualTo("Caedence");
        assertThat(userPage.getContent().get(3).getMiddleName()).isEqualTo("Denali");

    }


    /**
     * Tests that the filter functionality will filter users by a given string that appears in their first name.
     * This particular test tests that all Users which contain "Johnny", regardless of letter case, in their first name
     * are returned when the search value is "johnny".
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredFirstName() {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("Johnny");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getFirstName()).isEqualTo("Johnny");
    }

    /**
     * Tests that the filter functionality will filter users by a given string that appears in their middle name.
     * This particular test tests that all Users which contain "Tea", regardless of letter case, in their middle name
     * are returned when the search value is "tea".
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredMiddleName() {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("tea");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getMiddleName()).isEqualTo("Tea");
    }


    /**
     * Tests that the filter functionality will filter users by a given string that appears in their last name.
     * This particular test tests that all Users which contain "Hine", regardless of letter case, in their last name
     * are returned when the search value is "hine".
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredLastName() {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("hine");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(3);
        assertThat(userPage.getContent().get(0).getLastName()).isEqualTo("Hine");
        assertThat(userPage.getContent().get(1).getLastName()).isEqualTo("Hine");
        assertThat(userPage.getContent().get(2).getLastName()).isEqualTo("Hine");
    }

    /**
     * Tests that the filter functionality will filter users by a given string that appears in their nickname.
     * This particular test tests that all Users which contain "Min", regardless of letter case, in their last name
     * are returned when the search value is "Min".
     */
    @Test
    void whenFindAllUsersByNames_thenReturnFilteredNickname() {
        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = new ArrayList<>();
        names.add("Min");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames(names, pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getNickname()).isEqualTo("Min");
    }

}
