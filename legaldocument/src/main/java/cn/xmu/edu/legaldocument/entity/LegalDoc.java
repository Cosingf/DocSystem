package cn.xmu.edu.legaldocument.entity;

public class LegalDoc {
    private Long id;

    private String path;

    private Byte isEnrich;

    private String author;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Byte getIsEnrich() {
        return isEnrich;
    }

    public void setIsEnrich(Byte isEnrich) {
        this.isEnrich = isEnrich;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}