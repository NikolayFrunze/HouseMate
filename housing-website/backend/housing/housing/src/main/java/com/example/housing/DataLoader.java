package com.example.housing;

import com.example.housing.entity.Listings;
import com.example.housing.repository.ListingsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataLoader implements CommandLineRunner {

    private final ListingsRepository repo;

    public DataLoader(ListingsRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) return; // table already has houses, do nothing

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("houses.csv").getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // skip the header row
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] c = line.split(",");

                Listings h = new Listings();
                h.setName(c[0].trim());
                h.setAddress(c[1].trim());
                h.setRent(Integer.parseInt(c[2].trim()));
                h.setRooms(Integer.parseInt(c[3].trim()));
                h.setLatitude(Double.parseDouble(c[4].trim()));
                h.setLongitude(Double.parseDouble(c[5].trim()));
                h.setImageUrl(c[6].trim());
                h.setLandlordId(Long.parseLong(c[7].trim()));
                repo.save(h);
            }
        }
        System.out.println("Loaded " + repo.count() + " houses from houses.csv");
    }
}
