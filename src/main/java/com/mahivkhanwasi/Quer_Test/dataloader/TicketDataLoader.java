package com.mahivkhanwasi.Quer_Test.dataloader;

import com.mahivkhanwasi.Quer_Test.enums.TicketStatus;
import com.mahivkhanwasi.Quer_Test.model.Station;
import com.mahivkhanwasi.Quer_Test.model.Ticket;
import com.mahivkhanwasi.Quer_Test.repo.StationRepository;
import com.mahivkhanwasi.Quer_Test.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
@Order(2)
public class TicketDataLoader implements CommandLineRunner {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private final Random random = new Random();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api/tickets";

    private static final Path CSV_PATH = Paths.get("timings.csv");

    @Override
    public void run(String... args) {

        try {
            Files.deleteIfExists(CSV_PATH);
            try (BufferedWriter writer = Files.newBufferedWriter(
                    CSV_PATH,
                    StandardOpenOption.CREATE
            )) {
                writer.write("date,dayTimeMs,weekTimeMs,monthTimeMs,yearTimeMs");
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize CSV file: " + e.getMessage());
            e.printStackTrace();
        }

        List<Station> stations = stationRepository.findAll();

        if (stations.size() < 2) {
            System.err.println("Not enough stations to create tickets.");
            return;
        }

        LocalDate startDate = LocalDate.of(2025, 4, 18);
        LocalDate endDate = startDate.plusDays(2);
        int ticketCountPerDay = 200000;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            // Generate Tickets and store them
//            createTicket(ticketCountPerDay, stations, date);

            // Call APIs and record timings
            recordAndStoreTimings(date, date);
            System.out.println("Done for " + date.toString());
        }
    }

    private void createTicket(int ticketCountPerDay, List<Station>stations, LocalDate date){
        List<Ticket> generated = new ArrayList<>();

        for(int i=1;i<=ticketCountPerDay;i++){
            Collections.shuffle(stations, random);
            Station source = stations.get(0);
            Station destination = stations.get(1);

            double cost = 20 + (100 - 20) * random.nextDouble();

            Ticket ticket = new Ticket();
            ticket.setCreatedAt(date.atStartOfDay());
            ticket.setUpdatedAt(date.atStartOfDay());
            ticket.setSource(source);
            ticket.setDestination(destination);
            ticket.setCost(Math.round(cost * 100.0) / 100.0);

            TicketStatus[] statuses = TicketStatus.values();
            ticket.setStatus(statuses[random.nextInt(statuses.length)]);
            generated.add(ticket);
        }

        ticketRepository.saveAll(generated);
    }

    private void recordAndStoreTimings(LocalDate date, LocalDate writeDate) {
        try {
            long startDay = System.currentTimeMillis();
            var a = restTemplate.getForObject(BASE_URL + "/day?date=" + date, String.class);
            System.out.println(a);
            long dayTime = System.currentTimeMillis() - startDay;

            long startWeek = System.currentTimeMillis();
            var b = restTemplate.getForObject(BASE_URL + "/week?date=" + date, String.class);
            System.out.println(b);
            long weekTime = System.currentTimeMillis() - startWeek;
//
            long startMonth = System.currentTimeMillis();
            var c = restTemplate.getForObject(BASE_URL + "/month?date=" + date, String.class);
            System.out.println(c);
            long monthTime = System.currentTimeMillis() - startMonth;
//
            long startYear = System.currentTimeMillis();
            restTemplate.getForObject(BASE_URL + "/year?date=" + date, String.class);
            long yearTime = System.currentTimeMillis() - startYear;

            appendCsvLine(writeDate, dayTime, weekTime, monthTime, yearTime);
        } catch (Exception e) {
            System.err.println("Failed to record/store timings for date " + date + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void appendCsvLine(LocalDate date,
                               long dayTime, long weekTime,
                               long monthTime, long yearTime) throws IOException {
        boolean fileExists = Files.exists(CSV_PATH);

        try (BufferedWriter writer = Files.newBufferedWriter(
                CSV_PATH,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            if (!fileExists) {
                writer.write("date,dayTimeMs,weekTimeMs,monthTimeMs,yearTimeMs");
                writer.newLine();
            }

            String line = String.format(
                    "%s,%d,%d,%d,%d",
                    date,
                    dayTime,
                    weekTime,
                    monthTime,
                    yearTime
            );
            writer.write(line);
            writer.newLine();
        }
    }
}
