package sample;

import java.time.LocalDate;

public class User {
    private String department;
    private String name;
    private String name_org;
    private String email;
    private String telephone;
    private String position, fio;
    private int number;
    private int id_inner;
    private int id_external;
    private int id_executor;
    private int code;
    private int id;
    private LocalDate create_date;
    private LocalDate registration_date;
    private String type;
    private String executor;
    private String role;
    private String task;
    private LocalDate end_date;
    private int id_task;

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public User(int id, String name, String position, String telephone, String email, String role) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.telephone = telephone;
        this.email = email;
        this.role = role;
    }

    public User(int id, int number, LocalDate create_date, LocalDate registration_date, int code, String type, String name, String executor,
                LocalDate end_date, String task) {
        this.id = id;
        this.number = number;
        this.create_date = create_date;
        this.registration_date = registration_date;
        this.code = code;
        this.type = type;
        this.name = name;
        this.executor = executor;
        this.end_date = end_date;
        this.task = task;
    }

    public User(int number, String name, String type, LocalDate end_date) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.end_date = end_date;
    }

    public User(int number, String name, String task, String fio, String position) {
        this.name = name;
        this.number = number;
        this.task = task;
        this.fio = fio;
        this.position = position;
    }

    public User(int id_inner, String department, String name) {
        this.id_inner = id_inner;
        this.department = department;
        this.name = name;
    }

    public User(int id_external, int code, String name_org) {
        this.id_external = id_external;
        this.code = code;
        this.name_org = name_org;
    }

    public User(int id, LocalDate end_date, int id_executor, String name) {
        this.id = id;
        this.end_date = end_date;
        this.id_executor = id_executor;
        this.name = name;
    }

    public User(int id_task, int id_executor, String name, int code) {
        this.id_task = id_task;
        this.id_executor = id_executor;
        this.name = name;
        this.code = code;
    }

    public int getId_executor() {
        return id_executor;
    }

    public void setId_executor(int id_executor) {
        this.id_executor = id_executor;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_org() {
        return name_org;
    }

    public void setName_org(String name_org) {
        this.name_org = name_org;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId_inner() {
        return id_inner;
    }

    public void setId_inner(int id_inner) {
        this.id_inner = id_inner;
    }

    public int getId_external() {
        return id_external;
    }

    public void setId_external(int id_external) {
        this.id_external = id_external;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDate create_date) {
        this.create_date = create_date;
    }

    public LocalDate getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(LocalDate registration_date) {
        this.registration_date = registration_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

}
