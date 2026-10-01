IF DB_ID(N'BookStore_24110202') IS NULL CREATE DATABASE BookStore_24110202;
GO
USE BookStore_24110202;
GO
IF OBJECT_ID('dbo.users','U') IS NULL CREATE TABLE dbo.users (
 id int IDENTITY(1,1) NOT NULL CONSTRAINT PK_users PRIMARY KEY,
 email varchar(50) NOT NULL CONSTRAINT UQ_users_email UNIQUE,
 fullname nvarchar(50) NULL, phone int NULL, passwd varchar(32) NOT NULL,
 signup_date datetime NULL CONSTRAINT DF_users_signup DEFAULT GETDATE(), last_login datetime NULL,
 is_admin bit NULL CONSTRAINT DF_users_admin DEFAULT 0);
IF OBJECT_ID('dbo.books','U') IS NULL CREATE TABLE dbo.books (
 bookid int IDENTITY(1,1) NOT NULL CONSTRAINT PK_books PRIMARY KEY,
 isbn int NULL, title varchar(200) NULL, publisher varchar(100) NULL, price decimal(6,2) NULL,
 description text NULL, publish_date date NULL, cover_image varchar(200) NULL, quantity int NULL,
 CONSTRAINT CK_books_price CHECK(price IS NULL OR price>=0), CONSTRAINT CK_books_quantity CHECK(quantity IS NULL OR quantity>=0));
IF OBJECT_ID('dbo.author','U') IS NULL CREATE TABLE dbo.author (
 author_id int IDENTITY(1,1) NOT NULL CONSTRAINT PK_author PRIMARY KEY,
 author_name varchar(100) NULL, date_of_birth date NULL);
IF OBJECT_ID('dbo.book_author','U') IS NULL CREATE TABLE dbo.book_author (
 bookid int NOT NULL, author_id int NOT NULL, CONSTRAINT PK_book_author PRIMARY KEY(bookid,author_id),
 CONSTRAINT FK_book_author_books FOREIGN KEY(bookid) REFERENCES dbo.books(bookid), CONSTRAINT FK_book_author_author FOREIGN KEY(author_id) REFERENCES dbo.author(author_id));
IF OBJECT_ID('dbo.rating','U') IS NULL CREATE TABLE dbo.rating (
 userid int NOT NULL, bookid int NOT NULL, rating tinyint NULL, review_text text NULL,
 CONSTRAINT PK_rating PRIMARY KEY(userid,bookid), CONSTRAINT FK_rating_users FOREIGN KEY(userid) REFERENCES dbo.users(id),
 CONSTRAINT FK_rating_books FOREIGN KEY(bookid) REFERENCES dbo.books(bookid), CONSTRAINT CK_rating_range CHECK(rating IS NULL OR rating BETWEEN 1 AND 5));
GO

-- Cap nhat lai cac cot text/varchar sang nvarchar de luu tieng Viet khong bi dau cham hoi ?
IF EXISTS(SELECT 1 FROM sys.columns WHERE object_id=OBJECT_ID('dbo.books') AND name='cover_image' AND max_length < 400)
ALTER TABLE dbo.books ALTER COLUMN cover_image varchar(200) NULL;
ALTER TABLE dbo.rating ALTER COLUMN review_text nvarchar(max) NULL;
ALTER TABLE dbo.books ALTER COLUMN description nvarchar(max) NULL;
ALTER TABLE dbo.books ALTER COLUMN title nvarchar(200) NULL;
ALTER TABLE dbo.books ALTER COLUMN publisher nvarchar(100) NULL;
ALTER TABLE dbo.author ALTER COLUMN author_name nvarchar(100) NULL;
GO

IF OBJECT_ID('dbo.orders','U') IS NULL CREATE TABLE dbo.orders (
 order_id int IDENTITY(1,1) NOT NULL CONSTRAINT PK_orders PRIMARY KEY,
 user_id int NOT NULL,
 recipient_name nvarchar(100) NOT NULL,
 phone varchar(20) NOT NULL,
 shipping_address nvarchar(255) NOT NULL,
 note nvarchar(500) NULL,
 payment_method varchar(10) NOT NULL CONSTRAINT DF_orders_payment DEFAULT 'COD',
 status varchar(30) NOT NULL CONSTRAINT DF_orders_status DEFAULT 'NEW',
 total decimal(12,2) NOT NULL,
 created_at datetime2 NOT NULL CONSTRAINT DF_orders_created DEFAULT SYSUTCDATETIME(),
 updated_at datetime2 NOT NULL CONSTRAINT DF_orders_updated DEFAULT SYSUTCDATETIME(),
 CONSTRAINT FK_orders_users FOREIGN KEY(user_id) REFERENCES dbo.users(id),
 CONSTRAINT CK_orders_payment CHECK(payment_method='COD'),
 CONSTRAINT CK_orders_status CHECK(status IN ('NEW','CONFIRMED','PREPARING','SHIPPING','OUT_FOR_DELIVERY','DELIVERED','CANCELLED','RETURNED')),
 CONSTRAINT CK_orders_total CHECK(total>=0));
IF OBJECT_ID('dbo.order_items','U') IS NULL CREATE TABLE dbo.order_items (
 order_item_id int IDENTITY(1,1) NOT NULL CONSTRAINT PK_order_items PRIMARY KEY,
 order_id int NOT NULL,
 book_id int NULL,
 book_title nvarchar(200) NOT NULL,
 unit_price decimal(12,2) NOT NULL,
 quantity int NOT NULL,
 CONSTRAINT FK_order_items_orders FOREIGN KEY(order_id) REFERENCES dbo.orders(order_id),
 CONSTRAINT FK_order_items_books FOREIGN KEY(book_id) REFERENCES dbo.books(bookid) ON DELETE SET NULL,
 CONSTRAINT CK_order_items_price CHECK(unit_price>=0),
 CONSTRAINT CK_order_items_quantity CHECK(quantity>0));
IF NOT EXISTS(SELECT 1 FROM sys.indexes WHERE name='IX_orders_user_status_created' AND object_id=OBJECT_ID('dbo.orders'))
 CREATE INDEX IX_orders_user_status_created ON dbo.orders(user_id,status,created_at DESC);
IF NOT EXISTS(SELECT 1 FROM sys.indexes WHERE name='IX_orders_status_created' AND object_id=OBJECT_ID('dbo.orders'))
 CREATE INDEX IX_orders_status_created ON dbo.orders(status,created_at DESC);
IF NOT EXISTS(SELECT 1 FROM sys.indexes WHERE name='IX_order_items_order' AND object_id=OBJECT_ID('dbo.order_items'))
 CREATE INDEX IX_order_items_order ON dbo.order_items(order_id);
GO

IF NOT EXISTS(SELECT 1 FROM dbo.users WHERE email='admin@example.com') INSERT dbo.users(email,fullname,passwd,is_admin) VALUES('admin@example.com',N'Admin Demo','0e7517141fb53f21ee439b355b5a1d0a',1);
IF NOT EXISTS(SELECT 1 FROM dbo.users WHERE email='user@example.com') INSERT dbo.users(email,fullname,passwd,is_admin) VALUES('user@example.com',N'User Demo','448ddd517d3abb70045aea6929f02367',0);
GO
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='Nguyen Nhat Anh') INSERT dbo.author(author_name,date_of_birth) VALUES ('Nguyen Nhat Anh','1955-05-07');
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='To Hoai') INSERT dbo.author(author_name,date_of_birth) VALUES ('To Hoai','1920-09-27');
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='Nam Cao') INSERT dbo.author(author_name,date_of_birth) VALUES ('Nam Cao','1917-10-29');
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='Nguyen Du') INSERT dbo.author(author_name,date_of_birth) VALUES ('Nguyen Du','1765-01-03');
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='Vu Trong Phung') INSERT dbo.author(author_name,date_of_birth) VALUES ('Vu Trong Phung','1912-10-20');
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='Tran Dang Khoa') INSERT dbo.author(author_name,date_of_birth) VALUES ('Tran Dang Khoa','1958-04-24');
IF NOT EXISTS(SELECT 1 FROM dbo.author WHERE author_name='Doan Gioi') INSERT dbo.author(author_name,date_of_birth) VALUES ('Doan Gioi','1925-05-17');
GO

-- Bang tam chua sach kem link anh bia that (picsum.photos - anh ngau nhien co seed co dinh, luon ra cung 1 anh)
DECLARE @books TABLE(title varchar(200), isbn int, cover varchar(200));
INSERT @books VALUES
('Mat biec',100000001,'https://picsum.photos/seed/matbiec/300/450'),
('Cho toi xin mot ve di tuoi tho',100000002,'https://picsum.photos/seed/chotoixin/300/450'),
('Toi thay hoa vang tren co xanh',100000003,'https://picsum.photos/seed/hoavang/300/450'),
('De men phieu luu ky',100000004,'https://picsum.photos/seed/demen/300/450'),
('Lao Hac',100000005,'https://picsum.photos/seed/laohac/300/450'),
('Chi Pheo',100000006,'https://picsum.photos/seed/chipheo/300/450'),
('Truyen Kieu',100000007,'https://picsum.photos/seed/truyenkieu/300/450'),
('So do',100000008,'https://picsum.photos/seed/sodo/300/450'),
('Giong hang xom',100000009,'https://picsum.photos/seed/gionghangxom/300/450'),
('Con chim xanh bien',100000010,'https://picsum.photos/seed/conchimxanh/300/450'),
('Nhat ky trong tu',100000011,'https://picsum.photos/seed/nhatky/300/450'),
('Vang bong mot thoi',100000012,'https://picsum.photos/seed/vangbong/300/450'),
('Dat rung phuong Nam',100000013,'https://picsum.photos/seed/datrung/300/450');

INSERT dbo.books(isbn,title,publisher,price,description,publish_date,cover_image,quantity)
SELECT b.isbn,b.title,'NXB Tre',CAST(89.00 AS decimal(6,2)),'Sach demo cho bai thi','2020-01-01',b.cover,20
FROM @books b WHERE NOT EXISTS(SELECT 1 FROM dbo.books x WHERE x.title=b.title);
GO

-- Neu sach da ton tai tu truoc (chua co anh) thi cap nhat lai link anh
UPDATE bk SET bk.cover_image = t.cover
FROM dbo.books bk
JOIN (VALUES
('Mat biec','https://picsum.photos/seed/matbiec/300/450'),
('Cho toi xin mot ve di tuoi tho','https://picsum.photos/seed/chotoixin/300/450'),
('Toi thay hoa vang tren co xanh','https://picsum.photos/seed/hoavang/300/450'),
('De men phieu luu ky','https://picsum.photos/seed/demen/300/450'),
('Lao Hac','https://picsum.photos/seed/laohac/300/450'),
('Chi Pheo','https://picsum.photos/seed/chipheo/300/450'),
('Truyen Kieu','https://picsum.photos/seed/truyenkieu/300/450'),
('So do','https://picsum.photos/seed/sodo/300/450'),
('Giong hang xom','https://picsum.photos/seed/gionghangxom/300/450'),
('Con chim xanh bien','https://picsum.photos/seed/conchimxanh/300/450'),
('Nhat ky trong tu','https://picsum.photos/seed/nhatky/300/450'),
('Vang bong mot thoi','https://picsum.photos/seed/vangbong/300/450'),
('Dat rung phuong Nam','https://picsum.photos/seed/datrung/300/450')
) t(title,cover) ON t.title = bk.title
WHERE bk.cover_image IS NULL OR bk.cover_image = 'assets/images/book-placeholder.svg' OR bk.cover_image NOT LIKE 'http%';
GO

INSERT dbo.book_author(bookid,author_id)
SELECT b.bookid,a.author_id FROM dbo.books b JOIN dbo.author a ON a.author_name IN ('Nguyen Nhat Anh','To Hoai','Nam Cao','Nguyen Du','Vu Trong Phung','Tran Dang Khoa','Doan Gioi')
WHERE ((b.title='Mat biec' AND a.author_name IN ('Nguyen Nhat Anh','Tran Dang Khoa')) OR (b.title LIKE 'Cho toi%' AND a.author_name IN ('Nguyen Nhat Anh','Nam Cao'))
OR (b.title LIKE 'Toi thay%' AND a.author_name IN ('Nguyen Nhat Anh','Tran Dang Khoa')) OR (b.title LIKE 'De men%' AND a.author_name='To Hoai')
OR (b.title IN ('Lao Hac','Chi Pheo','Nhat ky trong tu') AND a.author_name='Nam Cao') OR (b.title='Truyen Kieu' AND a.author_name='Nguyen Du')
OR (b.title='So do' AND a.author_name='Vu Trong Phung') OR (b.title='Giong hang xom' AND a.author_name='Tran Dang Khoa')
OR (b.title='Con chim xanh bien' AND a.author_name='Nguyen Nhat Anh') OR (b.title='Vang bong mot thoi' AND a.author_name='To Hoai')
OR (b.title='Dat rung phuong Nam' AND a.author_name IN ('Doan Gioi','To Hoai')))
AND NOT EXISTS(SELECT 1 FROM dbo.book_author x WHERE x.bookid=b.bookid AND x.author_id=a.author_id);
GO

INSERT dbo.rating(userid,bookid,rating,review_text)
SELECT u.id,b.bookid,v.rating,v.review_text FROM (VALUES
('user@example.com','Mat biec',5,'Cau chuyen rat cam dong.'),('admin@example.com','Mat biec',4,'Mot cuon sach dang doc.'),
('user@example.com','Cho toi xin mot ve di tuoi tho',5,'Nhieu ky niem tuoi tho.'),('admin@example.com','De men phieu luu ky',5,'Phu hop moi lua tuoi.'),
('user@example.com','Lao Hac',4,'Tac pham kinh dien.'),('admin@example.com','Chi Pheo',5,'Nhan vat duoc khac hoa sau sac.'),
('user@example.com','Truyen Kieu',5,'Gia tri van hoc lon.'),('admin@example.com','So do',4,'Giong van tram biem.'),
('user@example.com','Giong hang xom',4,'Doc de thuong.'),('admin@example.com','Con chim xanh bien',5,'Sach hay.')
) v(email,title,rating,review_text) JOIN dbo.users u ON u.email=v.email JOIN dbo.books b ON b.title=v.title
WHERE NOT EXISTS(SELECT 1 FROM dbo.rating r WHERE r.userid=u.id AND r.bookid=b.bookid);
GO
