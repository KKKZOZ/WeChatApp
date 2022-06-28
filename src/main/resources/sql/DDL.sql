-- 科目一题库
create table question_1
(
    id            bigserial
        primary key,
    text_content  varchar(1500),
    img_content   varchar(100),
    choice_number numeric(2),
    answer       int,
    explanation   varchar(2000)

);

-- 科目四题库
create table question_4
(
    id            bigserial
        primary key,
    text_content  varchar(1500),
    img_content   varchar(100),
    choice_number numeric(2),
    answer       int,
    explanation   varchar(2000)

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
    name     varchar(20),
    password     varchar(20),
    role         varchar(10),
    "category"        int,
    invitation_code int,
    avatar_url  varchar(200),
    years_of_teaching numeric(2, 0),
    school_name varchar(100),
    register_date date,
    phone_number numeric(11, 0),
    primary key (id),
    unique (username)
);

create table mistake
(
    id           bigserial,
    user_id      varchar(30),
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
    user_id      varchar(30),
    question_id  bigint,
    category     int,
    primary key (id),
    UNIQUE (user_id, question_id, category)
);


create table practice_status
(
    id  bigserial,
    user_id varchar(30),
    category int,
    question_id bigint,
    primary key (id),
    UNIQUE (user_id, question_id,category)
);



create table class_group
(
    id  bigserial,
    teacher_id varchar(30),
    student_id varchar(30),
    primary key (id),
    unique (teacher_id, student_id)
);

create table asset
(
    id bigserial,
    file_name varchar(10000),
    file_content varchar(10000000),
    primary key (id),
    unique(file_name)
);