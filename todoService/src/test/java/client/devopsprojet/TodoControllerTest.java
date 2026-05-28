package client.devopsprojet;



import client.devopsprojet.controller.TodoController;
import client.devopsprojet.model.Todo;
import client.devopsprojet.service.TodoService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldGetAllTodos() throws Exception {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        when(todoService.getAllTodos()).thenReturn(List.of(todo));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));

        verify(todoService).getAllTodos();
    }


    @Test
    void shouldCreateTodo() throws Exception {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("New Todo");

        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Todo"));

        verify(todoService).createTodo(any(Todo.class));
    }


    @Test
    void shouldGetTodoById() throws Exception {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Todo detail");

        when(todoService.getTodoById(1L)).thenReturn(todo);

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Todo detail"));

        verify(todoService).getTodoById(1L);
    }


    @Test
    void shouldUpdateTodo() throws Exception {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Updated");

        when(todoService.updateTodo(eq(1L), any(Todo.class))).thenReturn(todo);

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));

        verify(todoService).updateTodo(eq(1L), any(Todo.class));
    }


    @Test
    void shouldDeleteTodo() throws Exception {

        doNothing().when(todoService).deleteTodo(1L);

        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService).deleteTodo(1L);
    }




}