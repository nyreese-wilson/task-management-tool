package com.example.taskmanagementtoolbackend.repository;

import com.example.taskmanagementtoolbackend.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
