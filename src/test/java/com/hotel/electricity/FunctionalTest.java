package com.hotel.electricity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.electricity.controller.Controller;
import com.hotel.electricity.corridors.Corridor;
import com.hotel.electricity.corridors.MainCorridor;
import com.hotel.electricity.corridors.SubCorridor;
import com.hotel.electricity.equipment.Equipment;
import com.hotel.electricity.equipment.Type;
import com.hotel.electricity.hotels.Floor;
import com.hotel.electricity.hotels.Hotel;
import com.hotel.electricity.sensors.MovementSensor;
import com.hotel.electricity.sensors.Sensor;
import com.hotel.electricity.util.Event;
import com.hotel.electricity.util.Settings;

import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * This is a test class which initializes the hotel automation system and the controller.
 * It executes a sequence of different types events - Movement detected and No Movement detected
 *
 * The input to the test execution is provided via a JSON file. The JSON file is expected to contain the following information:
 *
 * 1. Number of floors
 * 2. Number of main corridors
 * 3. Number of sub corridors
 * 4. Operating mode of the controller
 * 5. Sequence of simulated events. The event is expected to contain the following attributes:
 *      a. Floor number
 *      b. Corridor id
 *      c. If the movement was detected or not.
 */

public class FunctionalTest {

    /**
     * Number of floors: 2
     * Number of main corridors: 1
     * Number of sub corridors: 2
     * Mode: NIGHT
     * Events:
     * 1. Movement in Floor 1, Sub corridor 1
     * 2. No Movement in Floor 1, Sub corridor 1
     * 3. Movement in Floor 1, Sub corridor 2
     * 4. No Movement in Floor 1, Sub corridor 2
     * 5. Movement in Floor 2, Sub corridor 1
     * 6. No Movement in Floor 2, Sub corridor 1
     * 7. Movement in Floor 2, Sub corridor 2
     * 8. Movement in Floor 2, Sub corridor 2
     */
    @Test
    public void testInput1() {
        System.out.println("START --- Executing usecase from input1.json -- START");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Settings settings = readSettingsFromFile("input1.json");
        executeUsecase(settings);
        System.out.println("END --- Executing usecase from input1.json -- END");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * Number of floors: 1
     * Number of main corridors: 1
     * Number of sub corridors: 3
     * Mode: NIGHT
     * Events:
     * 1. Movement in Floor 1, Sub corridor 1
     * 2. Movement in Floor 1, Sub corridor 2
     * 3. Movement in Floor 1, Sub corridor 3
     * 4. No Movement in Floor 1, Sub corridor 1
     * 5. No Movement in Floor 1, Sub corridor 2
     * 6. No Movement in Floor 1, Sub corridor 3
     */
    @Test
    public void testInput2() {
        System.out.println("START --- Executing usecase from input2.json -- START");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Settings settings = readSettingsFromFile("input2.json");
        executeUsecase(settings);
        System.out.println("END --- Executing usecase from input2.json -- END");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * Number of floors: 2
     * Number of main corridors: 1
     * Number of sub corridors: 2
     * Mode: NIGHT
     * Events:
     * 1. Movement in Floor 1, Sub corridor 1
     * 2. Movement in Floor 2, Sub corridor 1
     * 3. Movement in Floor 1, Sub corridor 2
     * 4. No Movement in Floor 1, Sub corridor 2
     * 5. Movement in Floor 2, Sub corridor 2
     * 6. No Movement in Floor 2, Sub corridor 2
     * 7. No Movement in Floor 1, Sub corridor 1
     * 8. No Movement in Floor 2, Sub corridor 1
     */
    @Test
    public void testInput3() {
        System.out.println("START --- Executing usecase from input3.json -- START");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Settings settings = readSettingsFromFile("input3.json");
        executeUsecase(settings);
        System.out.println("END --- Executing usecase from input3.json -- END");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private void executeUsecase(Settings settings){
        Hotel hotel = new Hotel();
        createFloors( hotel, settings.getNumFloors());
        hotel.getFloors().forEach( floor -> createMainCorridors(floor, settings.getNumMainCorridors()));
        hotel.getFloors().forEach( floor -> createSubCorridors(floor, settings.getNumSubCorridors()));

        //Add equipments to each corridor
        hotel.getFloors().stream()
                .map( floor -> floor.getCorridors())
                .flatMap(Collection::stream)
                .forEach(this::addEquipments);

        // Add sensors to each corridor
        hotel.getFloors().forEach(this::addSensors);

        // Initialize the controller of the hotel with given mode
        Controller controller = new Controller();
        hotel.setController(controller);
        controller.initializeController(hotel);
        controller.printFloorsInfo();

        //Execute the sequence of events provided in input.json file
        execute(hotel, settings.getEvents());

        //Unregister the sensor events
        controller.unregisterFromSensorEvents();
    }

    private static void execute(Hotel hotel, List<Event> events){
        events.forEach( event -> {
            hotel.getFloorByNumber(event.getFloorNumber())
                    .ifPresent(floor -> floor.getCorridorById(event.getCorridorId())
                                        .ifPresent( corridor -> corridor.getSensor().eventDetected(event.isMovementDetected())));
        });
    }

    private void createFloors(Hotel hotel, int numFloors){
        for(int i = 0; i < numFloors; i++ ){
            hotel.addFloor( new Floor(i+1));
        }
    }

    private void createMainCorridors(Floor floor, int numMainCorridors){
        for(int i = 0; i < numMainCorridors; i++ ){
            floor.addCorridor( new MainCorridor(i+1));
        }
    }

    private void createSubCorridors(Floor floor, int numSubCorridors){
        for(int i = 0; i < numSubCorridors; i++ ){
            floor.addCorridor( new SubCorridor(i+1));
        }
    }

    private void addEquipments(Corridor corridor){
        Equipment light = new Equipment(Type.LIGHT, 5);
        corridor.addEquipment(light);

        Equipment ac = new Equipment(Type.AC, 10);
        corridor.addEquipment(ac);
    }

    private void addSensors(Floor floor){
        floor.getCorridors().forEach( corridor -> {
            Sensor sensor = new MovementSensor(floor, corridor);
            corridor.setSensor(sensor);
        });
    }

    /**
     * Read the settings input from the given file and map it to the settings.Settings object.
     * @param inputFileName
     * @return settings.Settings
     */
    private Settings readSettingsFromFile(String inputFileName){
        Settings settings = new Settings();
        try {
            ClassLoader classLoader = FunctionalTest.class.getClassLoader();
            URL fileUrl = classLoader.getResource(inputFileName);
            if( fileUrl != null ) {
                File inputFile = new File(fileUrl.getFile());
                String inputJson = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();
                settings = mapper.readValue(inputJson, Settings.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }

}
