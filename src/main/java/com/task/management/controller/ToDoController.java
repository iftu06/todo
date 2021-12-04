package com.task.management.controller;

import com.task.management.Utillity.ApiResponse;
import com.task.management.Utillity.ErrorMapper;
import com.task.management.Utillity.ToDoSearchField;
import com.task.management.dto.ToDoDto;
import com.task.management.error.ReturnStatus;
import com.task.management.model.ToDo;
import com.task.management.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Divineit-Iftekher on 8/8/2017.
 */
@RestController
public class ToDoController {


    @Autowired
    ToDoService toDoService;

    @CrossOrigin
    @PostMapping(value = "/todos")
    public Object save(@Valid @RequestBody ToDo toDo, BindingResult res) {

        if (res.hasErrors()) {
            Map<String, String> errorMap = ErrorMapper.mapError(res.getFieldErrors());
            return ApiResponse.builder().body(errorMap)
                    .status(ReturnStatus.ERROR)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        try {
            ToDoDto toDoDto = toDoService.create(toDo);
            return ResponseEntity.ok()
                    .body(ApiResponse.builder().body(toDoDto)
                            .httpStatus(HttpStatus.CREATED)
                            .status(ReturnStatus.SUCCESS)
                            .message("ToDO Created Successfully")
                            .build());
        } catch (HttpClientErrorException exp) {
            return ApiResponse.builder()
                    .status(ReturnStatus.ERROR)
                    .httpStatus(HttpStatus.NOT_IMPLEMENTED)
                    .message("Reason Unknown")
                    .build();

        }

    }

    @CrossOrigin
    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public Object list() {
        try {
            List<ToDoDto> projects = toDoService.getToDos();
            return ApiResponse.builder()
                    .httpStatus(HttpStatus.FOUND)
                    .status(ReturnStatus.SUCCESS)
                    .body(projects)
                    .build();
        } catch (HttpClientErrorException exp) {
            return ApiResponse.builder()
                    .status(ReturnStatus.ERROR)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/todos/{id}")
    public Object list(@PathVariable Integer id) {
        ToDoDto toDoDto = toDoService.getToDo(id);
        return ApiResponse.builder()
                .body(toDoDto).status(ReturnStatus.SUCCESS)
                .httpStatus(HttpStatus.FOUND).build();

    }


    @CrossOrigin
    @DeleteMapping(value = "/todos/delete/{id}")
    public Object delete(@PathVariable Integer id) {

        toDoService.remove(id);

        return ApiResponse.builder()
                .status(ReturnStatus.SUCCESS)
                .httpStatus(HttpStatus.NO_CONTENT)
                .message("Successfully deleted")
                .build();

    }


    @CrossOrigin
    @PutMapping(value = "/todos/{id}")
    public Object update(@RequestBody ToDo toDo, @PathVariable Integer id,
                         BindingResult res) {

        toDo.setId(id);
        if (res.hasErrors()) {
            Map<String, String> errorMap = ErrorMapper.mapError(res.getFieldErrors());
            return ApiResponse.builder().body(errorMap)
                    .status(ReturnStatus.ERROR)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        ToDoDto taskDto = toDoService.update(toDo);
        return ResponseEntity.ok()
                .body(ApiResponse.builder().body(taskDto)
                        .httpStatus(HttpStatus.CREATED)
                        .status(ReturnStatus.SUCCESS)
                        .message("Todo Updated Successfully")
                        .build());


    }

    @CrossOrigin
    @RequestMapping(value = "/todos/search", method = RequestMethod.GET)
    public ApiResponse searchTask(ToDoSearchField seachField) {
        List<ToDoDto> tasks = toDoService.searchToDo(seachField);
        return ApiResponse.builder().httpStatus(HttpStatus.FOUND)
                .status(ReturnStatus.SUCCESS)
                .body(tasks)
                .build();

    }


    @CrossOrigin
    @PutMapping(value = "/todos/markAsDone/{id}")
    public Object markasDone(@PathVariable Integer id) {

        ToDoDto taskDto = toDoService.markAsDone(id);
        return ResponseEntity.ok()
                .body(ApiResponse.builder().body(taskDto)
                        .httpStatus(HttpStatus.OK)
                        .status(ReturnStatus.SUCCESS)
                        .message("TODO is completed")
                        .build());


    }


}
