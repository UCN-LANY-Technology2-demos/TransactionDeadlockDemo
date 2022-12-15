USE [master]
GO
/****** Database [TransactionDeadlockDemo] ******/
CREATE DATABASE [TransactionDeadlockDemo]
GO

if not exists(select * from syslogins where name = 'student')
begin

CREATE LOGIN [student] WITH PASSWORD=N'P@$$w0rd', DEFAULT_DATABASE=[master], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF

end

USE [TransactionDeadlockDemo]
GO

/****** User [student] ******/
CREATE USER [student] FOR LOGIN [student] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_datareader] ADD MEMBER [student]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [student]
GO

/****** Table [Customers] ******/
CREATE TABLE [Customers](
	[Id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[LatestOrderStatus] [nchar](1) NOT NULL
)
GO

/****** Table [Orders] ******/
CREATE TABLE [Orders](
	[Id] [int] IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[CustomerId] [int] FOREIGN KEY REFERENCES [Customers](Id) NOT NULL,
	[Date] [date] NULL,
	[Total] [float] NULL,
	[Status] [nchar](1) NULL
)
GO
