package test;

import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void get() {
        Assertions.assertNotNull(Managers.getDefault(), "Таск менеджер не иницилизрован");
        assertNotNull(Managers.getDefaultHistory(), "Хистори менеджер не иницилизрован");
    }
}