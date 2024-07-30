drop table if exists account;

create table account (
  account_id              varchar(20) primary key,
  account_balance         numeric not null,
  account_currency        char(3),
  account_credit_limit    numeric
);

drop table if exists transaction;

create table transaction (
  transaction_ref       identity     not null primary key,
  transaction_type      char(6)      not null,
  transaction_amount    numeric      not null,
  transaction_currency  char(3)      not null,
  account_id            varchar(20)  not null
);