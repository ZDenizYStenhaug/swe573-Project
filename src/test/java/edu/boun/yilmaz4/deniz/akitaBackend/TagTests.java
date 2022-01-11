package edu.boun.yilmaz4.deniz.akitaBackend;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.TagRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class TagTests {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepo tagRepo;

    @Test
    void getInformationForTag() throws IOException {
        // create new tag
        Tag tag = createTag();
        Assertions.assertNotNull(tagRepo.findByName(tag.getName()));
        String actual = tagService.getMoreInformationAboutTag(tag);
        String expected = "Juggling is a physical skill, performed by a juggler, involving the manipulation of objects for recreation, entertainment, art or sport. The most recognizable form of juggling is toss juggling. Juggling can be the manipulation of one object or many objects at the same time, most often using one or two hands but also possible with feet. Jugglers often refer to the objects they juggle as props. The most common props are balls, clubs, or rings. Some jugglers use more dramatic objects such as knives, fire torches or chainsaws. The term juggling can also commonly refer to other prop-based manipulation skills, such as diabolo, plate spinning, devil sticks, poi, cigar boxes, contact juggling, hooping, yo-yo, and hat manipulation.";
        Assertions.assertEquals(expected, actual);
        // delete tag
        deleteTag(tag);
        Assertions.assertNull(tagRepo.findByName(tag.getName()));
    }

    Tag createTag() {
        Tag tag  = new Tag();
        tag.setName("juggling");
        return tagService.saveTag(tag);
    }

    void deleteTag(Tag tag) {
        tagRepo.deleteById(tag.getId());
    }



}
