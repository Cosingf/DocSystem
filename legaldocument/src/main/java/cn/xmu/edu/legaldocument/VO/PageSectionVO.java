package cn.xmu.edu.legaldocument.VO;

import java.util.List;

public class PageSectionVO {
    private Long pageId;
    private Integer pageNum;
    private List<QASectionVO> qaSectionVOS ;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<QASectionVO> getQaSectionVOS() {
        return qaSectionVOS;
    }

    public void setQaSectionVOS(List<QASectionVO> qaSectionVOS) {
        this.qaSectionVOS = qaSectionVOS;
    }
}
