DROP TABLE IF EXISTS
Hours, LineItems, Orders, Employees,
Ingredients, Dishes, Inventory;

DROP TRIGGER IF EXISTS inventoryAdd;
DROP TRIGGER IF EXISTS inventoryUpdate;

CREATE TABLE Inventory (
    invId integer AUTO_INCREMENT PRIMARY KEY,
    name varchar(30) NOT NULL,
    quantity float NOT NULL,
    unit enum(
        'pcs', 'doz', 'g', 'kg', 'ml', 'l', 'lb',
        'oz', 'cup', 'tsp', 'tbsp', 'gal'
    ) NOT NULL,
    reorderLevel float NOT NULL
);

delimiter $$

CREATE TRIGGER inventoryInsert
BEFORE INSERT on Inventory
FOR EACH ROW
BEGIN
    IF (NEW.quantity < 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Quantity must be greater than 0";
    END IF;
    IF (NEW.reorderLevel < 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Reorder Level must be greater than 0";
    END IF;
end $$

CREATE TRIGGER inventoryUpdate
BEFORE UPDATE on Inventory
FOR EACH ROW
BEGIN
    IF (NEW.quantity < 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Quantity must be greater than 0";
    END IF;
    IF (NEW.reorderLevel < 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Reorder Level must be greater than 0";
    END IF;

end $$

delimiter ;

CREATE TABLE Dishes (
    dishId integer AUTO_INCREMENT PRIMARY KEY,
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
    quantity float NOT NULL,
    PRIMARY KEY (invId, dishId),
    FOREIGN KEY (invId) REFERENCES Inventory (invId),
    FOREIGN KEY (dishId) REFERENCES Dishes (dishId)
);

CREATE TABLE Employees (
    emplId integer AUTO_INCREMENT PRIMARY KEY,
    firstName varchar(40) NOT NULL,
    middleInitial char(1),
    lastName varchar(40) NOT NULL,
    role enum(
        'Baker', 'Pastry Chef', 'Cashier', 'Barista',
        'Manager', 'Cleaner', 'Delivery Driver'
    ) NOT NULL,
    dateHired date NOT NULL
);

CREATE TABLE Orders (
    orderId integer AUTO_INCREMENT PRIMARY KEY,
    ccn varchar(16) NOT NULL,
    date date NOT NULL,
    emplId integer NOT NULL,
    total float NOT NULL,
    FOREIGN KEY (emplId) REFERENCES Employees (emplId)
);

CREATE TABLE LineItems (
    dishId integer NOT NULL,
    orderId integer NOT NULL,
    quantity integer NOT NULL DEFAULT 1,
    price float NOT NULL,
    specialInstructions varchar(250),
    PRIMARY KEY (dishId, orderId),
    FOREIGN KEY (dishId) REFERENCES Dishes (dishId),
    FOREIGN KEY (orderId) REFERENCES Orders (orderId)
);

CREATE TABLE Hours (
    hid integer AUTO_INCREMENT PRIMARY KEY,
    emplId integer NOT NULL,
    clockedIn TIMESTAMP NOT NULL,
    clockedOut TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (emplId) REFERENCES Employees (emplId)
);
