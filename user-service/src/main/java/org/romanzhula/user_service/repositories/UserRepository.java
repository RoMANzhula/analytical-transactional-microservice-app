package org.romanzhula.user_service.repositories;

import org.romanzhula.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByGithubId(String githubId);

}
