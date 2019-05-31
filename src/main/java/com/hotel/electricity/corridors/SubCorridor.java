package com.hotel.electricity.corridors;

import com.hotel.electricity.equipment.Priority;
import com.hotel.electricity.equipment.Type;

/**
 * Denotes a sub corridor
 */
public class SubCorridor extends Corridor {

    public SubCorridor(int id){
        super("SC"+id);
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
        getEquipments().stream()
                .filter(equipment -> equipment.getType() == Type.LIGHT)
                .forEach(equipment -> equipment.setPriority(Priority.HIGH));
    }
}
