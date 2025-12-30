package com.example.springdoc.adapter.web;

import com.example.springdoc.adapter.database.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(TaskRestController.CONTEXT)
@Tag(name = TaskRestController.CONTEXT, description = "Operations to manage tasks")
public record TaskRestController(TaskService service) {

	static final String CONTEXT = "/v1/tasks";

	@GetMapping
	@Operation(summary = "Returns tasks, optionally filtered by one or more ids")
	public List<Task> query(@RequestParam Optional<List<UUID>> id) {
		var hasId = id.isPresent() && !id.get().isEmpty();
		return hasId ? service.queryByIds(id.get()) : service.queryAll();
	}

	@PutMapping
	@Operation(summary = "Creates a new task")
	public Optional<Task> createTask(@RequestBody Task task) {
		return service.save(task);
	}

	@PatchMapping
	@Operation(summary = "Modifies an existing task")
	public Optional<Task> editTask(@RequestBody Task task) {
		return service.edit(task);
	}

	@DeleteMapping("/{ids}")
	@Operation(summary = "Deletes one or more tasks by ids")
	public List<Task> deleteTasksById(@PathVariable List<UUID> ids) {
		return service.deleteByIds(ids);
	}
}
