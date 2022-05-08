package app.core.utils;

import org.springframework.stereotype.Component;

import app.core.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ClientDetails {
	
	private int id;
	private String email;
	private ClientType clientType;
	
}
