package fastaisandbox;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FastAISandboxTest {

    @Test
    public void testCommandPolicy() {
        FastAISandbox sandbox = new FastAISandbox();
        assertTrue(sandbox.executeSafe("echo 'hello'"));
        assertFalse(sandbox.executeSafe("format C: /y"));
        assertFalse(sandbox.executeSafe("rm -rf /"));
    }

    @Test
    public void testPathJail() {
        FastAISandbox sandbox = new FastAISandbox();
        assertTrue(sandbox.isPathPermitted("data/file.txt"));
        assertFalse(sandbox.isPathPermitted("../../../windows/system32"));
    }
}