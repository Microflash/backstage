package com.example.task.adapter.web;

import com.example.task.adapter.database.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(TaskRestController.CONTEXT)
public record TaskRestController(TaskService service) {

	static final String CONTEXT = "/v1/tasks";

	@GetMapping
	public List<Task> query(@RequestParam Optional<List<UUID>> id) {
		var hasId = id.isPresent() && !id.get().isEmpty();
		return hasId ? service.queryByIds(id.get()) : service.queryAll();
	}

	@PutMapping
	public Optional<Task> createTask(@RequestBody Task task) {
		return service.save(task);
	}

	@PatchMapping
	public Optional<Task> editTask(@RequestBody Task task) {
		return service.edit(task);
	}

	@DeleteMapping("/{ids}")
	public List<Task> deleteTasksById(@PathVariable List<UUID> ids) {
		return service.deleteByIds(ids);
	}
}
