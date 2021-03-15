package com.learning.persistence.dao;

import com.learning.persistence.entities.Officer;
import com.learning.persistence.entities.Rank;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcOfficerDAO implements OfficerDAO {
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert insertOfficer;

  public JdbcOfficerDAO(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    insertOfficer = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("officers")
        .usingGeneratedKeyColumns("id");
  }

  @Override
  public Officer save(Officer officer) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("rank", officer.getRank());
    parameters.put("first_name", officer.getFirstName());
    parameters.put("last_name", officer.getLastName());

    Integer newId = (Integer) insertOfficer.executeAndReturnKey(parameters);

    officer.setId(newId);

    return officer;
  }

  @Override
  public Optional<Officer> findById(Integer id) {
    if (!existsById(id)) return Optional.empty();

    return Optional.of(jdbcTemplate.queryForObject(
        "SELECT * FROM officers WHERE id=?",
        new RowMapper<Officer>() {
          @Override
          public Officer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Officer(
                rs.getInt("id"),
                Rank.valueOf(rs.getString("rank")),
                rs.getString("first_name"),
                rs.getString("last_name")
            );
          }
        }, id
    ));
  }

  @Override
  public List<Officer> findAll() {
    return jdbcTemplate.query(
        "SELECT * FROM officers",
        (rs, rowNum) -> new Officer(
            rs.getInt("id"),
            Rank.valueOf(rs.getString("rank")),
            rs.getString("first_name"),
            rs.getString("last_name")
        )
    );
  }

  @Override
  public long count() {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM officers",
        Long.class
    );
  }

  @Override
  public void delete(Officer officer) {
    jdbcTemplate.update(
        "DELETE FROM officers WHERE id=?",
        officer.getId()
    );
  }

  @Override
  public boolean existsById(Integer id) {
    return jdbcTemplate.queryForObject(
        "SELECT EXISTS(SELECT 1 FROM officers WHERE id=?)",
        Boolean.class,
        id
    );
  }
}
