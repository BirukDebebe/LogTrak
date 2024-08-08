package logtrak_crm.service;

import logtrak_crm.dto.UserDto;
import logtrak_crm.model.User;

public interface UserService {
	
	User save (UserDto userDto);

}
