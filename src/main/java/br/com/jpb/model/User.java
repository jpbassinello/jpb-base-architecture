package br.com.jpb.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.jpb.Constants;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER")
@Getter
@EqualsAndHashCode(of = "email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotEmpty(message = "{user.name.notEmpty}")
	@Size(max = 255, message = "{user.name.maxSize}")
	@Column(name = "NAME")
	@Setter
	private String name;

	@NotEmpty(message = "{user.email.notEmpty}")
	@Size(max = 150, message = "{user.email.maxSize}")
	@Column(name = "EMAIL")
	private String email;

	@NotEmpty(message = "{user.password.notEmpty}")
	@Size(max = 150)
	@Column(name = "PASSWORD")
	@Setter
	private String password;

	@Column(name = "ACTIVE")
	private boolean active;

	@NotNull
	@Column(name = "REGISTERED_AT")
	private LocalDateTime registeredAt;

	@ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
	@JoinTable(name = "USER_ROLE",
			joinColumns = @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_USER_ROLE_USER_ID")))
	@Column(name = "ROLE", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Role> roles = EnumSet.of(Role.DEFAULT);

	@NotEmpty
	@Column(name = "TIME_ZONE_ID")
	private String timeZoneId = Constants.DEFAULT_TIME_ZONE_ID;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "USER_IDENTITY_PROVIDER", foreignKey = @ForeignKey(name = "FK_USER_IDENTITY_PROVIDER_USER_ID"))
	@MapKeyEnumerated(EnumType.STRING)
	@MapKeyColumn(name = "PROVIDER")
	@Column(name = "PROVIDER_USER_ID")
	private Map<IdentityProvider, String> identityProviders = new HashMap<>();

	@Transient
	@Setter
	private String transientTempPassword;

	public User(String name, String email, String password) {
		this(name, email, password, Role.DEFAULT);
	}

	public User(String name, String email, String password, Role mainRole) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.roles.add(mainRole);
		this.registeredAt = LocalDateTime.now();
		this.active = true;
	}

	@PrePersist
	@PreUpdate
	public void prePersistUpdate() {
		this.email = Optional
				.ofNullable(email)
				.map(String::toLowerCase)
				.orElse(null);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles
				.stream()
				.map(Role::name)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public String getFirstName() {
		return name.split("\\s+")[0];
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public boolean hasRole(Role role) {
		return roles.contains(role);
	}
}
