package com.hotel.electricity.corridors;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import com.hotel.electricity.equipment.Equipment;
import com.hotel.electricity.equipment.Type;
import com.hotel.electricity.sensors.Sensor;

/**
 * Denotes a corridor
 */

public abstract class Corridor {

    private String id;
    protected List<Equipment> equipments = new ArrayList<Equipment>();

    //This trail is necessary for reverting back the compensation done in case of no movement
    private Stack<Equipment> stateChangeTrail = new Stack<>();

    private boolean movementDetected;

    private Sensor sensor;

    Corridor(String id){
        this.id = id;
    }

    public abstract void setDefaultState();

    public String getId() {
        return id;
    }

    public List<Equipment> getEquipments() {
        return new ArrayList<>(equipments);
    }

    public void addEquipment( Equipment equipment){
        this.equipments.add(equipment);
    }

    public Stack<Equipment> getStateChangeTrail() {
        return stateChangeTrail;
    }

    public void setSensor( Sensor sensor){
        this.sensor = sensor;
    }

    public Sensor getSensor(){
        return this.sensor;
    }

    public boolean isMovementDetected() {
        return movementDetected;
    }

    public void setMovementDetected(boolean movementDetected) {
        this.movementDetected = movementDetected;
    }

    public void printCorridorInfo(){
        List<Equipment> lights = equipments.stream().filter(equipment -> equipment.getType() == Type.LIGHT).collect(Collectors.toList());
        List<Equipment> acs = equipments.stream().filter(equipment -> equipment.getType() == Type.AC).collect(Collectors.toList());

        lights.forEach( light -> {
            System.out.print(" "+light.getType()+" "+ (lights.indexOf(light)+1)+" : "+ light.getState());
        });
        acs.forEach( ac -> {
            System.out.print(" "+ac.getType()+" "+ (acs.indexOf(ac)+1)+" : "+ ac.getState());
        });
    }

    /**
     * Get power consumption of the corridor by summing up the power units consumed by each active equipment
     * @return totalPower
     */
    public int getPowerConsumption(){
        return equipments.stream().filter(equipment -> equipment.isOn())
                .map(equipment -> equipment.getPowerUnits())
                .reduce(0, (a,b) -> a + b);
    }
}
