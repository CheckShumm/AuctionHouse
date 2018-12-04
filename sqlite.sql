CREATE TABLE IF NOT EXISTS users (
	id integer primary KEY autoincrement,
	username text not null unique,
	password text not null unique,
	created_at real not null
);

CREATE TABLE IF NOT EXISTS items (
  id integer primary KEY autoincrement,
	name text not null unique,
	description text not null unique,
	created_at real not null
)

