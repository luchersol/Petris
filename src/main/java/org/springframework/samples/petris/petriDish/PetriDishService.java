package org.springframework.samples.petris.petriDish;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetriDishService {

    PetriDishRepository petriDishRepository;

    @Autowired
    public PetriDishService(PetriDishRepository petriDishRepository) {
        this.petriDishRepository = petriDishRepository;
    }

    @Transactional(readOnly = true)
    public PetriDish findPetriDishById(Integer id) {
        return petriDishRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<PetriDish> saveAllPetriDishes(List<PetriDish> petriDishes) {
        return (List<PetriDish>) petriDishRepository.saveAll(petriDishes);
    }

    @Transactional
    public PetriDish savePetriDish(PetriDish petriDish) throws UnfeasiblePetriDishUpdate {
        if (petriDish.getChipsPlayerBlue() == petriDish.getChipsPlayerRed() && petriDish.getChipsPlayerBlue() != 0)
            throw new UnfeasiblePetriDishUpdate();
        return petriDishRepository.save(petriDish);

    }

}
