DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS web_role;
DROP TABLE IF EXISTS web_user;

CREATE TABLE web_user (
  user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  user_first_name varchar not null,
  user_last_name varchar not null,
  user_login varchar not null UNIQUE,
  user_password varchar not null,
  user_score integer,
  user_admin boolean default false
);

CREATE TABLE web_role (
  role_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  role_name varchar not null
);

CREATE TABLE user_role (
  user_id int not null,
  role_id int not null,
  FOREIGN KEY (user_id) REFERENCES web_user(user_id),
  FOREIGN KEY (role_id) REFERENCES web_role(role_id)
);

CREATE INDEX idx_user_id ON web_user(user_id);
