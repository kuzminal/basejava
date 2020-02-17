create table resume
(
    uuid varchar(36) not null
        constraint resume_pk
            primary key,
    full_name text
);

alter table resume owner to postgres;

create table contact
(
    id serial not null
        constraint contact_pk
            primary key,
    type text not null,
    value text not null,
    resume_uuid varchar(36) not null
        constraint contact_resume_uuid_fk
            references resume
            on update restrict on delete cascade
);

alter table contact owner to postgres;

create unique index contact_resume_uuid_type_uindex
    on contact (resume_uuid, type);

create table section
(
    id serial not null
        constraint section_pk
            primary key,
    resume_uuid varchar(36)
        constraint section_resume_uuid_fk
            references resume
            on update restrict on delete cascade,
    type text,
    content text
);

alter table section owner to postgres;

create index section_resume_uuid_type_index
    on section (resume_uuid, type);