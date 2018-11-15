package com.revature.project.one.Models;

import java.sql.Timestamp;

public class Reimbursement {
	private Integer id;
	private Integer amount;
	private Timestamp submitted;
	private Timestamp resolved;
	private String description;
	private String receipt;
	private Integer author_id;
	private Integer resolver_id;
	private Integer status_id;
	private Integer type_id;

	public Reimbursement(int id, int amount, Timestamp submitted, Timestamp resolved, String description, String receipt,
			int author_id, int resolver_id, int status_id, int type_id) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receipt = receipt;
		this.author_id = author_id;
		this.resolver_id = resolver_id;
		this.status_id = status_id;
		this.type_id = type_id;
	}
	
	public Reimbursement(int amount, Timestamp currentTime, String description, String receipt,
			int author_id, int status_id, int type_id) {
		super();
		this.amount = amount;
		this.submitted = currentTime;
		this.description = description;
		this.receipt = receipt;
		this.author_id = author_id;
		this.status_id = status_id;
		this.type_id = type_id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public Integer getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public Integer getResolver_id() {
		return resolver_id;
	}

	public void setResolver_id(int resolver_id) {
		this.resolver_id = resolver_id;
	}

	public Integer getStatus_id() {
		return status_id;
	}

	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submitted=" + submitted + ", resolved=" + resolved
				+ ", description=" + description + ", receipt=" + receipt + ", author_id=" + author_id
				+ ", resolver_id=" + resolver_id + ", status_id=" + status_id + ", type_id=" + type_id + "]";
	}

}
