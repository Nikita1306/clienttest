package com.example.clienttest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Value("${test.idList}")
    private String idList;
    @Value("${test.wCount}")
    private String wCount;
    public String getList() {
        return this.idList;
    }
    public String getWCount() {
        return this.wCount;
    }
}
