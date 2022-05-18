package io.swagger.repository;

import io.swagger.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByFirstnameEquals(String firstname);

    User findByLastnameEquals(String lastname);

    List<User> findByFirstnameIgnoreCaseContainingAndLastnameIgnoreCaseContaining(String firstname, String lastname);
}
