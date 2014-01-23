create table Users(
            Id numeric(15,0) not null,
            FirstName varchar(100) null,
            LastName varchar(100) null,
            MiddleName varchar(100) null,
            Login varchar(100) null,
            AddressLine1 varchar(100) null,
            AddressLine2 varchar(100) null,
            PostIndex varchar(10) null,
            Phone1 varchar(11) null,
            CreatedAt datetime,
            UpdatedAt datetime null
)


select count(*) from Users

delete from Users
