package com.example.springtaskmgrv2.services;

import com.example.springtaskmgrv2.entities.TaskEntity;
import com.example.springtaskmgrv2.repositories.NotesRepository;
import com.example.springtaskmgrv2.repositories.TasksRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TasksServiceTest {

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private TasksService tasksService;

    @Test
    public void testGetAllTasks() {

        List<TaskEntity> taskList = new ArrayList<>();
        taskList.add(TaskEntity.builder().title("Test Task").description("Test Description").completed(false).build());
        taskList.add(TaskEntity.builder().title("Test Task 2").description("Test Description 2").completed(false).build());

        when(tasksRepository.findAll()).thenReturn(taskList);

        var tasks = tasksService.getAllTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }
}
