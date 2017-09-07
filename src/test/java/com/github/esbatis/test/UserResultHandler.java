package com.github.esbatis.test;

import com.github.esbatis.handler.ResultHandler;

public class UserResultHandler implements ResultHandler<Integer> {

    @Override
    public Integer handleResult(String result) {
        System.out.println(result);
        return 10;
    }
}
