package com.mahivkhanwasi.Quer_Test.repo;

import com.mahivkhanwasi.Quer_Test.dto.TicketSummaryRow;
import com.mahivkhanwasi.Quer_Test.dto.TicketSummaryRow1;
import com.mahivkhanwasi.Quer_Test.dto.TicketTotalStats;
import com.mahivkhanwasi.Quer_Test.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

//////////////////////////////// TEST 1 ////////////////////////////////
//    // This query retrieves tickets created within a given date range
//    List<Ticket> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

//////////////////////////////// TEST 2 ////////////////////////////////
//    @Query("SELECT new com.mahivkhanwasi.Quer_Test.dto.TicketSummaryRow(t.status, COUNT(t)) " +
//            "FROM Ticket t WHERE t.createdAt BETWEEN :start AND :end GROUP BY t.status")
//    List<TicketSummaryRow> getStatusCountsBetween(
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
//
//    @Query("SELECT new com.mahivkhanwasi.Quer_Test.dto.TicketTotalStats(COUNT(t), SUM(t.cost)) " +
//            "FROM Ticket t WHERE t.createdAt BETWEEN :start AND :end")
//    TicketTotalStats getTotalStatsBetween(
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );

    //////////////////////////////// TEST 3 ////////////////////////////////
    @Query(value = """
        SELECT 
            totals.total_tickets AS totalTickets,
            totals.total_cost AS totalCost,
            t.status AS status,
            COUNT(t.id) AS statusCount
        FROM ticket t
        JOIN (
            SELECT COUNT(*) AS total_tickets, COALESCE(SUM(cost), 0) AS total_cost
            FROM ticket
            WHERE created_at BETWEEN :start AND :end
        ) AS totals ON true
        WHERE t.created_at BETWEEN :start AND :end
        GROUP BY t.status, totals.total_tickets, totals.total_cost
        """, nativeQuery = true)
    List<TicketSummaryRow1> getFullSummaryBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
