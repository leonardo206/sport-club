package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.Organization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {}
