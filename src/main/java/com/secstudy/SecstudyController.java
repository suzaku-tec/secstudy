package com.secstudy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.sf.amateras.mirage.SqlManager;
import jp.sf.amateras.mirage.session.Session;
import jp.sf.amateras.mirage.session.SessionFactory;

@Controller
@RequestMapping(value = "secstudy")
public class SecstudyController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(SecstudyForm form) {
		return "secstudy/index";
	}

	/**
	 * SQLインジェクションを起こすために、わざとサニタイジングしない
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public String execute(SecstudyForm form, Model model)  {

		Session session = SessionFactory.getSession();
		SqlManager sqlManager = session.getSqlManager();
		 
		session.begin();
		try {
			System.out.println("name:" + form.getName());
			Map<String, Object> params = new HashMap<>();
			params.put("name", form.getName());
			List<Member> list = sqlManager.getResultListBySql(Member.class, "select id, name, comment from member where name = '" + form.getName() + "'");

			list.stream().forEach(x -> System.out.println(x));
			model.addAttribute("dtoList", list);

		} catch (Exception ex) {
		  session.rollback();
		  throw ex;
		} finally {
		  session.release();
		}

		return "secstudy/result";
	}

}
