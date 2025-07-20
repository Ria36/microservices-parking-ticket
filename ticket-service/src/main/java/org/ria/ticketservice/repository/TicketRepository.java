package org.ria.ticketservice.repository;

import java.util.List;
import java.util.Optional;

import org.ria.ticketservice.entity.TicketEntity;
import org.ria.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository  extends JpaRepository<TicketEntity, String> {

    Optional<TicketEntity> findByLicensePlateAndStatus(String licensePlate, String status);

    List<TicketEntity> findByLicensePlate(String licensePlate);

    List<TicketEntity> findByStatus(String status);

}
