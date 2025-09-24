package com.example.awslabs.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.awslabs.dto.CreateTopicRequestDto;
import com.example.awslabs.dto.PublishRequestDto;
import com.example.awslabs.dto.SubscribeRequestDto;
import com.example.awslabs.service.SnsService;

@Controller
public class SnsController 
{
    private final SnsService snsService;

    public SnsController(SnsService snsService) {
        this.snsService = snsService;
    }

    @PostMapping("/topic")
    public ResponseEntity<?> createTopic(@Validated @RequestBody CreateTopicRequestDto dto) {
        return new ResponseEntity<>(snsService.createTopic(dto), HttpStatus.CREATED);
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publish(@Validated @RequestBody PublishRequestDto dto) {
        return new ResponseEntity<>(snsService.publishMessage(dto), HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@Validated @RequestBody SubscribeRequestDto dto) {
        return new ResponseEntity<>(snsService.subscribe(dto), HttpStatus.OK);
    }
}
