package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.ClubCourtType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClubCourtType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClubCourtTypeRepository extends JpaRepository<ClubCourtType, Long>, JpaSpecificationExecutor<ClubCourtType> {}
