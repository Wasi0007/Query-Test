package com.mahivkhanwasi.Quer_Test.model;

import com.mahivkhanwasi.Quer_Test.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime createdAt;

    private Long createdBy = 1L;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long updatedBy = 1L;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station destination;

    private double cost;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}