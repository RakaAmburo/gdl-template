package com.template.module2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


class SomeControllerTest {

    @Test
    public void test(){
        Assertions.assertEquals("hola", "hola");
    }

}