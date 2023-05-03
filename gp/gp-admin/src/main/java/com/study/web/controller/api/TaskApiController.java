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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.common.config.Global;
import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.util.DateUtils;
import com.study.common.util.file.FileUploadUtils;
import com.study.system.entity.SysDictData;
import com.study.system.entity.SysUser;
import com.study.system.service.SysDictDataService;
import com.study.task.entity.Message;
import com.study.task.entity.Project;
import com.study.task.entity.Task;
import com.study.task.entity.Taskfile;
import com.study.task.service.MessageService;
import com.study.task.service.ProjectService;
import com.study.task.service.TaskService;
import com.study.task.service.TaskfileService;
import com.study.task.service.TempstepService;

import cn.hutool.core.convert.Convert;

/**
 * 任务和任务评论相关API 
 */
@RestController
@RequestMapping("openapi/task/task")
@CrossOrigin
public class TaskApiController extends BaseController{

	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskfileService taskfileService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TempstepService tempstepService;
	@Autowired
	private SysDictDataService dictDataService;
	@Autowired
	private MessageService messageService;
	
	/*******************任务相关***************************/
	
	// 我的任务列表
	@RequestMapping("mylist")
	public TableDataInfo mylist(@RequestBody Task task) {
		if (task == null) {
			task = new Task();
		}
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		task.setUser(user);
		List<Task> list = taskService.list(task);
		return getDataTable(list);
	}
	
	// 改变任务的状态
	@RequestMapping("updateStatus")
	public AjaxResult updateStatus(@RequestBody Task task) {
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
	
	
	// 删除任务
	@RequestMapping("delete")
	public AjaxResult delete(@RequestBody  Map<String, Object> map) {
		return toAjax(taskService.deleteByIds(Convert.toStr(map.get("ids"))));
	}
	
	
	// 根据项目projectId加载步骤list
	@RequestMapping("loadStepByProjectId/{projectId}")
	public TableDataInfo loadStepByProjectId(@PathVariable Long projectId) {
		Project project = projectService.getById(projectId);
		return getDataTable(tempstepService.listByTempId(project.getTemp().getId()));
	}
	
	// 加载优先级列表
	@RequestMapping("loadYxj")
	public TableDataInfo loadYxj(){
		SysDictData sdd = new SysDictData();
		sdd.setDictType("task_yxj");
		List<SysDictData> list = dictDataService.listByType(sdd);
		List<Map<String, Object>> mapList = new ArrayList<>();
		list.forEach(sd -> {
			Map<String, Object> map = new HashMap<>();
			map.put("value", sd.getDictValue());
			map.put("label", sd.getDictLabel());
			mapList.add(map);
		});
		return getDataTable(mapList);
	}
	
	// 文件上传
	@RequestMapping("uploadFile")
	public AjaxResult uploadFile(@RequestParam("files") MultipartFile[] files) {
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
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
	}
	
	// 文件删除
	@RequestMapping("file/delete")
	public AjaxResult filedelete(@RequestBody  Map<String, Object> map) {
		return toAjax(taskfileService.deleteById(Convert.toLong(map.get("ids"))));
	}
	
	
	// 新增任务
	@RequestMapping("add")
	public AjaxResult add(@RequestBody Task task) {
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		task.setUser(user);
		task.setEndTime(task.getEndTime() + " 23:59:59");
		int r = taskService.add(task);
		
		if (r > 0 && task.getFlist() != null && task.getFlist().size() > 0) {
			for(Taskfile tf: task.getFlist()) {
				tf.setTask(task);
				taskfileService.add(tf);
			}
		}
		
		return toAjax(r);
	}
	
	
	// 获取单个任务详情
	@RequestMapping("get/{id}")
	public AjaxResult get(@PathVariable Long id) {
		Task task = taskService.getById(id);
		task.setEndTime(task.getEndTime().substring(0,10));
		List<Taskfile> fileList = taskfileService.listByTaskId(id);
		task.setFlist(fileList);
		return AjaxResult.success(task);
	}
	
	// 修改任务
	@RequestMapping("update")
	public AjaxResult update(@RequestBody Task task) {
		task.setEndTime(task.getEndTime() + " 23:59:59");
		int r = taskService.update(task);
		if (task.getFlist() != null && task.getFlist().size() > 0) {
			for(Taskfile tf: task.getFlist()) {
				tf.setTask(task);
				taskfileService.add(tf);
			}
		}
		return toAjax(r);
	}
	
	// 根据项目ID查询任务
	@RequestMapping("listByProjectId/{projectId}")
	public TableDataInfo listByProjectId(@PathVariable Long projectId) {
		Project project = new Project();
		project.setId(projectId);
		Task task = new Task();
		task.setProject(project);
		List<Task> list = taskService.list(task);
		return getDataTable(list);
	}
	
	
	/*******************评论相关***************************/
	
	// 某个任务的评论列表
	@RequestMapping("message/list/{taskId}")
	public TableDataInfo messageList(@PathVariable Long taskId) {
		List<Message> list = messageService.listByTaskId(taskId);
		return getDataTable(list);
	}
	
	// 新增评论
	@RequestMapping("message/add")
	public AjaxResult messageAdd(@RequestBody Message message) {
		message.setTime(DateUtils.getTime());
		SysUser user = new SysUser();
		user.setUserId(getHeaderUserId());
		message.setUser(user);
		return toAjax(messageService.add(message));
	}
	
	
}
