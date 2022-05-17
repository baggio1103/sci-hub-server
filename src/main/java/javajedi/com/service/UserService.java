package javajedi.com.service;

import javajedi.com.data.UserData;

import java.util.List;

public interface UserService {

    List<UserData> findAllUsers();

    UserData findUserByUsername(String username);

}
