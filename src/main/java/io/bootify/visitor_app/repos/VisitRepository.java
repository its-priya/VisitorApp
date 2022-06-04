package io.bootify.visitor_app.repos;

import io.bootify.visitor_app.domain.Flat;
import io.bootify.visitor_app.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {
    public List<Visit> findByFlat(Flat flat);
}
