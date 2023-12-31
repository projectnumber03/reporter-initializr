package ru.plorum.reporterinitializr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.plorum.reporterinitializr.model.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("select r from Role as r join fetch r.privileges where r.id = :id")
    Optional<Role> findById(@Param("id") final UUID id);

    Role findByName(final String name);

    @Query("select count(r) from Role as r")
    Long countAll();

}
