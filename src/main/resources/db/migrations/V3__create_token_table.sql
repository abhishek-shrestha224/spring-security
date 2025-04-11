create table Sessions(
    id bigint not null primary key auto_increment,
    user bigint not null,
    revoked bool not null,
    foreign key (user) references Users(id)
);