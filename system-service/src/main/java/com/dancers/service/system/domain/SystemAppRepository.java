package com.dancers.service.system.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "apps", path = "apps")
@Repository
public interface SystemAppRepository extends JpaRepository<SystemApp, Long> {

    //    @RestResource(path = "names", rel = "names")
    List<SystemApp> findByAppName(@Param("appName") String appName);
}
