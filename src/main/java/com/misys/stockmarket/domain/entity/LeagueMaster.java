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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the LEAGUE_MASTER database table.
 * 
 */
@Entity
@Table(name = "LEAGUE_MASTER", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@SequenceGenerator(name = "SEQ_LEAGUE_MASTER")
public class LeagueMaster extends AuditableEntity implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LEAGUE_MASTER")
	@Column(name = "LEAGUE_ID")
	private long leagueId;

	@Column(name = "NAME", length = 50)
	private String name;

	@Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "DURATION", length = 1)
	private String duration;

	@Column(name = "QUALIFYING_AMOUNT", precision = 10, scale = 2)
	private BigDecimal qualifyingAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE")
	private Date startDate;

	@Column(length = 1)
	private String status;

	@Column(name = "STAGE", precision = 1)
	private BigDecimal stage;

	@OneToMany
	@Basic(fetch = FetchType.LAZY)
	@JoinColumn(name = "LEAGUE_ID", referencedColumnName = "LEAGUE_ID")
	private List<LeagueUser> leagueUsers;

	public long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(long leagueId) {
		this.leagueId = leagueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public BigDecimal getQualifyingAmount() {
		return qualifyingAmount;
	}

	public void setQualifyingAmount(BigDecimal qualifyingAmount) {
		this.qualifyingAmount = qualifyingAmount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<LeagueUser> getLeagueUsers() {
		return leagueUsers;
	}

	public void setLeagueUsers(List<LeagueUser> leagueUsers) {
		this.leagueUsers = leagueUsers;
	}

	public BigDecimal getStage() {
		return stage;
	}

	public void setStage(BigDecimal stage) {
		this.stage = stage;
	}

}
