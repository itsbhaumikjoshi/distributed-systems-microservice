package com.tasksmicroservices.taskservice.repository;

import com.tasksmicroservices.taskservice.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE tasks t set t.isCompleted  = true where t.id = :id")
    int setTaskAsCompleted(@Param("id") UUID id);
}
