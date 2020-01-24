package com.test.vulnerableapp.endpoints;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.test.vulnerableapp.operations.FeedbackOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test.vulnerableapp.model.Feedback;

@RestController
@RequestMapping("/api")
public class FeedbackEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackEndpoint.class);

    private FeedbackOperation feedbackService;

    @Autowired
    public FeedbackEndpoint(FeedbackOperation feedbackService) {
        this.feedbackService = feedbackService;
    }
    
    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getFeedbacks() {
        LOGGER.debug("Getting all feedbacks");

        return ResponseEntity.ok(feedbackService.retrieveAll());
    }
    
    @GetMapping("/feedback/{id}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable long id) {
        LOGGER.debug("Getting feedback with id {}", id);

        try {
            return ResponseEntity.ok(feedbackService.retrieveById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PostMapping("/feedback")
    public ResponseEntity addFeedback(@RequestBody Feedback feedback) {
        LOGGER.debug("Adding feedback {}", feedback);

        Optional<Feedback> addedFeedback = feedbackService.addFeedback(feedback);
        if (addedFeedback.isPresent()) {
            LOGGER.debug("User added to database");
            return ResponseEntity
                    .created(URI.create("/feedback/" + feedback.getId()))
                    .body(addedFeedback.get());
        }

        return ResponseEntity.badRequest().build();
    }
    
}
