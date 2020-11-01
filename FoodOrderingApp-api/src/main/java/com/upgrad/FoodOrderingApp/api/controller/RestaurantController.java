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

import java.util.*;

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

    /**
     *
     * @return List of all restaurants in the database
     *
     */
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        final List<RestaurantEntity> allRestaurants = restaurantBusinessService.getAllRestaurants();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> details = new ArrayList<RestaurantList>();
        for (RestaurantEntity n: allRestaurants) {
            RestaurantList detail = new RestaurantList();
            detail.setId(UUID.fromString(n.getUuid()));
            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPrice());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

            AddressEntity addressEntity = addressService.getAddressById(n.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());

            StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

            responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            responseAddressState.setStateName(stateEntity.getStateName());
            responseAddress.setState(responseAddressState);

            detail.setAddress(responseAddress);

            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :n.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

            Collections.sort(categoryLists);
            detail.setCategories(String.join(",", categoryLists));
            restaurantListResponse.addRestaurantsItem(detail);
        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     *
     * @param restaurant_name
     * @return List of all restaurants matched with given restaurant name
     * @throws RestaurantNotFoundException - when restaurant name field is empty
	 *
     */
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable String restaurant_name)
            throws RestaurantNotFoundException {

        if(restaurant_name == null || restaurant_name.isEmpty() || restaurant_name.equalsIgnoreCase("\"\"")){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        final List<RestaurantEntity> allRestaurants = restaurantBusinessService.getRestaurantsByName(restaurant_name);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> details = new ArrayList<RestaurantList>();
        for (RestaurantEntity n: allRestaurants) {
            RestaurantList detail = new RestaurantList();
            detail.setId(UUID.fromString(n.getUuid()));
            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPrice());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

            AddressEntity addressEntity = addressService.getAddressById(n.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());

            StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

            responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            responseAddressState.setStateName(stateEntity.getStateName());
            responseAddress.setState(responseAddressState);

            detail.setAddress(responseAddress);

            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :n.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

            Collections.sort(categoryLists);

            detail.setCategories(String.join(",", categoryLists));
            restaurantListResponse.addRestaurantsItem(detail);

        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /**
     *
     * @param category_id
     * @return List of all restaurants having given category id
     * @throws CategoryNotFoundException - When Given category id  field is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getRestaurantByCategoryId(@PathVariable String category_id) throws CategoryNotFoundException {

        if(category_id == null || category_id.isEmpty() || category_id.equalsIgnoreCase("\"\"")){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity matchedCategory = categoryBusinessService.getCategoryEntityByUuid(category_id);

        if(matchedCategory == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        final List<RestaurantCategoryEntity> allRestaurantCategories = restaurantBusinessService.getRestaurantByCategoryId(matchedCategory.getId());

        List<RestaurantList> details = new ArrayList<RestaurantList>();
        for (RestaurantCategoryEntity restaurantCategoryEntity:allRestaurantCategories) {
            RestaurantEntity n = restaurantCategoryEntity.getRestaurant();
            RestaurantList detail = new RestaurantList();
            detail.setId(UUID.fromString(n.getUuid()));
            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPrice());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

            AddressEntity addressEntity = addressService.getAddressById(n.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());

            StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

            responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            responseAddressState.setStateName(stateEntity.getStateName());
            responseAddress.setState(responseAddressState);

            detail.setAddress(responseAddress);

            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :n.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

            Collections.sort(categoryLists);
            detail.setCategories(String.join(",", categoryLists));
            details.add(detail);
        }

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    /**
     *
     * @param restaurant_id
     * @return Restaurant with details based on given restaurant id
     * @throws RestaurantNotFoundException - When given restaurant id field is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity restaurantByUUID(@PathVariable String restaurant_id) throws RestaurantNotFoundException {

        if(restaurant_id == null || restaurant_id.isEmpty() || restaurant_id.equalsIgnoreCase("\"\"")){
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        final RestaurantEntity restaurant = restaurantBusinessService.restaurantByUUID(restaurant_id);
        if(restaurant == null){
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        RestaurantDetailsResponse details = new RestaurantDetailsResponse();
        details.setId(UUID.fromString(restaurant.getUuid()));
        details.setRestaurantName(restaurant.getRestaurantName());
        details.setPhotoURL(restaurant.getPhotoUrl());
        details.setCustomerRating(restaurant.getCustomerRating());
        details.setAveragePrice(restaurant.getAvgPrice());
        details.setNumberCustomersRated(restaurant.getNumCustomersRated());

        AddressEntity addressEntity = addressService.getAddressById(restaurant.getAddress().getId());
        RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

        responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
        responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
        responseAddress.setLocality(addressEntity.getLocality());
        responseAddress.setCity(addressEntity.getCity());
        responseAddress.setPincode(addressEntity.getPincode());

        StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
        RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

        responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
        responseAddressState.setStateName(stateEntity.getStateName());
        responseAddress.setState(responseAddressState);

        details.setAddress(responseAddress);

        List<CategoryList> categoryLists = new ArrayList();
        for (CategoryEntity categoryEntity :restaurant.getCategoryEntities()) {
            CategoryList categoryListDetail = new CategoryList();
            categoryListDetail.setId(UUID.fromString(categoryEntity.getUuid()));
            categoryListDetail.setCategoryName(categoryEntity.getCategoryName());

            List<ItemList> itemLists = new ArrayList();
            for (ItemEntity itemEntity :categoryEntity.getItemEntities()) {
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
     *
     * @param authorization, customerRating, restaurant_id
     * @return Restaurant uuid of the rating updated restaurant
     * @throws RestaurantNotFoundException - When given restaurant id field is empty
     *         AuthorizationFailedException - When customer is not logged in or logged out or login expired
     *         InvalidRatingException - When the Rating value provided is invalid
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateCustomerRating(@RequestHeader("authorization") final String authorization, @RequestParam Double customerRating, @PathVariable String restaurant_id )
            throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException {

        String[] bearerToken = authorization.split("Bearer ");

        RestaurantEntity restaurantEntity = restaurantBusinessService.updateCustomerRating(customerRating, restaurant_id, bearerToken[1]);

        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantEntity.getUuid())).status("RESTAURANT RATING UPDATED SUCCESSFULLY");

        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
    }

}

}
