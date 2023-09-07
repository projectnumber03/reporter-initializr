package ru.plorum.reporterinitializr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor(force = true)
@Table(name = "CONTACT_PERSONS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactPerson {

    @Id
    UUID id;

    @Column(name = "FIRST_NAME")
    String firstName;

    @Column(name = "SECOND_NAME")
    String secondName;

    @Column(name = "LAST_NAME")
    String lastName;

    @Column
    String email;

    @Column
    String phone;

}
