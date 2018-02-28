package com.gomi.bos.service.base.imp;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.AreaReposity;
import com.gomi.bos.dao.base.FixedAreaReposity;
import com.gomi.bos.dao.base.OrderReposity;
import com.gomi.bos.dao.base.WorkBillReposity;
import com.gomi.bos.domain.base.Area;
import com.gomi.bos.domain.base.Courier;
import com.gomi.bos.domain.base.FixedArea;
import com.gomi.bos.domain.base.Order;
import com.gomi.bos.domain.base.SubArea;
import com.gomi.bos.domain.base.WorkBill;
import com.gomi.bos.service.base.OrderService;

@Service
@Transactional
public class OrderServiceImp implements OrderService {
	
	@Autowired
	private OrderReposity  orderReposity;
	@Autowired
	private FixedAreaReposity fixedAreaReposity;
	@Autowired
	private AreaReposity areaReposity;
	@Autowired
	private WorkBillReposity  workBillReposity;
	
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate  jmsTemplate;

	@Override
	public void saveOrder(Order order) {
		System.out.println("OrderServiceImp for test:order="+order);
		order.setOrderNum(UUID.randomUUID().toString());
		order.setOrderTime(new Date());
		order.setStatus("1");
		//自动分单--------
		/*
		 * 1.根据客户下单地址(寄件人地址)--查找登录的客户的地址(CRM存在数据库中的地址进行比对)，
		 * 如果地址相同完全匹配---》找到客户的地址的定区id---》
		 * 通过定区id关联的快递员--》找到匹配的快递员--》分单给快递员
		 * */
		String url="http://localhost:9001/crm_management/services/customerService/findFixedAreaIdByAddress?address="+order.getSendAddress();
		String  fixAreaId=WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(String.class);
		if(fixAreaId!=null){
			//分配快递员
			FixedArea fixedArea = fixedAreaReposity.findOne(fixAreaId);
			//订单关联快递员
			if(fixedArea!=null){
				Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
				if(iterator.hasNext()){
				Courier courier =iterator.next();
				order.setCourier(courier);
				//生成随机订单编号
				order.setOrderType("1");
				//保存订单
				orderReposity.save(order);
				//保存工单并短信通知快递员
				generateWorkBillAndSendMessage(order);
				return;
				}
			}
		}
		//根据寄件地址省市区，找到分区关键字，匹配关键字，实现自动分单
		Area  area=order.getSendArea();
		Area peisistArea=areaReposity.findByProvinceAndCityAndDistrict(area.getProvince(),area.getCity(),area.getDistrict());
			//匹配子区域
			for (SubArea subArea : peisistArea.getSubareas()) {
				if(order.getSendAddress().contains(subArea.getKeyWords())){
					//包含区域关键字--分配快递员
					Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
					if(iterator.hasNext()){
					Courier  courier=iterator.next();
					if(courier!=null){
						order.setCourier(courier);
						order.setOrderType("1");
						//保存订单
						orderReposity.save(order);
						generateWorkBillAndSendMessage(order);
						return;
					}
					}
				}
			}
				//匹配子区域
				for (SubArea subArea : peisistArea.getSubareas()) {
					//辅助关键字查1
					if(order.getSendAddress().contains(subArea.getAssistKeyWords())){
						//包含区域关键字--分配快递员
						Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
						if(iterator.hasNext()){
						Courier  courier=iterator.next();
						if(courier!=null){
							order.setCourier(courier);
							//保存订单
							order.setOrderType("1");
							orderReposity.save(order);
							generateWorkBillAndSendMessage(order);
							return;
							}
						}
					}
				}
			//人工分单
			order.setOrderType("2");
			orderReposity.save(order);
				
		}

	private void generateWorkBillAndSendMessage(final Order order) {
		// 生成工单--创建工单并保存
		WorkBill workBill=new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		//生成短信序号并设置
		final String smsNumber=org.apache.commons.lang3.RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);
		//设置订单及快递员
		workBill.setCourier(order.getCourier());
		workBill.setOrder(order);
		workBillReposity.save(workBill);
		//生成短信--发送短信
		jmsTemplate.send("bos_workbill", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage  mapMessage=session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("msg","短信序号："+smsNumber+"去取件地址："+
				order.getSendAddress()+",客户电话："+order.getSendMobile()+"，备注："+order.getRemark());
				return mapMessage;
			}
		});
		workBill.setPickstate("已通知快递员");
	}
	
	}


