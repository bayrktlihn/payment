package io.bayrktlihn.payment.repository;

import io.bayrktlihn.payment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.authorities a left join fetch u.roles r where u.active = true and u.username = :username and a.active = true and r.active = true")
    Optional<User> findUsersWithAuthoritiesAndRolesByUsername(@Param("username") String username);
}
