package com.tasksmicroservices.taskservice.controller;

import com.tasksmicroservices.taskservice.model.Task;
import com.tasksmicroservices.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@RequestHeader("X-USER-ID") String userId) {
        List<Task> tasks = taskService.findAllTasksForAUser(UUID.fromString(userId));
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestHeader("X-USER-ID") String userId, @RequestBody Task task) {
        task.setUserId(UUID.fromString(userId));
        Task createdTask = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        Optional<Task> task = taskService.getTaskByIdAndUserId(UUID.fromString(userId), UUID.fromString(id));
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(task.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        taskService.deleteTaskByIdForUserId(UUID.fromString(userId), UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<Object> setTaskAsCompleted(@RequestHeader("X-USER-ID") String userId, @PathVariable String id) {
        if(taskService.setTaskAsCompleted(UUID.fromString(userId), UUID.fromString(id))) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
