package com.hexagonal.ms_foodcourt.util;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;

import java.time.LocalDate;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_OWNER;

public class TestDataFactory {

    private TestDataFactory() {
    }

    public static RestaurantRequest mockRestaurantRequest() {
        RestaurantRequest restaurantRequest = new RestaurantRequest();
        restaurantRequest.setName("RestaurantName");
        restaurantRequest.setNit("123456");
        restaurantRequest.setAddress("Calle siempre viva 123");
        restaurantRequest.setPhoneNumber("+573167549634");
        restaurantRequest.setUrlLogo("https://www.google.com/imgres?q=logo&imgurl=https%3A%2F%2Fimg.freepik.com%2Fvector-gratis%2Fvector-degradado-logotipo-colorido-pajaro_343694-1365.jpg%3Fsemt%3Dais_incoming%26w%3D740%26q%3D80&imgrefurl=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&docid=bwrLlXToiyNaVM&tbnid=LPl9_U3RcRpszM&vet=12ahUKEwiCxabo2eKPAxU1RDABHWPNG88QM3oECBwQAA..i&w=740&h=740&hcb=2&ved=2ahUKEwiCxabo2eKPAxU1RDABHWPNG88QM3oECBwQAA");
        restaurantRequest.setOwnerId(1L);
        return restaurantRequest;
    }

    public static Restaurant mockRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("RestaurantName");
        restaurant.setNit("123456");
        restaurant.setAddress("Calle siempre viva 123");
        restaurant.setPhoneNumber("+573167549634");
        restaurant.setUrlLogo("https://www.google.com/imgres?q=logo&imgurl=https%3A%2F%2Fimg.freepik.com%2Fvector-gratis%2Fvector-degradado-logotipo-colorido-pajaro_343694-1365.jpg%3Fsemt%3Dais_incoming%26w%3D740%26q%3D80&imgrefurl=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&docid=bwrLlXToiyNaVM&tbnid=LPl9_U3RcRpszM&vet=12ahUKEwiCxabo2eKPAxU1RDABHWPNG88QM3oECBwQAA..i&w=740&h=740&hcb=2&ved=2ahUKEwiCxabo2eKPAxU1RDABHWPNG88QM3oECBwQAA");
        restaurant.setOwnerId(1L);
        return restaurant;
    }

    public static RestaurantEntity mockRestaurantEntity() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setName("RestaurantName");
        restaurant.setNit("123456");
        restaurant.setAddress("Calle siempre viva 123");
        restaurant.setPhoneNumber("+573167549634");
        restaurant.setUrlLogo("https://www.google.com/imgres?q=logo&imgurl=https%3A%2F%2Fimg.freepik.com%2Fvector-gratis%2Fvector-degradado-logotipo-colorido-pajaro_343694-1365.jpg%3Fsemt%3Dais_incoming%26w%3D740%26q%3D80&imgrefurl=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&docid=bwrLlXToiyNaVM&tbnid=LPl9_U3RcRpszM&vet=12ahUKEwiCxabo2eKPAxU1RDABHWPNG88QM3oECBwQAA..i&w=740&h=740&hcb=2&ved=2ahUKEwiCxabo2eKPAxU1RDABHWPNG88QM3oECBwQAA");
        restaurant.setOwnerId(1L);
        return restaurant;
    }

    public static User mockUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setIdNumber("1234");
        user.setPhoneNumber("+573167549634");
        user.setDateBirth(LocalDate.of(2000, 9, 17));
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(ROLE_OWNER);
        return user;
    }

    public static UserFeign mockUserFeign() {
        UserFeign user = new UserFeign();
        user.setId(1L);
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setIdNumber("1234");
        user.setPhoneNumber("+573167549634");
        user.setDateBirth(LocalDate.of(2000, 9, 17));
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(ROLE_OWNER);
        return user;
    }

}
