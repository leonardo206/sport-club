package com.mazimao.sportclub.repository;

import com.mazimao.sportclub.domain.Authority;
import com.mazimao.sportclub.domain.User;
import com.mazimao.sportclub.domain.UserDetails;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    Page<User> findAllByAuthoritiesIn(Pageable pageable, Set<Authority> authorities);

    @Query(
        "select user.id, user.login, user.firstName, user.lastName, user.email, user.imageUrl, user.activated, user.langKey " +
        "from User user left join Organization org on org.user=user.id where org.id <>:organizationId "
    )
    List<User> findAllInOrganization(String organizationId);
}
