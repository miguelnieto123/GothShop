create database GothWear;
use GothWear;

create table rol (
	id_rol int auto_increment primary key,
	name varchar(50) not null);

create table user (
	id_user int auto_increment primary key,
    name varchar(100) not null,
    email varchar(100) unique not null,
    password varchar(255) not null,
    status varchar(20),
    register_date date,
    id_rol int,
    foreign key (id_rol) references rol(id_rol));

create table product (
    id_product int auto_increment primary key,
    name varchar(100) not null,
    description text,
    price decimal(10, 2) not null,
    stock integer default 0,
    status boolean);
    
create table sellorder (
    id_order int auto_increment primary key,
    date datetime,
    total decimal(10, 2),
    status boolean,
    id_user int,
    id_product int,
    foreign key (id_user) references user(id_user),
    foreign key (id_product) references product(id_product));
	
create table detail_order (
    id_detail_order int auto_increment primary key,
    id_order int,
    id_product int,
    amount int not null,
    unitary_price decimal(10, 2),
    foreign key (id_order) references sellorder(id_order),
    foreign key (id_product) references product(id_product));
    
create table shoppingcart (
    id_shoppingcart int auto_increment primary key,
    id_user int,
    creation_date datetime,
    foreign key (id_user) references user(id_user));
    
create table detailcart (
    id_detailcart int auto_increment primary key,
    id_shoppingcart int,
    id_product int,
    amount int not null,
    foreign key (id_shoppingcart) references shoppingcart(id_shoppingcart),
    foreign key (id_product) references product(id_product));

create table promotion (
    id_promotion int auto_increment primary key,
    name varchar(100),
    discountporcentage decimal(5,2),
    initdate datetime,
    finishdate datetime);

create table colection (
    id_colection int auto_increment primary key,
    name varchar (100),
    description text);
    
create table favorite (
	id_favorite int auto_increment primary key,
    id_user int,
    id_product int,
    foreign key (id_user) references user(id_user),
    foreign key (id_product) references product(id_product));
    
    insert into rol (name) value ('ROLE_ADMIN');
    insert into rol (name) value ('ROLE_USER');
    
    