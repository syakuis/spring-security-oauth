-- I1dUKj5FJzw8c2tzVCZy
insert ignore into oauth2_client_details (access_token_validity, additional_information, authorities, authorized_grant_types, autoapprove, client_secret, refresh_token_validity, resource_ids, scope, web_server_redirect_uri, client_id) values (6000, null, null, 'client-credentials,password,refresh_token', null, '{bcrypt}$2a$10$MXkTCwNCzOpzntcjGC6K0OsoE68N5KR2c6qpm3MTFv7NzgALzDhEe', 6000, null, 'read', null, '5f4896080ecee6c7817c68afaf634b04ec74e9ccccd9b64a69845284ef36ff4fdb97480deaae1535');

insert ignore into account (blocked, disabled, name, password, registered_on, username, uid) values (0, 0, '테스트', '{bcrypt}$2a$10$IIzY6HXVbRgpgIkTcj7ocO8pvE4oHSNHmQMZW/lx7Lo2oySEPGc.i', '2021-05-02T13:09:17.276802', 'test', unhex(replace('c7b782b5-f8b5-11eb-972f-02a208506bd6', '-', '')));