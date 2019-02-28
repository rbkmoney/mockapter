package com.rbkmoney.mockapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"consul.enabled=false"})
@SpringBootTest(classes = MockapterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockapterApplicationTest {

    @LocalServerPort
    protected int port;

    @Test
    public void contextLoads() {

    }

}
