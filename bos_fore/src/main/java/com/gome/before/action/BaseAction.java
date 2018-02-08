package com.gome.before.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	private static final long serialVersionUID = 1118736024906867202L;
	protected T model;

	protected int page;
	protected int rows = 1;
	protected Pageable pageable = new PageRequest(page, rows);

	public void setPage(int page) {
		this.page = page;
		this.pageable = new PageRequest(page - 1, rows);
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.pageable = new PageRequest(page - 1, rows);
	}

	public BaseAction() {
		// 构造
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		// 获取真实的类型BaseAction<具体类型>
		@SuppressWarnings("unchecked")
		Class<T> Actualtype = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			// 反射----初始化构造对象
			model=Actualtype.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public T getModel() {

		return model;
	}

	protected void pushPageDataIntoStack(Page<T> pages) {
		// 利用map存储数据
		Map<String, Object> jsons = new HashMap<>();
		jsons.put("total", pages.getTotalElements());
		System.out.println("总条数" + pages.getTotalElements());
		jsons.put("rows", pages.getContent());
		// 压入值栈
		ServletActionContext.getContext().getValueStack().push(jsons);
	}

}
