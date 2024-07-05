-- DROP TABLE
--     Hours, LineItems, Orders, Employees,
--     Ingredients, Dishes, Inventory;

CREATE TABLE Inventory (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    quantity INTEGER NOT NULL,
    reorderLevel INTEGER NOT NULL
);

CREATE TABLE Dishes (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price FLOAT NOT NULL,
    category ENUM (
        'Bread', 'Pastries', 'Cakes', 'Cookies',
        'Muffins', 'Beverages', 'Sandwiches', 'Salads'
    ) NOT NULL,
    description VARCHAR(75)
);

CREATE TABLE Ingredients (
    invId INTEGER NOT NULL,
    dishId INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    unit VARCHAR(10) NOT NULL,
    PRIMARY KEY (invId, dishId),
    FOREIGN KEY (invId) REFERENCES Inventory(id),
    FOREIGN KEY (dishId) REFERENCES Dishes(id)
);

CREATE TABLE Employees (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(40) NOT NULL,
    middleInitial CHAR(1),
    lastName VARCHAR(40) NOT NULL,
    role ENUM(
        'Baker', 'Pastry Chef', 'Cashier', 'Barista',
        'Manager', 'Cleaner', 'Delivery Driver'
    ) NOT NULL,
    dateHired TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Orders (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    ccn VARCHAR(16) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    employeeId INTEGER NOT NULL,
    total FLOAT NOT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employees(id)
);

CREATE TABLE LineItems (
    dishId INTEGER NOT NULL,
    orderId INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    price FLOAT NOT NULL,
    specialInstructions VARCHAR(250),
    PRIMARY KEY (dishId, orderId),
    FOREIGN KEY (dishId) REFERENCES Dishes(id),
    FOREIGN KEY (orderId) REFERENCES Orders(id)
);

CREATE TABLE Hours (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    employeeId INTEGER NOT NULL,
    clockedIn TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    clockedOut TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employees(id)
);
