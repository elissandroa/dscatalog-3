package br.com.elissandro.DsCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.elissandro.DsCatalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
