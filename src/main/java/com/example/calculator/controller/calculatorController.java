package com.example.calculator.controller;

import com.example.calculator.service.Calculatorservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class calculatorController {
    Calculatorservice calculatorservice;

    @Autowired
    public  calculatorController(Calculatorservice calculatorservice){
        this.calculatorservice=calculatorservice;
    }
    @GetMapping("sum")
    int add(@RequestParam ("a") int a, @RequestParam ("b") int b){
        return calculatorservice.sum(a,b);

    }
}
