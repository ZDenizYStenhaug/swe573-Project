package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepo tagRepo;

    public Tag saveTag(Tag tag) {
        return tagRepo.save(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }

    public String checkIfTagExitst(Tag newTag) {
        String message = "";
        for (Tag tag : tagRepo.findAll()) {
            if (tag.getName().equals(newTag.getName()))
                return "The tag named " + tag.getName() + " already exists";
        }
        return message;
    }
}
