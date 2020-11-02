package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CustomerService customerAdminBusinessService;

    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public List<RestaurantEntity> restaurantsByRating() {
        return restaurantDao.restaurantsByRating();
    }

    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName.equals("") || restaurantName == "") {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        return restaurantDao.getRestaurantsByName(restaurantName);
    }

    public List<RestaurantCategoryEntity> getRestaurantByCategoryID(final Long categoryID) {
        return restaurantDao.getRestaurantByCategoryId(categoryID);
    }

    public List<RestaurantEntity> restaurantByCategory(final String categoryId) throws CategoryNotFoundException {

        if (categoryId.equals("") || categoryId == "") {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryByUUId(categoryId);

        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        List<RestaurantEntity> restaurantEntityList = categoryEntity.getRestaurants();
        restaurantEntityList.sort(Comparator.comparing(RestaurantEntity::getRestaurantName));
        return restaurantEntityList;
    }


    public RestaurantEntity restaurantByUUID(String restaurantUUID) throws RestaurantNotFoundException {
        if (restaurantUUID == null || restaurantUUID.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        RestaurantEntity restaurantEntity = restaurantDao.restaurantByUUID(restaurantUUID);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantDao.restaurantByUUID(restaurantUUID);
    }

    @Transactional
    public RestaurantEntity updateCustomerRating(final Double customerRating, final String restaurant_id, final String authorizationToken)
            throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

        final ZonedDateTime now = ZonedDateTime.now();

        customerAdminBusinessService.validateAccessToken(authorizationToken);

        if (restaurant_id == null || restaurant_id.isEmpty() || restaurant_id.equalsIgnoreCase("\"\"")) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        RestaurantEntity restaurantEntity = restaurantDao.restaurantByUUID(restaurant_id);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        if (customerRating == null || customerRating.isNaN() || customerRating < 1 || customerRating > 5) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }

        BigDecimal ratingCalculation = new BigDecimal(restaurantEntity.getCustomerRating(), MathContext.DECIMAL64);
        BigDecimal oldRatingCalculation = ratingCalculation.multiply(new BigDecimal(restaurantEntity.getNumberCustomersRated()));
        BigDecimal calculatedRating = (oldRatingCalculation.add(new BigDecimal(customerRating))).divide(new BigDecimal(restaurantEntity.getNumberCustomersRated() + 1));
        restaurantEntity.setCustomerRating(calculatedRating.doubleValue());
        restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated() + 1);

        restaurantDao.updateRestaurant(restaurantEntity);
        return restaurantEntity;
    }

    @Transactional
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double cu) throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

        if (restaurantEntity.getId() == null) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        if (restaurantEntity.getCustomerRating() < 1 && restaurantEntity.getCustomerRating() > 5) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }

        RestaurantEntity rEntity = restaurantDao.restaurantByUUID(restaurantEntity.getUuid());
        if (rEntity.getId() == restaurantEntity.getId()) {
            restaurantEntity.setCustomerRating(restaurantEntity.getCustomerRating());
            restaurantEntity.setNumberCustomersRated(rEntity.getNumberCustomersRated() + 1);
            return restaurantDao.updateRestaurantEntity(restaurantEntity);
        }
        return restaurantEntity;
    }
}
