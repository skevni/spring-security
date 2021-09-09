package ru.sklyarov.spring.security.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sklyarov.spring.security.entities.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    @Override
    Optional<Role> findById(Long id);

}
