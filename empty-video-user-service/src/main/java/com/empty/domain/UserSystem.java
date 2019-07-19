package com.empty.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserSystem {
    private Date reg = new Date();               //register date
    private boolean active = false;

    private List<String> roles;         //admin, user
    private String status;             //online，offline，invisible，ban
    private long point = 0;              //user level (1-10)
    private List<String> achievement;   //user achievement
}
