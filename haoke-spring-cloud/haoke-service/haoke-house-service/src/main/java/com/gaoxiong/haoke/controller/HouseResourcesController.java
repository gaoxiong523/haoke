package com.gaoxiong.haoke.controller;

import com.gaoxiong.haoke.entity.PageResult;
import com.gaoxiong.haoke.entity.Result;
import com.gaoxiong.haoke.entity.StatusCode;
import com.gaoxiong.haoke.pojo.HouseResources;
import com.gaoxiong.haoke.service.HouseResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/houseResources")
public class HouseResourcesController {

	@Autowired
	private HouseResourcesService houseResourcesService;


	/**
	 * 查询全部数据
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result findAll ( @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size ) {
		Page<HouseResources> allPage = houseResourcesService.findAllPage(page, size);
//		List<HouseResources> content = allPage.getContent();
//		long totalElements = allPage.getTotalElements();
//		int number = allPage.getNumber();
//		int size1 = allPage.getSize();
		PageInfo pageInfo = PageInfo.pageInfo(allPage);
		return new Result(true, StatusCode.OK, "查询成功",pageInfo);
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功", houseResourcesService.findById(Long.valueOf(id)));
	}


	/**
	 * 分页+多条件查询
	 * @param houseResources 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody HouseResources houseResources , @PathVariable int page, @PathVariable int size){
		Page<HouseResources> pageList = houseResourcesService.findSearch(houseResources, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<HouseResources>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param houseResources
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody HouseResources houseResources){
        return new Result(true,StatusCode.OK,"查询成功", houseResourcesService.findSearch(houseResources));
    }
	
	/**
	 * 增加
	 * @param houseResources
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody HouseResources houseResources  ){
		houseResourcesService.add(houseResources);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param houseResources
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody HouseResources houseResources, @PathVariable String id ){
		houseResources.setId(Long.valueOf(id));
		houseResourcesService.update(houseResources);
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		houseResourcesService.deleteById(Long.valueOf(id));
		return new Result(true,StatusCode.OK,"删除成功");
	}

}
