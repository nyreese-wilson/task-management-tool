package com.example.taskmanagementtoolbackend.service;

import com.example.taskmanagementtoolbackend.dto.*;
import com.example.taskmanagementtoolbackend.exception.ResourceNotFoundException;
import com.example.taskmanagementtoolbackend.model.*;
import com.example.taskmanagementtoolbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final WorkspaceRepository workspaceRepo;
    private final BoardRepository boardRepo;
    private final TaskListRepository listRepo;
    private final TaskRepository taskRepo;

    @Transactional(readOnly = true)
    public List<BoardDto> getAllBoards() {
        return boardRepo.findAll().stream().map(this::toBoardDto).toList();
    }

    @Transactional(readOnly = true)
    public BoardDto getBoardById(Long boardId) {
        Board board = boardRepo.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));
        return toBoardDto(board);
    }

    @Transactional
    public BoardDto createBoard(CreateBoardRequest request) {
        Workspace workspace = workspaceRepo.findById(request.workspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with id: " + request.workspaceId()));

        Board board = boardRepo.save(new Board(request.title(), workspace));
        return toBoardDto(board);
    }

    @Transactional
    public TaskListDto addListToBoard(Long boardId, CreateListRequest request) {
        Board board = boardRepo.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));

        int nextPosition = board.getLists().size();
        TaskList newList = listRepo.save(new TaskList(request.title(), nextPosition, board));
        return toTaskListDto(newList);
    }

    @Transactional
    public TaskDto addTaskToList(Long listId, CreateTaskRequest request) {
        TaskList list = listRepo.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("List not found with id: " + listId));

        int nextPosition = list.getTasks().size();
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TODO)
                .position(nextPosition)
                .taskList(list)
                .build();

        return toTaskDto(taskRepo.save(task));
    }

    @Transactional
    public TaskDto updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (request.title() != null && !request.title().isBlank()) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.targetListId() != null) {
            TaskList targetList = listRepo.findById(request.targetListId())
                    .orElseThrow(() -> new ResourceNotFoundException("Target list not found with id: " + request.targetListId()));
            task.setTaskList(targetList);
        }
        if (request.position() != null) {
            task.setPosition(request.position());
        }

        return toTaskDto(taskRepo.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepo.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        taskRepo.deleteById(taskId);
    }

    private BoardDto toBoardDto(Board board) {
        List<TaskListDto> listDtos = board.getLists().stream()
                .map(this::toTaskListDto)
                .toList();
        return new BoardDto(board.getId(), board.getTitle(), board.getWorkspace().getId(), listDtos);
    }

    private TaskListDto toTaskListDto(TaskList list) {
        List<TaskDto> taskDtos = list.getTasks().stream()
                .map(this::toTaskDto)
                .toList();
        return new TaskListDto(list.getId(), list.getTitle(), list.getPosition(), taskDtos);
    }

    private TaskDto toTaskDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPosition(),
                task.getTaskList().getId()
        );
    }
}
