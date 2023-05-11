Create Table `Customer` (
    id int NOT NULL AUTO_INCREMENT,
    email varchar(64),
    firstName varchar(64),
    lastName varchar(64),
    password varchar(255),
    status char(1) NOT NULL,
    lastLogin timestamp,
    CONSTRAINT PK_Customer PRIMARY KEY (id),
    CONSTRAINT UC_Customer_Email UNIQUE(email)
);

Create Unique Index UIDX_Customer_LastName_FirstName
    ON `Customer` (lastName,firstName);

Create Unique Index UIDX_Customer_FirstName_LastName
    ON `Customer` (firstName,lastName);

Create Table `Status` (
    status char(1) NOT NULL,
    statusName varchar(64),
    discountRate float(24),
    CONSTRAINT PK_Status PRIMARY KEY (status)
);

Create Table `Product` (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(32),
    quantity int(8),
    price float(53),
    CONSTRAINT PK_Product PRIMARY KEY (id)
);

Create Unique Index UIDX_Product_Name
    ON Product (name);

Create Index IDX_Product_Price
    ON Product (price);

Create Table `Orders` (
    id int NOT NULL AUTO_INCREMENT,
    customerId int NOT NULL,
    total float(53),
    orderDate date,
    deliveryDate date,
    CONSTRAINT PK_Orders PRIMARY KEY (id)
);

Create Table `OrderItem` (
    orderId int NOT NULL,
    productId int NOT NULL,
    quantity int(8),
    subTotal float(53),
    hasBeenRefunded bool,
    CONSTRAINT PK_OrderItem PRIMARY KEY (orderId,productId)
);

-- Foreign keys --

Alter Table `Customer`
    ADD CONSTRAINT FK_Customer_Status FOREIGN KEY (status) REFERENCES `Status` (status);

Alter Table `Orders`
    ADD CONSTRAINT FK_Orders_CustomerId FOREIGN KEY (customerId) REFERENCES `Customer` (id);

Alter Table `OrderItem`
    ADD CONSTRAINT FK_OrderItem_OrderId FOREIGN KEY (orderId) REFERENCES `Orders` (id),
    ADD CONSTRAINT FK_OrderItem_ProductId FOREIGN KEY (productId) REFERENCES `Product` (id);

