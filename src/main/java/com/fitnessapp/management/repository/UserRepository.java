package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.RefreshToken;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.repository.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    Optional<User> findUserByRole(@Param("role") Role role);

    @Query("SELECT u FROM User u JOIN u.refreshTokens rt WHERE rt = :refreshToken")
    Optional<User> findUserByRefreshToken(@Param("refreshToken") RefreshToken refreshToken);
}
