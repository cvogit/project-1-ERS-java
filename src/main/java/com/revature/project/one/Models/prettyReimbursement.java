package com.revature.project.one.Models;

import java.sql.Timestamp;

public class prettyReimbursement {
	private Integer id;
	private Integer amount;
	private Timestamp submitted;
	private Timestamp resolved;
	private String description;
	private String receipt;
	private String authorFirst;
	private String authorLast;
	private String resolverFirst;
	private String resolverLast;
	private String status;
	private String type;
	
	public prettyReimbursement(Integer id, Integer amount, Timestamp submitted, Timestamp resolved, String description,
			String receipt, String authorFirst, String authorLast, String resolverFirst, String resolverLast,
			String status, String type) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receipt = receipt;
		this.authorFirst = authorFirst;
		this.authorLast = authorLast;
		this.resolverFirst = resolverFirst;
		this.resolverLast = resolverLast;
		this.status = status;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
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

	public String getAuthorFirst() {
		return authorFirst;
	}

	public void setAuthorFirst(String authorFirst) {
		this.authorFirst = authorFirst;
	}

	public String getAuthorLast() {
		return authorLast;
	}

	public void setAuthorLast(String authorLast) {
		this.authorLast = authorLast;
	}

	public String getResolverFirst() {
		return resolverFirst;
	}

	public void setResolverFirst(String resolverFirst) {
		this.resolverFirst = resolverFirst;
	}

	public String getResolverLast() {
		return resolverLast;
	}

	public void setResolverLast(String resolverLast) {
		this.resolverLast = resolverLast;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
