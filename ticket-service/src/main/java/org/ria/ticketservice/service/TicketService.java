package org.ria.ticketservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.ria.ticketservice.client.ParkingLotClient;
import org.ria.ticketservice.entity.TicketEntity;
import org.ria.ticketservice.model.Ticket;
import org.ria.ticketservice.model.Vehicle;
import org.ria.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final ParkingLotClient parkingLotClient;
    private final TicketRepository ticketRepository;

    public TicketService(ParkingLotClient parkingLotClient, TicketRepository ticketRepository) {
        this.parkingLotClient = parkingLotClient;
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Vehicle vehicle) {
        if (vehicle == null || vehicle.getVehicleType() == null) {
            throw new RuntimeException("Vehicle type is required! Allowed values: BIKE, CAR, TRUCK");
        }
        ticketRepository.findByLicensePlateAndStatus(vehicle.getLicensePlate(), "ACTIVE")
                        .ifPresent(existingTicket -> {
                            throw new RuntimeException("Vehicle " + vehicle.getLicensePlate() + " is already parked! Ticket ID: " + existingTicket.getTicketId());
                        });
        String spotId = parkingLotClient.getFirstAvailableSpot();
        if (spotId == null) {
            throw new RuntimeException("No available spots for Parking");
        }
        boolean occupied = parkingLotClient.occupySpot(spotId);
        if (!occupied) {
            throw new RuntimeException("Failed to occupy spot");
        }
        String ticketId = UUID.randomUUID().toString();
        TicketEntity ticketEntity = TicketEntity.builder()
                                                .ticketId(ticketId)
                                                .spotId(spotId)
                                                .licensePlate(vehicle.getLicensePlate())
                                                .vehicleType(vehicle.getVehicleType())
                                                .entryTime(LocalDateTime.now())
                                                .status("ACTIVE")
                                                .build();

        ticketRepository.save(ticketEntity);
        return new Ticket(ticketId, spotId, vehicle, ticketEntity.getEntryTime());
    }

    public TicketEntity getTicket(String ticketId) {
        return ticketRepository.findById(ticketId)
                               .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));
    }

    public String exitTicket(String ticketId) {
        TicketEntity ticketEntity = ticketRepository.findById(ticketId)
                                                    .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));

        if (ticketEntity.getVehicleType() == null) {
            throw new RuntimeException("Vehicle type is missing for ticket: " + ticketId);
        }

        if ("EXITED".equalsIgnoreCase(ticketEntity.getStatus())) {
            return "Ticket " + ticketId + " is already exited. Total cost was: " + ticketEntity.getTotalCost();
        }

        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = java.time.Duration.between(ticketEntity.getEntryTime(), exitTime).toMinutes();

        double ratePerMinute;
        switch (ticketEntity.getVehicleType()) {
            case BIKE -> ratePerMinute = 0.5;
            case CAR -> ratePerMinute = 1.0;
            case TRUCK -> ratePerMinute = 2.0;
            default -> ratePerMinute = 1.5;
        }

        double cost = minutes * ratePerMinute;

        parkingLotClient.freeSpot(ticketEntity.getSpotId());
        //ticketRepository.deleteById(ticketId);
        ticketEntity.setStatus("EXITED");
        ticketEntity.setExitTime(exitTime);
        ticketEntity.setTotalCost(cost);
        ticketRepository.save(ticketEntity);

        return "Parking fee for ticket " + ticketId +
                " (" + ticketEntity.getVehicleType() + ") = " + cost + " for " + minutes + " minutes.";
    }

    public List<TicketEntity> getTicketHistory(String licensePlate) {
        if (licensePlate == null || licensePlate.isBlank()) {
            return ticketRepository.findAll();
        }
        return ticketRepository.findByLicensePlate(licensePlate);
    }

    public List<TicketEntity> getActiveTickets() {
        return ticketRepository.findByStatus("ACTIVE");
    }

}
