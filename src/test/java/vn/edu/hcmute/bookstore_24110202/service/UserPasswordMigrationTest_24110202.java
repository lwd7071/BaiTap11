package vn.edu.hcmute.bookstore_24110202.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.UserRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.UserServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.PasswordUtil_24110202;

class UserPasswordMigrationTest_24110202 {
    @Test void upgradesLegacyHashAfterSuccessfulLogin() {
        UserRepository_24110202 repo = mock(UserRepository_24110202.class);
        User_24110202 user = mock(User_24110202.class);
        when(repo.findByEmail("user@example.com")).thenReturn(user);
        when(user.getPasswd()).thenReturn(PasswordUtil_24110202.legacyMd5("Secret123"));
        when(user.getId()).thenReturn(7);

        User_24110202 authenticated = new UserServiceImpl_24110202(repo).authenticate("user@example.com", "Secret123");

        assertSame(user, authenticated);
        verify(repo).updatePassword(eq(7), argThat(hash -> PasswordUtil_24110202.verify("Secret123", hash)
                && !PasswordUtil_24110202.isLegacy(hash)));
        verify(repo).updateLastLogin(7);
    }

    @Test void leavesPasswordUnchangedAfterFailedLegacyLogin() {
        UserRepository_24110202 repo = mock(UserRepository_24110202.class);
        User_24110202 user = mock(User_24110202.class);
        when(repo.findByEmail("user@example.com")).thenReturn(user);
        when(user.getPasswd()).thenReturn(PasswordUtil_24110202.legacyMd5("Secret123"));

        assertNull(new UserServiceImpl_24110202(repo).authenticate("user@example.com", "wrong"));
        verify(repo, never()).updatePassword(anyInt(), anyString());
        verify(repo, never()).updateLastLogin(anyInt());
    }
}
