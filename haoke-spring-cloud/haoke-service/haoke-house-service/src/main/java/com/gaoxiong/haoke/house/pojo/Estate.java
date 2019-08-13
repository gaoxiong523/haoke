package com.gaoxiong.haoke.house.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_estate")
@Data
public class Estate implements Serializable{

	@Id
	private Long id;//


	
	private String name;//楼盘名称
	private String province;//所在省
	private String city;//所在市
	private String area;//所在区
	private String address;//具体地址
	private String year;//建筑年代
	private String type;//建筑类型
	private String property_cost;//物业费
	private String property_company;//物业公司
	private String developers;//开发商
	private java.util.Date created;//创建时间
	private java.util.Date updated;//更新时间



	
}
