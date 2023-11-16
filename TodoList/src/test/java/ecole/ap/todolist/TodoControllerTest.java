package ecole.ap.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ecole.ap.exceptions.ExceptionHandlingAdvice;
import ecole.ap.exceptions.ResourceAlreadyExistsException;
import ecole.ap.exceptions.ResourceNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = TodoController.class)
@Import(ExceptionHandlingAdvice.class)
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>() {{
            add(new Todo(1L,"unContenu", "20/11/2023",false));
            add(new Todo(2L,"unContenu", "04/10/2005",true));
            add(new Todo(3L,"unContenu", "12/05/2022",true));
            add(new Todo(4L,"unContenu", "03/02/2000",false));
        }};
        when(todoService.getAll()).thenReturn(todos);
        when(todoService.getById(3L)).thenReturn(todos.get(2));
        when(todoService.getById(49L)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void whenGettingId1L_shouldReturnSame() throws Exception{
        mockMvc.perform(get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$.id", is(1))
        ).andExpect(jsonPath("$.contenu", is("unContenu"))
        ).andExpect(jsonPath("$.datecreation", is("20/11/2023"))
        ).andExpect(jsonPath("$.statut", is(false))
        ).andReturn();
    }

    @Test
    void whenGettingAll_shouldGet4_andBe200() throws Exception {
        mockMvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(4))
        ).andDo(print());
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        mockMvc.perform(get("/todos/66")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingNew_shouldReturnLink_andShouldBeStatusCreated() throws Exception {
        Todo new_todo = new Todo(5L,"unContenu", "19/08/2023", true);
        ArgumentCaptor<Todo> todo_received = ArgumentCaptor.forClass(Todo.class);
        when(todoService.create(any())).thenReturn(new_todo);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new_todo))
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/todos/"+new_todo.getId())
        ).andDo(print());

        verify(todoService).create(todo_received.capture());
        assertEquals(new_todo, todo_received.getValue());
    }


    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        when(todoService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.todos.get(3)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceivetodoToUpdate_andReturnNoContent() throws Exception {
        Todo initial_todo = todos.get(1);
        Todo updated_todo = new Todo(initial_todo.getId(),"unContenu", "20/20/2000", initial_todo.getStatut());
        ArgumentCaptor<Todo> todo_received = ArgumentCaptor.forClass(Todo.class);

        mockMvc.perform(put("/todos/"+initial_todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_todo))
        ).andExpect(status().isNoContent());

        verify(todoService).update(anyLong(), todo_received.capture());
        assertEquals(updated_todo, todo_received.getValue());
    }

    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 32L;

        mockMvc.perform(delete("/todos/"+id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(todoService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }


}
