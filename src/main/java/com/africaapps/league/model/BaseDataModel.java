package com.africaapps.league.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseDataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long id;

	public void setId(Long id) {
		this.id = id;
	}
}
