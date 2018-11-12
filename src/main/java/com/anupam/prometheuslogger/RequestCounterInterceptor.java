package com.anupam.prometheuslogger;

import java.lang.reflect.Method;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.prometheus.client.Counter;

@Component
public class RequestCounterInterceptor extends HandlerInterceptorAdapter {
	  private Logger logger = LoggerFactory.getLogger(getClass());

	  /**
	   * Create a Prometheus Counter Obj
	   */
    private static final Counter requestTotal = Counter.build()
         .name("http_requests_total")
         .labelNames("method", "handler", "status")
         .help("Http Request Total").register();
    
    private static final Counter requestError = Counter.build()
            .name("http_requests_error")
            .labelNames("method", "handler", "status")
            .help("Http Request Error").register();
    
    private static final Counter requestSuccess = Counter.build()
            .name("http_requests_success")
            .labelNames("method", "handler", "status")
            .help("Http Request Success").register();
    

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)throws Exception {
    	logger.info("Handler Interceptor is called");
    	
         // Update counters
         String handlerLabel = handler.toString();
         // get short form of handler method name
         if (handler instanceof HandlerMethod) {
              Method method = ((HandlerMethod) handler).getMethod();
              handlerLabel = method.getDeclaringClass().getSimpleName() + "." + method.getName();
         }
         // Note (2)
         requestTotal.labels(request.getMethod(), handlerLabel, Integer.toString(response.getStatus())).inc();
         
         if(handlerLabel.equalsIgnoreCase("HomeController.endpointB")) {
        	 Random rand = new Random();
        	 int randomNum = rand.nextInt((10 - 0) + 1) + 0;
        	 int i = 0;
        	 while(i<randomNum) {
        		 requestError.labels(request.getMethod(), handlerLabel, Integer.toString(404)).inc();
        		 i++;
        	 }
        	 logger.info("Error logged "+randomNum+"times to prometheus");
        	 
         }else //endpointA
    	 {
        	 Random rand = new Random();
        	 int randomNum = rand.nextInt((10 - 0) + 1) + 0;
        	 int i = 0;
        	 while(i<randomNum) {
        		 requestSuccess.labels(request.getMethod(), handlerLabel, Integer.toString(200)).inc();
	        	 i++;
        	 }
        	 logger.info("Success logged "+randomNum+"times to prometheus");
        	 
         }
      
    }
}
