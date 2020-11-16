package com.dancers.service.system.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemOrgRepository extends JpaRepository<SystemOrg, Long> {

    List<SystemOrg> findByOrgName(@Param("orgName") String orgName);
}
