CREATE DATABASE IF NOT EXISTS identity_account CHARACTER SET utf8 COLLATE utf8_general_ci;

grant all privileges on identity_account.* to 'identity'@'%';

flush privileges;