package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.Authority;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    List<Authority> findAllByNameIn(List<String> name);
}
