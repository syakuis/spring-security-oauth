CREATE DATABASE IF NOT EXISTS oauth2_account CHARACTER SET utf8 COLLATE utf8_general_ci;

grant all privileges on oauth2_account.* to 'oauth2'@'%';

flush privileges;