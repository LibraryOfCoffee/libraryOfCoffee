package com.mametosho.presentation.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity

@RestController
class SampleController() {
  @GetMapping("/api/sample")
  fun sample(): ResponseEntity<String> {
    return ResponseEntity.ok("Sample response")
  }
}