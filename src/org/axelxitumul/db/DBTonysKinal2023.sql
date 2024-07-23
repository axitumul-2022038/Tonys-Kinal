/*
	Axel Antonio Xitumul Chén
    IN5AM
    2022038
    Fecha de Creación:
		28/03/2023
	Fecha de Modificación:
		09/03/2023
*/

Drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;

Use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8),
    primary key Pk_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionPlato varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(100) not null,
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

Create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoEmpleado varchar(8) not null,
    gradoCocinero varchar(50) not null,
    codigoTipoEmpelado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_Empleados_TipoEmpleado foreign key
		(codigoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoDeServicio varchar(150) not null,
    horaDeServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(150) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key(codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Presupuestos(
	codigoPresupuesto int not null auto_increment,
    fechaDeSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuestos_Empresas	 foreign key(codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
	cantidadPlatos int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key(codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

Create table Productos_has_Platos(
	productos_codigoProducto int not null,
	codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key(codigoProducto)
		references Productos(codigoProducto),
    constraint FK_Productos_has_Platos_PLatos foreign key(codigoPlato)
		references Platos(codigoPlato)    
);

Create table Servicios_has_Platos(
	servicios_CodigoServicio int not null,	
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_CodigoServicio (Servicios_CodigoServicio),
    constraint FK_Servicios_has_Platos_Servicios foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos_Platos foreign key(codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Empleados(
	servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,	
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicio foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados foreign key(codigoEmpleado)
		references Empleados(codigoEmpleado)
);
-- ---------------------------------------------------- Login ------------------------------------------------------------------------------------
Create table Usuario(
	codigoUsuario	int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario(codigoUsuario)
);

create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster(usuarioMaster)
);


-- Procesos Almacenados
-- login
Delimiter // 
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
		begin
			insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena) values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
		End //
Delimiter ;

Delimiter //
	create procedure sp_ListarUsuarios()
	begin
		select
			 U.codigoUsuario,
             U.nombreUsuario, 
             U.apellidoUsuario, 
             U.usuarioLogin, 
             U.contrasena
		from Usuario U;
	end //
Delimiter ; 

call sp_AgregarUsuario('Axel','Xitumul','Xitu','2006');
call sp_ListarUsuarios();


-- Agregar Empresa

Delimiter //
	create procedure sp_Agregar_Empresa( in nombre varchar(150), in direcc varchar(150), in telef varchar(8))
			begin 
				insert into Empresas(nombreEmpresa, direccion, telefono) values(nombre,direcc,telef);
            end //
Delimiter ;
call sp_Agregar_Empresa('Pizza Hut','zona 17','15935414');
call sp_Agregar_Empresa('McDonald','zona 18','98541236');
call sp_Agregar_Empresa('Gaterode','zona 21','25478541');
call sp_Agregar_Empresa('PriceSmart','zona 3, VillaNueva','14552265');
call sp_Agregar_Empresa('Honda','zona 1','1598721');
call sp_Agregar_Empresa('Cofiño Stahl','zona 5 ','12647865');
call sp_Agregar_Empresa('La Torre','zona 10','26845197');
call sp_Agregar_Empresa('Cervecería Centro Americana','zona 2 ','25698741');
-- Editar Empresa

Delimiter //
	create procedure sp_Editar_Empresa(in codEmpre int, in nombreem varchar(150), in direccio varchar(150), in telefo varchar(8))
			begin
            update Empresas E
				set E.nombreEmpresa = nombreem,
					E.direccion = direccio,
                    E.telefono = telefo
						where E.codigoEmpresa = codEmpre;
			end // 
Delimiter ;
-- Eliminar Empresa

Delimiter // 
	create procedure sp_Eliminar_Empresa(in codEmpre int)
			begin
				delete from Empresas   
					where codigoEmpresa = codEmpre;
            end // 
Delimiter ;

-- Listar Empresa

Delimiter // 
	create procedure sp_Listar_Empresas()
			begin
				select  E.codigoEmpresa,
						E.nombreEmpresa,
						E.direccion,
						E.telefono
					from Empresas E;
			end //
Delimiter ; 
call sp_Listar_Empresas();
-- Buscar Empresa           

Delimiter // 
	create procedure sp_Buscar_Empresa(in codEmpre int)
			begin
				select  E.codigoEmpresa,
						E.nombreEmpresa,
						E.direccion,
						E.telefono
					from Empresas E
						where E.codigoEmpresa = codEmpre;
			end //
Delimiter ; 

-- Agregar TipoEmpleado

Delimiter // 
	create procedure sp_Agregar_TipoEmpleado(in descripTipo varchar(100))
			begin
				Insert into TipoEmpleado(descripcionTipo) values(descripTipo);
            end //
Delimiter ;
call sp_Agregar_TipoEmpleado('Jefe');
call sp_Agregar_TipoEmpleado('Chef');
call sp_Agregar_TipoEmpleado('Cocinero');
call sp_Agregar_TipoEmpleado('Camarero');
call sp_Agregar_TipoEmpleado('Conserje');
call sp_Agregar_TipoEmpleado('Cocinero');
call sp_Agregar_TipoEmpleado('Bartender');
call sp_Agregar_TipoEmpleado('Anfitrion');
-- Eliminar TipoEmpleado
Delimiter // 
	create procedure sp_Eliminar_TipoEmpleado(in codTipoEmple int)
			begin
				delete from TipoEmpleado   
					where codigoTipoEmpleado = codTipoEmple;
            end // 
Delimiter ;
-- Editar TipoEmpleado

Delimiter // 
	create procedure sp_Editar_TipoEmpleado(in codTipoEmpleado int ,in  descripTipo varchar(100))
			begin
            Update TipoEmpleado T
				set T.descripcionTipo = descripTipo
					where T.codigoTipoEmpleado = codTipoEmpleado;
            end //
Delimiter ; 
-- Listar TipoEmpleado

Delimiter //
	create procedure sp_Listar_TipoEmpleado()
			begin
				Select  T.codigoTipoEmpleado,
						T.descripcionTipo
					from TipoEmpleado T;
			end // 
Delimiter ;
call sp_Listar_TipoEmpleado;

-- Buscar TipoEmpleado

Delimiter // 
	create procedure sp_Buscar_TipoEmpleado(in codTipo int)
			begin
            select  T.descripcionTipo 
			from TipoEmpleado T
				where T.codigoTipoEmpleado = codTipo;
			end //
Delimiter ; 

-- Agregar TipoPlato

Delimiter //
	create procedure sp_Agrega_TipoPlato(in descriPlato varchar(100))
			begin
				insert into TipoPlato(descripcionPlato)values(descriPlato);
			end // 
Delimiter ;
call sp_Agrega_TipoPlato('Entrantes');
call sp_Agrega_TipoPlato('Principal');
call sp_Agrega_TipoPlato('Guarnicion');
call sp_Agrega_TipoPlato('Postre');
call sp_Agrega_TipoPlato('Menu Infantil');

-- Editar TipoPlato

Delimiter // 
	create procedure sp_Editar_TipoPlato(in codTipoPlato int, in descripPlato varchar(100))
			begin
				update TipoPlato I 
					set I.descripcionPlato = descripPlato
						where I.codigoTipoPlato = codTipoPlato;
			end //
Delimiter ;

-- Eliminar TipoPlato

Delimiter // 
	create procedure sp_Eliminar_TipoPlato(in codTipoPlato int)
			begin
				delete from TipoPlato   
					where codigoTipoPlato = codTipoPlato;
            end // 
Delimiter ;

-- Listar TipoPlato

Delimiter //
	create procedure sp_Listar_TipoPlato()
			begin
				Select  I.codigoTipoPlato,
						I.descripcionPlato
					from TipoPlato I;
			end // 
Delimiter ;

-- Buscar TipoPlato

Delimiter //
	create procedure sp_Buscar_TipoPlato(in codTipoPlato int)
			begin
				Select I.codigoTipoPlato,
						I.descripcionPlato
					from TipoPlato I
						where I.codigoTipoPlato = codTipoPlato;
			end//
Delimiter ;

-- Agregar Productos

Delimiter //
	create procedure sp_Agregar_Productos(in nombrePr varchar(100), in cantid int)
			begin 
				Insert into Productos(nombreProducto, cantidad) values (nombrePr,cantid);
			end //
Delimiter ;
call sp_Agregar_Productos('Tortitas de carne Procasa',150);
call sp_Agregar_Productos('Harina',500);
call sp_Agregar_Productos('Queso',300);
call sp_Agregar_Productos('Salsa',650);
call sp_Agregar_Productos('Oregano',200);
call sp_Agregar_Productos('Peperoni',3600);
call sp_Agregar_Productos('Albahaca',5214);
call sp_Agregar_Productos('Lechuga',350);
call sp_Agregar_Productos('Mayonesa',365);
call sp_Agregar_Productos('Mostaza',5241);
-- Editar Productos

Delimiter //
	create procedure sp_Editar_Productos(in codigoProd int , in nombreProdu varchar(100) ,in  cant int )
			begin
				update Productos P
					set P.nombreProducto = nombreProdu,
						P.cantidad = cant
					where P.codigoProducto = codigoProd;
			end //
Delimiter ;

-- Eliminar Productos

Delimiter //
	create procedure sp_Eliminar_Productos(in codigoPro int )
			begin 
				delete from Productos 
					where codigoProducto = codigoPro;
			end //
Delimiter ;

-- Listar Productos

Delimiter //
	create procedure sp_Listar_Productos()
			begin
				select P.codigoProducto,
						P.nombreProducto, 
						P.cantidad
					from Productos P;
			end //
Delimiter ;

-- Buscar Productos

Delimiter //
	create procedure sp_Buscar_Productos(in codigoPro int)
			begin
				select P.codigoProducto,
						P.nombreProducto, 
						P.cantidad
					from Productos
						where P.codigoProducto = codigoPro;
			end //
Delimiter ;

-- Agregar Empleado

Delimiter // 
	create procedure sp_Agregar_Empleado(in numeroEmp int, in apellidosEmp varchar(150), in nombresEmp varchar(150),
    in direccionEmp varchar(150),in telefonoEmp varchar(8),in gradoCoci varchar(50), in codigoTipoEmp int)
			begin
				insert into Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoEmpleado,
                gradoCocinero, codigoTipoEmpelado) values(numeroEmp,apellidosEmp,nombresEmp,direccionEmp,telefonoEmp,gradoCoci,codigoTipoEmp);
			end //
Delimiter ;
call sp_Agregar_Empleado(1,'Xitumul Chén','Axel Antonio','lote 45 zona 18','12546854','No',1);
call sp_Agregar_Empleado(2,'Zabala Toc','Diego Estuardo','zona 10','89562314','Chef Ejecutivo',2);
call sp_Agregar_Empleado(3,'Yaxon Taquira','Juan Pablo','zona 7 Landivar','36524178','Sous Chef',2);
call sp_Agregar_Empleado(5,'Itzep Lemus','Christian Emanuel','La berbena','5987463','Aprendiz',3);
call sp_Agregar_Empleado(6,'Garcia Escobar','Carlos Manuel','Villa Nueva','95784512','Demi Chef',3);
call sp_Agregar_Empleado(7,'Boteo Granados','Juan Carlos','zona 15','15963247','No',7);

-- Editar Empleado

Delimiter //
	create procedure sp_Editar_Empleado(in codigoEmp int,in numeroEmp int, in apellidosEmp varchar(150), in nombresEmp varchar(150),
    in direccionEmp varchar(150),in telefonoEmp varchar(8),in gradoCoci varchar(50))
			begin
				update Empleados E
					set	E.numeroEmpleado = numeroEmp,
						E.apellidosEmpleado = apellidosEmp,
                        E.nombresEmpleado = nombresEmp,
                        E.direccionEmpleado = direccionEmp,
                        E.telefonoEmpleado = telefonoEmp,
                        E.gradoCocinero = gradoCoci
							where codigoEmpleado = codigoEmp ;
			end //
Delimiter ;

-- Eliminar Empleado

Delimiter //
	create procedure sp_Eliminar_Empleado(in codigoEmp int)
			begin
				delete from Empleados 
					where codigoEmpleado = codigoEmp;
			end //
Delimiter ;
-- call sp_Eliminar_Empleado(1);
-- Listar Empleado

Delimiter // 
	create procedure sp_Listar_Empleado()
			begin
				select E.codigoEmpleado, E.numeroEmpleado, E.apellidosEmpleado, E.nombresEmpleado,
                E.direccionEmpleado, E.telefonoEmpleado, E.gradoCocinero, E.codigoTipoEmpelado
					from Empleados E;
			end //
Delimiter ;
call sp_Listar_Empleado();

-- Buscar Empleado

Delimiter // 
	create procedure sp_Buscar_Empleado(in codigoEmp int)
			begin
				select E.codigoEmpleado, E.numeroEmpleado, E.apellidosEmpleado, E.nombresEmpleado, E.direccionEmpleado,
                E.telefonoEmpleado, E.gradoCocinero, E.codigoTipoEmpelado
					from Empleados E
						where codigoEmpleado = codigoEmp;
			end //
Delimiter ;

-- Agregar Servicios

Delimiter //
	create procedure sp_Agregar_Servicio(in fechaServi date, in tipoDeServi varchar(100), in horaDeServi time, 
    in lugarServi varchar(150),in telefonoConta varchar(150), in codigoEmp int)
			begin
				Insert into Servicios(fechaServicio, tipoDeServicio, horaDeServicio, lugarServicio, telefonoContacto, codigoEmpresa)
					values(fechaServi, tipoDeServi, horaDeServi, lugarServi, telefonoConta, codigoEmp);
			end //
Delimiter ;

call sp_Agregar_Servicio('2023-01-25','Buffet','12:40:15','Zona 18','15963247',5);
call sp_Agregar_Servicio('2023-05-24','Banquete','14:00:00','Zona 2 Villa Nueva','96325874',1);
call sp_Agregar_Servicio('2023-02-28','Coctel','15:30:05','Zona 9 Mixco','15874236',8);
call sp_Agregar_Servicio('2023-09-24','De Mesa','08:40:56','Zona 10','39852147',4);
call sp_Agregar_Servicio('2023-06-05','Familiar','10:25:26','Zona 21','78945612',6);
call sp_Agregar_Servicio('2023-07-02','Buffet','17:00:00','Zona 1','12345678',2);
call sp_Agregar_Servicio('2023-09-20','Banquete','09:06:12','Zona 7','98563214',3);
call sp_Agregar_Servicio('2023-04-26','De Mesa','12:30:00','Zona 5 Mixco','14523698',7);

-- Editar Servicios

Delimiter //
	create procedure sp_Editar_Servicio(in codigoServi int ,in fechaServi date, in tipoDeServi varchar(100), in horaDeServi time, 
    in lugarServi varchar(150),in telefonoConta varchar(150))
			begin
				update Servicios S
					set S.fechaServicio = fechaServi ,
						S.tipoDeServicio = tipoDeServi,
                        S.horaDeServicio = horaDeServi, 
                        S.lugarServicio = lugarServi,
                        S.telefonoContacto = telefonoConta
						where codigoServicio = codigoServi;
			end //
Delimiter ;

-- Eliminar Servicios

Delimiter //
	create procedure sp_Eliminar_Servicio(in codigoServi int)
			begin 
				delete from Servicios
					where codigoServicio = codigoServi; 
			end // 
Delimiter ;

-- Listar Servicio

Delimiter //
	create procedure sp_Listar_Servicios()
			begin
				select S.codigoServicio, S.fechaServicio, S.tipoDeServicio, S.horaDeServicio, S.lugarServicio, S.codigoEmpresa, S.telefonoContacto
					from Servicios S;
			end //
Delimiter ;
call sp_Listar_Servicios();
-- Buscar Servicio

Delimiter // 
	create procedure sp_Buscar_Servicio(in codigoServi int)
			begin
				select S.codigoServicio, S.fechaServicio, S.tipoDeServicio, S.horaDeServicio, S.lugarServicio, S.codigoEmpresa, S.telefonoContacto
					from Servicios S
						where codigoServicio = codigoServi;
			end //
Delimiter ;

-- Agregar Presupuesto

Delimiter // 
	create procedure sp_Agregar_Presupuesto(in fechaDeSoli date, in cantidadPres decimal(10,2) , in codigoEmp int)
			begin 
				insert into Presupuestos(fechaDeSolicitud, cantidadPresupuesto, codigoEmpresa) values(fechaDeSoli, cantidadPres, codigoEmp);
			end //
Delimiter ;
call sp_Agregar_Presupuesto('2023-05-01', 5124.20,1);
call sp_Agregar_Presupuesto('2023-01-01', 6521.46 ,2);
call sp_Agregar_Presupuesto('2023-04-25', 77854.99 ,5);
call sp_Agregar_Presupuesto('2023-02-14', 1526.65,8);
call sp_Agregar_Presupuesto('2023-06-12', 1587.90,4);
call sp_Agregar_Presupuesto('2023-05-29', 900.00,6);
call sp_Agregar_Presupuesto('2023-01-10',1000.00 ,3);
call sp_Agregar_Presupuesto('2023-05-28', 5999.99,7);
-- Editar Presupuesto

Delimiter //
	create procedure sp_Editar_Presupuesto(in codigoPres int , in fechaDeSoli date, in cantidadPres decimal(10,2))
			begin
				update Presupuestos P
					set P.fechaDeSolicitud = fechaDeSoli,
                        P.cantidadPresupuesto = cantidadPres
						where codigoPresupuesto = codigoPres;
			end //
Delimiter ;

-- Eliminar Presupuesto

Delimiter //
	create procedure sp_Eliminar_Presupuesto(in codigoPres int)
			begin
				delete from Presupuestos
					where codigoPresupuesto = codigoPres;
			end //
Delimiter ; 

-- Listar Presupuesto

Delimiter //
	create procedure sp_Listar_Presupuestos()
			begin
				select Pr.codigoPresupuesto, Pr.fechaDeSolicitud, Pr.cantidadPresupuesto, Pr.codigoEmpresa
					from Presupuestos Pr;
			end //
Delimiter ;
call sp_Listar_Presupuestos();
-- Buscar Presupuesto

Delimiter //
	create procedure sp_Buscar_Presupuesto(in codigoPres int)
			begin
				select Pr.codigoPresupuesto, Pr.fechaDeSolicitud, Pr.cantidadPresupuesto, Pr.codigoEmpresa
					from Presupuestos Pr
						where Pr.codigoPresupuesto = codigoPres;
			end //
Delimiter ;

-- Agregar Plato

Delimiter //
	create procedure sp_Agregar_Plato(in cant int, in nombrePl varchar(50), in descripcionPl varchar(150),
    in precioPl decimal(10,2), in codigoTipoPl int)
			begin
				insert into Platos(cantidadPlatos, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato) values(cant,nombrePl,descripcionPl,precioPl,codigoTipoPl);
			end //
Delimiter ;
call sp_Agregar_Plato(12,'Hamburguesa','Comida rica',35.50,1);
call sp_Agregar_Plato(15,'Pizza','peperoni o jamon',60.00,2);
call sp_Agregar_Plato(14,'Lasaña','pasta de lasaga con carne',60.50,1);
call sp_Agregar_Plato(25,'sopa de Pollo','con verduras y fideos recien hechos',53.99,3);
call sp_Agregar_Plato(24,'Papas fritas','papas deliciosas acompañadas de tu salsa favorita',15.00,4);
-- Editar Plato

Delimiter // 
	create procedure sp_Editar_Plato(in codPlato int, in cant int, in nombrePl varchar(50), in descripcionPl varchar(150),in precioPl decimal(10,2))
			begin
				update Platos Pl
					set Pl.cantidadPlatos = cant,
						Pl.nombrePlato = nombrePl,
                        Pl.descripcionPlato = descripcionPl,
                        Pl.precioPlato = precioPl
                        where codigoPlato = codPlato;
			end //
Delimiter ;

-- Eliminar Plato

Delimiter //
	create procedure sp_Eliminar_Plato(in codPlato int)
			begin
				delete from Platos 
					where codigoPlato = codPlato;
			end //
Delimiter ; 

-- Listar Plato

Delimiter //
	create procedure sp_Listar_Platos()
			begin
				select Pl.codigoPlato, Pl.cantidadPlatos, Pl.nombrePlato, Pl.descripcionPlato, Pl.precioPlato, Pl.codigoTipoPlato
					from Platos Pl;
			end //
Delimiter ;

-- Buscar Plato

Delimiter //
	create procedure sp_Buscar_Platos(in codPlato int)
			begin
				select Pl.cantidadPlatos, Pl.nombrePlato, Pl.descripcionPlato, Pl.precioPlato, Pl.codigoTipoPlato
					from Platos Pl
                    where Pl.codigoPlato = codPlato ;
			end //
Delimiter ;

-- Agregar Productos_has_Platos

Delimiter // 
	create procedure sp_Agregar_Productos_has_Platos(in Productos_codigoProducto int, in codPlato int, in codProducto int)
			begin 
				insert into Productos_has_Platos(Productos_codigoProducto, codigoPlato, codigoProducto) values(Productos_codigoProducto,codPlato, codProducto);
			end //
Delimiter ;

call sp_Agregar_Productos_has_Platos(1,1,1);
call sp_Agregar_Productos_has_Platos(2,2,5);
call sp_Agregar_Productos_has_Platos(4,4,3);
call sp_Agregar_Productos_has_Platos(5,5,4);
call sp_Agregar_Productos_has_Platos(6,2,6);
call sp_Agregar_Productos_has_Platos(7,5,7);
call sp_Agregar_Productos_has_Platos(8,2,8);
-- Eliminar Productos_has_Platos

Delimiter //
	create procedure sp_Eliminar_Productos_has_Platos(in Productos_codProducto int)
			begin
				delete from Productos_has_Platos
					where Productos_codigoProducto = Productos_codProducto;
			end //
Delimiter ; 

-- Listar Productos_has_Platos

Delimiter //
	create procedure sp_Listar_Productos_has_Platos()
			begin
				select Pp.Productos_codigoProducto, Pp.codigoPlato, Pp.codigoProducto
					from Productos_has_Platos Pp;
			end //
Delimiter ;

-- Buscar Productos_has_Platos

Delimiter //
	create procedure sp_Buscar_Productos_has_Platos(in Productos_codProducto int)
			begin
				select Pp.Productos_codigoProducto, Pp.codigoPlato, Pp.codigoProducto
					from Productos_has_Platos Pp
						where Pp.Productos_codigoProducto = Productos_codProduct;
			end //
Delimiter ;

-- Agrega Servicios_has_Platos

Delimiter // 
	create procedure sp_Agregar_Servicios_has_Platos(in Servicios_CodigoServicio int,in codServicio int, in codPlato int)
			begin 
				insert into Servicios_has_Platos(Servicios_CodigoServicio,codigoServicio, codigoPlato) values(Servicios_CodigoServicio,codServicio,codPlato);
			end //
Delimiter ;
call sp_Agregar_Servicios_has_Platos(1,1,1);
call sp_Agregar_Servicios_has_Platos(2,2,5);
call sp_Agregar_Servicios_has_Platos(3,3,4);
call sp_Agregar_Servicios_has_Platos(4,4,3);
call sp_Agregar_Servicios_has_Platos(5,5,2);
call sp_Agregar_Servicios_has_Platos(6,6,2);
call sp_Agregar_Servicios_has_Platos(7,7,5);
call sp_Agregar_Servicios_has_Platos(8,8,2);
-- Eliminar Servicios_has_Platos

Delimiter //
	create procedure sp_Eliminar_Servicios_has_Platos(in Servicios_CodServicio int)
			begin
				delete from Servicios_has_Platos 
					where Servicios_CodigoServicio = Servicios_CodServicio;
			end //
Delimiter ; 

-- Listar Servicios_has_Platos

Delimiter //
	create procedure sp_Listar_Servicios_has_Platos()
			begin
				select Sh.Servicios_CodigoServicio, Sh.codigoPlato, Sh.codigoServicio
					from Servicios_has_Platos Sh;
			end //
Delimiter ;

-- buscar Servicios_has_Platos

Delimiter //
	create procedure sp_Buscar_Servicios_has_Platos(in Servicios_CodServicio int)
			begin
				select Sh.Servicios_CodigoServicio, Sh.codigoPlato, Sh.codigoServicio
					from Servicios_has_Platos Sh
                    where Servicios_CodigoServicio = Servicios_CodServicio;
			end //
Delimiter ;


-- Agregar Servicios_has_Empleados

Delimiter // 
	create procedure sp_Agregar_Servicios_has_Empleados(in Servicios_codigoServicio int,in codServicio int, in codEmpleado int , in fechaEvent date, in horaEvent time, in lugarEvent varchar(150))
			begin 
				insert into Servicios_has_Empleados(Servicios_codigoServicio,codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento) 
					values(Servicios_codigoServicio,codServicio, codEmpleado, fechaEvent, horaEvent, lugarEvent);
			end //
Delimiter ;

call sp_Agregar_Servicios_has_Empleados(1,1,1,'2023-05-25','15:52:00','hotel camnino real');
call sp_Agregar_Servicios_has_Empleados(2,2,2,'2023-06-24','14:59:00','zona 18');
call sp_Agregar_Servicios_has_Empleados(3,3,4,'2023-07-14','09:48:00','zona 15');
call sp_Agregar_Servicios_has_Empleados(4,4,5,'2023-09-18','19:30:00','Portales');
call sp_Agregar_Servicios_has_Empleados(5,5,3,'2023-11-20','12:14:00','zona 7');
call sp_Agregar_Servicios_has_Empleados(6,6,5,'2023-08-10','17:45:00','zona 9');
call sp_Agregar_Servicios_has_Empleados(7,7,4,'2023-02-07','11:00:00','villa Nueva');
call sp_Agregar_Servicios_has_Empleados(8,8,6,'2023-01-10','20:55:00','MetroSur');

-- Eliminar Servicios_has_Empleados

Delimiter //
	create procedure sp_Eliminar_Servicios_has_Empleados(in Servicios_codServicio int)
			begin
				delete from Servicios_has_Empleados 
					where Servicios_codigoServicio = Servicios_codServicio;
			end //
Delimiter ; 

-- listar Servicios_has_Empleados

Delimiter //
	create procedure sp_Listar_Servicios_has_Empleados()
			begin
				select Se.Servicios_codigoServicio, Se.codigoServicio, Se.codigoEmpleado, Se.fechaEvento, Se.horaEvento, Se.lugarEvento
					from Servicios_has_Empleados Se;
			end //
Delimiter ;

-- buscar Servicios_has_Empleados

Delimiter //
	create procedure sp_Buscar_Servicios_has_Empleados(in Servicios_codServicio int)
			begin
				select Se.Servicios_codigoServicio, Se.codigoServicio, Se.codigoEmpleado, Se.fechaEvento, Se.horaEvento, Se.lugarEvento
					from Servicios_has_Empleados Se
						where Servicios_codigoServicio = Servicios_codServicio;
			end //
Delimiter ;

Delimiter $$
	Create procedure sp_Reporte(in cod int)
	Begin
	Select E.nombreEmpresa, P.cantidadPresupuesto, E.direccion,S.tipoDeServicio, S.lugarServicio, PL.nombrePlato, 
			PL.precioPlato,PL.cantidadPlatos, PR.nombreProducto, EM.nombresEmpleado, T.descripcionTipo
		from Empresas E 
			Inner join Presupuestos P on E.codigoEmpresa = P.codigoEmpresa
			Inner join Servicios S on E.codigoEmpresa = S.codigoEmpresa
			Inner join Servicios_has_Platos SP on S.codigoServicio = SP.codigoServicio
			Inner join Platos PL on SP.codigoPlato = PL.codigoPlato
			Inner join Productos_has_Platos PP on PL.codigoPlato = PP.codigoPlato
			Inner join Productos PR on PP.codigoProducto = PR.codigoProducto
			Inner join Servicios_has_Empleados SE on S.codigoServicio = SE.codigoServicio
			Inner join Empleados EM on SE.codigoEmpleado = EM.codigoEmpleado
			Inner join TipoEmpleado T on EM.codigoTipoEmpelado = T.codigoTipoEmpleado
                where E.codigoEmpresa = cod;
	End $$
Delimiter ;
call sp_Reporte(5);