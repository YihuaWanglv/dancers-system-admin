package com.dancers.service.system.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMenuRepository extends JpaRepository<SystemMenu, Long> {

    List<SystemMenu> findByMenuName(@Param("menuName") String menuName);
}
