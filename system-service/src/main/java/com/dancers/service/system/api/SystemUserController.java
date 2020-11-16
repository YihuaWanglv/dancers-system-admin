package com.dancers.service.system.api;

import com.dancers.service.system.domain.SystemUser;
import com.dancers.service.system.domain.SystemUserRepository;
import com.dancers.service.system.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class SystemUserController {

    @Resource
    SystemUserService systemUserService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SystemUser user) {

        systemUserService.create(user);
        return ResponseEntity.ok().body(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        systemUserService.delete(id);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SystemUser user) {
        user.setUserId(id);
        systemUserService.update(user);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SystemUser user = systemUserService.getById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<?> list(Pageable pageable, SystemUser user, Integer page) {
        log.info("pageable={}", pageable.toString());
        log.info("page={}", page);
        if (page != null && page > 0) {
            Page<SystemUser> list = systemUserService.list(user, pageable);
            return ResponseEntity.ok().body(list);
        } else {
            List<SystemUser> list = systemUserService.list(user);
            return ResponseEntity.ok().body(list);
        }
    }

    @Resource
    SystemUserRepository systemUserRepository;

    @GetMapping("/test")
    public ResponseEntity<?> test(Long userId) {
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        return ResponseEntity.ok().body(systemUserRepository.getRoleIdsByUserIdList(userIdList));
    }
}
