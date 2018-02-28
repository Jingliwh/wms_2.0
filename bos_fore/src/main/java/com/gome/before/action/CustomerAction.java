package com.gome.before.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.gomi.before.util.MailUtils;

import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
	private static final long serialVersionUID = 6359071223841607974L;
	@Autowired
	@Qualifier("redisTemplate1")
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate  jmsTemplate;

	@Action(value = "customer_SendSms")
	public String SendSms() throws UnsupportedEncodingException {
		// 随机生成4位验证码
		String randomCode = RandomStringUtils.randomNumeric(4);
		System.out.println(randomCode);
		// 将验证码存入session
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
		// 编辑短信
		 final String message = "尊敬的用户您好，本次获取的验证码为：" + randomCode +
		 ",服务电话：4006184000";
				// 调用接口发送短信
				// String http = SmsUtils.sendSmsByHTTP(model.getTelephone(), message);
				// 假象测试----
				//		String http = "000/xxxx";
				//		if (http.startsWith("000")) {
				//			return NONE;
				//		} else {
				//			throw new RuntimeException();
				//		}
			jmsTemplate.send("bos_msg", new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage  mapMessage=session.createMapMessage();
					mapMessage.setString("telephone", model.getTelephone());
					mapMessage.setString("msg",message);
					return mapMessage;
				}
			});
			return NONE;
	}

	// 获取表单传递的验证码
	private String checkCode;

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Action(value = "customer_register", results = {
			@Result(name = "success", type = "redirect", location = "signup-success.html"),
			@Result(name = "input", type = "redirect", location = "signup.html") })
	public String register() {
		String checkCodesession = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(model.getTelephone());
		// 校验验证码
		if (checkCodesession == null || !checkCodesession.equals(checkCode)) {
			ServletActionContext.getRequest().getSession().setAttribute("msg", "验证码输入错误");
			System.out.println("验证码输入错误");
			return INPUT;
		}
		// 如果验证码正确，提交---利用webService实现
		WebClient.create("http://localhost:9001/crm_management/services/customerService/register")
				.type(MediaType.APPLICATION_JSON).post(model);
		// 1发送激活邮件绑定邮箱
		// 生成激活码
		String activeCode = RandomStringUtils.randomNumeric(32);
		// 保存激活码到redis服务器
		redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
		// 编辑邮件内容
		String content = "尊敬的用户您好，请于24小时内点击下面的连接地址绑定邮箱:</br><a href='" + MailUtils.activeUrl + "?telephone="
				+ model.getTelephone() + "&activeCode=" + activeCode + "'>点此绑定邮箱地址</a>";
		// 发送邮件
		MailUtils.sendMail("物流项目", content, model.getEmail());

		return SUCCESS;
	}

	// 获取url传递的激活码，利用属性驱动
	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	@Action(value = "customer_activeMail")
	public String activeMail() throws IOException {
		// 1.设置数据响应格式
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		// 获取redis中的激活码
		String redisActiveCode = redisTemplate.opsForValue().get(model.getTelephone());
		if (redisActiveCode == null || !redisActiveCode.equals(activeCode)) {
			// 超时或者激活码不同导致无效
			ServletActionContext.getResponse().getWriter().println("激活码失效，请重新登录获取");
		} else {
			// 激活码有效
			String getUrl = "http://localhost:9001/crm_management/services/customerService/findByTelephone/"
					+ model.getTelephone();
			Customer customer = WebClient.create(getUrl).accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if (customer.getType() == null || customer.getType() != 1) {
				// 客户邮箱未激活--更新type为1，激活邮箱
				String updateType = "http://localhost:9001/crm_management/services/customerService/updateType/"
						+ model.getTelephone();
				WebClient.create(updateType).get();
				ServletActionContext.getResponse().getWriter().println("邮箱激活成功");
			} else {
				// 已激活
				ServletActionContext.getResponse().getWriter().println("该邮箱已注册，请勿重复激活");
			}
			redisTemplate.delete(model.getTelephone());
		}
		return NONE;
	}
	
	@Action(value = "customer_login", results = {
			@Result(name = "success", type = "redirect", location = "index.html#/myhome"),
			@Result(name = "input", type = "redirect", location = "login.html") })
	public  String login(){
		String sql="http://localhost:9001/crm_management/services/customerService/login?"
				+ "telephone="+model.getTelephone()+"&password="+model.getPassword();
		Customer  customer=WebClient.create(sql).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if(customer==null){
			return  INPUT;
		}
		ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
		return SUCCESS;
	}
}
