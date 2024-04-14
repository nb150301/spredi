package com.nb150301.springredis.controller;

import com.nb150301.springredis.entity.AirportEntity;
import com.nb150301.springredis.service.AirportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/airports")
public class AirportController {
  private final AirportService airportService;

  @GetMapping("/{airportCode}")
  public ResponseEntity<AirportEntity> getDetail(@PathVariable String airportCode) {
    return ResponseEntity.ok(airportService.getDetail(airportCode));
  }

  @GetMapping()
  public ResponseEntity<List<AirportEntity>> getListAirport(
      @RequestParam List<String> airportCodes) {
    return ResponseEntity.ok(airportService.getListAirport(airportCodes));
  }

  @PostMapping("")
  public ResponseEntity<AirportEntity> saveAirport(@RequestBody AirportEntity airport) {
    return ResponseEntity.ok(airportService.saveAirport(airport));
  }
}
