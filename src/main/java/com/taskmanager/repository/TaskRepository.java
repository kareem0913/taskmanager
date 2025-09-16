package com.taskmanager.repository;

import com.taskmanager.model.entity.Task;
import com.taskmanager.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    Page<Task> findAllByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Task c SET c.status = :status WHERE c.id = :id AND c.user.id = :userId")
    void updateStatusById(@Param("id") Long id, @Param("status") TaskStatus status, @Param("userId") Long userId);
}
