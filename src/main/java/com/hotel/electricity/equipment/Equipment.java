package com.hotel.electricity.equipment;

/**
 * Denotes an equipment and contain actions for switching them on and off.
 */

public class Equipment implements Comparable<Equipment>{

    private Type type;

    private State state = State.OFF;

    private int powerUnits;

    private Priority priority = Priority.LOW;

    public Equipment(Type type, int powerUnits){
        this.type = type;
        this.powerUnits = powerUnits;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isOn(){
        return this.state == State.ON;
    }

    public boolean isOff(){
        return this.state == State.OFF;
    }

    public State getState() {
        return state;
    }

    public int getPowerUnits() {
        return powerUnits;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void switchOn(){
        this.state = State.ON;
    }

    public void switchOff(){
        this.state = State.OFF;
    }

    /**
     * Sorts the equipments in the descending order of priority
     * @param equipment
     * @return
     */
    @Override
    public int compareTo(Equipment equipment) {
        return equipment.getPriority().getPriorityNumber() - this.getPriority().getPriorityNumber();
    }
}
