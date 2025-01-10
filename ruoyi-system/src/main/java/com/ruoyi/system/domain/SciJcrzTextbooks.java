package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教材软著对象 sci_jcrz_textbooks
 *
 * @author zwh
 * @date 2024-11-14
 */
public class SciJcrzTextbooks extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 专著或教材名称 */
    @Excel(name = "专著或教材名称")
    private String title;

    /** ISBN */
    @Excel(name = "ISBN")
    private String isbn;

    /** 类别 */
    @Excel(name = "类别")
    private String category;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 出版社 */
    @Excel(name = "出版社")
    private String publisher;

    /** 出版时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出版时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishDate;

    /** 类别 */
    @Excel(name = "具体类别")
    private String classification;

    /** 作者排名 */
    @Excel(name = "作者排名")
    private String authorRank;

    /** 科研分 */
    @Excel(name = "科研分")
    private Long researchScore;

    /** 学校是否用书 */
    @Excel(name = "学校是否用书")
    private String isSchoolBook;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getIsbn()
    {
        return isbn;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
    }
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String getPublisher()
    {
        return publisher;
    }
    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public Date getPublishDate()
    {
        return publishDate;
    }
    public void setClassification(String classification)
    {
        this.classification = classification;
    }

    public String getClassification()
    {
        return classification;
    }
    public void setAuthorRank(String authorRank)
    {
        this.authorRank = authorRank;
    }

    public String getAuthorRank()
    {
        return authorRank;
    }
    public void setResearchScore(Long researchScore)
    {
        this.researchScore = researchScore;
    }

    public Long getResearchScore()
    {
        return researchScore;
    }
    public void setIsSchoolBook(String isSchoolBook)
    {
        this.isSchoolBook = isSchoolBook;
    }

    public String getIsSchoolBook()
    {
        return isSchoolBook;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("title", getTitle())
                .append("isbn", getIsbn())
                .append("category", getCategory())
                .append("remarks", getRemarks())
                .append("publisher", getPublisher())
                .append("publishDate", getPublishDate())
                .append("classification", getClassification())
                .append("authorRank", getAuthorRank())
                .append("researchScore", getResearchScore())
                .append("isSchoolBook", getIsSchoolBook())
                .toString();
    }
}
