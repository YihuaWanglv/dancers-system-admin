package com.dancers.service.system.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "corps", path = "corps")
@Repository
public interface SystemCorpRepository extends JpaRepository<SystemCorp, Long> {

    List<SystemCorp> findByCorpName(@Param("corpName") String corpName);
}
