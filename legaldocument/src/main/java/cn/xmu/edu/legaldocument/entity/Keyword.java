package cn.xmu.edu.legaldocument.entity;

public class Keyword {
    private Long id;
    private String keyword;
    private Long bookId;
    private Long pageId;
    private Long wikiCorpusId;
    private Integer isMatched;

    public Integer getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(Integer isMatched) {
        this.isMatched = isMatched;
    }

    public Long getWikiCorpusId() {
        return wikiCorpusId;
    }

    public void setWikiCorpusId(Long wikiCorpusId) {
        this.wikiCorpusId = wikiCorpusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }
}