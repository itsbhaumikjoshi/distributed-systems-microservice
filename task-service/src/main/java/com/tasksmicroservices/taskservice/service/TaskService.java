package com.tasksmicroservices.taskservice.service;

import com.tasksmicroservices.taskservice.model.Task;
import com.tasksmicroservices.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAllTasksForAUser(UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task save(Task task) {
        task.setId(UUID.randomUUID());
        task.setIsCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        return savedTask;
    }

    public Optional<Task> getTaskByIdAndUserId(UUID userId, UUID id) {
        return taskRepository.findByIdAndUserId(id, userId);
    }

    public void deleteTaskByIdForUserId(UUID userId,UUID id) {
        taskRepository.deleteByIdAndUserId(id, userId);
    }

    public boolean setTaskAsCompleted(UUID userId, UUID id) {
        int updatedTasks = taskRepository.setTaskAsCompleted(id, userId);
        return updatedTasks > 0;
    }

}
