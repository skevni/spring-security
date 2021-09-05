create table authorities
(
    id   bigserial,
    name varchar(50) not null
        primary key (id)
);

create table roles_authorities
(
    role_id      int    not null,
    authority_id bigint not null,
    primary key (role_id, authority_id),
    foreign key (role_id) references roles (id),
    foreign key (authority_id) references authorities (id)
);

insert into authorities (name)
VALUES ('ReadProduct'),
       ('ModifyProduct'),
       ('CreateProduct');

insert into roles_authorities (user_id, authority_id)
values (1, 1),
       (2, 1),
       (2, 2),
       (2, 3);