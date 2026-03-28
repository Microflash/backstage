package com.example.task.adapter.database;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends ListCrudRepository<Task, UUID> {

	List<Task> findAllByIdIn(List<UUID> id);

	@Query("""
	update tasks
	set title = case when title is distinct from :title then coalesce(:title, title, '') else title end,
			created_by = case when created_by is distinct from :createdBy then coalesce(:createdBy, created_by, '') else created_by end,
			remind_at = case when remind_at is distinct from :remindAt then :remindAt else remind_at end
	where id = :id
	returning *
	""")
	Optional<Task> update(UUID id, String title, String createdBy, LocalDateTime remindAt);

	@Query("delete from tasks where id in (:ids) returning *")
	List<Task> deleteAllByIdIn(List<UUID> ids);
}
