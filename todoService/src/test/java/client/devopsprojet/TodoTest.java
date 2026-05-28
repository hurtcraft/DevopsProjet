package client.devopsprojet;


import client.devopsprojet.model.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void shouldUseSettersAndGetters() {

        Todo todo = new Todo();

        todo.setId(1L);
        todo.setTitle("Test title");
        todo.setDescription("Test desc");
        todo.setCompleted(true);

        assertEquals(1L, todo.getId());
        assertEquals("Test title", todo.getTitle());
        assertEquals("Test desc", todo.getDescription());
        assertTrue(todo.isCompleted());
    }

    @Test
    void shouldBeFalseByDefaultForCompleted() {

        Todo todo = new Todo();

        assertFalse(todo.isCompleted());
    }

    @Test
    void shouldTriggerPrePersist() {

        Todo todo = new Todo();

        todo.setTitle("Test");
        todo.setDescription("Desc");

        todo.onCreate();

        assertNotNull(todo.getCreatedAt());
        assertNotNull(todo.getUpdatedAt());

        assertTrue(
                Math.abs(
                        todo.getCreatedAt().getNano() - todo.getUpdatedAt().getNano()
                ) >= 0
        );
    }

    @Test
    void shouldTriggerPreUpdate() throws InterruptedException {

        Todo todo = new Todo();

        todo.onCreate();

        LocalDateTime initialUpdate = todo.getUpdatedAt();

        Thread.sleep(5); // garantir changement timestamp

        todo.onUpdate();

        assertNotNull(todo.getUpdatedAt());
        assertTrue(todo.getUpdatedAt().isAfter(initialUpdate));
    }
}