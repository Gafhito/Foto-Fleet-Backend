package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.ProductDto;
import com.digitalhouse.fotofleet.dtos.UpdateRolDto;
import com.digitalhouse.fotofleet.dtos.UserDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.Rol;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.repositories.UserRepository;
import com.digitalhouse.fotofleet.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RolService rolService;
    private final ProductService productService;
    private final JwtGenerator jwtGenerator;

    public Boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public String getRolByUserEmail(String email) {
        return userRepository.findUserRolesByEmail(email).get(0).getRoleName();
    }

    public UpdateRolDto updateRol(UpdateRolDto updateRolDto) throws ResourceNotFoundException, BadRequestException {
        if (updateRolDto.rol().equals("Admin")) throw new BadRequestException("No puede asignar un rol de Admin a este usuario");

        Optional<User> userOptional = getUserByEmail(updateRolDto.email());
        if (userOptional.isEmpty()) throw new ResourceNotFoundException("No existe un usuario con este email");

        Optional<Rol> rolOptional = rolService.getRolByName(updateRolDto.rol());
        if (rolOptional.isEmpty()) throw new ResourceNotFoundException("No existe un rol con este nombre");

        User user = userOptional.get();
        List<Rol> userRoles = user.getRoles();
        userRoles.set(0, rolOptional.get());
        user.setRoles(userRoles);
        userRepository.save(user);

        return new UpdateRolDto(user.getEmail(), user.getRoles().get(0).getRoleName());
    }

    public UserDto getProfile(String jwt) throws BadRequestException, ResourceNotFoundException {
        String email = jwtGenerator.getEmailOfJwt(jwt.substring(7));

        Optional<User> u = getUserByEmail(email);
        if (u.isEmpty()) throw new BadRequestException("No existe un usuario con este email");

        List<Product> products = u.get().getProductFavorites();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product p : products) {
            ProductDto productDto = productService.getDtoByProductId(p.getProductId());
            productDtos.add(productDto);
        }

        return new UserDto(u.get().getFirstName(), u.get().getLastName(), u.get().getEmail(), u.get().getAddress(), u.get().getPhone(), u.get().getRegistrationDate(), productDtos);
    }

    public void addFavorite(String jwt, Integer productId) throws BadRequestException, ResourceNotFoundException {
        Optional<User> user = getUserByEmail(jwtGenerator.getEmailOfJwt(jwt.substring(7)));
        Optional<Product> product = productService.getById(productId);

        if (user.isEmpty()) throw new BadRequestException("Error interno del sistema, póngase en contacto con soporte");
        if (product.isEmpty()) throw new ResourceNotFoundException("No existe el producto con el ID especificado");

        User u = user.get();
        List<Product> favorites = u.getProductFavorites();
        favorites.add(product.get());
        Set<Product> singleFavorites = new HashSet<>(favorites);  // Crea un listado de productos favoritos sin repetirlos
        favorites.clear();  // Limpia listado
        favorites.addAll(singleFavorites);  // Agrega nueva lista de favoritos sin repetir ninguno

        u.setProductFavorites(favorites);
        userRepository.save(u);  // Guarda el usuario con los cambios en listado de favoritos
    }
}
