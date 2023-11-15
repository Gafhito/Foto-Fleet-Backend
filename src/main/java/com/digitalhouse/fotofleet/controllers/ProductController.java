package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.services.ProductImageService;
import com.digitalhouse.fotofleet.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Permite la creación, el listado, búsqueda, actualización y borrado de productos")
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;

    @Operation(summary = "Obtener listado de productos", description = "Obtiene el listado de productos utilizando un sistema de paginación", responses = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam Integer page) {
        return new ResponseEntity<>(productService.listAllProducts(page), HttpStatus.OK);
    }

    @Operation(summary = "Obtener producto por ID", description = "Permite obtener un producto a través del ID proporcionado en la ruta de la URL", responses = {
            @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "No existe producto del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.getDtoByProductId(id), HttpStatus.OK);
    }

    @Operation(summary = "Carga de imágenes a un producto", description = "Permite cargar un listado de imágenes a un determinado producto cuyo ID es proporcionado como un parámetro en la URL", responses = {
            @ApiResponse(responseCode = "201", description = "Imágenes cargadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Excede el límite máximo de 6 imágenes secundarias", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe un producto del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/images")
    public ResponseEntity<?> uploadImages(@RequestParam Integer productId, @RequestParam MultipartFile primaryImage, @RequestParam List<MultipartFile> secondaryImages) throws BadRequestException, ResourceNotFoundException {
        Optional<Product> product = productService.getById(productId);
        if (product.isEmpty()) throw new ResourceNotFoundException("No existe el producto con el ID especificado");

        productImageService.uploadImagesForProduct(product.get(), primaryImage, secondaryImages);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Creación de producto", description = "Permite la creación de un nuevo producto", responses = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Ya existe un producto registrado con este nombre", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe una categoría con el ID proporcionado en el DTO", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) throws BadRequestException, ResourceNotFoundException {
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Operation(summary = "Elimina un producto", description = "Elimina un producto y sus imágenes asociadas a través del ID proporcionado en la URL de la petición", responses = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe el producto del ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) throws ResourceNotFoundException {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto a través del ID proporcionado en la URL de la petición", responses = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "No existe un producto registrado con el ID proporcionado", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe un status con el nombre proporcionado en el DTO", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(productService.updateProduct(id, productDto),HttpStatus.OK);
    }

    @Operation(summary = "Buscar producto", description = "Busca un producto a través del nombre y/o nombre de la categoría proporcionados como parámetro en la URL de la petición, se debe acompañar del parámetro 'page' para indicar el número de paginación", responses = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa", content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam Integer page,  @RequestParam String product, @RequestParam String categoryName) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.search(page, product, categoryName), HttpStatus.OK);
    }
}
