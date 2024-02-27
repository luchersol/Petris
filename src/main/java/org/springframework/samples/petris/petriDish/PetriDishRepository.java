package org.springframework.samples.petris.petriDish;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetriDishRepository extends CrudRepository<PetriDish, Integer>{
    
}
