package com.example.springdoc.adapter.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("tasks")
public record Task(
		@Id UUID id,
		String title,
		String createdBy,
		LocalDateTime remindAt) {
}
