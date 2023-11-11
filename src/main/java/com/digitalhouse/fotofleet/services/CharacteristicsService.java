package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.CharacteristicsDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Characteristics;
import com.digitalhouse.fotofleet.repositories.CharacteristicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacteristicsService {

    private final CharacteristicsRepository characteristicsRepository;

    public Characteristics createCharacteristics(CharacteristicsDto characteristicsDto){
        Characteristics c = new Characteristics(characteristicsDto.name(),characteristicsDto.description(),characteristicsDto.urlIcono());
        return characteristicsRepository.save(c);
    }

    public void deleteCharacteristcs(Integer id) throws ResourceNotFoundException{
        if(getCharacteristicById(id) == null) throw new ResourceNotFoundException("No existe característica con el ID: " + id);

        characteristicsRepository.deleteById(id);
    }

    public List<CharacteristicsDto> listAllCharacteristics(){
        List<Characteristics> characteristics = characteristicsRepository.findAll();
        List<CharacteristicsDto> characteristicsDtos = new ArrayList<>();

        for (Characteristics c : characteristics) {
            characteristicsDtos.add(new CharacteristicsDto(c.getName(),c.getDescription(),c.getUrlIcono()));
        }
        return characteristicsDtos;
    }

    public CharacteristicsDto getCharacteristicById(Integer id) throws ResourceNotFoundException {
        Optional<Characteristics> characteristics = characteristicsRepository.findById(id);
        if (characteristics.isEmpty()) throw new ResourceNotFoundException("No existe característica con el ID: " + id);

        return new CharacteristicsDto(characteristics.get().getName(),characteristics.get().getDescription(),characteristics.get().getUrlIcono());
    }

    public Characteristics updateCharacteristics(Integer id, CharacteristicsDto characteristicsDto) throws BadRequestException{
        Optional<Characteristics> characteristics = characteristicsRepository.findById(id);
        if (characteristics.isEmpty()) throw new BadRequestException("No es posible modificar la característica con ID: " + id + ", porque no está registrada");

        Characteristics c = characteristics.get();
        c.setName(characteristicsDto.name());
        c.setDescription(characteristicsDto.description());
        c.setUrlIcono(characteristicsDto.urlIcono());

        return characteristicsRepository.save(c);
    }

}
