create table Users(
    id bigint not null primary key auto_increment,
    first_name varchar(255),
    last_name varchar(255),
    age int,
    username varchar(16) not null unique,
    password varchar(255) not null
);

create table Roles(
    id bigint not null primary key auto_increment,
    name varchar(255) unique not null
);

create table Permissions(
    id bigint not null primary key auto_increment,
    name varchar(255) unique not null
);

create table user_roles(
    id int not null primary key auto_increment,
    user bigint not null,
    role bigint not null,
    foreign key (user) references Users(id),
    foreign key (role) references Roles(id)
);

create table role_permissions(
    id int not null primary key auto_increment,
    role bigint not null,
    permission bigint not null,
    foreign key (role) references Roles(id),
    foreign key (permission) references Permissions(id)
);
