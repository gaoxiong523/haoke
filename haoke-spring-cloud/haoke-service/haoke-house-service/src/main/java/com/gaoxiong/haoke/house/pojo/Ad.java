package com.gaoxiong.haoke.house.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_ad")
public class Ad implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id", insertable = false, nullable = false)
  private Long id;

  /**
   * 广告类型
   */
  @Column(name = "type")
  private Integer type;

  /**
   * 描述
   */
  @Column(name = "title")
  private String title;

  /**
   * 图片URL地址
   */
  @Column(name = "url")
  private String url;

  @Column(name = "created")
  private Date created;

  @Column(name = "updated")
  private Date updated;

  
}