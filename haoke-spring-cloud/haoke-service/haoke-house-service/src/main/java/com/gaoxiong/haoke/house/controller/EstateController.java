package com.gaoxiong.haoke.house.controller;

import com.gaoxiong.haoke.entity.PageResult;
import com.gaoxiong.haoke.entity.Result;
import com.gaoxiong.haoke.entity.StatusCode;
import com.gaoxiong.haoke.house.pojo.Estate;
import com.gaoxiong.haoke.house.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/estate")
public class EstateController {

	@Autowired
	private EstateService estateService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",estateService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",estateService.findById(Long.valueOf(id)));
	}


	/**
	 * 分页+多条件查询
	 * @param estate 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Estate estate , @PathVariable int page, @PathVariable int size){
		Page<Estate> pageList = estateService.findSearch(estate, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Estate>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param estate
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Estate estate){
        return new Result(true,StatusCode.OK,"查询成功",estateService.findSearch(estate));
    }
	
	/**
	 * 增加
	 * @param estate
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Estate estate  ){
		estateService.add(estate);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param estate
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Estate estate, @PathVariable String id ){
		estate.setId(Long.valueOf(id));
		estateService.update(estate);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		estateService.deleteById(Long.valueOf(id));
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
