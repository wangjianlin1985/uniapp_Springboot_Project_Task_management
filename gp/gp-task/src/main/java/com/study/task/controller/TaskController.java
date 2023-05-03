package com.study.task.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.study.common.config.Global;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.util.DateUtils;
import com.study.common.util.StringUtils;
import com.study.common.util.file.FileUploadUtils;
import com.study.system.entity.SysUser;
import com.study.task.entity.Project;
import com.study.task.entity.Projectuser;
import com.study.task.entity.Task;
import com.study.task.entity.Taskfile;
import com.study.task.entity.Tasklabel;
import com.study.task.entity.Tempstep;
import com.study.task.service.ProjectService;
import com.study.task.service.ProjectuserService;
import com.study.task.service.TaskService;
import com.study.task.service.TaskfileService;
import com.study.task.service.TasklabelService;
import com.study.task.service.TempstepService;

@Controller
@RequestMapping("/task/task")
public class TaskController extends BaseController {
	
	private static String prefix = "task/task";

	@Autowired
	private TaskService taskService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectuserService projectuserService;
	@Autowired
	private TasklabelService tasklabelService;
	@Autowired
	private TempstepService tempstepService;
	@Autowired
	private TaskfileService taskfileService;

	/**
	 * 我参与的
	 * @param mmap
	 * @return
	 */
	@GetMapping("taskUser")
	public String task(ModelMap mmap) {
		Projectuser pu = new Projectuser();
		pu.setStatus(1);
		SysUser user = new SysUser();
		user.setUserId(getUserId());
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
		mmap.addAttribute("projectList", mapList);
		return prefix + "/taskUser";
	}
	
	
	/**
	 * 我参与的任务列表
	 * @param task
	 * @return
	 */
	@RequestMapping("taskUserList")
	@ResponseBody
	public TableDataInfo list(Task task) {
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		task.setUser(user);
		startPage();
		List<Task> list = taskService.list(task);
		return getDataTable(list);
	}
	
	
	/**
	 * 新增信息
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		
		Projectuser pu = new Projectuser();
		pu.setStatus(1);
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		pu.setUser(user);
		List<Projectuser> puList = projectuserService.list(pu);
		List<Map<String, Object>> mapList = new ArrayList<>();
		for(Projectuser p : puList) {
			if (p.getProject().getStatus() == 1) {
				continue;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", p.getProject().getId());
			map.put("name", p.getProject().getName());
			mapList.add(map);
		}
		mmap.addAttribute("projectList", mapList);
		mmap.addAttribute("labelList", tasklabelService.list(new Tasklabel()));
		
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param label
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	@Transactional
	public AjaxResult addSave(@Validated Task task) {
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		task.setUser(user);
		int r = taskService.add(task);
		
		String fl = task.getFileList();
		if (StringUtils.isNotEmpty(fl)) {
			JSONArray array = JSONArray.parseArray(fl);
			for(int i=0;i<array.size();i++) {
				JSONObject object = array.getJSONObject(i);
				String path = object.getString("path");
				String name = object.getString("name");
				Taskfile tf = new Taskfile();
				tf.setName(name);
				tf.setPath(path);
				tf.setTask(task);
				taskfileService.add(tf);
			}
		}
		
		return toAjax(r);
	}

	
	/**
	 * 上传文件
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@PostMapping("uploadFile")
	@ResponseBody
	public AjaxResult uploadFile(@RequestParam("files") MultipartFile[] files) throws Exception {
		try {
			// 上传文件路径
			String filePath = Global.getUploadPath();
			List<Map<String, Object>> list = new ArrayList<>();
			for(MultipartFile file : files) {
				String path = FileUploadUtils.upload(filePath, file);
				Map<String, Object> map = new HashMap<>();
				map.put("path", path);
				map.put("name", file.getOriginalFilename());
				list.add(map);
			}
			return AjaxResult.success(list);
		} catch (Exception e) {
			return AjaxResult.error(e.getMessage());
		}
	}
	
	
	/**
	 * 删除文件
	 * @return
	 */
	@PostMapping("deleteFile")
	@ResponseBody
	public AjaxResult deleteFile(Long id){
		taskfileService.deleteById(id);
		return success();
	}
	
	
	/**
	 * 修改
	 * 
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Task task = taskService.getById(id);
		mmap.addAttribute("task", task);
		mmap.addAttribute("fileList", taskfileService.listByTaskId(id));
		mmap.addAttribute("tempstepList", listStepByProjectId(task.getProject().getId()));
		mmap.addAttribute("labelList", tasklabelService.list(new Tasklabel()));
		return prefix + "/edit";
	}
	
	

	/**
	 * 详情
	 * @return
	 */
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") Long id, ModelMap mmap) {
		Task task = taskService.getById(id);
		mmap.addAttribute("task", task);
		mmap.addAttribute("fileList", taskfileService.listByTaskId(id));
		return prefix + "/view";
	}
	
	
	/**
	 * 根据项目ID加载步骤
	 * @param projectId
	 * @return
	 */
	@RequestMapping("loadStepByProjectId/{projectId}")
	@ResponseBody
	public List<Tempstep> loadStepByProjectId(@PathVariable Long projectId) {
		Project project = projectService.getById(projectId);
		return tempstepService.listByTempId(project.getTemp().getId());
	}
	
	
	// 根据项目ID查询他所属的模板，再查询他的步骤
	public List<Tempstep> listStepByProjectId(Long projectId){
		Project project = projectService.getById(projectId);
		return tempstepService.listByTempId(project.getTemp().getId());
	}
	
	/**
	 * 修改保存
	 * @param label
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Task task) {
		int r = taskService.update(task);
		String fl = task.getFileList();
		if (StringUtils.isNotEmpty(fl)) {
			JSONArray array = JSONArray.parseArray(fl);
			for(int i=0;i<array.size();i++) {
				JSONObject object = array.getJSONObject(i);
				String path = object.getString("path");
				String name = object.getString("name");
				Taskfile tf = new Taskfile();
				tf.setName(name);
				tf.setPath(path);
				tf.setTask(task);
				taskfileService.add(tf);
			}
		}
		return toAjax(r);
	}
	
	/**
	 * 更改完成状态
	 * @param task
	 * @return
	 */
	@PostMapping("updateStatus")
	@ResponseBody
	public AjaxResult updateStatus(@Validated Task task) {
		Integer status = task.getStatus();   // 0进行中  1正常完成  2超时完成 3 不能完成  4 完成
		if (status == 4) {
			Task old = taskService.getById(task.getId());
			String endTime = old.getEndTime();
			String now = DateUtils.getTime();
			task.setFinishTime(now);
			if (now.compareTo(endTime) >= 0) {
				task.setStatus(2);
			} else {
				task.setStatus(1);
			}
		}
		return toAjax(taskService.updateStatus(task));
	}
	

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(taskService.deleteByIds(ids));
	}

	
	
	
	/**
	 * 我负责的的
	 * @param mmap
	 * @return
	 */
	@GetMapping("taskDuty")
	public String taskDuty(ModelMap mmap) {
		Project project = new Project();
		SysUser duty = new SysUser();
		duty.setUserId(getUserId());
		project.setDuty(duty);
		project.setStatus(0);
		List<Project> list = projectService.list(project);
		// 为空的话加入一个不存在的ID数据这样就查不出来了，
		if (list == null || list.size() == 0) {
			Project p = new Project();
			p.setId(1L);
			p.setName("暂无任务");
			list.add(p);
		}
		
		mmap.addAttribute("projectList", list);
		return prefix + "/taskDuty";
	}
	
	
	/**
	 * 我是管理员
	 * @param mmap
	 * @return
	 */
	@GetMapping("taskAdmin")
	public String taskAdmin(ModelMap mmap) {
		Project project = new Project();
		project.setStatus(0);
		List<Project> list = projectService.list(project);
		mmap.addAttribute("projectList", list);
		return prefix + "/taskAdmin";
	}
	
	/**
	 * 我负责的/我是管理员 任务列表
	 * @param task
	 * @return
	 */
	@RequestMapping("taskList")
	@ResponseBody
	public TableDataInfo taskList(Task task) {
		startPage();
		List<Task> list = taskService.list(task);
		return getDataTable(list);
	}
	
	
}
