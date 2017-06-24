/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  igor
 * Created: Jun 21, 2017
 */

create database db_restaurant;
use db_restaurant;

create table meals(
    id int not null auto_increment primary key,
    name varchar(100) not null,
    type enum('MAIN_COURSE', 'SIDE_DISH', 'DESSERT'),
    price double,
    isVegetarian boolean,
    isTransgenic boolean,
    isAvailable boolean
);

create table drinks(
    id int not null auto_increment primary key,
    name varchar(100) not null,
    price double,
    isAlcohol boolean,
    isAvailable boolean
);


use db_restaurant;
select * from drinks;
--drop database db_restaurant;