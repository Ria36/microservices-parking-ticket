package org.ria.ticketservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Vehicle {

    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}
