drop table
    Hours, LineItems, Orders, Employees,
    Ingredients, Dishes, Inventory;

create table Inventory (
    id integer auto_increment primary key,
    quantity integer not null,
    reorderLevel integer not null
);

create table Dishes (
    id integer auto_increment primary key,
    name varchar(50) not null,
    price float not null,
    category enum (
        'Bread', 'Pastries', 'Cakes', 'Cookies',
        'Muffins', 'Beverages', 'Sandwiches', 'Salads'
    ) not null,
    description varchar(75)
);

create table Ingredients (
    invId integer not null,
    dishId integer not null,
    amount integer not null,
    unit varchar(10) not null,
    primary key (invId, dishId),
    foreign key (invId) references Inventory(id),
    foreign key (dishId) references Dishes(id)
);

create table Employees (
    id integer auto_increment primary key,
    firstName varchar(40) not null,
    middleInitial char(1),
    lastName varchar(40) not null,
    role enum(
	    'Baker', 'Pastry Chef', 'Cashier', 'Barista',
        'Manager', 'Cleaner', 'Delivery Driver'
	) not null,
    dateHired timestamp not null default current_timestamp
);

create table Orders (
    id integer auto_increment primary key,
    ccn varchar(16) not null,
    date timestamp not null default current_timestamp,
    employeeId integer not null,
    total float not null,
    foreign key (employeeId) references Employees(id)
);

create table LineItems (
    dishId integer not null,
    orderId integer not null,
    quantity integer not null default 1,
    price float not null,
    specialInstructions varchar(250),
    primary key (dishId, orderId),
    foreign key (dishId) references Dishes(id),
    foreign key (orderId) references Orders(id)
);

create table Hours (
    id integer auto_increment primary key,
    employeeId integer not null,
    clockedIn timestamp not null default current_timestamp,
    clockedOut timestamp null default null,
    foreign key (employeeId) references Employees(id)
);