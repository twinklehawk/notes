CREATE TABLE notes (
	id IDENTITY PRIMARY KEY,
	ownder_id BIGINT,
	correlation_id BIGINT,
	title VARCHAR(128),
	content VARCHAR(4096)
)