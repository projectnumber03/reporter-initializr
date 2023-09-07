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
@Table(name = "REQUISITES")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Requisite {

    @Id
    UUID id;

    @Column
    String name;

    @Column
    String inn;

    @Column
    String kpp;

    @Column
    String ogrn;

    @Column
    String okpo;

    @Column
    String oktmo;

    @Column
    String address;

    @Column
    String bank;

    @Column
    String bik;

    @Column(name = "CORRESPONDENT_ACCOUNT")
    String correspondentAccount;

    @Column(name = "BANK_ACCOUNT")
    String bankAccount;

}
