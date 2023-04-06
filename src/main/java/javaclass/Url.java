package javaclass;

public class Url {
    private int idUrl;
    private int type;
    private String countryName;
    private int idWebsite;

    public Url(int idUrl, int type, String countryName, int idWebsite) {
        this.idUrl = idUrl;
        this.type = type;
        this.countryName = countryName;
        this.idWebsite = idWebsite;
    }

    public int getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(int idUrl) {
        this.idUrl = idUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getIdWebsite() {
        return idWebsite;
    }

    public void setIdWebsite(int idWebsite) {
        this.idWebsite = idWebsite;
    }

}
