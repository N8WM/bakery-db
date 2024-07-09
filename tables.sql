DROP TABLE
Hours, LineItems, Orders, Employees,
Ingredients, Dishes, Inventory;

CREATE TABLE Inventory (
    id integer AUTO_INCREMENT PRIMARY KEY,
    quantity integer NOT NULL,
    reorderLevel integer NOT NULL
);

CREATE TABLE Dishes (
    id integer AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) NOT NULL,
    price float NOT NULL,
    category enum(
        'Bread', 'Pastries', 'Cakes', 'Cookies',
        'Muffins', 'Beverages', 'Sandwiches', 'Salads'
    ) NOT NULL,
    description varchar(75)
);

CREATE TABLE Ingredients (
    invId integer NOT NULL,
    dishId integer NOT NULL,
    amount integer NOT NULL,
    unit varchar(10) NOT NULL,
    PRIMARY KEY (invId, dishId),
    FOREIGN KEY (invId) REFERENCES Inventory (id),
    FOREIGN KEY (dishId) REFERENCES Dishes (id)
);

CREATE TABLE Employees (
    id integer AUTO_INCREMENT PRIMARY KEY,
    firstName varchar(40) NOT NULL,
    middleInitial char(1),
    lastName varchar(40) NOT NULL,
    role enum(
        'Baker', 'Pastry Chef', 'Cashier', 'Barista',
        'Manager', 'Cleaner', 'Delivery Driver'
    ) NOT NULL,
    dateHired TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Orders (
    id integer AUTO_INCREMENT PRIMARY KEY,
    ccn varchar(16) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    employeeId integer NOT NULL,
    total float NOT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employees (id)
);

CREATE TABLE LineItems (
    dishId integer NOT NULL,
    orderId integer NOT NULL,
    quantity integer NOT NULL DEFAULT 1,
    price float NOT NULL,
    specialInstructions varchar(250),
    PRIMARY KEY (dishId, orderId),
    FOREIGN KEY (dishId) REFERENCES Dishes (id),
    FOREIGN KEY (orderId) REFERENCES Orders (id)
);

CREATE TABLE Hours (
    id integer AUTO_INCREMENT PRIMARY KEY,
    employeeId integer NOT NULL,
    clockedIn TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    clockedOut TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employees (id)
);
