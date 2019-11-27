
CREATE TABLE inner_guys(
    id_inner BIGSERIAL,
    department VARCHAR(20),
    name VARCHAR(20),
    PRIMARY KEY (id_inner)
);

CREATE TABLE external_guys(
    id_external BIGSERIAL,
    code BIGSERIAL,
    name_org VARCHAR(20),
    PRIMARY KEY (id_external)
);

CREATE TABLE executor(
    id_executor BIGSERIAL,
    fio VARCHAR(40),
    position VARCHAR(20),
    telephone VARCHAR(20),
    email VARCHAR(40),
    choice VARCHAR(20),
    PRIMARY KEY (id_executor)
);

CREATE TABLE document(
    id_document BIGSERIAL,
    number INT,
    create_date DATE,
    registration_date DATE,
    code INT,
    type VARCHAR(20),
    name VARCHAR(40),
    id_executor INT,
    end_date DATE,
    task VARCHAR(40),
    PRIMARY KEY (id_document),
    FOREIGN KEY (id_executor) REFERENCES executor(id_executor)  ON delete  set null
);
