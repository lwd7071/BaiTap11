package vn.edu.hcmute.bookstore_24110202.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import vn.edu.hcmute.bookstore_24110202.dto.FormResult_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.RegisterForm_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.UserRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.impl.UserRepositoryImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IUserService_24110202;
import vn.edu.hcmute.bookstore_24110202.util.PasswordUtil_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

public class UserServiceImpl_24110202 implements IUserService_24110202 {
    private final UserRepository_24110202 repo = new UserRepositoryImpl_24110202();

    public FormResult_24110202<RegisterForm_24110202> validateRegister(RegisterForm_24110202 form) {
        FormResult_24110202<RegisterForm_24110202> result = new FormResult_24110202<>(form);
        if (form == null) { result.addError("form", "Thông tin đăng ký bắt buộc"); return result; }
        String email = form.getEmail() == null ? "" : form.getEmail().trim().toLowerCase();
        if (!ValidationUtil_24110202.email(email)) result.addError("email", "Email không hợp lệ");
        else if (repo.existsByEmail(email)) result.addError("email", "Email đã tồn tại");
        String fullName = form.getFullname();
        if (fullName == null || fullName.isBlank() || fullName.trim().length() > 50)
            result.addError("fullname", "Họ tên bắt buộc, tối đa 50 ký tự");
        if (form.getPhone() != null && !form.getPhone().isBlank() && !ValidationUtil_24110202.nonNegativeInteger(form.getPhone()))
            result.addError("phone", "Số điện thoại không hợp lệ");
        String password = form.getPassword();
        if (password == null || password.length() < 6 || password.length() > 32)
            result.addError("password", "Mật khẩu phải từ 6 đến 32 ký tự");
        if (!Objects.equals(password, form.getConfirmPassword())) result.addError("confirmPassword", "Mật khẩu nhập lại không khớp");
        return result;
    }

    public void register(RegisterForm_24110202 form) {
        FormResult_24110202<RegisterForm_24110202> validation = validateRegister(form);
        if (!validation.isValid()) throw new IllegalArgumentException("Thông tin đăng ký không hợp lệ");
        User_24110202 user = new User_24110202();
        user.setEmail(form.getEmail().trim().toLowerCase());
        user.setFullname(form.getFullname().trim());
        user.setPhone(ValidationUtil_24110202.integer(form.getPhone()));
        user.setPasswd(PasswordUtil_24110202.md5(form.getPassword()));
        user.setSignupDate(LocalDateTime.now());
        user.setAdmin(false);
        repo.save(user);
    }

    public User_24110202 authenticate(String email, String password) {
        if (email == null || email.isBlank() || password == null) return null;
        User_24110202 user = repo.findByEmail(email.trim().toLowerCase());
        if (user != null && PasswordUtil_24110202.md5(password).equalsIgnoreCase(user.getPasswd())) {
            repo.updateLastLogin(user.getId());
            return user;
        }
        return null;
    }
}
