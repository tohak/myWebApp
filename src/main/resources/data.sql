INSERT  into usr (active, email, password, username)
values (true, 'mail@mail.com', '$2a$08$plsOzEhPSO/BmXl8gqM0B.O4HkoVhGv9KlHWHZhb.KBifhTGPQAm2', 'admin');

insert into user_role (user_id, roles)
values (1, 'USER'),(1, 'ADMIN');