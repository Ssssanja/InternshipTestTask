package com.space.service;

import com.space.exception.CrapRequestException;
import com.space.exception.NoEntityException;
import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ShipServImpl implements ShipService {
    private ShipRepository shipRepository;
    final static long EARLEST_DATE = new GregorianCalendar(2800, 01, 01).getTimeInMillis();
    final static long LASTEST_DATE = new GregorianCalendar(3020, 01, 01).getTimeInMillis();
    final static Double MIN_SPEED = 0.01D;
    final static Double MAX_SPEED = 0.99D;
    final static Integer MIN_CREW = 1;
    final static Integer MAX_CREW = 9999;
    final static int MAX_LENGTH_OF_STRING = 50;

    @Autowired
    public ShipServImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ship> getShips(Optional<String> name, Optional<String> planet, Optional<ShipType> shipType, Optional<Long> after, Optional<Long> before, Optional<Boolean> isUsed, Optional<Double> minSpeed, Optional<Double> maxSpeed, Optional<Integer> minCrewSize, Optional<Integer> maxCrewSize, Optional<Double> minRating, Optional<Double> maxRating, ShipOrder shipOrder, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(shipOrder.getFieldName()));;
        List<Ship>result = shipRepository.findAll(Specification.where(Filtreees.stringPFilter("name", name))
                .and(Filtreees.stringPFilter("planet", planet))
                .and(Filtreees.typeFilter("shipType", shipType))
                .and(Filtreees.dateFilter("prodDate", after, before))
                .and(Filtreees.booleanFilter("isUsed", isUsed))
                .and(Filtreees.speedFilter("speed", minSpeed, maxSpeed))
                .and(Filtreees.crewFilter("crewSize", minCrewSize, maxCrewSize))
                .and(Filtreees.ratingFilter("rating", minRating, maxRating)), pageable).getContent();

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int getCount(Optional<String> name, Optional<String> planet, Optional<ShipType> shipType, Optional<Long> after, Optional<Long> before, Optional<Boolean> isUsed, Optional<Double> minSpeed, Optional<Double> maxSpeed, Optional<Integer> minCrewSize, Optional<Integer> maxCrewSize, Optional<Double> minRating, Optional<Double> maxRating) {
        return shipRepository.findAll(Specification.where(Filtreees.stringPFilter("name", name))
                .and(Filtreees.stringPFilter("planet", planet))
                .and(Filtreees.typeFilter("shipType", shipType))
                .and(Filtreees.dateFilter("prodDate", after, before))
                .and(Filtreees.booleanFilter("isUsed", isUsed))
                .and(Filtreees.speedFilter("speed", minSpeed, maxSpeed))
                .and(Filtreees.crewFilter("crewSize", minCrewSize, maxCrewSize))
                .and(Filtreees.ratingFilter("rating", minRating, maxRating))).size();
    }

    @Override
    @Transactional
    public Ship createShip(Ship ship) {
        checkParams(ship);
        Double rating = getRatingOfShip(ship.getSpeed(), ship.getUsed(), ship.getProdDate());
        ship.setRating(rating);
        return shipRepository.saveAndFlush(ship);
    }

    private void checkParams(Ship ship) {
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null)
            throw new CrapRequestException("Smth is null, check it");

        checkShipName(ship.getName());
        checkShipName(ship.getPlanet());
        checkDate(ship.getProdDate());
        checkSpeed(ship.getSpeed());
        checkCrew(ship.getCrewSize());
        if (ship.getUsed() == null)
            ship.setUsed(false);
    }

    private void checkCrew(Integer crewSize) {
        if (MIN_CREW>crewSize||MAX_CREW<crewSize){
            throw new CrapRequestException("Crew size is too small, or too big: " + crewSize + " Check it:" + "\n It must be between" + MIN_CREW + " and " + MAX_CREW + " (Include)");
        }
    }

    private void checkSpeed(Double speed) {
        if (MIN_SPEED>speed||MAX_SPEED<speed){
            throw new CrapRequestException("Ship is too slow, or too fast: " + speed + " Check it:" + "\n It must be between" + MIN_SPEED + " and " + MAX_SPEED + " (Include)");
        }
    }

    private void checkDate(Date prodDate) {
        if (EARLEST_DATE>prodDate.getTime()||LASTEST_DATE<prodDate.getTime()){
            throw new CrapRequestException("Impossible production date, Check it:" + "\n It must be between" + "01.01.2800" + " and " + "01.01.3020" + " (Include)");
        }
    }

    private void checkShipName(String name) {
        if (name.length() >= MAX_LENGTH_OF_STRING||name.equals("")){
            throw new CrapRequestException("Too long or empty string: " + name);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Ship readShip(Long id) {
        if (id==null){throw new CrapRequestException("ID needed");}
        else {
            Optional<Ship> ship = shipRepository.findById(id);
            if (ship.isPresent())
                return ship.get();
            else throw new NoEntityException("ID: " + id + " not in database");
        }
    }

    @Override
    @Transactional
    public Ship updateShip(Long id, Ship shipToUpdate) {

        Ship existingShip = readShip(id);

        if (shipToUpdate.getName() != null) {
            checkShipName(shipToUpdate.getName());
            existingShip.setName(shipToUpdate.getName());
        }

        if (shipToUpdate.getPlanet() != null) {
            checkShipName(shipToUpdate.getPlanet());
            existingShip.setPlanet(shipToUpdate.getPlanet());
        }

        if (shipToUpdate.getCrewSize() != null) {
            checkCrew(shipToUpdate.getCrewSize());
            existingShip.setCrewSize(shipToUpdate.getCrewSize());
        }

        if (shipToUpdate.getProdDate() != null) {
            checkDate(shipToUpdate.getProdDate());
            existingShip.setProdDate(shipToUpdate.getProdDate());
        }

        if (shipToUpdate.getSpeed() != null) {
            checkSpeed(shipToUpdate.getSpeed());
            existingShip.setSpeed(shipToUpdate.getSpeed());
        }

        if (shipToUpdate.getUsed() != null) {
            existingShip.setUsed(shipToUpdate.getUsed());
        }

        if (shipToUpdate.getShipType() != null) {
            existingShip.setShipType(shipToUpdate.getShipType());
        }

        Double rating = getRatingOfShip(existingShip.getSpeed(), existingShip.getUsed(), existingShip.getProdDate());
        existingShip.setRating(rating);
        return shipRepository.saveAndFlush(existingShip);
    }

    @Override
    @Transactional
    public void deleteShip(Long id) {
        readShip(id);
        shipRepository.deleteById(id);
    }

    private Double getRatingOfShip (Double speed, Boolean isUsed, Date prodDate){
        Calendar c = new GregorianCalendar();
        c.setTime(prodDate);
        Double k = isUsed ? 0.5D : 1D;
        return Math.round((80*speed*k)/(3019 - (c.get(Calendar.YEAR))+1) * 100) / 100D;
    }
}
