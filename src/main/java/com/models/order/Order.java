package com.models.order;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.models.*;
import com.models.member.*;

public class Order extends Dto<Order> {
	
	private long orderNo; // 주문번호
	private String status; // 주문상태 order - 주문접수, incash - 입금확인, ready - 배송준비중, delivery - 배송중, done - 완료
	private String statusStr; // 주문상태 문구
	private String invoice; // 운송장 
	private String orderName; // 주문자명
	private String orderCellPhone; // 휴대전화번호
	private String orderEmail; // 이메일주소
	private String receiverName; // 수령자명
	private String receiverCellPhone; // 휴대전화번호
	private String receiverZonecode; // 배송지주소 - 우편번호
	private String receiverAddress; // 배송지주소 
	private String receiverAddressSub; // 배송지 나머지주소
	private int settlePrice; // 결제금액
	private String regDt; // 주문일시
	private int memNo; // 회원번호
	private Member member;
	private ArrayList<OrderGoods> items; 
	
	public Order() {}
	
	public Order(long orderNo, String status, String invoice, String orderName, String orderCellPhone,
			String orderEmail, String receiverName, String receiverCellPhone, String receiverZonecode,
			String receiverAddress, String receiverAddressSub, int settlePrice, String regDt, int memNo) {
		this.orderNo = orderNo;
		this.status = status;
		this.invoice = invoice;
		this.orderName = orderName;
		this.orderCellPhone = orderCellPhone;
		this.orderEmail = orderEmail;
		this.receiverName = receiverName;
		this.receiverCellPhone = receiverCellPhone;
		this.receiverZonecode = receiverZonecode;
		this.receiverAddress = receiverAddress;
		this.receiverAddressSub = receiverAddressSub;
		this.settlePrice = settlePrice;
		this.regDt = regDt;
		this.memNo = memNo;
		if (memNo > 0) {
			member = MemberDao.getInstance().getMember(memNo);
		}
		
		// 주문상품 
		items = OrderDao.getInstance().getGoodsInfo(orderNo);
		
		switch (status) {
			case "order" :
				this.statusStr = "주문접수";
				break;
			case "incash" :
				this.statusStr = "입금확인";
				break;
			case "ready" :
				this.statusStr = "배송준비중";
				break;
			case "delivery" :
				this.statusStr = "배송중";
				break;
			case "done" : 
				this.statusStr = "배송완료";
				break;
		}
	}

	public Order(ResultSet rs) throws SQLException {
		this(
			rs.getLong("orderNo"),
			rs.getString("status"),
			rs.getString("invoice"),
			rs.getString("orderName"),
			rs.getString("orderCellPhone"),
			rs.getString("orderEmail"),
			rs.getString("receiverName"),
			rs.getString("receiverCellPhone"),
			rs.getString("receiverZonecode"),
			rs.getString("receiverAddress"),
			rs.getString("receiverAddressSub"),
			rs.getInt("settlePrice"),
			rs.getString("regDt"),
			rs.getInt("memNo")
		);
	}
	
	
	
	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderCellPhone() {
		return orderCellPhone;
	}

	public void setOrderCellPhone(String orderCellPhone) {
		this.orderCellPhone = orderCellPhone;
	}

	public String getOrderEmail() {
		return orderEmail;
	}

	public void setOrderEmail(String orderEmail) {
		this.orderEmail = orderEmail;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverCellPhone() {
		return receiverCellPhone;
	}

	public void setReceiverCellPhone(String receiverCellPhone) {
		this.receiverCellPhone = receiverCellPhone;
	}

	public String getReceiverZonecode() {
		return receiverZonecode;
	}

	public void setReceiverZonecode(String receiverZonecode) {
		this.receiverZonecode = receiverZonecode;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverAddressSub() {
		return receiverAddressSub;
	}

	public void setReceiverAddressSub(String receiverAddressSub) {
		this.receiverAddressSub = receiverAddressSub;
	}

	public int getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(int settlePrice) {
		this.settlePrice = settlePrice;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public int getMemNo() {
		return memNo;
	}

	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	@Override
	public Order setResultSet(ResultSet rs) throws SQLException {
		return new Order(rs);
	}	
}
