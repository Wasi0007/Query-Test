package com.mahivkhanwasi.Quer_Test.dto;

import com.mahivkhanwasi.Quer_Test.enums.TicketStatus;

public record TicketSummaryRow(TicketStatus status, long count) {}