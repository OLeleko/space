package com.space.controller;


import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/ships")
public class ShipController {
    @Autowired
    ShipServiceImpl shipServiceImpl;

    @Autowired
    ShipRepository shipRepository;

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Ship> getShips(@RequestParam(value = "order", defaultValue = "ID") String order, @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize, @RequestParam Map<String, String> selectedParameters){


        ShipOrder shipOrder = ShipOrder.valueOf(order);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(shipOrder.getFieldName()));


        return shipServiceImpl.findAllShipFiltered(selectedParameters, pageable);
    }


    @GetMapping(value  = "{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Ship getShipId(@PathVariable Long id){
        if(id < 1 || !(id instanceof Long)){
            throw new BadRequestException();
        }

        return shipServiceImpl.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @GetMapping("count")
    public long getShipsCount(@RequestParam Map<String, String> selectedParameters){

        if(selectedParameters.size() == 0){
            return shipServiceImpl.allShipsCount();
        }else {
            return shipServiceImpl.countShips(selectedParameters);
        }
    }



    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public  void deleteShip(@PathVariable Long id){

        if(id < 1 || !(id instanceof Long)){
            throw new BadRequestException();
        }
        shipServiceImpl.findById(id).orElseThrow(() -> new NotFoundException());

        shipServiceImpl.deleteShip(id);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship newParams){

        if(id < 1 || !(id instanceof Long)){
            throw new BadRequestException();
        }

        Ship ship = shipRepository.findById(id).orElseThrow(() -> new NotFoundException());

        int paramCounter = 0;
        int currentYear = 3019;
        int yearOfProduction = 0;
        double k = 0;
        double speed = 0;

        if (newParams.getName() != null){
            if(newParams.getName().length() < 1 || newParams.getName().length() > 50){
                throw new BadRequestException();
            } else {
                paramCounter++;
                ship.setName(newParams.getName());
            }
        }

        if(newParams.getPlanet() != null){
            if(newParams.getPlanet().length() < 1 || newParams.getPlanet().length() > 50){
                throw new BadRequestException();
            } else {
                paramCounter++;
                ship.setPlanet(newParams.getPlanet());
            }
        }

        if(newParams.getShipType() != null){
            paramCounter++;
            ship.setShipType(newParams.getShipType());
        }

        if(newParams.getProdDate() != null){

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newParams.getProdDate());
            yearOfProduction = calendar.get(Calendar.YEAR);
            if(yearOfProduction < 2800 || yearOfProduction > currentYear){
                throw new BadRequestException();
            } else {
                paramCounter++;
                ship.setProdDate(newParams.getProdDate());
            }
        }

        if(newParams.getUsed() != null){
            paramCounter++;
            if(newParams.getSpeed().equals(true)){
                k = 0.5;
            } else{
                k = 1;
            }

            ship.setUsed(newParams.getUsed());
        }

        if(newParams.getSpeed() != null){
            if(newParams.getSpeed() < 0.01 || newParams.getSpeed() > 0.99){
                throw new BadRequestException();
            }else{
                paramCounter++;
                int r = (int)Math.round(newParams.getSpeed() * 100);
                double newSpeed = r / 100.0;
                speed = newSpeed;
                ship.setSpeed(newSpeed);
            }
        }

        if(newParams.getCrewSize() != null){
            if(newParams.getCrewSize() < 1 || newParams.getCrewSize() > 9999){
                throw new BadRequestException();
            }else{
                paramCounter++;
                ship.setCrewSize(newParams.getCrewSize());
            }
        }

        if(yearOfProduction == 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ship.getProdDate());
            yearOfProduction = calendar.get(Calendar.YEAR);
        }

        if(k == 0){
            if(ship.getUsed().equals(true)){
                k = 0.5;
            }else {
                k = 1;
        }
        }

        if(speed == 0){
            int r = (int)Math.round(ship.getSpeed() * 100);
            speed = r / 100.0;
        }



        if(paramCounter > 0) {
            double R = (80 * speed * k) / (currentYear - yearOfProduction + 1);
            int rn = (int)Math.round(R * 100.0);
            double Rn = rn / 100.0;
            ship.setRating(Rn);
            Ship updatedShip = shipRepository.save(ship);
            return updatedShip;
        }else{
            return ship;
        }


    }



    @RequestMapping(method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public Ship createShip(@RequestBody Ship newParams){

        int currentYear = 3019;
        int yearOfProduction = 0;
        double k = 0;
        double speed = 0;
        Ship newShip = new Ship();

        if(newParams.getName() == null || newParams.getName().length() < 1 || newParams.getName().length() > 50){
            throw new BadRequestException();
        }else{
            newShip.setName(newParams.getName());
        }

        if(newParams.getPlanet() == null || newParams.getPlanet().length() < 1 || newParams.getPlanet().length() > 50){
            throw new BadRequestException();
        }else{
            newShip.setPlanet(newParams.getPlanet());
        }

        if(newParams.getShipType() == null){
            throw new BadRequestException();
        }else {
            newShip.setShipType(newParams.getShipType());
        }

        if(newParams.getProdDate() != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newParams.getProdDate());
            yearOfProduction = calendar.get(Calendar.YEAR);
            if(yearOfProduction < 2800 || yearOfProduction > currentYear){
                throw new BadRequestException();
            }else{
                newShip.setProdDate(newParams.getProdDate());
            }
        }else{
            throw new BadRequestException();
        }

        if(newParams.getUsed() == null || newParams.getUsed() == false){
            k = 1;
            newShip.setUsed(false);
        }else{
            k = 0.5;
            newShip.setUsed(true);
        }

        if(newParams.getSpeed() == null || newParams.getSpeed() < 0.01 || newParams.getSpeed() > 0.99){
            throw new BadRequestException();
        }else{
            int r = (int)Math.round(newParams.getSpeed() * 100);
            speed = r / 100.0;
            newShip.setSpeed(speed);
        }

        if(newParams.getCrewSize() == null || newParams.getCrewSize() < 1 || newParams.getCrewSize() > 9999){
            throw new BadRequestException();
        }else{
            newShip.setCrewSize(newParams.getCrewSize());
        }

        double R = (80 * speed * k) / (currentYear - yearOfProduction + 1);
        newShip.setRating(R);

        return shipRepository.save(newShip);



    }






}
