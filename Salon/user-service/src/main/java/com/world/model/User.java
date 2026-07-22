package com.world.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String fullName;
    @NotBlank(message = "email is Mandatory")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Role is Mandatory")
    private String role;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

    private String phone;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @NotBlank(message ="Password is mandatory")
    private String password;

}
