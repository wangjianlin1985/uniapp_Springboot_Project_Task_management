package com.study.web.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.system.entity.SysUser;
import com.study.system.service.SysUserService;
import com.study.task.entity.Project;
import com.study.task.entity.Projecttemp;
import com.study.task.entity.Projectuser;
import com.study.task.service.ProjectService;
import com.study.task.service.ProjecttempService;
import com.study.task.service.ProjectuserService;

import cn.hutool.core.convert.Convert;

/**
 * 项目相关API 
 */
@RestController
@RequestMapping("openapi/task/project")
@CrossOrigin
public class ProjectApiController extends BaseController{

	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjecttempService projecttempService;
	@Autowired
	private ProjectuserService projectuserService;
	@Autowired
	private SysUserService userService;

	/****************************项目用户相关*********************************/
	
	// 项目的用户列表
	@RequestMapping("user/list/{projectId}")
	public TableDataInfo userList(@PathVariable Long projectId) {
		Project project = new Project();
		project.setId(projectId);
		Projectuser projectuser = new Projectuser();
		projectuser.setProject(project);
		List<Projectuser> list = projectuserService.list(projectuser);
		return getDataTable(list);
	}
	
	// 项目用户删除
	@RequestMapping("user/delete")
	public AjaxResult userDelete(@RequestBody Map<String, Object> map) {
		return toAjax(projectuserService.deleteByIds(Convert.toStr(map.get("ids"))));
	}
	
	// 项目用户邀请
	@RequestMapping("user/add")
	public AjaxResult userAdd(@RequestBody Projectuser pu) {
		SysUser searchUser = new SysUser();
		searchUser.setLoginName(pu.getSearchKey());
		SysUser user = userService.selectUserByLoginName(searchUser);
		if (user == null) {
			return error("用户不存在");
		}
		if (user.getUserId().equals(getHeaderUserId())) {
			return error("不能邀请自己");
		}
		pu.setStatus(0);
		pu.setUser(user);
		return toAjax(projectuserService.add(pu));
	}
	
	// 待邀请列表
	@RequestMapping("user/todoList")
	public TableDataInfo todoList() {
		List<Map<String, Object>> list = projectuserService.listTodo(getHeaderUserId());
		return getDataTable(list);
	}
	
	// 同意或不同意邀请
	@RequestMapping("user/updateStatus")
	public AjaxResult updateStatus(@RequestBody Projectuser pu) {
		return toAjax(projectuserService.update(pu));
	}
	
	
	// 我加入的正常项目列表
	@RequestMapping("user/listJoin")
	public TableDataInfo listJoin() {
		Projectuser pu = new Projectuser();
		pu.setStatus(1);
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		pu.setUser(user);
		List<Projectuser> puList = projectuserService.list(pu);
		List<Map<String, Object>> mapList = new ArrayList<>();
		for(Projectuser p : puList) {
			if (p.getProject() == null || p.getProject().getStatus() == 1) {
				continue;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", p.getProject().getId());
			map.put("name", p.getProject().getName());
			mapList.add(map);
		}
		return getDataTable(mapList);
	}
	
	/****************************项目相关*********************************/
	
	
	// 我创建的正常项目列表
	@RequestMapping("list")
	public TableDataInfo list() {
		Project project = new Project();
		SysUser duty = new SysUser();
		duty.setUserId(getHeaderUserId());
		project.setDuty(duty);
		project.setStatus(0);
		List<Project> list = projectService.list(project);
		return getDataTable(list);
	}
	
	
	// 我创建的回收项目列表
	@RequestMapping("listHuishou")
	public TableDataInfo listHuishou() {
		Project project = new Project();
		SysUser duty = new SysUser();
		duty.setUserId(getHeaderUserId());
		project.setDuty(duty);
		project.setStatus(1);
		List<Project> list = projectService.list(project);
		return getDataTable(list);
	}
	
	
	// 改变项目状态   回收/正常
	@RequestMapping("updateStatus")
	public AjaxResult updateStatus(@RequestBody Project project) {
		return toAjax(projectService.updateStatus(project));
	}
	
	
	// 项目删除
	@RequestMapping("delete")
	public AjaxResult delete(@RequestBody Map<String, Object> map) {
		Long[] idsLong = Convert.toLongArray(map.get("ids"));
		for(Long projectId : idsLong) {
			projectuserService.deleteByProjectId(projectId);
		}
		return toAjax(projectService.deleteByIds(Convert.toStr(map.get("ids"))));
	}
	
	// 加载所有的模板
	@RequestMapping("tempList")
	public TableDataInfo tempList(){
		return getDataTable(projecttempService.list(new Projecttemp()));
	}
	
	// 项目新增
	@RequestMapping("add")
	public AjaxResult add(@RequestBody Project project) {
		SysUser duty = new SysUser();
		duty.setUserId(getHeaderUserId());
		project.setDuty(duty);
		int r = projectService.add(project);
		if (r > 0) {
			Projectuser pu = new Projectuser();
			pu.setProject(project);
			pu.setUser(duty);
			pu.setStatus(1);
			projectuserService.add(pu);
		}
		return toAjax(r);
	}
	
	// 项目修改
	@RequestMapping("update")
	public AjaxResult update(@RequestBody Project project) {
		return toAjax(projectService.update(project));
	}
	
	// 项目通过ID查询单个
	@RequestMapping("get/{id}")
	public AjaxResult get(@PathVariable Long id) {
		return AjaxResult.success(projectService.getById(id));
	}
	
	
	
}
