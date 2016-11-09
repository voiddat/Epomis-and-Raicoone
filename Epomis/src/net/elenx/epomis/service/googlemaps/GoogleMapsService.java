package net.elenx.epomis.service.googlemaps;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import lombok.Data;
import lombok.SneakyThrows;
import net.elenx.epomis.model.Place;
import net.elenx.epomis.model.Travel;
import net.elenx.epomis.service.MapsService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
class GoogleMapsService implements MapsService {

    private final GeoApiContext geoApiContext;

    @SneakyThrows
    @Override
    public Travel analyzeTravel(String origin, String destination) {
        DistanceMatrixApiRequest distanceMatrixApiRequest = new DistanceMatrixApiRequest(geoApiContext);
        distanceMatrixApiRequest.origins(origin).destinations(destination);
        DistanceMatrix distanceMatrix = distanceMatrixApiRequest.await();
        DistanceMatrixRow[] distanceMatrixRow = distanceMatrix.rows;
        DistanceMatrixElement distanceMatrixElement = distanceMatrixRow[0].elements[0];
        String distance = distanceMatrixElement.distance.toString();
        String duration = distanceMatrixElement.duration.toString();
        return new Travel(distance, duration);
    }

    @Override
    public Optional<Place> findAddress(String address) {
        throw new NotYetImplementedException();
    }
}
