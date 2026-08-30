package ru.alex.entity;

import jakarta.persistence.*;
import ru.alex.entity.dto.UserRole;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "name",nullable = false,length = 20)
    private String name;

    @Column(name = "email",unique = true, nullable = false,length = 30)
    private String email;

    @Column(name = "password",nullable = false,length = 60)
    private String password;

    @Column(name = "role",nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Record> records;

    public User() {
    }

    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
    public boolean isSimpleUser(){
        return this.role == UserRole.USER;
    }
    public boolean isAdmin(){
       return this.role == UserRole.ADMIN;
    }
    public boolean isSuperAdmin(){
        return this.role == UserRole.SUPER_ADMIN;
    }
}
