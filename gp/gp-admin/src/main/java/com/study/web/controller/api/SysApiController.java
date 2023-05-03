package com.study.web.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.text.Convert;
import com.study.common.util.AESUtil;
import com.study.framework.shiro.service.SysPasswordService;
import com.study.framework.util.ShiroUtils;
import com.study.system.dao.SysUserRoleDao;
import com.study.system.entity.SysUser;
import com.study.system.entity.SysUserRole;
import com.study.system.service.SysUserService;

/**
 * 登录，注册，个人中心等相关接口 
 */
@RestController
@RequestMapping("openapi/system")
@CrossOrigin
public class SysApiController extends BaseController{

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysPasswordService passwordService;
	@Autowired
	private SysUserRoleDao userRoleDao;
	
	// 登录
	@RequestMapping("loginNocode")
	public AjaxResult loginNocode(@RequestBody Map<String, Object> map) {
		String loginName = Convert.toStr(map.get("username"));
		String password = Convert.toStr(map.get("password"));
		SysUser user = new SysUser();
		user.setLoginName(loginName);
		SysUser result = userService.selectUserByLoginName(user);
		if (result == null) {
			return error("用户名不存在");
		}
		String encrPwd = passwordService.encryptPassword(result.getLoginName(), password, result.getSalt());
		if (!encrPwd.equals(result.getPassword())) {
			return error("密码错误");
		}
		return AjaxResult.success(result);
	}
	
	// 注册
	@RequestMapping("register")
	public AjaxResult register(@RequestBody SysUser user) {
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		user.setOldpwd(AESUtil.encrypt(user.getPassword()));
		user.setDeptId(213L);
		user.setUserType(1);
		user.setStatus("0");
		int r = userService.add(user);
		
		SysUserRole ur = new SysUserRole();
		ur.setUserId(user.getUserId());
		ur.setRoleId(116L);
		r += userRoleDao.add(ur);
		return toAjax(r);
	}
	
	// 个人信息查询
	@RequestMapping("userinfo")
	public AjaxResult userinfo() {
		SysUser user = userService.getById(getHeaderUserId());
		return AjaxResult.success(user);
	}
	
	// 更新手机号
	@RequestMapping("updatePhonenumber")
	public AjaxResult updatePhonenumber(@RequestBody SysUser user) {
		user.setUserId(getHeaderUserId());
		return toAjax(userService.updatePhonenumber(user));
	}
	
	// 更新email
	@RequestMapping("updateEmail")
	public AjaxResult updateEmail(@RequestBody SysUser user) {
		user.setUserId(getHeaderUserId());
		return toAjax(userService.updateEmail(user));
	}
	
	// 更新姓名
	@RequestMapping("updateUserName")
	public AjaxResult updateUserName(@RequestBody SysUser user) {
		user.setUserId(getHeaderUserId());
		return toAjax(userService.updateUserName(user));
	}
	
	
}
