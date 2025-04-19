package com.mahivkhanwasi.Quer_Test.controller;

import com.mahivkhanwasi.Quer_Test.dto.TicketSummaryResponse;
import com.mahivkhanwasi.Quer_Test.service.TicketService;
import com.mahivkhanwasi.Quer_Test.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestParam String source,
                                               @RequestParam String destination,
                                               @RequestParam double cost) {
        return ResponseEntity.ok(ticketService.createTicket(source, destination, cost));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }


    @GetMapping("/day")
    public ResponseEntity<?> getDailySummary(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ticketService.getDailySummary(date));
    }

    @GetMapping("/week")
    public ResponseEntity<?> getWeeklySummary(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ticketService.getWeeklySummary(date));
    }

    @GetMapping("/month")
    public ResponseEntity<?> getMonthlySummary(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ticketService.getMonthlySummary(date));
    }

    @GetMapping("/year")
    public ResponseEntity<?> getYearlySummary(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ticketService.getYearlySummary(date));
    }
}
