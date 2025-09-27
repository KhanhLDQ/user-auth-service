package org.tommap.tomuserloginrestapis.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tommap.tomuserloginrestapis.service.ITransactionHandler;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.util.function.Supplier;

@Service
public class TransactionHandlerImpl implements ITransactionHandler {
    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public <T> T runInNewTransaction(Supplier<T> supplier) { //no input -> produce output
        return supplier.get();
    }
}
