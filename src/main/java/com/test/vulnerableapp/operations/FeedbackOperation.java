package com.test.vulnerableapp.operations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.Feedback;
import com.test.vulnerableapp.repositories.FeedbackRepository;

@Service
public class FeedbackOperation {
    private FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackOperation(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Optional<Feedback> retrieveById(long id) {
        return feedbackRepository.findById(id);
    }

    public List<Feedback> retrieveAll() { return feedbackRepository.findAll(); }

    public Optional<Feedback> addFeedback(Feedback feedback) {
        /*if (feedback.isValid()) {
            return Optional.of(feedbackRepository.saveAndFlush(feedback));
        }*/

        return Optional.of(feedbackRepository.saveAndFlush(feedback));
        //return Optional.empty();
    }
}
