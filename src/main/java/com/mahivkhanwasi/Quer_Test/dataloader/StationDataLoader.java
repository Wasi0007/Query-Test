package com.mahivkhanwasi.Quer_Test.dataloader;

import com.mahivkhanwasi.Quer_Test.model.Station;
import com.mahivkhanwasi.Quer_Test.repo.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class StationDataLoader implements CommandLineRunner {

    @Autowired
    private StationRepository stationRepository;

    @Override
    public void run(String... args) {
        List<String> stations = List.of(
                "Uttara North", "Uttara Center", "Uttara South", "Pallabi",
                "Mirpur-11", "Mirpur-10", "Kazipara", "Shewrapara",
                "Agargaon", "Bijoy Sarani", "Farmgate", "Kawran Bazar",
                "Shahbagh", "Dhaka University", "Motijheel"
        );

        for (String name : stations) {
            stationRepository.findByName(name).orElseGet(() -> stationRepository.save(new Station(name)));
        }
    }
}
