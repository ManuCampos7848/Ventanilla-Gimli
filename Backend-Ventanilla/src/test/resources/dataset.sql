-- dataset.sql

------------------------------ Crear Administrador ---------------------------------
/**/
insert into cuenta values (100, "admin2442@gmail.com", "$2a$10$k6ANLEXelA4lhJ/KfXsoouNfrjyyMC/vLHmeR/nd3OORc9p0bT4Xq");
insert into administrador values ("Manu", 100);
insert into empleado values (1, "Manu", "3107231756", "1094881156", 100);


------------------------------ Crear Empleado ---------------------------------
insert into cuenta values (101, "karlos@gmail.com", "$2a$10$k6ANLEXelA4lhJ/KfXsoouNfrjyyMC/vLHmeR/nd3OORc9p0bT4Xq");
insert into empleado values (1, "Karl Marx", "3107256743", "1093829483", 101);


------------------------------ Crear Clientes ---------------------------------
insert into cuenta values (16, "bilbob@gmail.com", "$2a$10$k6ANLEXelA4lhJ/KfXsoouNfrjyyMC/vLHmeR/nd3OORc9p0bT4Xq");
insert into cliente values (1, "Bilbo Bolson", "22334455", "Bolson Cerrado", 16);




------------------------------ Crear Productos ---------------------------------
insert into producto values (1, 10, 1, "Bebida alcoholica con mucho volumen, ojito, recomendable tomar cuando se monta skate",
                             20000, "Importadora Dislicores S.A", 5);
insert into producto_nombres_alcohol values (1, "Four Loko");


insert into producto values (2, 7, 1, "Cerveza rica para el calor", 3000, "Centrar cervecera de Colombia",
                             5);
insert into producto_nombres_alcohol values (2, "Poker 473ml");

insert into producto values (3, 5, 2, "Gomitas trululu rico aperitivo", 5000, "Colombina S.A",
                             15);
insert into producto_nombres_dulces values (3, "Gomitas Trululu 77g");

insert into producto values (4, 15, 2, "Dulce ponque de cocholate", 3500, "Productos Ramo",
                             19);
insert into producto_nombres_dulces values (4, "Chocorramo 65g");

insert into producto values (5, 20, 3, "Gaseosa Colombiana clasica fria", 2500, "Postobon",
                             21);
insert into producto_nombres_gaseosas values (5, "Colombiana 400ml");

insert into producto values (6, 4, 3, "Gaseosa glacial economica pero rica", 1500, "Postobon",
                             21);
insert into producto_nombres_gaseosas values (6, "Glacial 400ml");

insert into cuenta values (5, "donsirius@gmail.com", "$2a$10$k6ANLEXelA4lhJ/KfXsoouNfrjyyMC/vLHmeR/nd3OORc9p0bT4Xq");
insert into cliente values (1, "Sirius Black", "32374839", "Hogsmeade", 5);