package com.example.provence.repository;

import com.example.provence.model.Item;
import com.example.provence.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {
    List<Order> findByItemsId(Long item);

}
