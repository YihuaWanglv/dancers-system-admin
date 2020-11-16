package com.dancers.service.system.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    List<SystemUser> findByUsername(@Param("username") String username);

    @Query("SELECT u.systemRoles FROM SystemUser u WHERE u.userId = :userId")
    Set<SystemRole> getUserTagsByUserId(@Param("userId") long userId);

    @Query(value = "select ur.role_id from system_user_role ur " +
            "left join system_user u on ur.user_id=u.user_id " +
            "left join system_role r on ur.role_id=r.role_id " +
            "where u.state = 1 and u.is_deleted=0 and r.is_deleted=0 and r.state=1 and ur.user_id=:userId",
            nativeQuery = true)
    List<Long> getRoleIdListByUserId(@Param("userId") long userId);

    @Query(value = "select ur.user_id as userId, GROUP_CONCAT(ur.role_id) as roleIds from system_user_role ur " +
            "left join system_user u on ur.user_id=u.user_id " +
            "left join system_role r on ur.role_id=r.role_id " +
            "where u.is_deleted=0 and u.state=1 and r.is_deleted=0 and r.state=1 " +
            "and ur.user_id in :userIdList " +
            "GROUP BY ur.user_id",
            nativeQuery = true)
    List<UserRoleIds> getRoleIdsByUserIdList(@Param("userIdList") List<Long> userIdList);

    public static interface UserRoleIds {
        Long getUserId();
        String getRoleIds();
    }
}
