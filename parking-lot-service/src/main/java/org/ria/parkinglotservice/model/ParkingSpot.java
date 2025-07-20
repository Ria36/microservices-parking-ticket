package org.ria.parkinglotservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParkingSpot {

    private String id;
    private boolean isFree;
    private VehicleType vehicleType;

}
