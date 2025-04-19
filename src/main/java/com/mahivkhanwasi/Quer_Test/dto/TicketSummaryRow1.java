package com.mahivkhanwasi.Quer_Test.dto;

import com.mahivkhanwasi.Quer_Test.enums.TicketStatus;

public interface TicketSummaryRow1 {
    long getTotalTickets();
    double getTotalCost();
    TicketStatus getStatus();
    long getStatusCount();
}