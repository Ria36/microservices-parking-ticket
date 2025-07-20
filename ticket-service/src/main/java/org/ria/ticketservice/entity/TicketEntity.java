package org.ria.ticketservice.entity;

import java.time.LocalDateTime;

import org.ria.ticketservice.model.VehicleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "ticket")
@Builder
public class TicketEntity {

    @Id
    private String ticketId;
    private String spotId;
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double totalCost;
    private String status;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

}


