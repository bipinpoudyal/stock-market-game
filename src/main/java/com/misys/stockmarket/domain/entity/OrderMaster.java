package com.misys.stockmarket.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the ORDER_MASTER database table.
 * 
 */
@Entity
@Table(name = "ORDER_MASTER")
@SequenceGenerator(name = "SEQ_ORDER_MASTER")
public class OrderMaster extends AuditableEntity implements BaseEntity, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ORDER_MASTER")
	@Column(name = "ORDER_ID")
	private long orderId;

	@Column(length = 1)
	private String intraday;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORDER_DATE")
	private Date orderDate;

	@Column(name = "ORDER_PRICE", precision = 8, scale = 2)
	private BigDecimal orderPrice;

	@Column(name = "PRICE_TYPE", length = 2)
	private String priceType;

	@Column(length = 2)
	private String status;

	@Column(name = "\"TYPE\"", length = 1)
	private String type;

	@ManyToOne
	@JoinColumn(name = "STOCK_ID")
	private StockMaster stockMaster;

	@ManyToOne
	@JoinColumn(name = "LEAGUE_USER_ID")
	private LeagueUser leagueUser;

	@OneToMany
	@Basic(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID")
	private List<OrderExecution> orderExecutions;

	private BigDecimal volume;

	public OrderMaster() {
	}

	public long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getIntraday() {
		return this.intraday;
	}

	public void setIntraday(String intraday) {
		this.intraday = intraday;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getOrderPrice() {
		return this.orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getPriceType() {
		return this.priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getVolume() {
		return this.volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public StockMaster getStockMaster() {
		return stockMaster;
	}

	public void setStockMaster(StockMaster stockMaster) {
		this.stockMaster = stockMaster;
	}

	public LeagueUser getLeagueUser() {
		return leagueUser;
	}

	public void setLeagueUser(LeagueUser leagueUser) {
		this.leagueUser = leagueUser;
	}

	@Override
	public String toString() {
		return "OrderMaster [orderId=" + orderId + ", intraday=" + intraday
				+ ", orderDate=" + orderDate + ", orderPrice=" + orderPrice
				+ ", priceType=" + priceType + ", status=" + status + ", type="
				+ type + ", stockMaster=" + stockMaster + ", leagueUser="
				+ leagueUser + ", orderExecutions=" + orderExecutions
				+ ", volume=" + volume + "]";
	}

	public List<OrderExecution> getOrderExecutions() {
		return orderExecutions;
	}

	public void setOrderExecutions(List<OrderExecution> orderExecutions) {
		this.orderExecutions = orderExecutions;
	}

}