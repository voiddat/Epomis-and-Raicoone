package net.elenx.epomis.service.googlemaps;

import com.google.maps.GeoApiContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MapsConfiguration {

    @Bean
    GeoApiContext geoApiContext() {
        return new GeoApiContext().setApiKey("AIzaSyBaZKqq-l2iQqSnwNo4-sVlUZAksowEwq");
    }
}
