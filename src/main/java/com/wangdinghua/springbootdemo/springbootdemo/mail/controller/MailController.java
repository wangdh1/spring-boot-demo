package com.wangdinghua.springbootdemo.springbootdemo.mail.controller;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import freemarker.template.Configuration;

@RestController
public class MailController {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@GetMapping("/sendMail")
	public void sendMail() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("576333708@qq.com");
		message.setTo("1562034719@qq.com");
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容~~~");
		mailSender.send(message);
	}
	
	@GetMapping("/sendMail1")
	public void sendMail1() {
		MimeMessage message = null;
	    try {
	        message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom("576333708@qq.com");
	        helper.setTo("1562034719@qq.com");
	        helper.setTo("xuchao@chinacloud.com.cn");
	        helper.setSubject("标题：发送Html内容");

	        StringBuffer sb = new StringBuffer();
	        sb.append("<h1>大标题-h1</h1>")
	          .append("<p style='color:#F00'>红色字</p>")
	          .append("<p style='text-align:right'>右对齐</p>");
	        helper.setText(sb.toString(), true);
	    	helper.addAttachment("附件.yml",  new ClassPathResource("application.yml"));
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }

	    mailSender.send(message);
	}
	
	/**
	 * 由代码可知，只需在想要内联的地方使用 cid:xx 引用内联附件，然后用 addInline(xx, file)指定附件即可。两处的 xx 必须一致。
	 */
	@GetMapping("/sendMail2")
	public void sendMail2() {
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("576333708@qq.com");
			helper.setTo("1562034719@qq.com");
			helper.setSubject("标题：发送Html内容");
			
			StringBuffer sb = new StringBuffer();
			sb.append("<h1>关注我</h1><h1><img src=\"cid:attach_wdh\"/></h1>");
			helper.setText(sb.toString(), true);
			helper.addInline("attach_wdh", new ClassPathResource("config/weixin.png"));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		mailSender.send(message);
	}
	
	/**
	 * freemarker 模板
	 * @throws Exception
	 */
	@GetMapping("/sendMail3")
	public void sendMail3() throws Exception {
		MimeMessage message = this.mailSender.createMimeMessage();
		// 第二个参数表示是否开启multipart模式
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
		messageHelper.setFrom("576333708@qq.com");
		messageHelper.setTo("1562034719@qq.com");
		messageHelper.setTo("383503946@qq.com");
		messageHelper.setSubject("蚂蚁你好~");
		Map<String, Object> model = new HashMap<>();
		model.put("username", "吴健");
		model.put("event", "吴健");
		
		
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
		configuration.setClassForTemplateLoading(this.getClass(), "/templates");
		
		String content = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("mail.ftl"), model);
		
		// 第二个参数表示是否html，设为true
		messageHelper.setText(content, true);
		this.mailSender.send(message);
	}
	
	/**
	 * volocity模板
	 * @throws Exception 
	 */
	@GetMapping("/sendMail4")
	public void sendMail4() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("576333708@qq.com");
		helper.setTo("1562034719@qq.com");
		helper.setSubject("主题：模板邮件");
		VelocityContext model = new VelocityContext();
		model.put("username", "hehe");
		StringWriter stringWriter = new StringWriter();
		velocityEngine.mergeTemplate("templates/template.vm", "UTF-8", model, stringWriter);
//		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template.vm", "UTF-8", model);
		helper.setText(stringWriter.toString(), true);
		mailSender.send(mimeMessage);
	}

}
