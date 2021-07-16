package org.seng302.keyword;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.Keyword;
import org.seng302.model.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * KeywordRepository test Class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class KeywordRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KeywordRepository keywordRepository;

    private Optional<Keyword> foundKeyword;

    private List<Keyword> foundKeywords;
    private static Keyword keyword1;
    private static Keyword keyword2;

    private Keyword keyword1;
    private Keyword keyword2;
    private Keyword keyword3;
    private Keyword keyword4;
    private Keyword keyword5;

    @BeforeEach
    void setup() throws Exception {
        keyword1 = new Keyword("Car", LocalDateTime.now());
        keyword2 = new Keyword("Cat", LocalDateTime.now());
        keyword3 = new Keyword("Meat", LocalDateTime.now());
        keyword4 = new Keyword("food", LocalDateTime.now());
        keyword5 = new Keyword("TOON", LocalDateTime.now());
        entityManager.persistAndFlush(keyword1);
        entityManager.persistAndFlush(keyword2);
        entityManager.persistAndFlush(keyword3);
        entityManager.persistAndFlush(keyword4);
        entityManager.persistAndFlush(keyword5);
    @BeforeAll
    static void before() throws Exception {
        keyword1 = new Keyword("car", LocalDateTime.now());
        keyword2 = new Keyword("cat", LocalDateTime.now());
    }

    /**
     * Tests findByName method returns the correct keyword when name exists
     */
    @Test
    void whenFindByExistingName_ThenReturnKeyword() {
        // given
        // when
        foundKeyword = keywordRepository.findByName(keyword1.getName());

        // then
        assertThat(foundKeyword).isPresent();
        assertThat(keyword1.getId()).isEqualTo(foundKeyword.get().getId());
        assertThat(keyword1.getName()).isEqualTo(foundKeyword.get().getName());
    }

    /**
     * Tests findByName method when the keyword doesn't exist returns an empty Optional
     */
    @Test
    void whenFindByUnknownName_ThenDontReturnKeyword() {
        // given
        String name = "qwerty";
        // when
        foundKeyword = keywordRepository.findByName(name);
        // then
        assertThat(foundKeyword).isNotPresent();
    }

    /**
     * Tests findAllByNameIgnoreCaseContaining method returns a valid list of keywords with an existing substring
     */
    @Test
    void whenFindAllContaining_returnsValidList() {
        // given
        String substring = "ca";
        // when
        foundKeywords = keywordRepository.findAllByNameIgnoreCaseContaining(substring);
        // then
        assertThat(foundKeywords.size()).isEqualTo(2);
        assertThat(foundKeywords.get(0)).isEqualTo(keyword1);
        assertThat(foundKeywords.get(1)).isEqualTo(keyword2);
    }

    /**
     * Tests findAllByNameIgnoreCaseContaining method returns an empty list when a non-existing substring is used
     */
    @Test
    void whenFindAllByUnknownQuery_returnsEmptyList() {
        // given
        String substring = "xax";
        // when
        foundKeywords = keywordRepository.findAllByNameIgnoreCaseContaining(substring);
        // then
        assertThat(foundKeywords.size()).isEqualTo(0);
    }

    /**
     * Tests findAllByNameIgnoreCaseContaining method returns a valid list of keywords when wrong capitalization is used
     */
    @Test
    void whenFindAllByRandomCase_returnValidList() {
        // given
        String substring = "oO";
        // when
        foundKeywords = keywordRepository.findAllByNameIgnoreCaseContaining(substring);
        // then
        assertThat(foundKeywords.size()).isEqualTo(2);
        assertThat(foundKeywords.get(0)).isEqualTo(keyword4);
        assertThat(foundKeywords.get(1)).isEqualTo(keyword5);
    }

    /**
     * Tests findById method successfully returns a keyword
     */
    @Test
    void whenFindByExistingId_ThenReturnKeyword() {
        // Given
        entityManager.persistAndFlush(keyword2);
        // When
        foundKeyword = keywordRepository.findById(keyword2.getId());
        // Then
        assertThat(foundKeyword).isPresent();
        assertThat(keyword2.getId()).isEqualTo(foundKeyword.get().getId());
        assertThat(keyword2.getName()).isEqualTo(foundKeyword.get().getName());
    }

    /**
     * Tests findById method when the keyword doesn't exist returns an empty Optional
     */
    @Test
    void whenFindByUnknownId_ThenReturnKeyword() {
        // Given
        Integer fakeId = 100;
        // When
        foundKeyword = keywordRepository.findById(fakeId);
        // Then
        assertThat(foundKeyword).isNotPresent();
    }
}
