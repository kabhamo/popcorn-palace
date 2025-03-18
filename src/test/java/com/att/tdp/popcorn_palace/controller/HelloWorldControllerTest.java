package com.att.tdp.popcorn_palace.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldControllerTest {

    @Test
    public void testSayHello() {
        String expected = "Hello World";
        HelloWorldController controller = Mockito.mock(HelloWorldController.class);
        Mockito.when(controller.sayHello()).thenReturn("Hello");
        String actual = controller.sayHello();
        assertEquals(expected, actual);
    }

}