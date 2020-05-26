package br.com.jpb.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserVo {

	private long id;
	private String name;
	private String email;
	private boolean active;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime registeredAt;
	private Set<Role> roles;
	private String timeZoneId;

	public UserVo(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.active = user.isActive();
		this.registeredAt = user.getRegisteredAt();
		this.roles = user.getRoles();
		this.timeZoneId = user.getTimeZoneId();
	}
}
