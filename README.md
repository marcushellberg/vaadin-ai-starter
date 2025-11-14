# Vaadin + Spring AI Integration Example

A modern AI-powered chat application built with Vaadin and Spring AI, demonstrating how to create intelligent, interactive web applications with server-side Java UI components, LLM integration, tool calling, and MCP (Model Context Protocol) server support.

## Features

- 🎨 **Modern Vaadin UI**: Server-side Java components with real-time streaming chat interface
- 🤖 **Spring AI Integration**: Seamless integration with OpenAI's GPT models
- 🔧 **Tool Calling**: Expose Java methods to the LLM via `@Tool` annotations
- 🌐 **REST API Integration**: Call external APIs from AI tool functions
- 📡 **MCP Server Support**: Connect to Model Context Protocol servers for extended capabilities
- ⚡ **Server Push**: Real-time streaming responses with Vaadin Push
- 🎯 **Type-safe Development**: Full Java type safety from UI to AI integration

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Vaadin 24.9.4** - Server-side web framework
- **Spring AI 1.1.0** - LLM integration framework
- **OpenAI GPT 5.1** - Language model
- **Maven** - Build system

## Prerequisites

- Java 21 or later
- OpenAI API key
- Maven 3.6+ (or use the included wrapper)
- **Recommended**: [Vaadin IDE plugin](https://plugins.jetbrains.com/plugin/8917-vaadin) for IntelliJ IDEA or VSCode

## Getting Started

### 1. Set up OpenAI API Key

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your-api-key-here
```

Or add it to your IDE's run configuration.

### 2. Run the Application

#### Option A: Using Vaadin IDE Plugin (Recommended)

For the best development experience with **hotswap** and instant code updates:

1. Open the project in your IDE
2. Install the Vaadin plugin for IntelliJ IDEA or VSCode/Cursor/Windsurf through the plugin store
3. Navigate to `Application.java`
4. Select the **Debug with Hotswap** button in the plugin toolbar, or right-click `Application.java` and select **"Debug with Hotswap"**
5. The application will start with HotswapAgent enabled, allowing you to modify Java code without restarting

The app will be available at: http://localhost:8080

With hotswap enabled, you can modify your Java code and see changes instantly without server restart!

#### Option B: Using Maven

If you prefer command-line or don't have the Vaadin plugin:

```bash
./mvnw spring-boot:run
```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run
```

The application will start and open automatically in your browser at http://localhost:8080

### 3. Try the Chat Interface

Ask the AI to use the available tools:
- "What is the factorial of 10?"
- "What are today's electricity prices in Finland?"
- "Search the Vaadin documentation for Button components" (via MCP server)

## Project Structure

```
src/main/java/com/vaadin/example/ai/
├── Application.java                 # Spring Boot entry point with @Push enabled
├── service/
│   └── SampleService.java          # Service with @Tool annotated methods
└── ui/
    ├── MainLayout.java             # App layout with navigation drawer
    └── AiChat.java                 # Chat UI with streaming message list

src/main/resources/
├── application.properties          # Spring configuration
└── mcp.json                       # MCP server configurations
```

## Key Concepts

### 1. Vaadin UI Components

The chat interface is built entirely in Java using Vaadin components:

```java
@Route("")
@Menu(title = "AI Chat")
public class AiChat extends VerticalLayout {
    // MessageList displays chat history
    // MessageInput handles user input
    // Streaming responses update UI in real-time
}
```

### 2. Spring AI ChatClient

The `ChatClient` is configured with tools and MCP servers:

```java
chatClient = builder
    .defaultToolCallbacks(tools)      // MCP server tools from mcp.json
    .defaultTools(sampleService)      // Local @Tool methods
    .build();
```

### 3. Tool Calling with @Tool

Expose Java methods to the LLM by annotating them with `@Tool`:

```java
@Tool(description = "Calculate factorial of a number")
public BigInteger factorial(
    @ToolParam(description = "The number to calculate the factorial of") int n
) {
    // Implementation
    return result;
}
```

The LLM can automatically call these methods when needed to answer user queries.

### 4. REST API Integration

Tools can call external APIs:

```java
@Tool(description = "Fetch today's electricity prices from Liukuri.fi")
public String fetchTodaysElectricityPricesJson() {
    return restClient
        .get()
        .uri("https://liukuri.fi/api/todaysPrices.json")
        .retrieve()
        .body(String.class);
}
```

### 5. MCP Server Support

The project includes MCP (Model Context Protocol) server integration. Configure servers in `src/main/resources/mcp.json`:

```json
{
  "mcpServers": {
    "Vaadin": {
      "command": "npx",
      "args": ["-y", "mcp-remote", "https://mcp.vaadin.com/docs"]
    }
  }
}
```

MCP servers provide additional capabilities like searching documentation, accessing databases, or integrating with external services.

### 6. Streaming Responses

Responses are streamed token-by-token for a smooth user experience:

```java
chatClient.prompt()
    .user(userPrompt)
    .stream()
    .content()
    .subscribe(token ->
        ui.access(() -> responseItem.appendText(token))
    );
```

The `@Push` annotation on `Application.java` enables server-to-client push for real-time updates.

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-5.1

# MCP Configuration
spring.ai.mcp.client.stdio.servers-configuration=classpath:mcp.json

# Vaadin
vaadin.launch-browser=true
```

## Building for Production

To create a production-optimized build:

```bash
./mvnw clean package -Pproduction
```

This creates an executable JAR in `target/` with:
- Optimized frontend bundle
- Minified JavaScript and CSS
- Production-ready Vaadin components

Run the production JAR:

```bash
java -jar target/ai-0.0.1-SNAPSHOT.jar
```

## Customization Ideas

Extend this starter project with:

- **RAG (Retrieval-Augmented Generation)**: Add vector stores and document embedding
- **Conversation Memory**: Configure chat history and context retention
- **Custom Tools**: Add more `@Tool` methods for your domain logic
- **Multiple Views**: Create additional Vaadin views with different AI use cases
- **Authentication**: Add Spring Security for user management
- **Database Integration**: Store chat history with Spring Data JPA
- **Custom MCP Servers**: Build your own MCP servers for specific integrations
- **Multi-modal AI**: Add image analysis with vision models

## Development Tips

### Hot Reload

When using the Vaadin IDE plugin with hotswap:
- Java code changes apply instantly (no restart needed)
- Frontend changes are detected automatically
- Spring beans are reloaded when modified

### Debugging

- Add breakpoints in your `@Tool` methods to see when the LLM calls them
- Check console output for tool execution logs
- Use browser DevTools to inspect WebSocket traffic for Push updates

### Testing

Run tests with:

```bash
./mvnw test
```

## Learn More

- [Vaadin Documentation](https://vaadin.com/docs)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [ChatClient API](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
- [Vaadin IDE Plugin](https://vaadin.com/docs/latest/tools/ide-plugins)
- [Model Context Protocol (MCP)](https://modelcontextprotocol.io/)

## License

This is an example project for demonstration purposes. Feel free to use it as a starting point for your own AI-powered applications.

