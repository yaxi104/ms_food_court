package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFeign {

    private Long id;

    private String firstName;

    private String lastName;

    private String idNumber;

    private String phoneNumber;

    private LocalDate dateBirth;

    private String email;

    private String password;

    private Long roleId;

    private Long restaurantId;
}
