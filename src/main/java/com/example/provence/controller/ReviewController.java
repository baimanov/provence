package com.example.provence.controller;

import com.example.provence.model.Category;
import com.example.provence.model.Review;
import com.example.provence.repository.ReviewRepository;
import com.example.provence.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/provence/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    // Endpoint to get all menu categorys
    @GetMapping
    public ResponseEntity<List<Review>> reviews() {
        log.info("right");
        List<Review> reviews = reviewRepository.findAll();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/add")
    public ResponseEntity<Review> addReviews(@RequestBody Review review) {
        reviewRepository.save(review);
        return ResponseEntity.ok(review);
    }
}
