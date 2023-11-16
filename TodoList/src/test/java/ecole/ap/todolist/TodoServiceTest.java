package ecole.ap.todolist;

import ecole.ap.exceptions.ResourceAlreadyExistsException;
import ecole.ap.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TodoServiceTest {

    private TodoService todoService;

    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(){{
            add(new Todo(1L,"unContenu", "12/05/2006", true));
            add(new Todo(2L,"unContenu", "22/04/2008", true));
            add(new Todo(3L,"unContenu", "05/08/2022", false));
        }};
        when(todoService.getAll()).thenReturn(todos);
        when(todoService.getById(7L)).thenReturn(todos.get(4));
        when(todoService.getById(49L)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void whenGettingAll_shouldReturn3() {
        assertEquals(3, todoService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        assertAll(
                () -> assertEquals(todos.get(0), todoService.getById(1L)),
                () -> assertEquals(todos.get(2), todoService.getById(3L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> todoService.getById(12L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        Todo toCreate = new Todo(6L,"unContenu", "12/05/2017", true);

        assertEquals(toCreate, todoService.create(toCreate));
    }

    @Test
    void whenCreatingWithSameId_shouldReturnEmpty() {
        Todo same_todo = todos.get(0);

        assertThrows(ResourceAlreadyExistsException.class, ()->todoService.create(same_todo));
    }

    @Test
    void whenUpdating_shouldModifyTodo() {
        Todo initial_todo = todos.get(2);
        Todo new_todo = new Todo(initial_todo.getId(),"unContenu", "uneDate", initial_todo.getStatut());

        todoService.update(new_todo.getId(), new_todo);
        Todo updated_user = todoService.getById(initial_todo.getId());
        assertEquals(new_todo, updated_user);
        assertTrue(todoService.getAll().contains(new_todo));
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        Todo todo = todos.get(2);

        assertThrows(ResourceNotFoundException.class, ()->todoService.update(75L, todo));
    }

    @Test
    void whenDeletingExistingTodo_shouldNotBeInTodosAnymore() {
        Todo todo = todos.get(1);
        Long id = todo.getId();

        todoService.delete(id);
        assertFalse(todoService.getAll().contains(todo));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        Long id = 68L;

        assertThrows(ResourceNotFoundException.class, ()->todoService.delete(id));
    }


}
