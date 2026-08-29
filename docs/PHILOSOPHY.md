# FastAISandbox Philosophy — Microsecond In-Process Cages

1. **Lightweight Over Heavyweight**: Spawning Docker containers takes seconds; FastAISandbox validates security in microseconds.
2. **Zero Trust for LLM Output**: Agent code and tool calls must be treated as untrusted user input.
3. **Pure Java Portability**: Security governance should work out-of-the-box on any standard JVM.