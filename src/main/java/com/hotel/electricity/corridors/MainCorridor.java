package com.hotel.electricity.corridors;

import com.hotel.electricity.equipment.Priority;
import com.hotel.electricity.equipment.Type;

/**
 * Denotes a main corridor
 */
public class MainCorridor extends Corridor {

    public MainCorridor(int id){
        super("MC"+id);
    }

    /**
     * Sets the default state of equipments.
     */
    @Override
    public void setDefaultState() {
        //By default, all ACs are switched ON, all the time
        getEquipments().stream()
                .filter(equipment -> equipment.getType() == Type.AC)
                .forEach(equipment -> equipment.switchOn());

        setNightModeOn();
    }

    /**
     * Sets the priority of equipments in NIGHT mode
     */
    private void setNightModeOn(){
        //All the lights need to be switched on during night mode in main corridor
        getEquipments().stream()
                .filter(equipment -> equipment.getType() == Type.LIGHT)
                .forEach(equipment -> {
                    equipment.switchOn();
                    equipment.setPriority(Priority.HIGH);
                });
    }
}
