package com.example.springtaskmgrv2.services;

import com.example.springtaskmgrv2.entities.NoteEntity;
import com.example.springtaskmgrv2.entities.TaskEntity;
import com.example.springtaskmgrv2.repositories.NotesRepository;
import com.example.springtaskmgrv2.repositories.TasksRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TasksService {
    final TasksRepository tasksRepository;

    final NotesRepository notesRepository;

    public TasksService(TasksRepository tasksRepository, NotesRepository notesRepository) {
        this.tasksRepository = tasksRepository;
        this.notesRepository = notesRepository;
    }

    public List<TaskEntity> getTasksByCompleted(boolean completed) {
        return tasksRepository.findAllByCompleted(completed);
    }

    public List<NoteEntity> getNotesByTaskId(String id) {
        Optional<TaskEntity> task = findTaskById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        return notesRepository.findAllByTaskId(Integer.parseInt(id));
    }

    public NoteEntity createNoteForTask(String id, NoteEntity note) {
        Optional<TaskEntity> task = findTaskById(id);
        if (task.isPresent()) {
            note.setTask(task.get());
            return notesRepository.save(note);
        }
        throw new TaskNotFoundException(id);
    }

    public void deleteNoteForTask(String taskId, String noteId) {
        Optional<TaskEntity> task = findTaskById(taskId);
        if (task.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }
        notesRepository.deleteById(Integer.parseInt(noteId));
    }

    public static class TaskNotFoundException extends IllegalArgumentException {
        public TaskNotFoundException(String id) {
            super("Task with id [" + id + "] not found");
        }
    }

    public void deleteTask(String id) {
        Optional<TaskEntity> task = findTaskById(id);
        if (task.isPresent()) {
            notesRepository.deleteAll(notesRepository.findAllByTaskId(task.get().getId()));
            tasksRepository.delete(task.get());
            return;
        }
        throw new TaskNotFoundException(id);
    }

    public List<TaskEntity> getTasksByTitle(String title) {
        return tasksRepository.findAllByTitleContainingIgnoreCase(title);
    }

    public List<TaskEntity> getAllTasks() {
        return tasksRepository.findAll();
    }

    public TaskEntity getTaskById(String id) {
        Optional<TaskEntity> task = findTaskById(id);
        if (task.isPresent()) {
            return task.get();
        }
        throw new TaskNotFoundException(id);
    }

    public TaskEntity createTask(TaskEntity task) {
        return tasksRepository.save(task);
    }

    public TaskEntity updateTask(String id, TaskEntity task) {
        return findTaskById(id)
                .map(t -> {
                    t.setTitle(task.getTitle());
                    t.setCompleted(task.getCompleted());
                    t.setDueDate(task.getDueDate());
                    return tasksRepository.save(t);
                })
                .orElseGet(() -> {
                    return tasksRepository.save(task);
                });
    }

    public TaskEntity patchTask(String id, TaskEntity task) {
        Optional<TaskEntity> optionalTask = findTaskById(id);

        if (!optionalTask.isEmpty()) {
            optionalTask.map(t -> {
                if (task.getTitle() != null) t.setTitle(task.getTitle());
                if (task.getDescription() != null) t.setDescription(task.getDescription());
                if (task.getCompleted() != null) t.setCompleted(task.getCompleted());
                if (task.getDueDate() != null) t.setDueDate(task.getDueDate());
                return tasksRepository.save(t);
            });
        }
        throw new TaskNotFoundException(id);
    }

    public Optional<TaskEntity> findTaskById(String id) {
        return tasksRepository.findById(Integer.parseInt(id));
    }
}

