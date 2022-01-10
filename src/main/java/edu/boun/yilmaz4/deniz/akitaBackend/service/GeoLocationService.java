package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.GeoLocation;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.GeoLocationRepo;
import org.apache.commons.io.IOUtils;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Service
public class GeoLocationService {

    @Autowired
    private GeoLocationRepo geoLocationRepo;

    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "AIzaSyCNRYFjOeKsdj020wVO1KimvBqLkS57-4o";

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

    public double[] getGeoLocation(String address) throws IOException, JSONException {
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

    public GeoLocation saveGeoLocation(String address) throws IOException, JSONException {
        // 0: lat, 1: lng
        double[] values = getGeoLocation(address);
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(values[0]);
        geoLocation.setLongitude(values[1]);
        return save(geoLocation);
    }


}
