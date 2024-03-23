package com.example.provence.controller;

import com.example.provence.model.Category;
import com.example.provence.model.Item;
import com.example.provence.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/provence/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // Endpoint to get all menu categorys
    @GetMapping
    public ResponseEntity<List<Category>> getAllMenuCategories() {
        log.info("right");
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Endpoint to get a menu category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/add_category")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        categoryService.createMenuCategory(category);
        return ResponseEntity.ok("Item registered successfully: " + category.toString());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteItem(@RequestParam("id") Long id) {
        categoryService.deleteMenuCategory(id);
        return ResponseEntity.badRequest().body("Category deleted successfully.");
    }
}
