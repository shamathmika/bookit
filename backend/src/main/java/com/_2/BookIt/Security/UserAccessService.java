package com._2.BookIt.Security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("userAccess")
public class UserAccessService {
    public boolean canAccessUser(String userId, Authentication auth) {
        return userId.equals(auth.getName()); // assuming JWT sets principal as user ID
    }
}
