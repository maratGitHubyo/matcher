package ru.sloggers.matcher.services;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AddressExtractorTest {

    private static final String BINDING_FOLDER = "photos";

    private String[] extractAddress(Path directory) {
        var name = directory.toAbsolutePath().toString();
        int bindingIndex = name.indexOf(BINDING_FOLDER);
        return java.util.Arrays.stream(name.substring(bindingIndex + BINDING_FOLDER.length())
                .replaceFirst("^[\\\\/]+", "")
                .split("[/\\\\]+"))
            .map(String::trim)
            .toArray(String[]::new);
    }

    @Test
    void testExtractAddressFromWindowsPath() {
        Path windowsPath = Paths.get("D:\\work-station\\projects\\IDEA\\matcher\\src\\main\\resources\\photos\\г Белинский\\Колычевская\\120\\0\\22");
        String[] expected = {"г Белинский", "Колычевская", "120", "0", "22"};
        assertArrayEquals(expected, extractAddress(windowsPath));
    }

    @Test
    void testExtractAddressFromLinuxPath() {
        Path linuxPath = Paths.get("/home/user/projects/photos/г Белинский/Колычевская/120/0/22");
        String[] expected = {"г Белинский", "Колычевская", "120", "0", "22"};
        assertArrayEquals(expected, extractAddress(linuxPath));
    }

    @Test
    void testExtractAddressFromMacOSPath() {
        Path macPath = Paths.get("/Users/user/photos/г Белинский/Колычевская/120/0/22");
        String[] expected = {"г Белинский", "Колычевская", "120", "0", "22"};
        assertArrayEquals(expected, extractAddress(macPath));
    }
}

