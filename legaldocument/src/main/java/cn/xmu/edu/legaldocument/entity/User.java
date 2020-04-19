package cn.xmu.edu.legaldocument.entity;

public class User {
    private Long id;

    private String account;

    private String password;

    private String salt;

    private String email;

    public User() {
    }

    public User(String account) {
        this.account = account;
        this.password = "";
        this.salt = "";
        this.email= "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}