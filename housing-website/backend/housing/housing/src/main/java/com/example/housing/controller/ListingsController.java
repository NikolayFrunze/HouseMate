package com.example.housing.controller;

import com.example.housing.entity.Listings;
import com.example.housing.repository.ListingsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController //tells spring this handles restapi requests and returns data in json
@RequestMapping("/api/houses") //sets the base url for all endpoints in this controller
public class ListingsController {
    private final ListingsRepository repo;

    public ListingsController(ListingsRepository repo) { //
        this.repo = repo;
    }

    @GetMapping //GET request to /api/houses
    public List<Listings> getAll() {
        return repo.findAll(); //retrieves all houses from the database and returns them as a list
    }
    @GetMapping ("/{id}") //GET request to /api/houses/{id} where {id} is a path variable representing the id of the house to retrieve
    public ResponseEntity<Listings> getByOne(@PathVariable Long id) {
        return repo.findById(id) //searches id with the house 
        .map(ResponseEntity::ok) //returns http 200 if found
        .orElse(ResponseEntity.notFound().build()); //else 404
    }
    //POST request to /api/houses
    @PostMapping
    public Listings create(@RequestBody Listings house) { //request body is the house object to create
        return repo.save(house); //saves the house to the database and returns the saved house
    }
}
