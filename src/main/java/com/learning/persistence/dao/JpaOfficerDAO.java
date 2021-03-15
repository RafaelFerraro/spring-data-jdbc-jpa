package com.learning.persistence.dao;

import com.learning.persistence.entities.Officer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOfficerDAO implements OfficerDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Officer save(Officer officer) {
    entityManager.persist(officer);
    return officer;
  }

  @Override
  public Optional<Officer> findById(Integer id) {
    return Optional.ofNullable(entityManager.find(Officer.class, id));
  }

  @Override
  public List<Officer> findAll() {
    var query = entityManager.createQuery(
        "Select o from Officer o",
        Officer.class);

    return query.getResultList();
  };

  @Override
  public long count() {
    var query = entityManager.createQuery(
        "Select count(o.id) from Officer o",
        Long.class
    );

    return query.getSingleResult();
  }

  @Override
  public void delete(Officer officer) {
    entityManager.remove(officer);
  }

  @Override
  public boolean existsById(Integer id) {
    var count = entityManager.createQuery(
        "Select count(o.id) from Officer o where o.id=:id",
        Long.class
    ).setParameter("id", id).getSingleResult();

    return count > 0;
  }
}
