create table questions
(
    id            bigint not null
        primary key,
    category      varchar(10),
    text_content  varchar(500),
    img_content   varchar(100),
    choice_number numeric(2),
    answers       varchar(10),
    explanation   varchar(400)
);

comment
on table questions is '题库';


create table choice
(
    id bigint not null
        primary key,
    question_id bigint references questions(id),
    content varchar(300),
    order numeric(2,0),
    is_image boolean
);
1

create table app_user
(
    id bigint not null,
    username varchar(20),
    nickname varchar(20),
    password varchar(20),
    role varchar(10),
    stage numeric(2,0),
    gender varchar (10),
    phone_number numeric (11,0),
    primary key (id)
);