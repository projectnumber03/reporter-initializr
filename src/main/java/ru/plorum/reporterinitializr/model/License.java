package ru.plorum.reporterinitializr.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "LICENSE")
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class License {

    @Id
    @NonNull
    @Column(length = 36)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    @Column(name = "START_DATE")
    LocalDate startDate;

    @Column(name = "FINISH_DATE")
    LocalDate finishDate;

    @Enumerated(EnumType.STRING)
    Type type;

    public License(@NonNull final UUID id, final User user, final LocalDate startDate) {
        this.id = id;
        this.user = user;
        this.startDate = startDate;
    }

    public enum Type {
        FREE,
        PROFESSIONAL,
        CORPORATIVE,
        BUSINESS
    }

}
