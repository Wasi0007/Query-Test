package com.mahivkhanwasi.Quer_Test.dto;

import com.mahivkhanwasi.Quer_Test.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class TicketSummaryResponse {
    private long totalTickets;
    private double totalCost;
    private Map<TicketStatus, Long> statusCount;
}