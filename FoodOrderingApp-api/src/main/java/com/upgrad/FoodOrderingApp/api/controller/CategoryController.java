package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService categoryBusinessService;

    /**
     *
     * @return All categories stored in database
     * Getting the list of all categories with help of category business service
     * return response entity with CategoriesList(details) and Http status
     *
     */
    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllCategories() {

        final List<CategoryEntity> allCategories = categoryBusinessService.getAllCategories();
        List<CategoryListResponse> categoriesList = new ArrayList<CategoryListResponse>();
        for (CategoryEntity n: allCategories) {
            CategoryListResponse categoryDetail = new CategoryListResponse();
            categoryDetail.setCategoryName(n.getCategoryName());
            categoryDetail.setId(UUID.fromString(n.getUuid()));
            categoriesList.add(categoryDetail);
        }
        return new ResponseEntity<>(categoriesList, HttpStatus.OK);
    }

    /**
     *
     * @return Category with full details like items based on given category id
     * @throws CategoryNotFoundException - when category id field is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(
            @PathVariable("category_id") final String categoryId)
            throws CategoryNotFoundException{
        CategoryEntity categoryEntity = categoryBusinessService.getCategoryById(categoryId.toLowerCase());
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse().id(UUID.fromString(categoryEntity.getUuid())).categoryName(categoryEntity.getCategoryName());
        for (ItemEntity itemEntity : categoryEntity.getItemEntities()) {
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .itemName(itemEntity.getItemName())
                    .price(itemEntity.getPrice())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType()));
            categoryDetailsResponse.addItemListItem(itemList);
        }
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }

}
