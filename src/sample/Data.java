package sample;

import java.time.LocalDate;

public class Data {


    private String department;
    private String name;
    private String name_org;
    private String email;
    private String telephone;
    private String position;
    private int number;
    private int id_inner,id_external, code,id;
    private LocalDate create_date;
    private LocalDate registration_date;
    private String type;
    private String executor;

    public Data (int id, int number, LocalDate create_date, LocalDate registration_date, int code, String type, String name, String executor){
        this.id=id;
        this.number=number;
        this.create_date=create_date;
        this.registration_date=registration_date;
        this.code=code;
        this.type=type;
        this.name=name;
        this.executor=executor;
    }

    public Data(int id, String name, String position, String telephone, String email){
        this.id=id;
        this.name=name;
        this.position=position;
        this.telephone=telephone;
        this.email=email;
    }

    public Data(int id_inner, String department, String name){
        this.id_inner=id_inner;
        this.department=department;
        this.name=name;
    }

    public Data(int id_external, int code, String name_org){
        this.id_external=id_external;
        this.code = code;
        this.name_org=name_org;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
}
