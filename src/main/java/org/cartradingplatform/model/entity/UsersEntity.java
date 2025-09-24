package org.cartradingplatform.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cartradingplatform.model.enums.Gender;
import org.cartradingplatform.model.enums.RoleName;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UsersEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name= "password_hash", unique = true, nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "number_phone", length = 15)
    private String numberPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
