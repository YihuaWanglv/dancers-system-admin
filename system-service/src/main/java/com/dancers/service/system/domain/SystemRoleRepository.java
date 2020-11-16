package com.dancers.service.system.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {

    List<SystemRole> findByRoleName(@Param("roleName") String roleName);
}
