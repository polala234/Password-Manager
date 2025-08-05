public class Password {
    //password paramteres
    private String name;
    private String password;
    private String category;
    private String login;
    private String website;
    private String location;

    public Password(String name, String password, String category, String login, String website, String location) {
        this.name = name;
        this.password = password;
        this.category = category;
        this.login = login;
        this.website = website;
        this.location = location;
    }

    //getters
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getCategory() { return category; }
    public String getLogin() { return login; }
    public String getWebsite() { return website; }
    public String getLocation() { return location; }
}
