package net.elenx.epomis.service;

import net.elenx.epomis.model.Place;
import net.elenx.epomis.model.Travel;

import java.util.Optional;

public interface MapsService {

    Travel analyzeTravel(String origin, String destination);

    Optional<Place> findAddress(String address);
}
