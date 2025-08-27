package com.bankz.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeRegisterControllerTest {

    private EmployeeRegisterController controller;

    @BeforeEach
    void setUp() {
        controller = new EmployeeRegisterController();
    }

    @Test
    void testControllerInitialization() {
        // Test that the controller can be instantiated
        assertNotNull(controller);
    }

    @Test
    void testInitializeMethod() {
        // Test that the initialize method runs without exceptions
        assertDoesNotThrow(() -> {
            controller.initialize();
        });
    }
}