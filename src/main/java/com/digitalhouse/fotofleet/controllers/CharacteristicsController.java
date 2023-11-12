package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.CharacteristicsDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.models.Characteristics;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.services.CharacteristicsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characteristics")
@RequiredArgsConstructor
@Tag(name = "Características", description = "Permite la creación, el listado, la actualización y borrado de características de productos")
public class CharacteristicsController {
    private final CharacteristicsService characteristicsService;

    @Operation(summary = "Obtener listado de características", description = "Obtiene el listado de todas las características existentes al momento", responses = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente", content = @Content(schema = @Schema(implementation = Characteristics.class)))
    })
    @GetMapping
    public ResponseEntity<?> listAllCharacteristics(){
        return new ResponseEntity<>(characteristicsService.listAllCharacteristics(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener característica por ID", description = "Permite obtener una característica a través del ID proporcionado en la ruta de la URL", responses = {
            @ApiResponse(responseCode = "200", description = "Característica obtenida exitosamente", content = @Content(schema = @Schema(implementation = Characteristics.class))),
            @ApiResponse(responseCode = "404", description = "No existe la característica del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacteristicById(@PathVariable Integer id) throws ResourceNotFoundException{
        return new ResponseEntity<>(characteristicsService.getCharacteristicById(id), HttpStatus.OK);
    }

    @Operation(summary = "Creación de característica", description = "Permite la creación de una nueva característica", responses = {
            @ApiResponse(responseCode = "209", description = "Característica creada", content = @Content(schema = @Schema(implementation = Characteristics.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción")
    })
    @PostMapping
    public ResponseEntity<?> createCharacteristics(@RequestBody CharacteristicsDto characteristicsDto){
        return new ResponseEntity<>(characteristicsService.createCharacteristics(characteristicsDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Asignación de características a un producto", description = "Permite cargar un listado de características a un determinado producto cuyo ID es proporcionado en la URL", responses = {
            @ApiResponse(responseCode = "200", description = "Características asignadas exitosamente", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "El listado de características no puede estar vacío", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe un producto del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PostMapping("/{productId}")
    public ResponseEntity<?> addCharacteristics(@PathVariable Integer productId, @RequestBody List<Characteristics> characteristics) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(characteristicsService.addCharacteristicsToProduct(productId, characteristics), HttpStatus.OK);
    }

    @Operation(summary = "Elimina una característica", description = "Elimina una característica a través del ID proporcionado en la URL de la petición, si la característica está asignada a uno o varios productos no se permitirá el borrado", responses = {
            @ApiResponse(responseCode = "204", description = "Característica eliminada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe la característica del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacteristcs(@PathVariable Integer id) throws ResourceNotFoundException {
        characteristicsService.deleteCharacteristcs(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Actualizar característica", description = "Actualiza una característica a través del ID proporcionado en la URL de la petición", responses = {
            @ApiResponse(responseCode = "200", description = "Característica actualizada exitosamente", content = @Content(schema = @Schema(implementation = Characteristics.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe la categoría del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacteristics(@PathVariable Integer id, @RequestBody CharacteristicsDto characteristicsDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(characteristicsService.updateCharacteristics(id, characteristicsDto), HttpStatus.OK);
    }
}
