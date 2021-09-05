package ru.sklyarov.spring.security.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sklyarov.spring.security.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
