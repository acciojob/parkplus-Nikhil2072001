package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String phoneName;
    private String password;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List <Reservation> reservationList;

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

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public User(String name, String phoneName, String password) {
        this.name = name;
        this.phoneName = phoneName;
        this.password = password;
    }

    public User(int id, String name, String phoneName, String password, List<Reservation> reservationList) {
        this.id = id;
        this.name = name;
        this.phoneName = phoneName;
        this.password = password;
        this.reservationList = reservationList;
    }

    public User() {
    }
}
