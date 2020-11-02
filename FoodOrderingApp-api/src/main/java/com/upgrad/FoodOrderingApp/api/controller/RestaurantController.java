package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantBusinessService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StateService stateBusinessService;

    @Autowired
    private CategoryService categoryBusinessService;

    @Autowired
    private CustomerService customerService;

    /**
     * @return List of all restaurants in the database
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        List<RestaurantEntity> restaurantEntityList = restaurantBusinessService.restaurantsByRating();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                    .stateName(restaurantEntity.getAddress().getState().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .locality(restaurantEntity.getAddress().getLocality())
                    .city(restaurantEntity.getAddress().getCity())
                    .state(restaurantDetailsResponseAddressState);

            String categoriesString = categoryBusinessService.getCategoriesByRestaurant(restaurantEntity.getUuid())
                    .stream()
                    .map(o -> String.valueOf(o.getCategoryName()))
                    .collect(Collectors.joining(", "));
            Double temp3 = BigDecimal.valueOf(restaurantEntity.getCustomerRating()).setScale(1, RoundingMode.HALF_UP).doubleValue();
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .customerRating(new BigDecimal(temp3))
                    .averagePrice(restaurantEntity.getAvgPrice())
                    .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                    .address(restaurantDetailsResponseAddress)
                    .categories(categoriesString);
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /*
     *
     * @return List of all restaurants matched with given restaurant name
     * @throws RestaurantNotFoundException - when restaurant name field is empty
     *
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(
            @PathVariable("restaurant_name") final String restaurantName)
            throws RestaurantNotFoundException {
        List<RestaurantEntity> restaurantEntityList = restaurantBusinessService.restaurantsByName(restaurantName);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        if (restaurantEntityList.isEmpty()) {
            return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
        }

        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                    .stateName(restaurantEntity.getAddress().getState().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .locality(restaurantEntity.getAddress().getLocality())
                    .city(restaurantEntity.getAddress().getCity())
                    .state(restaurantDetailsResponseAddressState);

            String categoriesString = categoryBusinessService.getCategoriesByRestaurant(restaurantEntity.getUuid())
                    .stream()
                    .map(o -> String.valueOf(o.getCategoryName()))
                    .collect(Collectors.joining(", "));
            Double temp2 = BigDecimal.valueOf(restaurantEntity.getCustomerRating()).setScale(1, RoundingMode.HALF_UP).doubleValue();
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .customerRating(new BigDecimal(temp2))
                    .averagePrice(restaurantEntity.getAvgPrice())
                    .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                    .address(restaurantDetailsResponseAddress)
                    .categories(categoriesString);
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * @return List of all restaurants having given category id
     * @throws CategoryNotFoundException - When Given category id  field is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategoryId(
            @PathVariable("category_id") final String categoryId)
            throws CategoryNotFoundException {
        List<RestaurantEntity> restaurantEntityList = restaurantBusinessService.restaurantByCategory(categoryId);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                    .stateName(restaurantEntity.getAddress().getState().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .locality(restaurantEntity.getAddress().getLocality())
                    .city(restaurantEntity.getAddress().getCity())
                    .state(restaurantDetailsResponseAddressState);

            String categoriesString = categoryBusinessService.getCategoriesByRestaurant(restaurantEntity.getUuid())
                    .stream()
                    .map(o -> String.valueOf(o.getCategoryName()))
                    .collect(Collectors.joining(", "));
            Double temp1 = BigDecimal.valueOf(restaurantEntity.getCustomerRating()).setScale(1, RoundingMode.HALF_UP).doubleValue();
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .customerRating(new BigDecimal(temp1))
                    .averagePrice(restaurantEntity.getAvgPrice())
                    .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                    .address(restaurantDetailsResponseAddress)
                    .categories(categoriesString);
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     * @param restaurant_id
     * @return Restaurant with details based on given restaurant id
     * @throws RestaurantNotFoundException - When given restaurant id field is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity restaurantByUUID(@PathVariable String restaurant_id) throws RestaurantNotFoundException {

        if (restaurant_id == null || restaurant_id.isEmpty() || restaurant_id.equalsIgnoreCase("\"\"")) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        final RestaurantEntity restaurant = restaurantBusinessService.restaurantByUUID(restaurant_id);
        if (restaurant == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        RestaurantDetailsResponse details = new RestaurantDetailsResponse();
        details.setId(UUID.fromString(restaurant.getUuid()));
        details.setRestaurantName(restaurant.getRestaurantName());
        details.setPhotoURL(restaurant.getPhotoUrl());
        BigDecimal customerRating = new BigDecimal(restaurant.getCustomerRating(), MathContext.DECIMAL64);
        details.setCustomerRating(customerRating);
        details.setAveragePrice(restaurant.getAvgPrice());
        details.setNumberCustomersRated(restaurant.getNumberCustomersRated());

        AddressEntity addressEntity = addressService.getAddressById((long) restaurant.getAddress().getId());
        RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

        responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
        responseAddress.setFlatBuildingName(addressEntity.getFlatBuilNo());
        responseAddress.setLocality(addressEntity.getLocality());
        responseAddress.setCity(addressEntity.getCity());
        responseAddress.setPincode(addressEntity.getPincode());

        StateEntity stateEntity = stateBusinessService.getStateById((long) addressEntity.getState().getId());
        RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

        responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
        responseAddressState.setStateName(stateEntity.getStateName());
        responseAddress.setState(responseAddressState);

        details.setAddress(responseAddress);

        List<CategoryList> categoryLists = new ArrayList();
        for (CategoryEntity categoryEntity : restaurant.getCategoryEntities()) {
            CategoryList categoryListDetail = new CategoryList();
            categoryListDetail.setId(UUID.fromString(categoryEntity.getUuid()));
            categoryListDetail.setCategoryName(categoryEntity.getCategoryName());

            List<ItemList> itemLists = new ArrayList();
            for (ItemEntity itemEntity : categoryEntity.getItems()) {
                ItemList itemDetail = new ItemList();
                itemDetail.setId(UUID.fromString(itemEntity.getUuid()));
                itemDetail.setItemName(itemEntity.getItemName());
                itemDetail.setPrice(itemEntity.getPrice());
                itemDetail.setItemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType()));
                itemLists.add(itemDetail);
            }
            categoryListDetail.setItemList(itemLists);
            categoryLists.add(categoryListDetail);
        }

        details.setCategories(categoryLists);

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    /**
     * @param authorization, customerRating, restaurant_id
     * @return Restaurant uuid of the rating updated restaurant
     * @throws RestaurantNotFoundException - When given restaurant id field is empty
     *                                     AuthorizationFailedException - When customer is not logged in or logged out or login expired
     *                                     InvalidRatingException - When the Rating value provided is invalid
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(
            @RequestParam(name = "customer_rating") final Double customerRating,
            @PathVariable("restaurant_id") final String restaurantId,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = null;
        if (bearerToken.length == 1) {
            throw new AuthorizationFailedException("ATHR-005", "Use valid authorization format <Bearer accessToken>");
        } else {
            customerEntity = customerService.getCustomer(bearerToken[1]);
        }

        RestaurantEntity restaurantEntity = restaurantBusinessService.restaurantByUUID(restaurantId);
        restaurantBusinessService.updateRestaurantRating(restaurantEntity, customerRating);

        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantId))
                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
    }


}
