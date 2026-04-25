package com.medilabo.demographics.domain;
import jakarta.persistence.*;
import lombok.*;

/**
 * Domain for the patient class that defines the fields
 */
@Data
@Entity
@Table(name = "patient")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String dateOfBirth;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String address;

    private String phone;
}
