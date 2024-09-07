package com.otocom.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtoDto {

    private Integer otoId;

    @NotBlank(message = "Please provide car's model.")
    private String model;

    @NotBlank(message = "Please provide car's brand.")
    private String brand;

    private Integer mileage;

    @NotBlank(message = "Please provide car's fuel.")
    private String fuel;

    @NotBlank(message = "Please provide car's transmission.")
    private String transmission;

    private Integer year;

    private Integer price;

    @NotBlank(message = "Please provide car's color.")
    private String color;

    @NotBlank(message = "Please provide car's picture.")
    private String photo;

    @NotBlank(message = "Please provide photo's url.")
    private String photoUrl;
}