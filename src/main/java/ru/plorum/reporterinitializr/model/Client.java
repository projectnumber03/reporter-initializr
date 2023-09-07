package ru.plorum.reporterinitializr.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(force = true)
@Table(name = "CLIENTS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {

    @Id
    @GeneratedValue(generator = "cli-generator")
    @GenericGenerator(name = "cli-generator", strategy = "ru.plorum.reporterinitializr.util.ClientIdGenerator")
    String id;

    @Enumerated(EnumType.STRING)
    Type type;

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

    @Column(name = "CREATED_ON")
    LocalDateTime createdOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "REQUISITE_ID")
    Requisite requisites;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DIRECTOR_ID")
    Director director;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTACT_PERSON_ID")
    ContactPerson contactPerson;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    @Transient
    Boolean personalDataAgreement;

    @Getter
    @AllArgsConstructor
    public enum Type {
        PHYSICAL("ФЛ"),
        JURIDICAL("ЮЛ");

        private final String name;

    }

}
