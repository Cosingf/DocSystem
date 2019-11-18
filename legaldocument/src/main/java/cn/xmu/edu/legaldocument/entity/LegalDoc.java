package cn.xmu.edu.legaldocument.entity;

public class LegalDoc {
    private Long id;

    private String path;

    private int isEnrich;

    private String author;

    private String name;

    private int isPublic;

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

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

    public int getIsEnrich() {
        return isEnrich;
    }

    public void setIsEnrich(int isEnrich) {
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