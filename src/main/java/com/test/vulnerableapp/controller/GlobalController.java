package com.test.vulnerableapp.controller;

import com.test.vulnerableapp.operations.UserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UserOperation userOperation;

    @ModelAttribute
    public void setBirthday(Model model) {
        model.addAttribute("birthday", userOperation.getUserListByTodayDate());
    }
}