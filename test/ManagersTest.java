package tests;

import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void get() {
        Assertions.assertNotNull(Managers.getDefault(), "Таск менеджер не иницилизрован");
        assertNotNull(Managers.getDefaultHistory(), "Хистори менеджер не иницилизрован");
        assertNotNull(Managers.getFileBackedManager(Path.of("testfiles/test1.txt")), "Хистори менеджер не иницилизрован");
    }

}