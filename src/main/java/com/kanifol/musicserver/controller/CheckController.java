package com.kanifol.musicserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "check")
public class CheckController {

   @GetMapping
   public ResponseEntity<String> check() {
       return ResponseEntity.ok("OK");
   }
}
