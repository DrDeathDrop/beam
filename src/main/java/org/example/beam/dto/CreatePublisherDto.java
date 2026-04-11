package org.example.beam.dto;

public class CreatePublisherDto {
    private Long id;
    private String name;
    private String country;
    private String yearsOfEstablishment;
    private String website;
    private String founded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getYearsOfEstablishment() {
        return yearsOfEstablishment;
    }

    public void setYearsOfEstablishment(String yearsOfEstablishment) {
        this.yearsOfEstablishment = yearsOfEstablishment;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }
}
