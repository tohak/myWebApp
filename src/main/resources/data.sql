INSERT  into usr_tbl (active, email, password, user_name)
values (true, 'mail@mail.com', '$2a$08$plsOzEhPSO/BmXl8gqM0B.O4HkoVhGv9KlHWHZhb.KBifhTGPQAm2', 'admin');

insert into user_role (user_id, roles)
values (1, 'USER'),(1, 'ADMIN');