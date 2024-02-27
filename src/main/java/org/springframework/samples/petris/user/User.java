package org.springframework.samples.petris.user;

import org.hibernate.validator.constraints.Length;
import org.springframework.samples.petris.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity{
	
	@Column(unique = true)
	@NotBlank
	String username;

	@NotBlank
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "La contraseña debe tener digitos y caracteres")
	@Length(min=8, message = "La longitud mínima es de 8 caracteres")
	protected String password;

	@NotNull
	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	protected String email;

	@NotNull
	protected Boolean online;
	
	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "authority")
	Authorities authority;

	public Boolean hasAuthority(String auth) {
		return authority.getAuthority().equals(auth);
	}

	public Boolean hasAnyAuthority(String... authorities) {
		Boolean cond = false;
		for (String auth : authorities) {
			if (auth.equals(authority.getAuthority()))
				cond = true;
		}
		return cond;
	}

}
