package client.devopsprojet;



import client.devopsprojet.model.Todo;
import client.devopsprojet.repository.TodoRepository;
import client.devopsprojet.service.SendNotificationService;
import client.devopsprojet.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@ActiveProfiles("test")

class TodoServiceTest {

    private TodoRepository todoRepository;
    private SendNotificationService sendNotificationService;
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoRepository = mock(TodoRepository.class);
        sendNotificationService = mock(SendNotificationService.class);

        todoService = new TodoService(todoRepository, sendNotificationService);
    }


    @Test
    void shouldCreateTodo_andSendNotification() {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo saved = todoService.createTodo(todo);

        assertNotNull(saved);
        assertEquals("Test Todo", saved.getTitle());

        verify(todoRepository).save(todo);
        verify(sendNotificationService).sendNotification(
                eq("TODO_CREATED"),
                eq(1L),
                eq("Nouvelle tâche créée")
        );
    }


    @Test
    void shouldReturnAllTodos() {

        Todo t1 = new Todo();
        t1.setTitle("Todo 1");

        Todo t2 = new Todo();
        t2.setTitle("Todo 2");

        when(todoRepository.findAll()).thenReturn(List.of(t1, t2));

        List<Todo> result = todoService.getAllTodos();

        assertEquals(2, result.size());
        verify(todoRepository).findAll();
    }


    @Test
    void shouldReturnTodoById() {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo result = todoService.getTodoById(1L);

        assertEquals("Test", result.getTitle());
    }

    @Test
    void shouldThrowWhenTodoNotFound() {

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> todoService.getTodoById(1L));
    }


    @Test
    void shouldUpdateTodo_andSendNotification() {

        Todo existing = new Todo();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setDescription("Old desc");
        existing.setCompleted(false);

        Todo update = new Todo();
        update.setTitle("New");
        update.setDescription("New desc");
        update.setCompleted(true);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(existing)).thenReturn(existing);

        Todo result = todoService.updateTodo(1L, update);

        assertEquals("New", result.getTitle());
        assertTrue(result.isCompleted());

        verify(todoRepository).save(existing);
        verify(sendNotificationService).sendNotification(
                "TODO_UPDATED",
                1L,
                "Tâche mise à jour"
        );
    }


    @Test
    void shouldDeleteTodo_andSendNotification() {

        when(todoRepository.existsById(1L)).thenReturn(true);

        todoService.deleteTodo(1L);

        verify(todoRepository).deleteById(1L);
        verify(sendNotificationService).sendNotification(
                "TODO_DELETED",
                1L,
                "Tâche supprimée"
        );
    }

    @Test
    void shouldThrowWhenDeleteTodoNotFound() {

        when(todoRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> todoService.deleteTodo(1L));

        verify(todoRepository, never()).deleteById(anyLong());
    }


    @Test
    void shouldMarkAsCompleted_andSendNotification() {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setCompleted(false);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo result = todoService.markAsCompleted(1L);

        assertTrue(result.isCompleted());

        verify(todoRepository).save(todo);
        verify(sendNotificationService).sendNotification(
                "TODO_COMPLETED",
                1L,
                "Tâche terminée"
        );
    }
}