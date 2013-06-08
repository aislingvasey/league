package com.africaapps.league.model.game;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.africaapps.league.model.BaseDataModel;
import com.africaapps.league.validation.UpdateGroup;

@NamedQuery(name="isExistingUsername", query="select count(u.id) from User u where u.username = :username")
@Entity
@Table(name="game_user_details")
public class User extends BaseDataModel {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String cellNumber;
	private Set<UserTeam> teams;
	
	public User() {
		this.teams = new HashSet<UserTeam>();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[User: ");
		builder.append(" id:").append(id);
		builder.append(" username:").append(username);
		builder.append(" firstName:").append(firstName);
		builder.append(" lastName:").append(lastName);
		builder.append(" emailAddress:").append(emailAddress);
		builder.append(" cellNumber:").append(cellNumber);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof User)) {
			return false;
		} else {
			User u = (User) o;
			if (u.getId().equals(id)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@NotNull(groups={UpdateGroup.class})
	@Id
  @SequenceGenerator(name="user_seq", sequenceName="user_seq", allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq")
  @Column(name="id", nullable=false)
	public Long getId() {
		return id;
	}
	
	@NotNull
	@Size(min=1, max=100, message="{validate.username.range}")
  @Column(name="username", length=100, nullable=false, unique=true)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Size(min=1, max=1000, message="{validate.password.range}")
  @Column(name="password", length=1000, nullable=true)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Size(min=1, max=100, message="{validate.firstname.range}")
  @Column(name="first_name", length=100, nullable=true)
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Size(min=1, max=100, message="{validate.lastname.range}")
  @Column(name="last_name", length=100, nullable=true)
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Size(min=1, max=100, message="{validate.emailaddress.range}")
  @Column(name="email_address", length=100, nullable=true)
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Size(min=1, max=50, message="{validate.cellnumber.range}")
  @Column(name="cell_number", length=50, nullable=true)
	public String getCellNumber() {
		return cellNumber;
	}
	
	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}

	@OneToMany(mappedBy = "user")
	public Set<UserTeam> getTeams() {
		return teams;
	}

	public void setTeams(Set<UserTeam> teams) {
		this.teams = teams;
	}
}
