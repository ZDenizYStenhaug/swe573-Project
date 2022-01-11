package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.TagRepo;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    private static final String WIKI_URL = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=";
    @Autowired
    private TagRepo tagRepo;

    public Tag saveTag(Tag tag) {
        return tagRepo.save(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }

    public Tag findByName(String name) {
        return tagRepo.findByName(name);
    }

    private String getJSONFromWikipedia(Tag tag) throws IOException {

        URL url = new URL(WIKI_URL + URLEncoder.encode(tag.getName(), "UTF-8"));
        // Open the Connection
        URLConnection conn = url.openConnection();
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        IOUtils.copy(conn.getInputStream(), output);
        //close connection
        output.close();
        return output.toString();
    }

    public String getMoreInformationAboutTag(Tag tag) throws IOException {
        String wikiOutput = getJSONFromWikipedia(tag);
        JSONObject json = new JSONObject(wikiOutput);
        JSONObject pages = json.getJSONObject("query").getJSONObject("pages");
        String key ;
        JSONObject value = new JSONObject();
        Iterator<String> keys = pages.keys();
        while(keys.hasNext()) {
            key = keys.next();
            value = pages.getJSONObject(key);
        }
        String extract = value.getString("extract");
        return extract;
    }

}
