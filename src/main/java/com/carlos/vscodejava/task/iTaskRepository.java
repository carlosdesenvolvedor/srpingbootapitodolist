package com.carlos.vscodejava.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface iTaskRepository extends JpaRepository<TaskModel,UUID> {
    
}
