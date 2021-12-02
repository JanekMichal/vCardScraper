package com.example.vCard.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Company {
    String name;
    String telephone;
    String email;
    String postalCode;
    String street;
    String city;
    String country;
    String url;
}
