package com.hotel.electricity.sensors;

import java.util.Optional;
import java.util.Random;

import com.hotel.electricity.corridors.Corridor;
import com.hotel.electricity.hotels.Floor;
import com.hotel.electricity.listeners.EventListener;

/**
 * Denotes a sensor
 */
public abstract class Sensor {

    private String id;

    private EventListener eventListener;

    public Sensor(Floor floor, Corridor corridor) {
        this.id = generateSensorId(floor, corridor);
    }

    public String getId() {
        return id;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public abstract void eventDetected(boolean detected);

    public void registerListener(EventListener eventListener){
        this.eventListener = eventListener;
    }

    public void unregisterListener(){
        this.eventListener = null;
    }

    protected void notifyListener(boolean detected){
        Optional.of(eventListener)
                .ifPresent( eventListener -> eventListener.onEventDetected(this.getId(), detected));
    }

    private String generateSensorId(Floor floor, Corridor corridor){
        String id = floor.getNumber()+"-"+corridor.getId();
        Random random = new Random();
        id += "-"+ random.nextInt(1000);;
        return id;
    }
}
