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

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        task.setId(UUID.randomUUID());
        task.setIsCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        return savedTask;
    }

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    public void deleteTaskById(UUID id) {
        taskRepository.deleteById(id);
    }

    public boolean setTaskAsCompleted(UUID id) {
        int updatedTasks = taskRepository.setTaskAsCompleted(id);
        return updatedTasks > 0;
    }

}
