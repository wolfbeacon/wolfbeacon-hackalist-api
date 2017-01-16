package com.wolfbeacon.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Blob;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "hackalist_hackathons")
public class Hackathon {

    public Hackathon() {

    }

    public Hackathon(Long id, String title, String eventLink, Date startDate, Date endDate, Date lastUpdatedDate, Integer year, String location, String host, Integer length, String size, Boolean travel, Boolean prize, Boolean highSchoolers, String cost, String facebookLink, String twitterLink, String googlePlusLink, String imageLink, Double latitude, Double longitude, String notes) {
        this.id = id;
        this.title = title;
        this.eventLink = eventLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.year = year;
        this.location = location;
        this.host = host;
        this.length = length;
        this.size = size;
        this.travel = travel;
        this.prize = prize;
        this.highSchoolers = highSchoolers;
        this.cost = cost;
        this.facebookLink = facebookLink;
        this.twitterLink = twitterLink;
        this.googlePlusLink = googlePlusLink;
        this.imageLink = imageLink;
        this.latitude = latitude;
        this.longitude = longitude;
        try {
            this.notes.setBytes(1, notes.getBytes());
        } catch (Exception e) {

        }
    }

    @Id
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "event_link", nullable = false)
    private String eventLink;
    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    @Column(name = "last_updated_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date lastUpdatedDate;
    @Column(name = "year", nullable = false)
    private Integer year;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "host", nullable = false)
    private String host;
    @Column(name = "length")
    private Integer length;
    @Column(name = "size")
    private String size;
    @Column(name = "travel")
    private Boolean travel;
    @Column(name = "prize")
    private Boolean prize;
    @Column(name = "high_schoolers")
    private Boolean highSchoolers;
    @Column(name = "cost")
    private String cost;
    @Column(name = "facebook_link")
    private String facebookLink;
    @Column(name = "twitter_link")
    private String twitterLink;
    @Column(name = "gplus_link")
    private String googlePlusLink;
    @Column(name = "image_link")
    private String imageLink;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "notes", length = 1000)
    private Blob notes;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hackathon hackathon = (Hackathon) o;
        return Objects.equals(id, hackathon.id) &&
                Objects.equals(title, hackathon.title) &&
                Objects.equals(eventLink, hackathon.eventLink) &&
                Objects.equals(startDate, hackathon.startDate) &&
                Objects.equals(endDate, hackathon.endDate) &&
                Objects.equals(lastUpdatedDate, hackathon.lastUpdatedDate) &&
                Objects.equals(year, hackathon.year) &&
                Objects.equals(location, hackathon.location) &&
                Objects.equals(host, hackathon.host) &&
                Objects.equals(length, hackathon.length) &&
                Objects.equals(size, hackathon.size) &&
                Objects.equals(travel, hackathon.travel) &&
                Objects.equals(prize, hackathon.prize) &&
                Objects.equals(highSchoolers, hackathon.highSchoolers) &&
                Objects.equals(cost, hackathon.cost) &&
                Objects.equals(facebookLink, hackathon.facebookLink) &&
                Objects.equals(twitterLink, hackathon.twitterLink) &&
                Objects.equals(googlePlusLink, hackathon.googlePlusLink) &&
                Objects.equals(imageLink, hackathon.imageLink) &&
                Objects.equals(latitude, hackathon.latitude) &&
                Objects.equals(longitude, hackathon.longitude) &&
                Objects.equals(notes, hackathon.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, eventLink, startDate, endDate, lastUpdatedDate, year, location, host, length, size, travel, prize, highSchoolers, cost, facebookLink, twitterLink, googlePlusLink, imageLink, latitude, longitude, notes);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getTravel() {
        return travel;
    }

    public void setTravel(Boolean travel) {
        this.travel = travel;
    }

    public Boolean getPrize() {
        return prize;
    }

    public void setPrize(Boolean prize) {
        this.prize = prize;
    }

    public Boolean getHighSchoolers() {
        return highSchoolers;
    }

    public void setHighSchoolers(Boolean highSchoolers) {
        this.highSchoolers = highSchoolers;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getGooglePlusLink() {
        return googlePlusLink;
    }

    public void setGooglePlusLink(String googlePlusLink) {
        this.googlePlusLink = googlePlusLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Blob getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        try {
            this.notes.setBytes(1, notes.getBytes());
        } catch (Exception e) {

        }
    }
}
