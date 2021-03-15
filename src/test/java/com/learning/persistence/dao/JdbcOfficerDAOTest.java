package com.learning.persistence.dao;

import com.learning.persistence.entities.Officer;
import com.learning.persistence.entities.Rank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest // starts spring boot framework
@Transactional // rolls back each test by default
public class JdbcOfficerDAOTest {
  @Qualifier("jdbcOfficerDAO")
  @Autowired
  private OfficerDAO officerDAO;

  @Test
  public void save() {
    Officer officer = new Officer(Rank.LIEUTENANT, "Nuia", "Uhuru");
    officer = officerDAO.save(officer);
    assertNotNull(officer.getId());
  }

  @Test
  public void findByIdThatExists() {
    Optional<Officer> officer = officerDAO.findById(1);
    assertTrue(officer.isPresent());
    assertEquals(1, officer.get().getId().intValue());
  }

  @Test
  public void findByIdThatDoesNotExist() {
    Optional<Officer> officer = officerDAO.findById(999);
    assertTrue(officer.isEmpty());
  }

  @Test
  public void count() {
    assertEquals(5, officerDAO.count());
  }

  @Test
  public void findAll() {
    List<String> officerNames = officerDAO.findAll().stream().map(Officer::getLastName).collect(Collectors.toList());
    assertThat(officerNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
  }

  @Test
  public void delete() {
    IntStream.rangeClosed(1, 5)
        .forEach(id -> {
          Optional<Officer> officer = officerDAO.findById(id);
          assertTrue(officer.isPresent());
          officerDAO.delete(officer.get());
        });
    assertEquals(0, officerDAO.count());
  }

  @Test
  public void existsById() {
    IntStream.rangeClosed(1, 5)
        .forEach(id -> assertTrue(officerDAO.existsById(id)));
  }
}
