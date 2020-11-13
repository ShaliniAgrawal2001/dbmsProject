package com.shalini.dbms.repositories;

import com.shalini.dbms.models.VerificationToken;

public interface TokenRepository {
    public VerificationToken getVerificationToken(String token);
    public void save(VerificationToken verificationToken);
}
