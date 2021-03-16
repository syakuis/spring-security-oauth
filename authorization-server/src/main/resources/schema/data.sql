INSERT INTO `role` (`name`, `disabled`) VALUES ('ROLE_USER', 0);
INSERT INTO `role` (`name`, `disabled`) VALUES ('ROLE_ADMIN', 0);

INSERT INTO `role_hierarchy` (`role_id`,`role_child_id`) SELECT a.id, b.id FROM role as a join role as b where a.name = 'ROLE_ADMIN' and b.name = 'ROLE_USER';