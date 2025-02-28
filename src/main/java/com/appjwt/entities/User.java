package com.appjwt.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.appjwt.validation.ExistByUsername;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	@NotBlank
	private String name;
	
	@Column(unique = true)
	@NotBlank
	@Size(min = 5, max = 20)
	@ExistByUsername
	private String username;
	
	@NotBlank
	@Size(min = 8)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private boolean enabled;
	
	@ManyToMany
	@JsonIgnoreProperties(value = {"users", "handler", "hibernateLazyInitializer"})
	@JoinTable(
		name = "users_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"),
		uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})}
	)
	private List<Role> roles;
	
	@Transient
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private boolean admin;
	
	@PrePersist
	public void prePersist() {
		this.enabled = false;
	}

	public User() {
		super();
		this.roles = new ArrayList<>();
	}

	public User(String name, String username, boolean enabled, List<Role> roles) {
		super();
		this.name = name;
		this.username = username;
		this.enabled = enabled;
		this.roles = roles;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id && Objects.equals(username, other.username);
	}
	
	
}
