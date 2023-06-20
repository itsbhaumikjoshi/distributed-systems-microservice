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
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        UUID idUUID = UUID.fromString(id);
        Optional<Task> task = taskService.getTaskById(idUUID);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(task.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable String id) {
        UUID UUIDId = UUID.fromString(id);
        taskService.deleteTaskById(UUIDId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<Object> setTaskAsCompleted(@PathVariable String id) {
        UUID UUIDId = UUID.fromString(id);
        if(taskService.setTaskAsCompleted(UUIDId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
