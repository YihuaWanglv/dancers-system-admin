package com.dancers.service.system.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;

@NoRepositoryBean
public interface CustomRepository<T, ID> extends JpaRepository<T, ID> {

    @Override
    @RestResource(exported = false)
    void deleteById(ID id);

    @Override
    @RestResource(exported = false)
    void delete(T t);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends T> t);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
