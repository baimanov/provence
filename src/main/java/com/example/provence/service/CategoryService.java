package com.example.provence.service;

import com.example.provence.model.Category;
import com.example.provence.model.Item;
import com.example.provence.model.Order;
import com.example.provence.repository.CategoryRepository;
import com.example.provence.repository.ItemRepository;
import com.example.provence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    public final CategoryRepository categoryRepository;
    public final ItemRepository itemRepository;
    public final OrderRepository orderRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new IllegalStateException("Menu item with ID " + id + " not found");
        }
    }

    public Item createMenuItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteMenuItem(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        List<Order> orders = orderRepository.findByItemsId(id);
        log.info("service");
        if (itemOptional.isPresent()) {
            orderRepository.deleteAll(orders);
            itemRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Order not found with ID: " + id);
        }
    }

    public void findAllByCategory_IdAndDelete(Long id){
        List<Item> items = itemRepository.findAllByCategory_Id(id);
        for (Item item : items) {
            deleteMenuItem(item.getId());
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            // Handle not found case (e.g., throw an exception or return null)
            throw new IllegalStateException("Menu category with ID " + id + " not found");
        }
    }

    public void createMenuCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteMenuCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            findAllByCategory_IdAndDelete(id);
            categoryRepository.delete(categoryOptional.get());
        } else {
            throw new IllegalStateException("Order not found with ID: " + id);
        }
    }

}
