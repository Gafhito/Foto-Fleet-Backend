package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.ImageDto;
import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Category;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.Status;
import com.digitalhouse.fotofleet.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final StatusService statusService;
    private final ProductImageService productImageService;

    @Autowired
    ObjectMapper mapper;

    public Page<Product> listAllProducts(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return productRepository.listAllProducts(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(ProductDto productDto) throws BadRequestException {
        Optional<Category> category = categoryService.getCategoryById(productDto.categoryId());
        Optional<Status> status = statusService.getStatusByName("Active");
        if (category.isEmpty()) throw new BadRequestException("No existe la categoría especificada");

        return productRepository.save(new Product(productDto.name(), productDto.description(), category.get(), productDto.rentalPrice(), productDto.stock(), status.get()));
    }

    public Optional<Product> getById(Integer id) {
        return productRepository.findById(id);
    }

    public ProductDto getDtoByProductId(Integer id) throws ResourceNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) throw new ResourceNotFoundException("No existe un producto con este ID");

        List<ImageDto> imageDtos = productImageService.listImagesByProductId(id);

        return new ProductDto(product.get().getName(), product.get().getDescription(), product.get().getCategory().getCategoryId(), product.get().getRentalPrice(), product.get().getStock(), product.get().getStatus().getName(), imageDtos);
    }

    public void deleteProduct(Integer id) throws ResourceNotFoundException{
        if(getDtoByProductId(id) == null){
            throw new ResourceNotFoundException("No existe el producto con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    /*public ProductDto updateProduct(ProductDto productDto) throws BadRequestException{
        if(productRepository.findById(productDto.getId()).isEmpty()){
            throw new BadRequestException("No es posible actualizar el producto con ID: " + productDto.getId() + ", porque no está registrado");
        }
        Product product = mapper.convertValue(productDto, Product.class);
        return mapper.convertValue(productRepository.save(product), ProductDto.class);
    }*/

    public void existsProductById(Integer id) throws ResourceNotFoundException {
        if (!productRepository.existsById(id)) throw new ResourceNotFoundException("No existe un producto con este ID");
    }
}
