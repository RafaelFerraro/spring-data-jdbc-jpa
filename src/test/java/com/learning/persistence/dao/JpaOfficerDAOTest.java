package com.learning.persistence.dao;

import com.learning.persistence.entities.Officer;
import com.learning.persistence.entities.Rank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JpaOfficerDAOTest {
  @Qualifier("jpaOfficerDAO")
  @Autowired
  private OfficerDAO officerDAO;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  public void testSave() {
    Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
    officerDAO.save(officer);
    assertNotNull(officer.getId());
  }

  @Test
  public void findOneIfExists() {
    var ids = jdbcTemplate.query(
        "Select id from officers",
        (rs, rowNum) -> rs.getInt("id")
    );

    ids.forEach(id -> {
      Optional<Officer> officer = officerDAO.findById(id);
      assertTrue(officer.isPresent());
      assertEquals(id, officer.get().getId());
    });
  }

  @Test
  public void findOneThatDoesNotExist() {
    Optional<Officer> officer = officerDAO.findById(999);
    assertTrue(officer.isEmpty());
  }

  @Test
  public void findAll() {
    var expected = Arrays.asList("Kirk", "Picard", "Sisko", "Janeway", "Archer");
    List<String> officerNames = officerDAO.findAll().stream()
        .map(Officer::getLastName).collect(Collectors.toList());

    assertEquals(officerNames, expected);
  }

  @Test
  public void count() {
    assertEquals(5, officerDAO.count());
  }

  @Test
  public void delete() {
    List<Integer> ids = jdbcTemplate.query(
        "Select id from officers",
        (rs, rowNum) -> rs.getInt("id")
    );

    ids.forEach(id -> {
      Optional<Officer> officer = officerDAO.findById(id);
      officerDAO.delete(officer.get());
    });

    assertEquals(0, officerDAO.count());
  }

  @Test
  public void existsById() {
    List<Long> ids = jdbcTemplate.query(
        "Select id from officers",
        (rs, rowNum) -> rs.getLong("id")
    );

    ids.forEach(id -> assertTrue(officerDAO.existsById(id.intValue())));
  }
}
