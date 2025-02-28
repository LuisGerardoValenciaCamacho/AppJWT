package com.appjwt.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String name;
	
	private String clave;
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnoreProperties(value = {"roles", "handler", "hibernateLazyInitializer" })
	private List<User> users;

	public Role() {
		super();
		this.users = new ArrayList<>();
	}

	public Role(String name, String clave) {
		super();
		this.name = name;
		this.clave = clave;
	}

	public Role(long id, String name, String clave) {
		super();
		this.id = id;
		this.name = name;
		this.clave = clave;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clave, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(clave, other.clave) && id == other.id && Objects.equals(name, other.name);
	}
}
