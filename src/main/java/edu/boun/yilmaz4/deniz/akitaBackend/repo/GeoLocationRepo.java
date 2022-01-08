package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GeoLocationRepo extends JpaRepository<GeoLocation,Long> {
}
