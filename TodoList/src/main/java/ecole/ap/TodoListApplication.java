package ecole.ap;

import ecole.ap.todolist.Todo;
import ecole.ap.todolist.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TodoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoListApplication.class, args);
	}

	@Bean
	public CommandLineRunner setUpBDD(TodoRepository todoRepository) {
		return (args) -> {
			Todo todo1 = new Todo(1L,"unContenu", "20/11/2023",false);
			Todo todo2 = new Todo(2L,"unContenu", "04/10/2005",true);
			Todo todo3 = new Todo(3L,"unContenu", "12/05/2022",true);
			Todo todo4 = new Todo(4L,"unContenu", "03/02/2000",false);
			List<Todo> todos = new ArrayList<>() {{
				add(todo1);
				add(todo2);
				add(todo3);
				add(todo4);
			}};
			todoRepository.saveAll(todos);
		};
	}

}
