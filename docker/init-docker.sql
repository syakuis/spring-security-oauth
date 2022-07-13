CREATE DATABASE IF NOT EXISTS oauth2 CHARACTER SET utf8 COLLATE utf8_general_ci;

grant all privileges on oauth2.* to 'oauth2'@'%';

flush privileges;