package com.hotel.electricity.hotels;

import org.junit.Test;

import com.hotel.electricity.corridors.MainCorridor;
import com.hotel.electricity.corridors.SubCorridor;
import com.hotel.electricity.equipment.Equipment;
import com.hotel.electricity.equipment.Type;
import com.hotel.electricity.hotels.Floor;

import static org.junit.Assert.assertEquals;


public class FloorTest {

    @Test
    public void testCreateFloor(){
        Floor floor = new Floor(3);
        assertEquals("true", floor.getNumber(), 3);
    }

    @Test
    public void testCreateFloorWithMainCorridor(){
        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridor(mc);

        assertEquals("true", floor.getMainCorridorsSize(), 1);
    }

    @Test
    public void testCreateFloorWithSubCorridor(){
        Floor floor = new Floor(1);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridor(sc);

        assertEquals("true", floor.getSubCorridorsSize(), 1);
    }

    @Test
    public void testGetFloorMaxPowerConsumption(){
        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridor(mc);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridor(sc);

        //Max power consumption should be (mc * 15) + (sc * 10)
        assertEquals("true", floor.getMaxPowerConsumption(), 25);
    }

    @Test
    public void testFloorPowerConsumption(){
        Floor floor = new Floor(1);
        MainCorridor mc = new MainCorridor(1);
        floor.addCorridor(mc);
        SubCorridor sc = new SubCorridor(1);
        floor.addCorridor(sc);

        Equipment light1 = new Equipment(Type.LIGHT, 5);
        light1.switchOn();
        mc.addEquipment(light1);
        Equipment ac1 = new Equipment(Type.AC, 10);
        ac1.switchOn();
        mc.addEquipment(ac1);

        Equipment light2 = new Equipment(Type.LIGHT, 5);
        light2.switchOff();
        sc.addEquipment(light2);
        Equipment ac2 = new Equipment(Type.AC, 10);
        ac2.switchOn();
        sc.addEquipment(ac2);

        assertEquals("true", floor.getPowerConsumption(), 25);
    }

}
