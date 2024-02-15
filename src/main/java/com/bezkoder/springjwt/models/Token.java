package com.bezkoder.springjwt.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "token", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "token") 
    })
public class Token {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id = Long.MIN_VALUE;
	
	private String token;
	
	@Enumerated(EnumType.STRING)
	private TokenType tokenType;
	
	public boolean revoked;
	
	public boolean expired;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_fk")
	public User user;

	@Override
	public String toString() {
		return "Token [id=" + id + ", token=" + token + ", tokenType=" + tokenType + ", revoked=" + revoked
				+ ", expired=" + expired + "]";
	}

	public Token() {
		super();
	}

	public Token(User user, String jwt) {
		this.user = user;
		this.token = jwt;
		this.tokenType = TokenType.BEARER;
		this.revoked = false;
		this.expired = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
