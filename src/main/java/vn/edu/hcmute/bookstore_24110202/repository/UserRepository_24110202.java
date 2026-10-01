package vn.edu.hcmute.bookstore_24110202.repository;

import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;

public interface UserRepository_24110202 {
    User_24110202 findByEmail(String email);
    boolean existsByEmail(String email);
    void save(User_24110202 u);
    void updateLastLogin(Integer id);
    void updatePassword(Integer id, String passwordHash);
}
