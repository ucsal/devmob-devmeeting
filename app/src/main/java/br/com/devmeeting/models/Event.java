package br.com.devmeeting.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

@Entity(indexes = {
        @Index(value = "title, date", unique = true)
})
public class Event {

    @Id
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Date date;

    @NotNull
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private String website;

    @Generated(hash = 1249040578)
    public Event(Long id, @NotNull String title, @NotNull Date date, @NotNull String address,
            @NotNull Double latitude, @NotNull Double longitude, @NotNull String website) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.website = website;
    }

    @Generated(hash = 344677835)
    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @NotNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NotNull Date date) {
        this.date = date;
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    @NotNull
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull Double latitude) {
        this.latitude = latitude;
    }

    @NotNull
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull Double longitude) {
        this.longitude = longitude;
    }

    @NotNull
    public String getWebsite() {
        return website;
    }

    public void setWebsite(@NotNull String website) {
        this.website = website;
    }
}
