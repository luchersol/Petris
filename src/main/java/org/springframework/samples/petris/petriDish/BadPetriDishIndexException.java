package org.springframework.samples.petris.petriDish;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadPetriDishIndexException extends Exception{
    
}
