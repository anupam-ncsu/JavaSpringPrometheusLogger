package com.anupam.prometheuslogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.prometheus.client.Counter;

@RestController
public class HomeController {
     private Logger logger = LoggerFactory.getLogger(getClass());
     
 
     @RequestMapping("/endpointA")
     public String endpointA() throws InterruptedException {
          logger.info("/endpointA");
          return "Success generator at endpoint A";
          
     }
 
     @RequestMapping("/endpointB")
     public String endpointB() throws InterruptedException {
    	 
          logger.info("/endpointB");
          return "Error generator at endpoint B";
     }
}