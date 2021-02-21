package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.ClubType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClubType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClubTypeRepository extends JpaRepository<ClubType, Long>, JpaSpecificationExecutor<ClubType> {}
