package com.example.calculator.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class Calculatorservice {
    final static double PI =3.142;
   @Cacheable("sum")
    public int sum(int a,  int b){
       try {
           Thread.sleep(3000);
       }catch (InterruptedException e){
           throw new RuntimeException(e);
       }
       return  a+ b ;
   }

}

