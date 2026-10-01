package vn.edu.hcmute.bookstore_24110202.service;

import vn.edu.hcmute.bookstore_24110202.dto.FormResult_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.RegisterForm_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;

public interface IUserService_24110202 {
    FormResult_24110202<RegisterForm_24110202> validateRegister(RegisterForm_24110202 f);
    void register(RegisterForm_24110202 f);
    User_24110202 authenticate(String email, String password);
}
