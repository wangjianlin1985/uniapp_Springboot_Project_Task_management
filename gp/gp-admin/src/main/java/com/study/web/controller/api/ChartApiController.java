package com.study.web.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.util.DateUtils;
import com.study.system.entity.SysDictData;
import com.study.system.entity.SysUser;
import com.study.system.service.SysDictDataService;
import com.study.task.entity.Project;
import com.study.task.entity.Projectuser;
import com.study.task.entity.Task;
import com.study.task.service.ProjectService;
import com.study.task.service.ProjectuserService;
import com.study.task.service.TaskService;

import cn.hutool.core.convert.Convert;


/**
 * 数据分析相关API 
 */
@RestController
@RequestMapping("openapi/chart")
@CrossOrigin
public class ChartApiController extends BaseController{
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectuserService projectuserService;
	@Autowired
	private SysDictDataService dictDataService;
	
	/**
	 * 概览柱图
	 * @return
	 */
	@RequestMapping("gl")
	public AjaxResult gl() {
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		
		List<Project> projectList = projectService.list(new Project());
		int countProject = projectList.size(); // 项目总数
		
		
		Projectuser pu = new Projectuser();
		pu.setUser(user);
		pu.setStatus(1);
		List<Projectuser> joinList = projectuserService.list(pu);
		int countProjectJoin = joinList.size();     // 我参加的项目
		
		
		Task task = new Task();
		task.setUser(user);
		List<Task> taskList = taskService.list(task); 
		int countTask = taskList.size();            // 我的任务总数
		
		List<Task> taskListTotal = taskService.list(new Task());
		int countTaskTotal = taskListTotal.size(); // 任务总数
		
		Map<String, Object> map = new HashMap<>();
		map.put("countProject", countProject);
		map.put("countProjectJoin", countProjectJoin);
		map.put("countTask", countTask);
		map.put("countTaskTotal", countTaskTotal);
		return AjaxResult.success(map);
	}


	/**
	 * 优先级饼图
	 * @return
	 */
	@RequestMapping("yxj")
	public AjaxResult yxj() {
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		Task task = new Task();
		task.setUser(user);
		List<Task> taskList = taskService.list(task); 
		// 优先级
		SysDictData sdd = new SysDictData();
		sdd.setDictType("task_yxj");
		List<SysDictData> list = dictDataService.listByType(sdd);
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String, Object> yxjMap = new HashMap<>();
		list.forEach(sd -> {
			Map<String, Object> map = new HashMap<>();
			map.put("value", sd.getDictValue());
			map.put("label", sd.getDictLabel());
			mapList.add(map);
			
			yxjMap.put(sd.getDictValue(), 0);
		});
		for(Task t : taskList) {
			Object object = yxjMap.get(t.getYxj());
			if (object == null) {
				yxjMap.put(t.getYxj(), 1);
			} else {
				yxjMap.put(t.getYxj(), Convert.toInt(object)+1);
			}
		}
		return AjaxResult.success(yxjMap);
	}
	
	
	/**
	 * 完成状态饼图
	 * @return
	 */
	@RequestMapping("status")
	public AjaxResult status() {
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		Task task = new Task();
		task.setUser(user);
		List<Task> taskList = taskService.list(task); 
		
		int statusTodo = 0; // 待办且未超时， 超时的算到了逾期里面
		int statusOk = 0;   // 正常完成
		int statusLate = 0; // 逾期或逾期完成
		int statusNo = 0;   // 不能完成
		
		// 0进行中  1正常完成  2超时完成 3 不能完成
		String now = DateUtils.getTime();
		for(Task t : taskList) {
			if (t.getStatus() == 3) {
				statusNo ++;
			} else if (t.getStatus() == 1) {
				statusOk ++;
			} else {
				boolean flag = t.getEndTime().compareTo(now) > 0; // 未超时
				if (t.getStatus() == 0 && flag) {
					statusTodo ++ ;
				} else {
					statusLate ++ ;
				}
			}
		}
		Map<String, Object> mmap = new HashMap<>();
		mmap.put("statusTodo", statusTodo);
		mmap.put("statusOk", statusOk);
		mmap.put("statusLate", statusLate);
		mmap.put("statusNo", statusNo);
		return AjaxResult.success(mmap);
	}
	
	
	
	
	
	
}
