package com.hotel.electricity.controllers;

import org.junit.Test;

import com.hotel.electricity.controller.Controller;
import com.hotel.electricity.corridors.MainCorridor;
import com.hotel.electricity.corridors.SubCorridor;
import com.hotel.electricity.equipment.Equipment;
import com.hotel.electricity.equipment.Type;
import com.hotel.electricity.hotels.Floor;
import com.hotel.electricity.hotels.Hotel;
import com.hotel.electricity.sensors.MovementSensor;
import com.hotel.electricity.sensors.Sensor;

import static org.junit.Assert.assertTrue;


public class ControllerTest {

    @Test
    public void testControllerRegistrationToEvents(){
        Controller controller = new Controller();
        Hotel hotel = new Hotel();
        hotel.setController(controller);

        Floor floor = new Floor(1);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridor(sc);
        hotel.addFloor(floor);

        Equipment light1 = new Equipment(Type.LIGHT, 5);
        sc.addEquipment(light1);
        Equipment ac1 = new Equipment(Type.AC, 10);
        sc.addEquipment(ac1);

        sc.setSensor(new MovementSensor(floor, sc));

        controller.initializeController(hotel);
        assertTrue(sc.getSensor().getEventListener() instanceof Controller);
        controller.unregisterFromSensorEvents(hotel);
    }

    @Test
    public void testCompensatePowerConsumption(){
        Controller controller = new Controller();
        Hotel hotel = new Hotel();
        hotel.setController(controller);

        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridor(mc);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridor(sc);
        hotel.addFloor(floor);

        Equipment light1 = new Equipment(Type.LIGHT, 5);
        sc.addEquipment(light1);
        Equipment ac1 = new Equipment(Type.AC, 10);
        sc.addEquipment(ac1);

        Equipment light2 = new Equipment(Type.LIGHT, 5);
        mc.addEquipment(light2);
        Equipment ac2 = new Equipment(Type.AC, 10);
        mc.addEquipment(ac2);

        Sensor sensor = new MovementSensor(floor, sc);
        sc.setSensor(sensor);

        controller.initializeController(hotel);

        sc.getSensor().eventDetected(true);
        assertTrue(floor.getPowerConsumption() <= floor.getMaxPowerConsumption());
        controller.unregisterFromSensorEvents(hotel);
    }
}
