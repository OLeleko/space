package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class ShipServiceImpl implements ShipService{
    @Autowired
    private ShipRepository shipRepository;

    @Override
    public List<Ship> findAllShipFiltered(Map<String, String> selectedParameters, Pageable pageable) {
        Page page = shipRepository.findAll(new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(selectedParameters.get("name") != null){
                    predicates.add(cb.like(root.get("name"), "%" + selectedParameters.get("name") + "%"));
                }

                if(selectedParameters.get("planet") != null){
                    predicates.add(cb.like(root.get("planet"), "%" + selectedParameters.get("planet") + "%"));
                }

                if(selectedParameters.get("shipType") != null){
                    ShipType shipType = ShipType.valueOf(selectedParameters.get("shipType"));

                    predicates.add(cb.equal(root.get("shipType"),  shipType));
                }

                if(selectedParameters.get("after") != null){
                    long ms = Long.parseLong(selectedParameters.get("after"));
                    Date dateAfter = new Date(ms);
                    predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("prodDate"), dateAfter));

                }

                if(selectedParameters.get("before") != null){
                    long bms = Long.parseLong(selectedParameters.get("before"));
                    Date dateAfter = new Date(bms);
                    predicates.add(cb.lessThanOrEqualTo(root.<Date>get("prodDate"), dateAfter));

                }

                if(selectedParameters.get("isUsed") != null){
                    Boolean value = "true".equalsIgnoreCase(selectedParameters.get("isUsed")) ? Boolean.TRUE : "false".equalsIgnoreCase(selectedParameters.get("isUsed")) ? Boolean.FALSE : null;

                    predicates.add(cb.equal(root.get("isUsed"), value));
                }

                if(selectedParameters.get("minSpeed") != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("speed"), selectedParameters.get("minSpeed")));

                }

                if(selectedParameters.get("maxSpeed") != null){

                    predicates.add(cb.lessThanOrEqualTo(root.get("speed"),  selectedParameters.get("maxSpeed")));
                }

                if(selectedParameters.get("minCrewSize") != null){

                    predicates.add(cb.greaterThanOrEqualTo(root.get("crewSize"),  selectedParameters.get("minCrewSize")));
                }

                if(selectedParameters.get("maxCrewSize") != null){

                    predicates.add(cb.lessThanOrEqualTo(root.get("crewSize"),  selectedParameters.get("maxCrewSize")));
                }

                if(selectedParameters.get("minRating") != null){

                    predicates.add(cb.greaterThanOrEqualTo(root.get("rating"),  selectedParameters.get("minRating")));
                }

                if(selectedParameters.get("maxRating") != null){

                    predicates.add(cb.lessThanOrEqualTo(root.get("rating"),  selectedParameters.get("maxRating")));
                }

                /*if(selectedParameters.get("order") != null){
                    ShipOrder shipOrder = ShipOrder.valueOf(selectedParameters.get("order"));
                    cq.orderBy(cb.desc(root.get(shipOrder.getFieldName())));
                }*/



                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        page.getTotalElements();
        page.getTotalPages();
        return page.getContent();


    }

    @Override
    public Optional<Ship> findById(Long id) {
        return shipRepository.findById(id);
    }

    @Override
    public long countShips(Map<String, String> selectedParameters) {
        return shipRepository.count(new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(selectedParameters.get("name") != null){
                    predicates.add(cb.like(root.get("name"), "%" + selectedParameters.get("name") + "%"));
                }

                if(selectedParameters.get("planet") != null){
                    predicates.add(cb.like(root.get("planet"), "%" + selectedParameters.get("planet") + "%"));
                }

                if(selectedParameters.get("shipType") != null){
                    ShipType shipType = ShipType.valueOf(selectedParameters.get("shipType"));

                    predicates.add(cb.equal(root.get("shipType"),  shipType));
                }

                if(selectedParameters.get("after") != null){
                    long ms = Long.parseLong(selectedParameters.get("after"));
                    Date dateAfter = new Date(ms);
                    predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("prodDate"), dateAfter));

                }

                if(selectedParameters.get("before") != null){
                    long bms = Long.parseLong(selectedParameters.get("before"));
                    Date dateAfter = new Date(bms);
                    predicates.add(cb.lessThanOrEqualTo(root.<Date>get("prodDate"), dateAfter));

                }

                if(selectedParameters.get("isUsed") != null){
                    Boolean value = "true".equalsIgnoreCase(selectedParameters.get("isUsed")) ? Boolean.TRUE : "false".equalsIgnoreCase(selectedParameters.get("isUsed")) ? Boolean.FALSE : null;

                    predicates.add(cb.equal(root.get("isUsed"), value));
                }

                if(selectedParameters.get("minSpeed") != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("speed"), selectedParameters.get("minSpeed")));

                }

                if(selectedParameters.get("maxSpeed") != null){

                    predicates.add(cb.lessThanOrEqualTo(root.get("speed"),  selectedParameters.get("maxSpeed")));
                }

                if(selectedParameters.get("minCrewSize") != null){

                    predicates.add(cb.greaterThanOrEqualTo(root.get("crewSize"),  selectedParameters.get("minCrewSize")));
                }

                if(selectedParameters.get("maxCrewSize") != null){

                    predicates.add(cb.lessThanOrEqualTo(root.get("crewSize"),  selectedParameters.get("maxCrewSize")));
                }

                if(selectedParameters.get("minRating") != null){

                    predicates.add(cb.greaterThanOrEqualTo(root.get("rating"),  selectedParameters.get("minRating")));
                }

                if(selectedParameters.get("maxRating") != null){

                    predicates.add(cb.lessThanOrEqualTo(root.get("rating"),  selectedParameters.get("maxRating")));
                }

                /*if(selectedParameters.get("order") != null){
                    ShipOrder shipOrder = ShipOrder.valueOf(selectedParameters.get("order"));
                    cq.orderBy(cb.desc(root.get(shipOrder.getFieldName())));
                }*/



                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

   /* @Override
    public List<Ship> findAll() {
        return shipRepository.findAll();
    }*/

    public Iterable<Ship> allShips(){
        return shipRepository.findAll();
    }

    @Override
    public long allShipsCount() {
        return shipRepository.count();
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }

    /*@Override
    public void updateShip(Long id) {

    }*/

}
