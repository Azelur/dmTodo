package ecole.ap.todolist;

import java.util.List;
import ecole.ap.exceptions.ResourceAlreadyExistsException;
import ecole.ap.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("jpa")
public interface TodoService {
    List<Todo> getAll();

    Todo getById(Long id);

    Todo create(Todo newTodo) throws ResourceAlreadyExistsException;

    void update(Long id, Todo updatedTodo) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
}
