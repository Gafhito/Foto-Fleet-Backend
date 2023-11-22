package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.ProductRatingDto;
import com.digitalhouse.fotofleet.dtos.RentalResponseDto;
import com.digitalhouse.fotofleet.dtos.UserDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.ProductRating;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.repositories.ProductRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductRatingService {
    private final ProductRatingRepository productRatingRepository;
    private final ProductService productService;
    private final UserService userService;
    private final RentalDetailService rentalDetailService;

    public List<ProductRatingDto> listRantingsByProductId(Integer productId) throws ResourceNotFoundException {
        Optional<Product> product = productService.getById(productId);
        if (product.isEmpty()) throw new ResourceNotFoundException("No existe el producto con el ID proporcionado");

        List<ProductRating> productRatings = productRatingRepository.findByProductId(productId);
        if (productRatings.isEmpty()) throw new ResourceNotFoundException("El producto no tiene ratings aún");

        List<ProductRatingDto> productRatingDtos = new ArrayList<>();
        for (ProductRating pr : productRatings) {
            UserDto userDto = new UserDto(pr.getUser().getFirstName(), pr.getUser().getLastName(), pr.getUser().getEmail());
            productRatingDtos.add(new ProductRatingDto(pr.getRatingId(), userDto, pr.getRating(), pr.getReview(), pr.getRatingDate()));
        }

        return productRatingDtos;
    }

    public ProductRatingDto createReview(String jwt, Integer productId, ProductRatingDto productRatingDto) throws BadRequestException, ResourceNotFoundException {
        User user = userService.getUserByJwt(jwt);
        UserDto userDto = new UserDto(user.getFirstName(), user.getLastName(), user.getEmail());
        Optional<Product> product = productService.getById(productId);
        if (product.isEmpty()) throw new BadRequestException("No existe el producto con este ID");
        if (rentalDetailService.checkUserRentalProduct(user.getUserId(), productId)) throw new ResourceNotFoundException("El usuario aún no tiene alquileres completos del producto");

        if (productRatingDto.rating() < 1 || productRatingDto.rating() > 5) throw new BadRequestException("Error, las review has de ser de 1 a 5 estrellas");

        ProductRating productRating = productRatingRepository.save(new ProductRating(user, product.get(), productRatingDto.rating(), productRatingDto.review()));

        return new ProductRatingDto(productRating.getRatingId(), userDto, productRating.getRating(), productRating.getReview(), productRating.getRatingDate());
    }
}
