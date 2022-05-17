package javajedi.com.service;

import javajedi.com.data.UserData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PrivacyService {

    UserData register(UserData userData);

    UserData login(String username, String password);

    UserData updateProfileImage(String username, MultipartFile profileImage) throws IOException;

    void delete(String username);

    byte[] getTemporaryProfileImage(String username) throws IOException;

    byte[] getProfileImage(String username, String fileName) throws IOException;

    UserData editUserProfile(String username, UserData userData);

}
