package com.example.calculator.service;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
    final  double pi =3.142;
    public int sub(int a, int b) {
        return a - b;
    }
}