package io.github.gabrielhe4.shop_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters")
    private String country;

    @NotBlank
    @Size(min = 5, message = "Zip code must be at least 5 characters")
    private String zipcode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
