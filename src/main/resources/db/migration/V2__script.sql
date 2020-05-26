create table `USER` (
    `ID` bigint not null auto_increment,
    `ACTIVE` bit not null,
    `EMAIL` varchar(150) not null,
    `NAME` varchar(255) not null,
    `PASSWORD` varchar(150) not null,
    `REGISTERED_AT` datetime not null,
    `TIME_ZONE_ID` varchar(255) not null,
    primary key (`ID`));

create table `USER_ROLE` (
    `USER_ID` bigint not null,
    `ROLE` varchar(50) not null,
    primary key (`USER_ID`, `ROLE`));

alter table `USER_ROLE` add constraint FK_USER_ROLE_USER_ID foreign key (`USER_ID`) references `USER` (`ID`);

CREATE TABLE `USER_IDENTITY_PROVIDER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL,
  `PROVIDER` varchar(50) NOT NULL,
  `PROVIDER_USER_ID` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_USER_IDENTITY_PROVIDER_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_USER_IDENTITY_PROVIDER_USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`ID`)
);
