package com.example.springtaskmgrv2.controllers;

import com.example.springtaskmgrv2.entities.ErrorResponse;
import com.example.springtaskmgrv2.entities.NoteEntity;
import com.example.springtaskmgrv2.entities.TaskEntity;
import com.example.springtaskmgrv2.services.TasksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    /**
     * Show all existing tasks
     * GET /tasks
     *
     * @return List of tasks
     */
    // @GetMapping is a Spring annotation that tells Spring to map this method to the /tasks path
    // and to only respond to GET requests
    @GetMapping("") // GET /tasks
    public List<TaskEntity> getAllTasks() {
        return tasksService.getAllTasks();
    }

    @PostMapping("")  // POST /tasks
    // @RequestBody is a Spring annotation that tells Spring to deserialize the JSON body of the request into a TaskEntity object
    // and pass it to the method as a parameter
    public TaskEntity createTask(@RequestBody TaskEntity task) {
        return tasksService.createTask(task);
    }

    @PutMapping("/{id}")  // PUT /tasks/1
    public TaskEntity updateTask(@PathVariable String id, @RequestBody TaskEntity task) {
        return tasksService.updateTask(id, task);
    }

    @PatchMapping("/{id}")  // PATCH /tasks/1
    public TaskEntity patchTask(@PathVariable String id, @RequestBody TaskEntity task) {
        return tasksService.patchTask(id, task);
    }

    @DeleteMapping("/{id}")  // DELETE /tasks/1
    public void deleteTask(@PathVariable String id) {
        tasksService.deleteTask(id);
    }

    @GetMapping("/{id}") //  GET /tasks/1
    // @PathVariable is a Spring annotation that tells Spring to get the value of the {id} path variable
    // and pass it to the method as a parameter
    public TaskEntity getTaskById(@PathVariable String id) {
        return tasksService.getTaskById(id);
    }

    @GetMapping(params = "title") // GET /tasks?title=foo
    public List<TaskEntity> getTasksByTitle(@RequestParam String title) {
        return tasksService.getTasksByTitle(title);
    }

    @GetMapping(params = "completed") // GET /tasks?completed=true
    public List<TaskEntity> getTasksByCompleted(@RequestParam boolean completed) {
        return tasksService.getTasksByCompleted(completed);
    }

    @GetMapping("/{id}/notes") // GET /tasks/1/notes
    public List<NoteEntity> getNotesByTaskId(@PathVariable String id) {
        return tasksService.getNotesByTaskId(id);
    }

    @PostMapping("/{id}/notes") // POST /tasks/1/notes
    public NoteEntity createNoteForTask(@PathVariable String id, @RequestBody NoteEntity note) {
        return tasksService.createNoteForTask(id, note);
    }

    @DeleteMapping("/{taskId}/notes/{noteId}") // DELETE /tasks/1/notes/1
    public void deleteNoteForTask(@PathVariable String taskId, @PathVariable String noteId) {
        tasksService.deleteNoteForTask(taskId, noteId);
    }


    @ExceptionHandler(TasksService.TaskNotFoundException.class)
    ResponseEntity<ErrorResponse> handleErrors(TasksService.TaskNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
