package com.ishan.springtaskmgr;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@RestController
public class TaskController {

    private final List<Task> taskList;
    private AtomicInteger taskId = new AtomicInteger(0);

    public TaskController() {
        this.taskList = new ArrayList<>();
        taskList.add(new Task(taskId.incrementAndGet(), "Task 1", "Description 1", new Date()));
        taskList.add(new Task(taskId.incrementAndGet(), "Task 2", "Description 2", new Date()));
        taskList.add(new Task(taskId.incrementAndGet(), "Task 3", "Description 3", new Date()));
    }

    /**
     * Show all existing tasks
     * GET /tasks
     *
     * @return List of tasks
     */
    @GetMapping("/tasks")
    private List<Task> getTasks() {
        return taskList;
    }

    /**
     * Create a new task
     * POST /tasks
     * Body:
     * <pre>
     *      {
     *          "title": "Task 4",
     *          "description": "Description 4",
     *          "dueDate": "2021-01-01"
     *      }
     *  </pre>
     *
     * @param task Task object sent by client
     * @return Task object created
     */
    @PostMapping("/tasks")
    Task createTask(@RequestBody Task task) {
        var newTask = new Task(taskId.incrementAndGet(), task.getTitle(), task.getDescription(), task.getDueDate());
        this.taskList.add(newTask);
        return newTask;
    }

    /**
     * Get a task by id
     *
     * @param id
     * @return Task object
     */
    @GetMapping("/tasks/{id}")
    Task getTask(@PathVariable("id") Integer id) {
        return taskList.stream().filter(task -> task.getId() == id).findFirst().orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Delete a task by given id
     *
     * @param id Task id to delete
     * @return the deleted task
     */
    @DeleteMapping("/tasks/{id}")
    Task deleteTask(@PathVariable("id") Integer id) {
        Optional<Task> optionalTask = taskList.stream().filter(task -> task.getId() == id).findFirst();

        if (optionalTask.isPresent()) {
            taskList.remove(optionalTask.get());
            return optionalTask.get();
        }
        throw new ResourceNotFoundException("Task with id: " + id + " not found");
    }

    /**
     * Update a task by given id
     *
     * @param id   Task id to update
     * @param task Task object sent by client
     * @return the updated task
     */
    @PatchMapping("/tasks/{id}")
    Task updateTask(@PathVariable("id") Integer id, @RequestBody Task task) {
        Optional<Task> optionalTask = taskList.stream().filter(item -> item.getId() == id).findFirst();
        if (optionalTask.isEmpty()) {
            throw new ResourceNotFoundException("Task with id: " + id + " not found");
        }

        Task taskToUpdate = optionalTask.get();
        if (task.getTitle() != null) {
            taskToUpdate.setTitle(task.getTitle());
        }
        if (task.getDescription() != null) {
            taskToUpdate.setDescription(task.getDescription());
        }
        if (task.getDueDate() != null) {
            taskToUpdate.setDueDate(task.getDueDate());
        }
        return taskToUpdate;
    }
}
