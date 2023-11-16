package ecole.ap.todolist;

import ecole.ap.exceptions.ResourceAlreadyExistsException;
import ecole.ap.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class TodoJPAService implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            return todo.get();
        } else {
            throw new ResourceNotFoundException("Todo", id);
        }
    }

    @Override
    public Todo create(Todo newTodo) throws ResourceAlreadyExistsException {
        if (todoRepository.existsById(newTodo.getId())) {
            throw new ResourceAlreadyExistsException("Todo already exist", newTodo);
        }
        else {
            return todoRepository.save(newTodo);
        }
    }

    @Override
    public void update(Long id, Todo updatedTodo) throws ResourceNotFoundException {
    Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            todoRepository.save(updatedTodo);
        }
        else {
            throw new ResourceAlreadyExistsException("Todo already exist", todo);
        }
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            todoRepository.deleteById(id);
        }
        else {
            throw new ResourceNotFoundException("Todo doesn't exist", todo);
        }
    }
}
