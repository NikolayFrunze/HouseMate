package com.example.housing.controller;

import com.example.housing.entity.Listings;
import com.example.housing.repository.ListingsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController //tells spring this handles restapi requests and returns data in json
@RequestMapping("/api/listings") //sets the base url for all endpoints in this controller
public class ListingsController {
    private final ListingsRepository repo;

    public ListingsController(ListingsRepository repo) { //constructor injection: spring injects the repo into the controller when it creates it
        this.repo = repo;
    }

    @GetMapping
public List<Listings> getAll(@RequestParam(name = "maxRent", required = false) Integer maxRent,
                            @RequestParam(name = "Rooms" , required = false) Integer rooms) {
    if (maxRent != null && rooms != null) {
        return repo.findByRentLessThanEqualAndRooms(maxRent, rooms);}
    if (maxRent != null) {
        return repo.findByRentLessThanEqual(maxRent);}
    if (rooms != null) {
        return repo.findByRooms(rooms);}
    return repo.findAll();}


    @GetMapping ("/{id}") //GET request to /api/listings/{id} where {id} is a path variable representing the id of the house to retrieve
   public ResponseEntity<Listings> getOne(@PathVariable("id") Long id) {
        return repo.findById(id) //searches id with the house 
        .map(ResponseEntity::ok) //returns http 200 if found
        .orElse(ResponseEntity.notFound().build());} //else 404
        

    //POST request to /api/listings with a house object in the request body to create a new house
    @PostMapping
    public Listings create(@RequestBody Listings house) { //request body is the house object to create
        return repo.save(house);} //saves the house to the database and returns the saved house
    
}
