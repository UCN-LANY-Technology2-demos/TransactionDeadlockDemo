--TRUNCATE TABLE Customers;

INSERT INTO Customers VALUES ('Rick', 'A');
INSERT INTO Customers VALUES ('Morty', 'A');

--TRUNCATE TABLE Orders;

INSERT INTO Orders (CustomerId, Date, Total, Status) VALUES (1, '2022-10-11', 100, 'A');
INSERT INTO Orders (CustomerId, Date, Total, Status) VALUES (1, '2022-11-05', 200, 'F');
INSERT INTO Orders (CustomerId, Date, Total, Status) VALUES (2, '2022-05-05', 150, 'F');

     