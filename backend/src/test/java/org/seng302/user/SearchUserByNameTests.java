package org.seng302.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * SearchUserByName test class - specifically for testing the searching user by name feature of the UserRepository class
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class SearchUserByNameTests {

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
    private List<User> searchUsers;

    /**
     * Creates and inserts all users for testing.
     * Ideally this would be BeforeAll.
     * BeforeEach works but will replace all users before each test. Only functional difference when testing is that they will have new IDs.
     * @throws Exception Any exception.
     */
    @BeforeEach
    public void setup() throws Exception {

        dGAA = new User(
                "Johnny",
                "Doe",
                "Pete",
                "Aldeniz",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "325 Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);

        user = new User("testfirst",
                "Dentri",
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

        anotherUser = new User ("Caedence",
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

        searchUser1= new User(
                "Alex",
                "Hine",
                "Ben",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "129 Mastic Trail, Frank Sound, Cayman Islands, Caribbean, North America",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser2 = new User(
                "Crad",
                "Taylor",
                "Barth",
                "Cra",
                "Biography123",
                "chad.taylor@example.com",
                LocalDate.of(2008, 2, 2),
                "0271316678",
                "80416 Jon Loop, Shaanxi, China",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser3 = new User(
                "Naomi",
                "Wilson",
                "I",
                "Gm",
                "Biography",
                "naomi.wilson@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                "9205 Monique Vista, Bururi, Bigomogomo, Africa",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser4 = new User(
                "Seti",
                "Rodger",
                "Tea",
                "Murphy",
                "Biography",
                "seth.murphy@example.com",
                LocalDate.of(2008, 2, 2),
                "027188316",
                "240 Bernhard Run, Southland, New Zealand",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser5 = new User(
                "Minttu",
                "Rine",
                "A",
                "Min",
                "Biography",
                "minttu.wainio@example.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "186 Simpsons Road, Ashburton, Canterbury, New Zealand",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser6 = new User(
                "Francisca",
                "Benitez",
                "T",
                "Fran",
                "Biography",
                "francisca.benitez@example.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "14798 Terry Highway, Queenstown-Lakes District, New Zealand",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUser7 = new User(
                "Francisca",
                "Bznitez",
                "Denali",
                "Fran",
                "Biography",
                "francisca.benitez@example.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "3396 Bertram Parkway, Central Otago, New Zealand",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        searchUsers = List.of(dGAA, user, anotherUser, searchUser1, searchUser2, searchUser3, searchUser4,
                searchUser5, searchUser6, searchUser7);

        for (User searchUser: searchUsers) {
            entityManager.persist(searchUser);

        }
        entityManager.flush();

    }

    /**
     * Tests that the search functionality will order users by nickname in ascending order i.e. in alphabetical order.
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnNicknameOrderedUsersAscending() throws Exception {

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
        orderedNicknames.add("Gm");
        orderedNicknames.add("Min");
        orderedNicknames.add("Murphy");
        orderedNicknames.add("nick");
        orderedNicknames.add("testnick");

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("", pageable);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getNickname()).isEqualTo(orderedNicknames.get(i));
        }

    }

    /**
     * Tests that the search functionality will order users by nickname in descending order i.e. in reverse alphabetical order.
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnNicknameOrderedUsersDescending() throws Exception {

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
        orderedNicknames.add("Fran");
        orderedNicknames.add("Fran");
        orderedNicknames.add("Cra");
        orderedNicknames.add("Aldeniz");


        // when
        Page<User> userPage = userRepository.findAllUsersByNames("", pageable);
//        assertThat(userPage.getContent()).isEqualTo(0);

        // then
        for (int i = 0; i < userPage.getContent().size(); i++) {
            assertThat(userPage.getContent().get(i).getNickname()).isEqualTo(orderedNicknames.get(i));
        }

    }

    /*

    Full name ascending/descending
    email ascending/descending
    address ascending/descending
    Ordering is consistent with duplicate values (secondary order by needed)

    Pagination test half full page
    Pagination test that we receive page 2 or later
    Pagination test empty page
    Pagination test full page
    Pagination test ordering works across pages, not just within a page

     */





//    Filter by empty string gives all users
//    Filter by non-existent input


    /**
     * Tests that the filter functionality will give all Users when the search value is the empty string.
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnFilteredNoMatch() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 11;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("nothing", pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(0);
    }

    /**
     * Tests that the filter functionality will give all Users when the search value is the empty string.
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnFilteredNoSearchValue() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 11;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("", pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(10);
    }


    /**
     * Tests that the filter functionality will filter users by a given letter that appears in any of their first, middle, last
     * or nick names.
     * This particular test tests that all Users which contain an "H" or "h" in their first, middle, last or nick names
     * are returned when the search value is "h".
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnFilteredSingleLetter() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("h", pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(4);
        assertThat(userPage.getContent().get(0).getFirstName()).isEqualTo("Johnny");
        assertThat(userPage.getContent().get(1).getLastName()).isEqualTo("Hine");
        assertThat(userPage.getContent().get(2).getMiddleName()).isEqualTo("Barth");
        assertThat(userPage.getContent().get(3).getNickname()).isEqualTo("Murphy");

    }

    /**
     * Tests that the filter functionality will filter users by a given substring that appears in any of their first, middle, last
     * or nick names.
     * This particular test tests that all Users which contain "den", regardless of letter case, in their first, middle, last or nick names
     * are returned when the search value is "den".
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnFilteredSubstring() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("den", pageable);

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
    public void whenFindAllUsersByNames_thenReturnFilteredFirstName() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("Johnny", pageable);

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
    public void whenFindAllUsersByNames_thenReturnFilteredMiddleName() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("tea", pageable);

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
    public void whenFindAllUsersByNames_thenReturnFilteredLastName() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("hine", pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getLastName()).isEqualTo("Hine");
    }

    /**
     * Tests that the filter functionality will filter users by a given string that appears in their nickname.
     * This particular test tests that all Users which contain "Min", regardless of letter case, in their last name
     * are returned when the search value is "Min".
     */
    @Test
    public void whenFindAllUsersByNames_thenReturnFilteredNickname() throws Exception {

        //given
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<User> userPage = userRepository.findAllUsersByNames("Min", pageable);

        // then
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getNickname()).isEqualTo("Min");
    }

}
