package com.example.housing.entity;  //file name
import jakarta.persistence.*; //tools that talk to db

@Entity //make table
public class Listings {
    @Id //unique number identifying each row
    @GeneratedValue(strategy= GenerationType.IDENTITY) //makes db generate no.
     private Long id;

private String name; //private= columns
private String address;
private int rent;
private int rooms;
@Column (length = 1000) //allow long string for image url
private String imageUrl;
private long landlordId;

private Double latitude;
private Double longitude;

//Empty constructor required by JPA
public Listings() {
}

//Getters: allow another class to read the value of a private variable 

public Long getId() { return id;}
public String getName() {return name;}
public String getAddress() {return address;}
public int getRent() {return rent;}
public int getRooms() {return rooms;}
public String getImageUrl() {return imageUrl;}
public long getLandlordId() {return landlordId;}
public Double getLatitude() {return latitude;}
public Double getLongitude() {return longitude;}

//Setters (allow another class to change the value of a private variable)
public void setId(Long id) {this.id = id;}
public void setName(String name) {this.name = name;}
public void setAddress(String address) {this.address = address;}
public void setRent(int rent) {this.rent = rent;}
public void setRooms(int rooms) {this.rooms = rooms;}
public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
public void setLandlordId(long landlordId) {this.landlordId = landlordId;}
public void setLatitude(Double latitude) {this.latitude = latitude;}
public void setLongitude(Double longitude) {this.longitude = longitude;}
}
