package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.CharacteristicsDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Characteristics;
import com.digitalhouse.fotofleet.services.CharacteristicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characteristics")
@RequiredArgsConstructor
public class CharacteristicsController {

    private final CharacteristicsService characteristicsService;

    @GetMapping
    public ResponseEntity<?> listAllCharacteristics(){
        return new ResponseEntity<>(characteristicsService.listAllCharacteristics(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacteristicById(Integer id) throws ResourceNotFoundException{
        return new ResponseEntity<>(characteristicsService.getCharacteristicById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCharacteristics(@RequestBody CharacteristicsDto characteristicsDto){
        Characteristics characteristics = characteristicsService.createCharacteristics(characteristicsDto);
        return new ResponseEntity<>(characteristics, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacteristcs(@PathVariable Integer id) throws ResourceNotFoundException {
        characteristicsService.deleteCharacteristcs(id);
        return ResponseEntity.status(HttpStatus.OK).body("Característica eliminada exitosamente.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacteristics(@PathVariable Integer id, @RequestBody CharacteristicsDto characteristicsDto) throws BadRequestException{
        return new ResponseEntity<>(characteristicsService.updateCharacteristics(id, characteristicsDto), HttpStatus.OK);
    }

}
