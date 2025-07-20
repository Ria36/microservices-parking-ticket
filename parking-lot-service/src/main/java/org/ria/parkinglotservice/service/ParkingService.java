package org.ria.parkinglotservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ria.parkinglotservice.model.ParkingSpot;
import org.ria.parkinglotservice.model.VehicleType;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {

    private final List<ParkingSpot> parkingSpots = new ArrayList<>();

    public ParkingService(){
        parkingSpots.add(new ParkingSpot("1", true, VehicleType.BIKE));
        parkingSpots.add(new ParkingSpot("2", true, VehicleType.BIKE));
        parkingSpots.add(new ParkingSpot("3", true, VehicleType.CAR));
        parkingSpots.add(new ParkingSpot("4", true, VehicleType.CAR));
        parkingSpots.add(new ParkingSpot("5", true, VehicleType.TRUCK));
        parkingSpots.add(new ParkingSpot("6", true, VehicleType.TRUCK));
    }

    public List<ParkingSpot> getAvailableParkingSlots(){
        return parkingSpots.stream().filter(ParkingSpot::isFree).collect(Collectors.toList());
    }

    public boolean occupyParkingSlot(String spotId){
        for (ParkingSpot parkingSpot : parkingSpots) {
            if(parkingSpot.getId().equals(spotId) && parkingSpot.isFree()){
                parkingSpot.setFree(false);
                return true;
            }
        }
        return false;
    }

    public boolean freeParkingSlot(String spotId){
        for (ParkingSpot parkingSpot : parkingSpots) {
            if(parkingSpot.getId().equals(spotId) && !parkingSpot.isFree()){
                parkingSpot.setFree(true);
                return true;
            }
        }
        return false;
    }

    public void freeAllSpots() {
        for (ParkingSpot spot : parkingSpots) {
            spot.setFree(true);
        }
    }

}
