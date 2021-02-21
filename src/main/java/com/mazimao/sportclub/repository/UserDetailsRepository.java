package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.UserDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>, JpaSpecificationExecutor<UserDetails> {}
