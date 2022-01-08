package edu.boun.yilmaz4.deniz.akitaBackend.model;

import java.util.Set;

public class SearchResponse {

    private Set<Tag> selectedTags;

    public Set<Tag> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(Set<Tag> selectedTags) {
        this.selectedTags = selectedTags;
    }
}
