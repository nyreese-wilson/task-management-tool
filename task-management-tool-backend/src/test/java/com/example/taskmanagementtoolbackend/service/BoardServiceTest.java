package com.example.taskmanagementtoolbackend.service;

import com.example.taskmanagementtoolbackend.dto.CreateTaskRequest;
import com.example.taskmanagementtoolbackend.dto.TaskDto;
import com.example.taskmanagementtoolbackend.exception.ResourceNotFoundException;
import com.example.taskmanagementtoolbackend.model.*;
import com.example.taskmanagementtoolbackend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock private WorkspaceRepository workspaceRepo;
    @Mock private BoardRepository boardRepo;
    @Mock private TaskListRepository listRepo;
    @Mock private TaskRepository taskRepo;

    @InjectMocks private BoardService boardService;

    @Test
    void addTaskToList_ShouldAssignNextPositionAndTodoStatus() {
        Board board = new Board("Test Board", new Workspace());
        TaskList list = new TaskList("To-Do", 0, board);
        list.getTasks().add(new Task()); // 1 existing task -> next position should be 1

        when(listRepo.findById(1L)).thenReturn(Optional.of(list));
        when(taskRepo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskDto result = boardService.addTaskToList(1L, new CreateTaskRequest("Write Tests", "Add Mockito tests"));

        assertEquals("Write Tests", result.title());
        assertEquals(TaskStatus.TODO, result.status());
        assertEquals(1, result.position());
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    void getBoardById_WhenNotFound_ShouldThrowException() {
        when(boardRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> boardService.getBoardById(99L));
    }
}

