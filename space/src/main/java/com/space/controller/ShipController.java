package com.space.controller;

import com.space.exception.CrapRequestException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/ships")
public class ShipController {
    private final ShipService shipService;

    @Autowired
    public ShipController(final ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public List<Ship> getShips(@RequestParam(value = "name", required = false) Optional<String> name,
                               @RequestParam(value = "planet", required = false) Optional<String> planet,
                               @RequestParam(value = "shipType", required = false) Optional<ShipType> shipType,
                               @RequestParam(value = "after", required = false) Optional<Long> after,
                               @RequestParam(value = "before", required = false) Optional<Long> before,
                               @RequestParam(value = "isUsed", required = false) Optional<Boolean> isUsed,
                               @RequestParam(value = "minSpeed", required = false) Optional<Double> minSpeed,
                               @RequestParam(value = "maxSpeed", required = false) Optional<Double> maxSpeed,
                               @RequestParam(value = "minCrewSize", required = false) Optional<Integer> minCrewSize,
                               @RequestParam(value = "maxCrewSize", required = false) Optional<Integer> maxCrewSize,
                               @RequestParam(value = "minRating", required = false) Optional<Double> minRating,
                               @RequestParam(value = "maxRating", required = false) Optional<Double> maxRating,
                               @RequestParam(value = "order", defaultValue = "ID") ShipOrder order,
                               @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        return shipService.getShips(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);
    }

    @GetMapping(value = "count")
    public Integer getShipsCount(@RequestParam(value = "name", required = false) Optional<String> name,
                                 @RequestParam(value = "planet", required = false) Optional<String> planet,
                                 @RequestParam(value = "shipType", required = false) Optional<ShipType> shipType,
                                 @RequestParam(value = "after", required = false) Optional<Long> after,
                                 @RequestParam(value = "before", required = false) Optional<Long> before,
                                 @RequestParam(value = "isUsed", required = false) Optional<Boolean> isUsed,
                                 @RequestParam(value = "minSpeed", required = false) Optional<Double> minSpeed,
                                 @RequestParam(value = "maxSpeed", required = false) Optional<Double> maxSpeed,
                                 @RequestParam(value = "minCrewSize", required = false) Optional<Integer> minCrewSize,
                                 @RequestParam(value = "maxCrewSize", required = false) Optional<Integer> maxCrewSize,
                                 @RequestParam(value = "minRating", required = false) Optional<Double> minRating,
                                 @RequestParam(value = "maxRating", required = false) Optional<Double> maxRating) {
        return shipService.getCount(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @PostMapping
    public Ship createShip(@RequestBody Ship ship) {
        return shipService.createShip(ship);

    }

    @GetMapping(value = "/{id}")
    public Ship readShip(@PathVariable(value = "id") String idString) {
        Long id = validateAndGetId(idString);
        return shipService.readShip(id);
    }

    @PostMapping(value = "/{id}")
    public Ship updateShip(@PathVariable(value = "id") String idString,@RequestBody Ship ship) {
        Long id = validateAndGetId(idString);
        ship.setId(id);
        return shipService.updateShip(id, ship);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteShip(@PathVariable(value = "id") String idString) {
        Long id = validateAndGetId(idString);
        shipService.deleteShip(id);
    }

    private Long validateAndGetId(String idString) {
        Long result = null;
        try {
            result = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new CrapRequestException("Invalid id: " + idString);
        }
        if (result <= 0) {
            throw new CrapRequestException("Invalid id: " + idString);
        }
        return result;
    }
}