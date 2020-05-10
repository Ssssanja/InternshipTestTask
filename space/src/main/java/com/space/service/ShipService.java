package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import java.util.List;
import java.util.Optional;

public interface ShipService {
    List<Ship> getShips(Optional<String> name,
                        Optional<String> planet,
                        Optional<ShipType> shipType,
                        Optional<Long> after,
                        Optional<Long> before,
                        Optional<Boolean> isUsed,
                        Optional<Double> minSpeed,
                        Optional<Double> maxSpeed,
                        Optional<Integer> minCrewSize,
                        Optional<Integer> maxCrewSize,
                        Optional<Double> minRating,
                        Optional<Double> maxRating,
                        ShipOrder shipOrder,
                        Integer pageNumber,
                        Integer pageSize);

    int getCount(Optional<String> name,
                 Optional<String> planet,
                 Optional<ShipType> shipType,
                 Optional<Long> after,
                 Optional<Long> before,
                 Optional<Boolean> isUsed,
                 Optional<Double> minSpeed,
                 Optional<Double> maxSpeed,
                 Optional<Integer> minCrewSize,
                 Optional<Integer> maxCrewSize,
                 Optional<Double> minRating,
                 Optional<Double> maxRating);


    Ship createShip(Ship ship);

    Ship readShip(Long id);

    Ship updateShip(Long id, Ship ship);

    void deleteShip(Long id);

}