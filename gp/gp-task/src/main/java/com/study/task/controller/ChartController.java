package com.study.task.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.common.core.base.BaseController;
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
 * 后台数据分析 
 */
@Controller
@RequestMapping("/task/chart")
public class ChartController extends BaseController{
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectuserService projectuserService;
	@Autowired
	private SysDictDataService dictDataService;
	
	@RequestMapping("index")
	public String index(ModelMap mmap) {
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		
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
		
		// 状态百分比，
		int statusTodo = 0; // 待办且未超时， 超时的算到了逾期里面
		int statusOk = 0;   // 正常完成
		int statusLate = 0; // 逾期或逾期完成
		int statusNo = 0;   // 不能完成
		
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
			
			Object object = yxjMap.get(t.getYxj());
			if (object == null) {
				yxjMap.put(t.getYxj(), 1);
			} else {
				yxjMap.put(t.getYxj(), Convert.toInt(object)+1);
			}
		}
		
		
		mmap.addAttribute("countTask", countTask);
		mmap.addAttribute("countTaskTotal", countTaskTotal);
		mmap.addAttribute("countProject", countProject);
		mmap.addAttribute("countProjectJoin", countProjectJoin);
		mmap.addAttribute("yxjMap", yxjMap);
		mmap.addAttribute("mapList", mapList);
		mmap.addAttribute("statusTodo", statusTodo);
		mmap.addAttribute("statusOk", statusOk);
		mmap.addAttribute("statusLate", statusLate);
		mmap.addAttribute("statusNo", statusNo);
		return "task/chart/chart";
		
	}
	
	

}
