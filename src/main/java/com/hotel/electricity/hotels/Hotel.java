package com.hotel.electricity.hotels;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel.electricity.controller.Controller;

/**
 * Denotes a hotel
 */

public class Hotel {

    private String name;

    private Controller controller;

    private List<Floor> floors = new ArrayList<Floor>();

    public Hotel () {}

    public Hotel( String name ){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void addFloor( Floor floor ){
        this.floors.add(floor);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController(){
        return this.controller;
    }

    public Optional<Floor> getFloorByNumber(int number){
        Optional<Floor> floorOptional = floors.stream()
                .filter(floor -> floor.getNumber() == number)
                .findFirst();

        return floorOptional;
    }
}
