package com.example.book_tracker_service.response_and_request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(HttpStatus status, String messages, Object data) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status.value());
        map.put(messages, data);

        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(HttpStatus status, List<String> messages, List<Object> data) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status.value());
        int size = Math.min(messages.size(), data.size());
        for(int i = 0; i < size; i++) map.put(messages.get(i), data.get(i));

        return new ResponseEntity<>(map, status);
    }
}