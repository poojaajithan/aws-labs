package com.example.awslabs.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

//example program to try out API gateway with lambda
public class HelloWorldLambda implements RequestHandler<Map<String,Object>, String> {
    @Override
    public String handleRequest(Map<String,Object> input, Context context) {
        return "{\"message\": \"Hello from Lambda\"}";
    }
}
