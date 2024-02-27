package org.springframework.samples.petris.petriDish;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PetriDishServiceTest {

    @Autowired
    PetriDishService petriDishService;


    @Test
    public void shouldFindPetriDishById(){
        PetriDish pd = this.petriDishService.findPetriDishById(2);
        assertEquals(2, pd.getId());
        assertEquals(2, pd.getChipsPlayerBlue());
        assertEquals(1, pd.getChipsPlayerRed());
    }
    
}
