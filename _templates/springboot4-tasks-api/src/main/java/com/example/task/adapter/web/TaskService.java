package com.example.task.adapter.web;

import com.example.task.adapter.database.Task;
import com.example.task.adapter.database.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public record TaskService(TaskRepository repository) {

	public List<Task> queryAll() {
		return repository.findAll();
	}

	public List<Task> queryByIds(List<UUID> ids) {
		return repository.findAllByIdIn(ids);
	}

	public Optional<Task> save(Task task) {
		return Optional.of(repository.save(task));
	}

	public Optional<Task> edit(Task task) {
		return repository.update(task.id(), task.title(), task.createdBy(), task.remindAt());
	}

	public List<Task> deleteByIds(List<UUID> ids) {
		return repository.deleteAllByIdIn(ids);
	}
}
