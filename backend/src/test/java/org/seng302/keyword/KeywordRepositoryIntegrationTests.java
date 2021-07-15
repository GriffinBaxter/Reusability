package org.seng302.keyword;

import org.junit.jupiter.api.BeforeAll;
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

    private static Keyword keyword1;
    private static Keyword keyword2;

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
        entityManager.persistAndFlush(keyword1);

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
