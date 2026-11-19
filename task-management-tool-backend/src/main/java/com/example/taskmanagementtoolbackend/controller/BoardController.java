package com.example.taskmanagementtoolbackend.controller;

import com.example.taskmanagementtoolbackend.dto.*;
import com.example.taskmanagementtoolbackend.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }

    @PostMapping("/boards")
    public ResponseEntity<BoardDto> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(request));
    }

    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<TaskListDto> addList(@PathVariable Long boardId, @Valid @RequestBody CreateListRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.addListToBoard(boardId, request));
    }

    @PostMapping("/lists/{listId}/tasks")
    public ResponseEntity<TaskDto> addTask(@PathVariable Long listId, @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.addTaskToList(listId, request));
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(boardService.updateTask(taskId, request));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boardService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
