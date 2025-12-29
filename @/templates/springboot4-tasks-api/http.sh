# list all tasks
curl -s http://localhost:8080/v1/tasks

# list specific tasks by id
curl -s http://localhost:8080/v1/tasks?id=<id1>&id=<id2>

# add new task
curl -s -X PUT http://localhost:8080/v1/tasks --json '{"title": "Unsubscribe Netflix","by": "ravi"}'

# patch an existing task
curl -s -X PATCH http://localhost:8080/v1/tasks --json '{"remindAt": "2025-12-30T00:44:52","id": <id>}'

# delete tasks by id
curl -s -X DELETE http://localhost:8080/v1/tasks/<id1>,<id2>
