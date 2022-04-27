-- 科目一题库
create table question_1
(
    id            bigserial
        primary key,
    text_content  varchar(500),
    img_content   varchar(100),
    choice_number numeric(2),
    answers       varchar(10),
    explanation   varchar(400),
    unique ("text_content", "img_content", "choice_number", "answers",explanation)
);

-- 科目四题库
create table question_4
(
    id            bigserial
        primary key,
    text_content  varchar(500),
    img_content   varchar(100),
    choice_number numeric(2),
    answers       varchar(10),
    explanation   varchar(400),
    unique ("text_content", "img_content", "choice_number", "answers",explanation)
);


create table choice
(
    id              bigserial
        primary key,
    question_id     bigint,
    "category"      int,
    content         varchar(300),
    order_of_choice numeric(2, 0),
    is_image        boolean,
    unique ("question_id", "category", "content", "order_of_choice", "is_image")
);


create table app_user
(
    id           bigserial,
    username     varchar(20),
    nickname     varchar(20),
    password     varchar(20),
    role         varchar(10),
    stage        numeric(2, 0),
    gender       varchar(10),
    phone_number numeric(11, 0),
    primary key (id)
);

create table mistake
(
    id           bigserial,
    user_id      bigint,
    question_id  bigint,
    category     int,
    wrong_choice int,
    primary key (id),
    UNIQUE (user_id, question_id, category, wrong_choice)
);