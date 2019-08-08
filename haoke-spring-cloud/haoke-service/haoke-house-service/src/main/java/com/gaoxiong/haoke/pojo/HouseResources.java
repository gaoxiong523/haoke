package com.gaoxiong.haoke.pojo;

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
@Table(name="tb_house_resources")
@Data
public class HouseResources implements Serializable{

	@Id
	private Long id;//


	
	private String title;//房源标题
	private Long estate_id;//楼盘id
	private String building_num;//楼号（栋）
	private String building_unit;//单元号
	private String building_floor_num;//门牌号
	private Integer rent;//租金
	private Integer rent_method;//租赁方式，1-整租，2-合租
	private Integer payment_method;//支付方式，1-付一押一，2-付三押一，3-付六押一，4-年付押一，5-其它
	private String house_type;//户型，如：2室1厅1卫
	private String covered_area;//建筑面积
	private String use_area;//使用面积
	private String floor;//楼层，如：8/26
	private String orientation;//朝向：东、南、西、北
	private Integer decoration;//装修，1-精装，2-简装，3-毛坯
	private String facilities;//配套设施， 如：1,2,3
	private String pic;//图片，最多5张
	private String house_desc;//描述
	private String contact;//联系人
	private String mobile;//手机号
	private Integer time;//看房时间，1-上午，2-中午，3-下午，4-晚上，5-全天
	private String property_cost;//物业费
	private java.util.Date created;//
	private java.util.Date updated;//

	

	
}
