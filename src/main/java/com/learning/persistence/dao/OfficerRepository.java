package com.learning.persistence.dao;

import com.learning.persistence.entities.Officer;
import com.learning.persistence.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {
  List<Officer> findByRank(@Param("rank") Rank rank);
  List<Officer> findByLastName(@Param("last_name") String lastName);
}
