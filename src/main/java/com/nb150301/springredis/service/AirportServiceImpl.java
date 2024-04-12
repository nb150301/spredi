package com.nb150301.springredis.service;

import com.nb150301.springredis.entity.AirportEntity;
import com.nb150301.springredis.repository.AirportRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AirportServiceImpl implements AirportService {
  private static final String AIRPORTS_KEY = "airports_key";
  private final AirportRepository airportRepository;
  private final HashOperations<Object, String, Object> hashOperations;

  @Autowired
  public AirportServiceImpl(
      AirportRepository airportRepository, RedisTemplate<Object, Object> redisTemplate) {
    this.airportRepository = airportRepository;
    this.hashOperations = redisTemplate.opsForHash();
  }

  @Override
  public AirportEntity getDetail(String airportCode) {
    Object airportFromCache = hashOperations.get(AIRPORTS_KEY, airportCode);
    if (airportFromCache != null) {
      log.info("Cache hit with key: " + airportCode);
      return (AirportEntity) airportFromCache;
    }

    log.info("Cache miss with key: " + airportCode);
    AirportEntity airport =
        airportRepository.findById(airportCode).orElseThrow(EntityNotFoundException::new);
    hashOperations.putIfAbsent(AIRPORTS_KEY, airportCode, airport);
    return airport;
  }

  @Override
  public List<AirportEntity> getListAirport(List<String> airportCodes) {
    List<Object> airportFromCache = hashOperations.multiGet(AIRPORTS_KEY, airportCodes);
    List<AirportEntity> cachedAirports =
        airportFromCache.stream().filter(Objects::nonNull).map(a -> (AirportEntity) a).toList();
    List<String> cachedAirportCodes =
        cachedAirports.stream().map(AirportEntity::getAirportCode).toList();
    log.info("Cache hit with keys: {}", cachedAirportCodes);
    List<String> nonCachedAirportCodes =
        airportCodes.stream()
            .map(String.class::cast)
            .filter(code -> !cachedAirportCodes.contains(code))
            .toList();
    if (nonCachedAirportCodes.isEmpty()) {
      return cachedAirports;
    }

    log.info("Cache miss with keys: {}", nonCachedAirportCodes);
    List<AirportEntity> airports = airportRepository.findAllById(nonCachedAirportCodes);
    Map<String, AirportEntity> mapAirportByCode =
        airports.stream()
            .collect(Collectors.toMap(AirportEntity::getAirportCode, Function.identity()));
    hashOperations.putAll(AIRPORTS_KEY, mapAirportByCode);
    airports.addAll(cachedAirports);
    return airports;
  }

  @Override
  public AirportEntity saveAirport(AirportEntity airport) {
    AirportEntity savedAirport = airportRepository.save(airport);
    hashOperations.put(AIRPORTS_KEY, savedAirport.getAirportCode(), airport);
    return savedAirport;
  }
}
