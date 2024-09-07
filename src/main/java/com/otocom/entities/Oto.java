package com.otocom.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Set;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Oto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer otoId;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Please provide car's model.")
    private String model;

    @Column(nullable = false)
    @NotBlank(message = "Please provide car's brand.")
    private String brand;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    @NotBlank(message = "Please provide car's fuel.")
    private String fuel;

    @Column(nullable = false)
    @NotBlank(message = "Please provide car's transmission.")
    private String transmission;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @NotBlank(message = "Please provide car's color.")
    private String color;

    @Column(nullable = false)
    @NotBlank(message = "Please provide car's picture.")
    private String photo;

}


