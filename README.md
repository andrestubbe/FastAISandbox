> [!WARNING]
> **🚧 WIP — Active AI Pipeline Construction & Architecture Optimization in Progress.**

# FastAISandbox [ALPHA-2026-09-08] — In-Process Security Governor & Isolation Jail for Java AI Agents

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastAISandbox/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastAISandbox)

---

**⚡ In-process command security governor, virtual filesystem jail, and resource isolation substrate for Java AI agents.**

**FastAISandbox** is a lightweight execution cage built for autonomous agents (**[FastAIAgent](https://github.com/andrestubbe/FastAIAgent)**, **[FastAIMCP](https://github.com/andrestubbe/FastAIMCP)**) and code-generation environments. It prevents jailbreaks, destructive shell commands, directory traversal exploits, and runaway resource consumption without requiring heavy Docker containers or virtual machines.

---

## Quick Start

```java
import fastaisandbox.FastAISandbox;

public class Example {
    public static void main(String[] args) {
        FastAISandbox sandbox = new FastAISandbox();

        // 1. Safe Command Execution with Security Governance
        boolean isSafe = sandbox.executeSafe("python script.py --input data.json");
        System.out.println("Execution Allowed: " + isSafe);

        // 2. Destructive Command Interception
        boolean isDestructive = sandbox.executeSafe("rm -rf /");
        System.out.println("Destructive Blocked: " + !isDestructive);

        // 3. Virtual Chroot Filesystem Jail Validation
        boolean validPath = sandbox.isPathPermitted("workspace/output.csv");
        boolean jailbreakEscape = sandbox.isPathPermitted("../../etc/passwd");
        System.out.println("Jail Escape Intercepted: " + !jailbreakEscape);
    }
}
```

---

## Table of Contents

- [Why FastAISandbox?](#why-fastaisandbox)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Architecture Overview](#architecture-overview)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Technical Demos & Benchmarks](#technical-demos--benchmarks)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastAISandbox?

Autonomous agents generating shell scripts, compiling code, and invoking system tools present severe host execution risks:

- **Heavyweight Containers** — Spinning up Docker containers or microVMs introduces 500–2,000 ms startup latency per tool invocation.
- **Destructive OS Actions** — Uncontrolled LLM code execution can accidentally overwrite system binaries or trigger fork bombs.
- **Path Traversal Attacks** — Malicious agent tools can attempt relative `../../` traversal to escape working directories.

FastAISandbox eliminates container startup penalty with microsecond in-process governance:

| Feature | Docker / MicroVM Containers | OS Security Sandboxes | FastAISandbox |
|:---|:---|:---|:---|
| **Startup Overhead** | 500–2,000 ms (Daemon startup) | 50–150 ms (Process spawning) | < 1 µs (In-process memory) |
| **Command Policy Overhead**| High container daemon overhead | OS permission lookup latency | > 11,600,000 checks / sec |
| **Filesystem Isolation** | Block-device copy-on-write | OS Chroot / Jails (Root required)| Virtual Path Jail (Zero root privileges) |
| **External Daemons** | Requires Docker / Podman | Requires OS daemon configuration | Pure Java 17+ (Zero external daemons) |
| **Memory Footprint** | 50–200 MB per container | Variable per process sandbox | Minimal JVM heap footprint |

---

## Key Features

- 🛡️ **Command Security Policy** — Intercepts destructive shell commands, formatting utilities, and fork bombs.
- 📁 **Virtual Filesystem Jail** — Restricts file modifications strictly inside sandboxed workspace boundaries.
- ⏱️ **CPU & Execution Governance** — Enforces millisecond-level execution deadlines and process timeouts.
- ⚡ **Extreme Throughput** — Validates over 11,600,000 commands per second directly within the JVM.
- 📊 **FastANSI 120-Column HUD** — Rich console telemetry displaying security verdicts, permitted paths, and status trees.

---

## Real-World Use Cases

- 🤖 **Autonomous Coding Agent Jail**: Protect host development environments by intercepting destructive commands (`rm -rf`, `mkfs`, `dd`, fork bombs) before agent code is passed to the OS shell.
- 🔌 **Untrusted MCP Tool Execution**: Cage third-party Model Context Protocol (MCP) servers and tools inside virtual directory boundaries, preventing sensitive file exfiltration (`/etc/passwd`, `id_rsa`).
- 📁 **Multi-Tenant Workspace Isolation**: Confine autonomous subagents strictly to designated project subdirectories (`workspace/agent_42/`) with zero risk of path traversal escapes (`../../`).
- ⚡ **High-Throughput Policy Gateway**: Filter thousands of automated shell commands and system tool invocations per second with sub-microsecond latency and zero GC allocations.

---

## Architecture Overview

FastAISandbox provides safety governance for the FastJava agent runtime:

- 📦 **[FastAISandbox](https://github.com/andrestubbe/FastAISandbox)** (Security Governor): Validates commands, canonicalizes paths, and jails filesystem access.
- 🤖 **[FastAIAgent](https://github.com/andrestubbe/FastAIAgent)** (Autonomous Mind): Dispatches tool requests through security filters.
- ⚡ **[FastAIRuntime](https://github.com/andrestubbe/FastAIRuntime)** (Execution Body): Spawns native processes subject to sandbox constraints.
- 🔌 **[FastAIMCP](https://github.com/andrestubbe/FastAIMCP)** (Protocol Runtime): Governs Model Context Protocol tool execution boundaries.

---

## Performance Benchmarks

FastAISandbox is profiled using **JMH** to guarantee zero-overhead security governance:

| Benchmark Operation | Score (ops/ms) | Ops per Second | Memory Allocation |
|:---|:---|:---|:---|
| **Command Security Policy Validation** | **~11,600 ops/ms** | **> 11.6 Million** | **Minimal string inspection** |
| **Filesystem Path Jail Canonicalization** | **~2.2 ops/ms** | **> 2,200 / sec** | **Zero external IO syscalls** |

*Measured on Windows 11 x64, Intel Core i5 (Surface Pro 8), JDK 21.0.12.1.*

---

## API Quick Reference

| Method / Class | Return Type | Description |
|:---|:---|:---|
| `sandbox.executeSafe(command)` | `boolean` | Validates command against destructive blacklists and execution policies. |
| `sandbox.isPathPermitted(relativePath)` | `boolean` | Validates that a path does not escape the virtual filesystem jail. |
| `sandbox.getPolicy()` | `ExecutionPolicy` | Retrieves active security policy and rule configurations. |

---

## Technical Demos & Benchmarks

| Case | Java Example | Launcher | Description |
|:---|:---|:---|:---|
| **Interactive 120-Column HUD Demo** | [Demo.java](examples/Demo/src/main/java/fastaisandbox/demo/Demo.java) | `run-demo.bat` | Terminal demonstration of command blocking and virtual path jail traversal protection. |
| **JMH Microbenchmark Suite** | [Benchmark.java](examples/Benchmark/src/main/java/fastaisandbox/benchmark/Benchmark.java) | `run-benchmark.bat` | Formal OpenJDK JMH throughput measurements across command policy and path jail kernels. |

---

## Installation

### Option 1: Maven (Recommended via JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastAISandbox</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastAISandbox:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. 📦 **[FastAISandbox-0.1.0.jar](https://github.com/andrestubbe/FastAISandbox/releases/download/0.1.0/FastAISandbox-0.1.0.jar)** (The Core Sandbox Engine)
2. ⚙️ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (The Mandatory Runtime Substrate)

---

## Documentation

- **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, security policies, and filesystem jail specs.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The architectural rationale for in-process JVM sandboxing.
- **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones, native Windows Job Objects, and Linux cgroups.
- **[CHANGELOG.md](docs/CHANGELOG.md)**: Release history and version migration details.
- **[COMPILE.md](docs/COMPILE.md)**: Build instructions and source compilation guide.

---

## Platform Support

| Platform | Architecture | Status | Notes |
|:---|:---|:---|:---|
| Windows 10/11 | x64, ARM64 | ✅ Fully Supported | Native high-performance pure Java |
| Linux | x64, ARM64 | ✅ Fully Supported | Tested on Ubuntu / Debian / RHEL |
| macOS | Apple Silicon, x64 | ✅ Fully Supported | Tested on macOS Sonoma / Sequoia |

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastAI](https://github.com/andrestubbe/FastAI) — Unified AI client interface for Java
- [FastAIAgent](https://github.com/andrestubbe/FastAIAgent) — Autonomous agent loop, intent-graphs, and tool execution
- [FastAIBot](https://github.com/andrestubbe/FastAIBot) — Zero-bloat bot harnesses and persona runtime
- [FastAIGraph](https://github.com/andrestubbe/FastAIGraph) — In-memory knowledge graph and multi-hop relationship engine
- [FastAIGuard](https://github.com/andrestubbe/FastAIGuard) — Deterministic AI prompt injection and tool firewall
- [FastAIHybrid](https://github.com/andrestubbe/FastAIHybrid) — Dense-sparse hybrid search fusion (BM25 + Vectors)
- [FastAIMatcher](https://github.com/andrestubbe/FastAIMatcher) — Automated SOX compliance and hybrid rule matching engine
- [FastAIMCP](https://github.com/andrestubbe/FastAIMCP) — Model Context Protocol (MCP) server & tool integration
- [FastAIMemory](https://github.com/andrestubbe/FastAIMemory) — Conversation history, sliding windows, and rolling summaries
- [FastAIMetrics](https://github.com/andrestubbe/FastAIMetrics) — Ultra-fast lock-free token, latency, cost tracking and evaluation engine
- [FastAIModel](https://github.com/andrestubbe/FastAIModel) — Native local inference runtime (GGUF/ONNX)
- [FastAIRag](https://github.com/andrestubbe/FastAIRag) — Ultra-fast document chunking and vector retrieval
- [FastAIReasoner](https://github.com/andrestubbe/FastAIReasoner) — Deterministic planning, chain-of-thought, and self-correction
- [FastAIRerank](https://github.com/andrestubbe/FastAIRerank) — Cross-encoder relevance filtering and Top-N prompt pruner
- [FastAIRuntime](https://github.com/andrestubbe/FastAIRuntime) — Sandboxed process runner and tool-calling execution pipeline
- [FastAIState](https://github.com/andrestubbe/FastAIState) — Lock-free shared agent state & blackboard memory
- [FastAIVectorDB](https://github.com/andrestubbe/FastAIVectorDB) — High-throughput SIMD/AVX2 vector database
- [FastAIVision](https://github.com/andrestubbe/FastAIVision) — High-speed local multimodal vision, UI-element grounding, and screen-VLM engine
- [FastCore](https://github.com/andrestubbe/FastCore) — Unified JNI loader and platform abstraction

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*