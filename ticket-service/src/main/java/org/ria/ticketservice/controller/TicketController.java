package org.ria.ticketservice.controller;

import java.util.List;

import org.ria.ticketservice.entity.TicketEntity;
import org.ria.ticketservice.model.Ticket;
import org.ria.ticketservice.model.Vehicle;
import org.ria.ticketservice.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/entry")
    public Ticket createTicket(@RequestBody Vehicle vehicle) {
        return ticketService.createTicket(vehicle);
    }

    @GetMapping("/{ticketId}")
    public TicketEntity getTicket(@PathVariable String ticketId) {
        return ticketService.getTicket(ticketId);
    }

    @PostMapping("/exit/{ticketId}")
    public String exitTicket(@PathVariable String ticketId) {
        return ticketService.exitTicket(ticketId);
    }

    @GetMapping("/history")
    public List<TicketEntity> getTicketHistory(@RequestParam(required = false) String licensePlate) {
        return ticketService.getTicketHistory(licensePlate);
    }

    @GetMapping("/active")
    public List<TicketEntity> getActiveTickets() {
        return ticketService.getActiveTickets();
    }

}
