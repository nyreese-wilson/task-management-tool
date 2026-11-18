package com.example.taskmanagementtoolbackend.config;

import com.example.taskmanagementtoolbackend.model.*;
import com.example.taskmanagementtoolbackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final WorkspaceRepository workspaceRepo;
    private final BoardRepository boardRepo;
    private final TaskListRepository listRepo;
    private final TaskRepository taskRepo;

    public DataSeeder(UserRepository userRepo, WorkspaceRepository workspaceRepo,
                      BoardRepository boardRepo, TaskListRepository listRepo, TaskRepository taskRepo) {
        this.userRepo = userRepo;
        this.workspaceRepo = workspaceRepo;
        this.boardRepo = boardRepo;
        this.listRepo = listRepo;
        this.taskRepo = taskRepo;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            User defaultUser = userRepo.save(new User("dev@portfolio.com", "Demo Developer"));
            Workspace workspace = workspaceRepo.save(new Workspace("My First Workspace", defaultUser));
            Board board = boardRepo.save(new Board("Portfolio Project Roadmap", workspace));

            TaskList todo = listRepo.save(new TaskList("To-Do", 0, board));
            TaskList inProgress = listRepo.save(new TaskList("In Progress", 1, board));
            TaskList done = listRepo.save(new TaskList("Done", 2, board));

            taskRepo.save(new Task("Design Database Schema", "Create ERD and foreign keys", TaskStatus.DONE, 0, done));
            taskRepo.save(new Task("Build REST APIs", "Implement Board, List, and Task endpoints", TaskStatus.IN_PROGRESS, 0, inProgress));
            taskRepo.save(new Task("Build React Kanban Board", "Render columns and cards", TaskStatus.TODO, 0, todo));
        }
    }
}

