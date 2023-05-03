package com.study.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.common.util.DateUtils;
import com.study.system.entity.SysUser;
import com.study.task.entity.Message;
import com.study.task.service.MessageService;

/**
 * 后台任务评论
 */
@Controller
@RequestMapping("/task/message")
public class MessageController extends BaseController{

	private static String prefix = "task/message";
    
    @Autowired
	private MessageService messageService;

    
	@GetMapping("index/{taskId}")
	public String temp(@PathVariable Long taskId,ModelMap mmap) {
		mmap.addAttribute("taskId", taskId);
		return prefix + "/message";
	}

	@RequestMapping("list/{taskId}")
	@ResponseBody
	public TableDataInfo list(@PathVariable Long taskId) {
		startPage();
		List<Message> list = messageService.listByTaskId(taskId);
		return getDataTable(list);
	}
	

	/**
	 * 新增信息
	 */
	@GetMapping("/add/{taskId}")
	public String add(@PathVariable Long taskId,ModelMap mmap) {
		mmap.addAttribute("taskId", taskId);
		return prefix + "/add";
	}
	

	/**
	 * 新增保存
	 * @param temp
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Message message) {
		message.setTime(DateUtils.getTime());
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		message.setUser(user);
		return toAjax(messageService.add(message));
	}
	
	

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(messageService.deleteByIds(ids));
	}
	
	
}
