package com.example.provence.repository;

import com.example.provence.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategory_Id(Long id);
    List<Item> findByNameContainingIgnoreCase(String name);
    // You can define custom query methods here if needed
    Item findItemByNameContainingIgnoreCase(String name);

}
