CREATE TABLE DbLease (
    ip VARCHAR(255) NOT NULL UNIQUE,
    hostname VARCHAR(255),
    leasedTo VARCHAR(255),
    lastContact BIGINT,
    PRIMARY KEY (ip)
);