package com.core.app.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "tp_tracking", name = "t_users")
public class UserEntity extends CommonEntity{
    
	@Id
	@GeneratedValue(generator = "USER_ID_GENERATOR", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "USER_ID_GENERATOR", sequenceName = "T_USERS_USER_ID_SEQ",allocationSize=1)
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "username")
	private String userName;
	
	@Column(name = "email")
	private String email;

    @Column(name = "password")
	private String password;

    @OneToMany(mappedBy = "user")
	@JsonIgnore
    private List<BankAccountEntity> bankAccounts;

	public UserEntity(String userName) {
		this.userName = userName;
	}

	
}