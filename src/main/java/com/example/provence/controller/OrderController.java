package com.example.provence.controller;

import com.example.provence.model.Category;
import com.example.provence.model.Email;
import com.example.provence.model.Order;
import com.example.provence.model.Vacancy;
import com.example.provence.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/provence/orders")
public class OrderController {
    private final OrderService orderService;
    @Value("${spring.mail.username}")
    private String fromEmail ;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        return orderOptional.map(order -> new ResponseEntity<>(order, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add_order")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) throws MessagingException {
        Order createdOrder = orderService.createOrder(order);
        String content = orderService.beautifyMessage(order);
        orderService.sendEmail("Заказы с сайта Provence", content, fromEmail);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PostMapping("/send_vacancy")
    public ResponseEntity<Vacancy> sendVacancy(@RequestBody Vacancy vacancy) throws MessagingException {
        String content = orderService.beautifyVacancy(vacancy);
        orderService.sendEmail("Вакансии с сайта Provence", content, fromEmail);
        return new ResponseEntity<>(vacancy, HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteOrder(@RequestParam("id") Long id) {
        try {
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private AtomicInteger code = new AtomicInteger(0);

    @PostMapping("/verificate")
    public ResponseEntity<Integer> verificateEmail(@RequestBody Email email) {
        int generatedCode = orderService.generateCode();
        code.set(generatedCode);
        try {
            orderService.sendEmail("Код подтверждение с сайта Provence", String.valueOf(generatedCode), email.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(generatedCode, HttpStatus.OK);
    }

    @PostMapping("/code")
    public ResponseEntity<Boolean> checkCode(@RequestBody int codeCheck) {
        if(codeCheck != code.get()){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
