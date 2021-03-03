package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.Organization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {
    List<Organization> findAllByUserId(String user_id);

    @Query("select id, organizationName, taxNumber, status, user from Organization where id =:id and user =:userId")
    Optional<Organization> findOneByUserId(@Param("id") Long id, @Param("userId") String userId);

    @Query("select id, organizationName, taxNumber, status, user from Organization where user =:userId")
    Optional<Organization> findByUserId(@Param("userId") String userId);

    Optional<Organization> findOneByOrganizationName(String name);
}
