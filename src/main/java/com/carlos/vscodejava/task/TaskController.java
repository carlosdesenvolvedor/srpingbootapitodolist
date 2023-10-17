package com.carlos.vscodejava.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    
    @Autowired
    private iTaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity creater(@RequestBody TaskModel taskModel, HttpServletRequest request){
        
        var idUser = request.getAttribute("idUser");

        taskModel.setIdUser((UUID) idUser);
        
        var currentDate = LocalDateTime.now();
        
        if(currentDate.isAfter(taskModel.getStartAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data atual");

        }


        var task = this.taskRepository.save(taskModel);
    
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

}
