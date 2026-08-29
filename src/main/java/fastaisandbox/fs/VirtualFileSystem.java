package fastaisandbox.fs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class VirtualFileSystem {
    private final Path jailRoot;

    public VirtualFileSystem() {
        try {
            this.jailRoot = Files.createTempDirectory("fastai_sandbox_jail_");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary sandbox jail", e);
        }
    }

    public VirtualFileSystem(Path jailRoot) {
        this.jailRoot = jailRoot;
    }

    public boolean isPathPermitted(String relativePath) {
        if (relativePath == null || relativePath.contains("..")) return false;
        File target = new File(jailRoot.toFile(), relativePath);
        try {
            return target.getCanonicalPath().startsWith(jailRoot.toFile().getCanonicalPath());
        } catch (IOException e) {
            return false;
        }
    }

    public Path getJailRoot() { return jailRoot; }
}