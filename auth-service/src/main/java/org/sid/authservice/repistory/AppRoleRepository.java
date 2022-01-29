package org.sid.authservice.repistory;

import org.sid.authservice.entities.AppRole;
import org.sid.authservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
   AppRole findByRoleName(String roleName);
}
