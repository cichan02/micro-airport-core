package by.piskunou.solvdlaba.domain;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {

    public enum Gender {
        MALE, FEMALE
    }
    public enum Age {
        ADULT, CHILD, INFANT;
    }

    private Long id;
    private Country country;
    private String firstname;
    private String surname;
    private Passport passport;
    private LocalDate dateOfBirth;
    private Age age;
    private Gender gender;

    public Passenger(Long id) {
        this.id = id;
    }

    public Passenger(Age age) {
        this.age = age;
    }

    public Passenger(Long id, Passport passport) {
        this.id = id;
        this.passport = passport;
    }

    public Passenger(long id, String firstname, String surname) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
    }

}
