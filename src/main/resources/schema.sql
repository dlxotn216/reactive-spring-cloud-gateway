CREATE TABLE APP_CLIENT
(
    CLIENT_KEY BIGINT NOT NULL 
        PRIMARY KEY AUTO_INCREMENT,
    MODIFIED_AT DATETIME NOT NULL,
    CLIENT_ID VARCHAR(100) NOT NULL,
    CLIENT_SECRET VARCHAR(500) NOT NULL,
    CONNECT_URL VARCHAR(100) NOT NULL,
    REASON VARCHAR(1000) NOT NULL,
    DELETED TINYINT(1) NULL,
    CREATED_BY BIGINT NOT NULL,
    CREATED_AT DATETIME NULL,
    MODIFIED_BY BIGINT NOT NULL
);

