package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.ClubManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClubManager entity.
 */
@Repository
public interface ClubManagerRepository extends JpaRepository<ClubManager, Long>, JpaSpecificationExecutor<ClubManager> {
    Optional<ClubManager> findByUserId(String id);

    @Query(
        value = "select distinct clubManager from ClubManager clubManager left join fetch clubManager.clubs",
        countQuery = "select count(distinct clubManager) from ClubManager clubManager"
    )
    Page<ClubManager> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct clubManager from ClubManager clubManager left join fetch clubManager.clubs")
    List<ClubManager> findAllWithEagerRelationships();

    @Query("select clubManager from ClubManager clubManager left join fetch clubManager.clubs where clubManager.id =:id")
    Optional<ClubManager> findOneWithEagerRelationships(@Param("id") Long id);
}
