package com.nb150301.springredis.service;



import com.nb150301.springredis.entity.AirportEntity;

import java.util.List;

public interface AirportService {
  AirportEntity getDetail(String airportCode);

  List<AirportEntity> getListAirport(List<Object> airportCodes);

  AirportEntity saveAirport(AirportEntity airport);
}
