package org.tommap.tomuserloginrestapis.service;

import java.util.function.Supplier;

public interface ITransactionHandler {
    <T> T runInNewTransaction(Supplier<T> supplier);
}
