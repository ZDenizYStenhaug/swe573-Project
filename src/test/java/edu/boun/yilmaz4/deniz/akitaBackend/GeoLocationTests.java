package edu.boun.yilmaz4.deniz.akitaBackend;

import edu.boun.yilmaz4.deniz.akitaBackend.service.GeoLocationService;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class GeoLocationTests {

    @Autowired
    private GeoLocationService geoLocationService;

    @Test
    void getGeoLocationTest() throws IOException, JSONException {
        String googleOutput = geoLocationService.getJSONByGoogle("caddebostan");
        double[] geolocation =  geoLocationService.getGeoLocation(googleOutput);
        Assertions.assertEquals(40.9679267, geolocation[0]);
        Assertions.assertEquals(29.0619934, geolocation[1]);

    }
}
