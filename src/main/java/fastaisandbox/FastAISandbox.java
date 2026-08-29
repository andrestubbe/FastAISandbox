package fastaisandbox;

import fastaisandbox.fs.VirtualFileSystem;
import fastaisandbox.process.IsolatedProcessRunner;
import fastaisandbox.security.ExecutionPolicy;

public final class FastAISandbox {

    private final ExecutionPolicy policy;
    private final VirtualFileSystem vfs;
    private final IsolatedProcessRunner runner;

    public FastAISandbox() {
        this(new ExecutionPolicy(5000, 256 * 1024 * 1024));
    }

    public FastAISandbox(ExecutionPolicy policy) {
        this.policy = policy;
        this.vfs = new VirtualFileSystem();
        this.runner = new IsolatedProcessRunner(policy);
    }

    public boolean executeSafe(String command) {
        return runner.execute(command);
    }

    public boolean isPathPermitted(String relativePath) {
        return vfs.isPathPermitted(relativePath);
    }

    public ExecutionPolicy getPolicy() { return policy; }
    public VirtualFileSystem getVfs() { return vfs; }
}