insert into mission_status (id, status)
values (1, 'Aberto');
insert into mission_status (id, status)
values (2, 'Pendente');
insert into mission_status (id, status)
values (3, 'Cancelado');
insert into mission_status (id, status)
values (4, 'Concluído');

insert into public.position (id, position_name)
values (1, 'Analista de Dados');
insert into public.position (id, position_name)
values (2, 'Engenheiro de Software');
insert into public.position (id, position_name)
values (3, 'Programador');
insert into public.position (id, position_name)
values (4, 'Designer');
insert into public.position (id, position_name)
values (5, 'Web Designer');
insert into public.position (id, position_name)
values (6, 'Recepcionista');
insert into public.position (id, position_name)
values (7, 'Secretário/a');
insert into public.position (id, position_name)
values (8, 'Contabilista');
insert into public.position (id, position_name)
values (9, 'Advogado');
insert into public.position (id, position_name)
values (10, 'Segurança');

insert into users (id, email, firstname, is_enabled, lastname, password, role)
values (1, 'belmiro@admin.com', 'Belmiro Bernardo', true, 'Mungoi',
        '$2a$10$6gJEPJDfCv4k5VAKnxuaX.7EY5hjT5OupKKPcIXk.ORnoM9xqWs/m', 'ADMIN');
