package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.CategoryDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Permite la creación, el listado, la actualización y borrado de categorías de productos")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Creación de categoría", description = "Permite la creación de una nueva categoría", responses = {
            @ApiResponse(responseCode = "209", description = "Categoría creada", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción")
    })
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Asignación de imagen a una categoría", description = "Permite cargar una imagen a una categoría que no la tenga", responses = {
            @ApiResponse(responseCode = "209", description = "Imagen almacenada exitosamente", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Error al procesar la imagen", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe la categoría del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam Integer categoryId, @RequestParam MultipartFile file) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(categoryService.uploadImage(categoryId, file), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener categoría por ID", description = "Permite obtener una categoría a través del ID proporcionado en la ruta de la URL", responses = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "No existe la categoría del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws ResourceNotFoundException {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener listado de categorías", description = "Obtiene el listado de todas las categorías existentes al momento", responses = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente", content = @Content(schema = @Schema(implementation = Category.class)))
    })
    @GetMapping
    public ResponseEntity<?> listCategories() {
        return new ResponseEntity<>(categoryService.listCategories(), HttpStatus.OK);
    }

    @Operation(summary = "Elimina una categoría", description = "Elimina categoría a través del ID proporcionado en la URL de la petición", responses = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe la categoría del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) throws ResourceNotFoundException{
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría a través del ID proporcionado en la URL de la petición", responses = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente", content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe la categoría del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryDto),HttpStatus.OK);
    }
}
