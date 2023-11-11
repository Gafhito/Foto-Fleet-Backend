package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.CharacteristicsDto;
import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Characteristics;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.repositories.CharacteristicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacteristicsService {
    private final CharacteristicsRepository characteristicsRepository;
    private final ProductService productService;

    public Characteristics createCharacteristics(CharacteristicsDto characteristicsDto){
        Characteristics c = new Characteristics(characteristicsDto.name(),characteristicsDto.description(),characteristicsDto.urlIcono());
        return characteristicsRepository.save(c);
    }

    public void deleteCharacteristcs(Integer id) throws ResourceNotFoundException{
        if(getCharacteristicById(id) == null) throw new ResourceNotFoundException("No existe característica con el ID: " + id);

        characteristicsRepository.deleteById(id);
    }

    public List<Characteristics> listAllCharacteristics(){
        return characteristicsRepository.findAll();
    }

    public Characteristics getCharacteristicById(Integer id) throws ResourceNotFoundException {
        Optional<Characteristics> characteristics = characteristicsRepository.findById(id);
        if (characteristics.isEmpty()) throw new ResourceNotFoundException("No existe característica con el ID: " + id);

        return characteristics.get();
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

    public Product addCharacteristicsToProduct(Integer productId, List<Characteristics> characteristics) throws ResourceNotFoundException, BadRequestException {
        Optional<Product> product = productService.getById(productId);
        if (product.isEmpty()) throw new ResourceNotFoundException("No existe el producto con el ID especificado");
        List<Characteristics> productCharacteristics = product.get().getCharacteristics();

        for (Characteristics c : characteristics) {
            Characteristics characteristic = getCharacteristicById(c.getCharacteristicsId());
            productCharacteristics.add(characteristic);
        }

        // Se quitan características duplicadas
        Set<Characteristics> characteristicsSet = new HashSet<>(productCharacteristics);
        productCharacteristics.clear();
        productCharacteristics.addAll(characteristicsSet);

        Product p = product.get();
        p.setCharacteristics(productCharacteristics);

        return productService.updateProductWhithCharacteristics(p);
    }
}
