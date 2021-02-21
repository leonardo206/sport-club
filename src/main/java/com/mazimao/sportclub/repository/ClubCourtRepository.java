package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.ClubCourt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClubCourt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClubCourtRepository extends JpaRepository<ClubCourt, Long>, JpaSpecificationExecutor<ClubCourt> {}
