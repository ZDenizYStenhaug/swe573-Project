package edu.boun.yilmaz4.deniz.akitaBackend.model;

import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Badge;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @Transient
    private String passwordConfirm;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(nullable = false, columnDefinition = "varchar(8) default 'USER'")
    private Role role;

    @Column(nullable = false)
    private int credit;

    @Column(nullable = false)
    private int reputationPoints;

    @Column(nullable = false)
    private Badge badge;

    private String photo;

    @ManyToMany
    @JoinTable(name = "interests",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> interests;

    @ManyToMany
    @JoinTable(name = "talents",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> talents;

    @OneToMany(mappedBy = "offerer")
    private Set<Offer> offers = new HashSet<>();

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String firstName) {
        this.username = firstName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/user-photos/" + id + "/" + photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public Set<Tag> getInterests() {
        return interests;
    }

    public void setInterests(Set<Tag> interests) {
        this.interests = interests;
    }

    public Set<Tag> getTalents() {
        return talents;
    }

    public void setTalents(Set<Tag> talents) {
        this.talents = talents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getReputationPoints() {
        return reputationPoints;
    }

    public void setReputationPoints(int reputationPoints) {
        this.reputationPoints = reputationPoints;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

//    public Set<Offer> getOffers() {
//        return offers;
//    }
}
