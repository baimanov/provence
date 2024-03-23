package com.example.provence.controller;

import com.example.provence.model.Item;
import com.example.provence.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/provence/items")
@RequiredArgsConstructor
public class ItemController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllMenuItems() {
        log.info("right");
        List<Item> items = categoryService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // Endpoint to get a menu item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = categoryService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/add_to_category")
    public ResponseEntity<String> addToCategory(@RequestBody Item item) {
        if (categoryService.getCategoryById(item.getCategory().getId()) != null) {
            return ResponseEntity.ok("Item registered successfully: " + categoryService.createMenuItem(item).toString());
        }
        return ResponseEntity.badRequest().body("Item registration failed. Category doesn't exists.");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteItem(@RequestParam("id") Long id) {
        log.info("controller");
        categoryService.deleteMenuItem(id);
        return ResponseEntity.ok("Item deleted successfully.");
    }

    @PostMapping("/to_stop_menu")
    public ResponseEntity<String> toStopMenu(@RequestParam("id") Long id) {
        log.info("toStopMenu");
        Item item = categoryService.getItemById(id);
        item.setStopMenu(true);
        categoryService.createMenuItem(item);
        return ResponseEntity.ok("Item Stop successfully.");
    }

    @PostMapping("/delete_stop_menu")
    public ResponseEntity<String> deleteStopMenu(@RequestParam("id") Long id) {
        log.info("deleteStopMenu");
        Item item = categoryService.getItemById(id);
        item.setStopMenu(false);
        categoryService.createMenuItem(item);
        return ResponseEntity.ok("Item deleted successfully.");
    }

    @PostMapping("/bool")
    public ResponseEntity<Boolean> checkItem(@RequestParam("id") Long id) {
        if(categoryService.getItemById(id).isStopMenu()){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
