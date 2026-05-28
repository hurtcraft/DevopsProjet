package client.devopsprojet;



import client.devopsprojet.model.Todo;
import client.devopsprojet.repository.TodoRepository;
import client.devopsprojet.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    private TodoRepository todoRepository;
    private TodoService todoService;

    @BeforeEach
    void setUp() {

        todoRepository = mock(TodoRepository.class);

        WebClient webClient = WebClient.builder().build();

        todoService = new TodoService(todoRepository, webClient);
    }

    @Test
    void shouldCreateTodo() {

        Todo todo = new Todo();
        todo.setTitle("Test Todo");

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo saved = todoService.createTodo(todo);

        assertNotNull(saved);
        assertEquals("Test Todo", saved.getTitle());

        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void shouldReturnAllTodos() {

        Todo todo1 = new Todo();
        todo1.setTitle("Todo 1");

        Todo todo2 = new Todo();
        todo2.setTitle("Todo 2");

        when(todoRepository.findAll())
                .thenReturn(List.of(todo1, todo2));

        List<Todo> todos = todoService.getAllTodos();

        assertEquals(2, todos.size());
    }

    @Test
    void shouldReturnTodoById() {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test");

        when(todoRepository.findById(1L))
                .thenReturn(Optional.of(todo));

        Todo result = todoService.getTodoById(1L);

        assertEquals("Test", result.getTitle());
    }

    @Test
    void shouldDeleteTodo() {

        when(todoRepository.existsById(1L)).thenReturn(true);

        todoService.deleteTodo(1L);

        verify(todoRepository, times(1))
                .deleteById(1L);
    }
}