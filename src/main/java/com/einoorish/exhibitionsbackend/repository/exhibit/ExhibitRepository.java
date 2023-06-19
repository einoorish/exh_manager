package com.einoorish.exhibitionsbackend.repository.exhibit;

import com.einoorish.exhibitionsbackend.model.exhibit.Exhibit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {
    List<Exhibit> findByUserId(Long userId);
}
