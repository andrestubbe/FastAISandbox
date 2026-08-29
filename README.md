# FastAISandbox 0.1.0 [ALPHA] — In-Process Security Governor & Isolation Jail for Java AI Agents

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastAISandbox/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Cross--Platform-lightgrey.svg)]()
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
- [Quick Start](#quick-start)
- [Features](#features)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Technical Examples & Hero Demos](#technical-examples--hero-demos)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastAISandbox?

Autonomous agents generating code and executing host tools present significant security risks:

- **Heavyweight Containers**: Spinning up Docker containers introduces 500–2,000 ms startup latency per tool call.
- **Destructive OS Actions**: Uncontrolled LLM code execution can accidentally delete files or trigger fork bombs.
- **Path Traversal Attacks**: Malicious tools can escape working directories via relative `../` traversal.

**FastAISandbox** solves this:

- **Microsecond Policy Validation**: Inspects commands and validates execution policies in under **1 microsecond**.
- **Virtual Chroot Jail**: Traps file writes and read permissions strictly inside an isolated temporary directory tree.
- **Zero External Daemons**: Runs 100% within the JVM without Docker, WSL, or root privileges.

---

## Features

- **🛡️ Command Security Policy**: Intercepts destructive shell commands, fork bombs, and formatting tools.
- **📁 Virtual Filesystem Jail**: Restricts file modifications strictly to sandboxed workspace directories.
- **⏱️ CPU & Execution Governance**: Enforces millisecond-level execution deadlines.
- **⚡ Extreme Throughput**: Validates over 13,400,000 commands per second on the JVM.
- **📊 FastANSI 120-Column HUD**: Terminal telemetry displaying security verdicts, permitted paths, and resource limits.

---

## Performance Benchmarks

FastAISandbox is rigorously profiled using **JMH** to guarantee zero overhead.

| Metric / Operation Type | Score (ops/ms) | Ops per Second |
|---|---|---|
| **Command Security Policy Validation** | **~13,443 ops/ms** | **> 13.4 Million** |
| **Filesystem Path Jail Canonicalization** | **~3.67 ops/ms** | **> 3,670** |

*Measured on Windows 11 x64, Intel Core i5 (Surface Pro 8), JDK 21.0.12.1.*

---

## API Quick Reference

| Method | Description |
|---|---|
| `sandbox.executeSafe(command)` | Validates and executes tool commands under policy rules. |
| `sandbox.isPathPermitted(relativePath)` | Validates that a path does not escape the virtual filesystem jail. |
| `sandbox.getPolicy()` | Returns current execution governance policy. |

---

## Technical Examples & Hero Demos

| Case | Java Example | Launcher | Description |
|---|---|---|---|
| **Interactive 120-Column HUD Demo** | [Demo.java](src/main/java/fastaisandbox/Demo.java) | `run-demo.bat` | Terminal demonstration of command blocking and virtual path jail traversal protection. |
| **JMH Microbenchmark Suite** | [FastAISandboxBenchmark.java](examples/Benchmark/src/main/java/fastaisandbox/benchmark/FastAISandboxBenchmark.java) | `run-benchmark.bat` | Formal OpenJDK JMH throughput measurements across policy and jail kernels. |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependency to your `pom.xml`:

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

* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, security policies, and filesystem jail specs.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The architectural rationale for in-process JVM sandboxing.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones, native Windows Job Objects, and Linux cgroups.
* **[CHANGELOG.md](docs/CHANGELOG.md)**: Release history and version migration details.

---

## Platform Support

| Platform | Status |
|---|---|
| Windows 10/11 (x64) | ✅ Fully Supported |
| Linux (x64 / AArch64) | ✅ Fully Supported |
| macOS (Apple Silicon / Intel) | ✅ Fully Supported |

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---

## Related Projects

Combine FastAISandbox with other FastJava security and agent engines:

* [**FastAIGuard**](https://github.com/andrestubbe/FastAIGuard) — Deterministic AI prompt injection and tool firewall.
* [**FastAIAgent**](https://github.com/andrestubbe/FastAIAgent) — Autonomous agent orchestration substrate.
* [**FastAIMCP**](https://github.com/andrestubbe/FastAIMCP) — Model Context Protocol tool runtime.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster.*