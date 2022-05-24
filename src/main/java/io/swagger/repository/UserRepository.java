package io.swagger.repository;

import io.swagger.model.entity.User;
import io.swagger.model.user.UserIbanSearchDTO;
import io.swagger.model.user.UserSearchDTO;
import io.swagger.model.utils.DTOEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    List<User> findUserByFirstnameContainingIgnoreCaseAndLastnameContainingIgnoreCase(String firstname, String lastname);
    @Query(name = "findUserByIban", nativeQuery = true)
    UserIbanSearchDTO findUserByIban(@Param("iban") String iban);

    @Query(name = "findUsersWithAccount", nativeQuery = true)
    List<UserIbanSearchDTO> findUsersWithAccount(@Param("firstname") String firstname, @Param("lastname") String lastname);

    @Query(name = "findUsersWithNoAccount", nativeQuery = true)
    List<UserSearchDTO> findUsersNoAccount(@Param("firstname") String firstname, @Param("lastname") String lastname);

    @Query(name = "findUsersAll", nativeQuery = true)
    List<UserIbanSearchDTO> findUsersAll(@Param("firstname") String firstname, @Param("lastname") String lastname);
}
