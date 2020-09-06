package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ShipService {
    List<Ship> findAllShipFiltered(Map<String, String> selectedParameters, Pageable pageable);
    Optional<Ship> findById(Long id);

    long countShips(Map<String, String> selectedParameters);
    /*List<Ship> findAll();*/

    long allShipsCount();
    void deleteShip(Long id);
    /*void updateShip(Lo);*/


}
