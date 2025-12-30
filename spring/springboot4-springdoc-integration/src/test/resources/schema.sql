create extension if not exists "uuid-ossp";
drop table if exists tasks;
create table tasks (
	id         uuid primary key      default uuid_generate_v1(),
	title      text not null,
	created_by text not null,
	remind_at  timestamptz
);
