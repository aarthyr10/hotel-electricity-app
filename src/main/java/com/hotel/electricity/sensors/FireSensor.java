package com.hotel.electricity.sensors;

import com.hotel.electricity.corridors.Corridor;
import com.hotel.electricity.hotels.Floor;


public class FireSensor extends Sensor {

    public FireSensor(Floor floor, Corridor corridor){
        super(floor, corridor);
    }

    public void eventDetected(boolean detected){
        this.notifyListener(detected);
    }
}
