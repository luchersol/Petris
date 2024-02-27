package org.springframework.samples.petris.petriDish;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petris.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/petris/petriDish")
@SecurityRequirement(name = "bearerAuth")
public class PetriDishController {

    PetriDishService petriDishService;

    @Autowired
    public PetriDishController(PetriDishService petriDishService) {
        this.petriDishService = petriDishService;
    }

     @Operation(summary = "Creates a list of petri dishes (the boardGame)", description = "Creates the boardgame of a match", tags = { "matches", "post", "petriDishes" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boardgame created", content = {
                    @Content(schema = @Schema(implementation = PetriDish.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<PetriDish>> saveAllPetriDishes(@RequestBody @Valid List<PetriDish> petriDishes) {
        List<PetriDish> body = new ArrayList<>();
        petriDishService.saveAllPetriDishes(petriDishes).forEach(pd -> body.add(pd));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


       @Operation(summary = "Updates a petri dish", description = "Updates a petri dish given its id", tags = { "matches", "put", "petriDishes" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Petri dish updated", content = {
                    @Content(schema = @Schema(implementation = PetriDish.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request is not correct", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", description = "You need full authentication to get the resource", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(schema = @Schema()) }),
    })
    @PutMapping("/{petriDishId}")
	@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PetriDish> updatePetriDish(@PathVariable("petriDishId") Integer petriDishId, @RequestBody @Valid PetriDish petriDish) 
    throws UnfeasiblePetriDishUpdate, BadPetriDishIndexException{
        PetriDish body =petriDishService.findPetriDishById(petriDishId);
        if(body==null) throw new ResourceNotFoundException("La placa con id "+petriDishId+" no existe");
        if(body.getIndex()!=petriDish.getIndex()) throw new BadPetriDishIndexException();
        body.setChipsPlayerBlue(petriDish.getChipsPlayerBlue());
        body.setChipsPlayerRed(petriDish.getChipsPlayerRed());
        return ResponseEntity.ok(body);

    }

}
