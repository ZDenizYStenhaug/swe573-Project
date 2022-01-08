package edu.boun.yilmaz4.deniz.akitaBackend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import edu.boun.yilmaz4.deniz.akitaBackend.model.GeoLocation;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.GeoLocationRepo;
import org.json.JSONObject;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeoLocationImpService {

    @Autowired
    private GeoLocationRepo geoLocationRepo;

    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "INSERT API KEY";

    public String getJSONByGoogle(String address) throws IOException {
        /*
        his method is taken from http://burnignorance.com/iphone-development-tips/google-geocoding-api-v3-implementation-in-java/
        I added the api key.
         */

        URL url = new URL(URL + "?address=" + URLEncoder.encode(address, "UTF-8")+ "&key=" + API_KEY);

        // Open the Connection
        URLConnection conn = url.openConnection();
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        IOUtils.copy(conn.getInputStream(), output);
        output.close();
        return output.toString();
    }

    public double[] getGeoLocation(String address) throws IOException {
        String googleOutput = getJSONByGoogle(address);
        JSONObject json = new JSONObject(googleOutput);
        double[] geolocation = new double[2];
        double lng = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        double lat = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        geolocation[0] = lat;
        geolocation[1] = lng;
        return geolocation;
    }

    @Transactional
    public GeoLocation save(GeoLocation geoLocation){
        return geoLocationRepo.save(geoLocation);
    }

    public GeoLocation getGeolocation(String address) throws IOException {
        // 0: lat, 1: lng
        double[] values = getGeoLocation(address);
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLongitude(values[0]);
        geoLocation.setLongitude(values[1]);

        return save(geoLocation);
    }
}
