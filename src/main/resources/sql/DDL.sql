-- 科目一题库
create table question_1
(
    id            bigserial
        primary key,
    text_content  varchar(500),
    img_content   varchar(100),
    choice_number numeric(2),
    answer       int,
    explanation   varchar(400),
    unique ("text_content", "img_content", "choice_number", "answer",explanation)
);

-- 科目四题库
create table question_4
(
    id            bigserial
        primary key,
    text_content  varchar(500),
    img_content   varchar(100),
    choice_number numeric(2),
    answer       int,
    explanation   varchar(400),
    unique ("text_content", "img_content", "choice_number", "answer",explanation)
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
    id           varchar(30),
    username     varchar(20),
    nickname     varchar(20),
    password     varchar(20),
    role         varchar(10),
    "category"        int,
    gender       varchar(10),
    phone_number numeric(11, 0),
    primary key (id),
    unique (username)
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

-- 收藏
create table favorite
(
    id           bigserial,
    user_id      bigint,
    question_id  bigint,
    category     int,
    primary key (id),
    UNIQUE (user_id, question_id, category)
);


create table practice_status
(
    id  bigserial,
    user_id bigint,
    category int,
    question_id bigint,
    primary key (id),
    UNIQUE (user_id, question_id,category)
);



create table class_group
(
    id  bigserial,
    teacher_id bigint,
    student_id bigint,
    primary key (id),
    unique (teacher_id, student_id)
);