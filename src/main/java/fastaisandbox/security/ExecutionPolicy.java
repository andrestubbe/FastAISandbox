package fastaisandbox.security;

import java.util.HashSet;
import java.util.Set;

public final class ExecutionPolicy {
    private final long maxExecutionTimeMs;
    private final long maxMemoryBytes;
    private final Set<String> blockedCommands = new HashSet<>();

    public ExecutionPolicy(long maxExecutionTimeMs, long maxMemoryBytes) {
        this.maxExecutionTimeMs = maxExecutionTimeMs;
        this.maxMemoryBytes = maxMemoryBytes;
        // Default blocked destructive shell commands
        blockedCommands.add("rm -rf");
        blockedCommands.add("format");
        blockedCommands.add("mkfs");
        blockedCommands.add("dd if=/dev");
        blockedCommands.add(":(){ :|:& };:");
        blockedCommands.add("shutdown");
    }

    public boolean isCommandPermitted(String command) {
        if (command == null || command.trim().isEmpty()) return false;
        String lower = command.toLowerCase();
        for (String blocked : blockedCommands) {
            if (lower.contains(blocked)) return false;
        }
        return true;
    }

    public long getMaxExecutionTimeMs() { return maxExecutionTimeMs; }
    public long getMaxMemoryBytes() { return maxMemoryBytes; }
}