package com.mahivkhanwasi.Quer_Test.service;

import com.mahivkhanwasi.Quer_Test.dto.TicketSummaryResponse;
import com.mahivkhanwasi.Quer_Test.dto.TicketSummaryRow;
import com.mahivkhanwasi.Quer_Test.dto.TicketSummaryRow1;
import com.mahivkhanwasi.Quer_Test.dto.TicketTotalStats;
import com.mahivkhanwasi.Quer_Test.enums.TicketStatus;
import com.mahivkhanwasi.Quer_Test.model.Station;
import com.mahivkhanwasi.Quer_Test.model.Ticket;
import com.mahivkhanwasi.Quer_Test.repo.StationRepository;
import com.mahivkhanwasi.Quer_Test.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private StationRepository stationRepository;

    public Ticket createTicket(String sourceName, String destinationName, double cost) {
        Station source = stationRepository.findByName(sourceName)
                .orElseThrow(() -> new RuntimeException("Source station not found"));
        Station destination = stationRepository.findByName(destinationName)
                .orElseThrow(() -> new RuntimeException("Destination station not found"));

        Ticket ticket = new Ticket();
        ticket.setSource(source);
        ticket.setDestination(destination);
        ticket.setCost(cost);
        ticket.setStatus(TicketStatus.BOUGHT);
        ticket.setCreatedBy(1L);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    //////////////////////////////// TEST 1 ////////////////////////////////
//      Performance
//      date,dayTimeMs,weekTimeMs,monthTimeMs,yearTimeMs
//      2025-04-19,59762,65322,90573,0
//      2025-04-20,59371,65083,90505,0
//      2025-04-21,59285,64882,90787,0
//      6g heap size and yearly data can not be fetched even with 10g heap size
//
//
//
//    public TicketSummaryResponse getDailySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(23, 59, 59);
//
//        long start = System.currentTimeMillis();
//        List<Ticket> tickets = ticketRepository.findByCreatedAtBetween(startOfDay, endOfDay);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("Query time : " + end);
//        return createTicketSummary(tickets);
//    }
//
//    public TicketSummaryResponse getWeeklySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.minusDays(1).plusWeeks(1).atTime(23, 59, 59);
//
//        long start = System.currentTimeMillis();
//        List<Ticket> tickets = ticketRepository.findByCreatedAtBetween(startOfDay, endOfDay);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("Query time : " + end);
//        return createTicketSummary(tickets);
//    }
//
//    public TicketSummaryResponse getMonthlySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusMonths(1).atTime(23, 59, 59);
//
//        long start = System.currentTimeMillis();
//        List<Ticket> tickets = ticketRepository.findByCreatedAtBetween(startOfDay, endOfDay);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("Query time : " + end);
//        return createTicketSummary(tickets);
//    }
//
//    public TicketSummaryResponse getYearlySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusYears(1).atTime(23, 59, 59);
//
//        long start = System.currentTimeMillis();
//        List<Ticket> tickets = ticketRepository.findByCreatedAtBetween(startOfDay, endOfDay);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("Query time : " + end);
//        return createTicketSummary(tickets);
//    }
//
//    private TicketSummaryResponse createTicketSummary(List<Ticket> tickets) {
//        if (tickets == null) {
//            tickets = List.of();
//        }
//
//        Map<TicketStatus, Long> statusCount = tickets.stream()
//                .filter(t -> t.getStatus() != null)
//                .collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
//
//        for (TicketStatus s : TicketStatus.values()) {
//            statusCount.putIfAbsent(s, 0L);
//        }
//        return new TicketSummaryResponse(tickets.size(), tickets.stream().mapToDouble(Ticket::getCost).sum(), statusCount);
//    }


    //////////////////////////////// TEST 2 ////////////////////////////////
//      Performance
//      date,dayTimeMs,weekTimeMs,monthTimeMs,yearTimeMs
//      2025-04-19,95012,95242,97705,129438
//      2025-04-20,94762,95149,97511,129300
//      2025-04-21,94840,95313,97503,129187
//      2g heap size
//
//
//    public TicketSummaryResponse getDailySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(23, 59, 59);
//
//            return getSummary(startOfDay, endOfDay);
//    }
//
//    public TicketSummaryResponse getWeeklySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.minusDays(1).plusWeeks(1).atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
//
//    public TicketSummaryResponse getMonthlySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusMonths(1).atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
//
//    public TicketSummaryResponse getYearlySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusYears(1).atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
//
//    public TicketSummaryResponse getSummary(LocalDateTime start, LocalDateTime end) {
//        long queryStart = System.currentTimeMillis();
//
//        TicketTotalStats stats = ticketRepository.getTotalStatsBetween(start, end);
//        List<TicketSummaryRow> statusCounts = ticketRepository.getStatusCountsBetween(start, end);
//
//        long queryDuration = System.currentTimeMillis() - queryStart;
//        System.out.println("Query time: " + queryDuration + " ms");
//
//        Map<TicketStatus, Long> statusMap = new EnumMap<>(TicketStatus.class);
//        for (TicketStatus status : TicketStatus.values()) {
//            statusMap.put(status, 0L); // default
//        }
//
//        for (TicketSummaryRow row : statusCounts) {
//            statusMap.put(row.status(), row.count());
//        }
//
//        return new TicketSummaryResponse(stats.totalTickets(), stats.totalCost(), statusMap);
//    }

    //////////////////////////////// TEST 3 ////////////////////////////////
//      Performance
//      date,dayTimeMs,weekTimeMs,monthTimeMs,yearTimeMs
//      2025-04-20,102238,99257,101006,129723
//      2025-04-21,93850,94494,96673,128649
//      2025-04-22,93841,94398,96666,128445
//      2g heap size
//
//
//    public List<TicketSummaryRow1> getSummary(LocalDateTime start, LocalDateTime end) {
//        return ticketRepository.getFullSummaryBetween(start, end);
//    }
//
//    public List<TicketSummaryRow1> getDailySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
//
//    public List<TicketSummaryRow1> getWeeklySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.minusDays(1).plusWeeks(1).atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
//
//    public List<TicketSummaryRow1> getMonthlySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusMonths(1).atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
//
//    public List<TicketSummaryRow1> getYearlySummary(LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.plusYears(1).atTime(23, 59, 59);
//
//        return getSummary(startOfDay, endOfDay);
//    }
}
