Create Table `Toto` (
    id int NOT NULL AUTO_INCREMENT,
    myDate date,
    myTime timestamp,
    myBool bool,
    myChar char(1),
    myString varchar(64),
    mySizedString varchar(256),
    myInt int,
    mySizedInt int(16),
    myFloat float(24),
    myDouble float(53),
    CONSTRAINT PK_Toto PRIMARY KEY (id),
    CONSTRAINT UC_Toto_MyString_MyChar UNIQUE(myString,myChar)
);

Create Index IDX_Toto_MyInt_MyFloat_MyDouble
    ON Toto (myInt,myFloat,myDouble);

Create Unique Index UIDX_Toto_MyString_MyInt
    ON `Toto` (myString,myInt);

Create Table `Other` (
    name varchar(64) NOT NULL,
    isOther bool NOT NULL,
    something1 int NOT NULL,
    something2 int NOT NULL,
    favoriteNumber int NOT NULL,
    CONSTRAINT PK_Other PRIMARY KEY (name,isOther,favoriteNumber)
);

-- Foreign keys --

Alter Table `Other`
    ADD CONSTRAINT FK_Other_Something1 FOREIGN KEY (something1) REFERENCES `Toto` (id),
    ADD CONSTRAINT FK_Other_Something2 FOREIGN KEY (something2) REFERENCES `Toto` (id);

