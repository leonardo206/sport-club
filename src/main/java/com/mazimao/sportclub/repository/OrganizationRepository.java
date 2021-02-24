package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.EntityAuditEvent;
import com.mazimao.sportclub.domain.Organization;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {
    Page<Organization> findAllByUserId(String user_id, Pageable pageable);

    @Query("select id, organizationName, taxNumber, status, user from Organization where id =:id and user =:userId")
    Optional<Organization> findOneByUserId(@Param("id") Long id, @Param("user_id") String userId);
}
