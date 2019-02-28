package com.rbkmoney.mockapter.model.response.error;

import lombok.ToString;

@ToString
public class UnexpectedError implements Error {

    @Override
    public RuntimeException newException() {
        return new RuntimeException();
    }

}
