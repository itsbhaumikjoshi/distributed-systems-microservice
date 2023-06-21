package com.tasksmicroservices.taskservice.repository;

import com.tasksmicroservices.taskservice.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByIdAndUserId(UUID id, UUID userId);

    List<Task> findByUserId(UUID userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE tasks t set t.isCompleted  = true where t.id = :id AND t.userId = :userId")
    int setTaskAsCompleted(@Param("id") UUID id, @Param("userId") UUID userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM tasks t WHERE t.id = :id AND t.userId = :userId")
    void deleteByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

}
