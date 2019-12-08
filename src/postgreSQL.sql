CREATE TABLE inner_employee(
    id_employee BIGSERIAL,
    department VARCHAR(20),
    FOREIGN KEY (id_employee) REFERENCES employee(id_employee) ON delete set null
);


CREATE TABLE task(
    id_task BIGSERIAL,
    id_executor INT,
    name VARCHAR(20),
    code INT,
    PRIMARY KEY (id_task)
);

CREATE TABLE employee(
    id_employee BIGSERIAL,
    name VARCHAR(40),
    position VARCHAR(20),
    telephone VARCHAR(20),
    email VARCHAR(40),
    role VARCHAR(20),
    PRIMARY KEY (id_employee)
);

CREATE TABLE document(
    id_document BIGSERIAL,
    id_author INT,
    id_controller INT,
    id_task VARCHAR(20),
    create_date DATE,
    registration_date DATE,
    end_date DATE,
    name VARCHAR(40),
    type VARCHAR(20),
    PRIMARY KEY (id_document),
    FOREIGN KEY (id_author) REFERENCES employee(id_employee) ON delete set null,
    FOREIGN KEY (id_controller) REFERENCES employee(id_employee) ON delete set null
);