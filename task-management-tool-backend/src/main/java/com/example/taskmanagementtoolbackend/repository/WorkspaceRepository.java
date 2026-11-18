package com.example.taskmanagementtoolbackend.repository;

import com.example.taskmanagementtoolbackend.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}

