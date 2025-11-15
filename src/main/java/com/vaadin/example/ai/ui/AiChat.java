package com.vaadin.example.ai.ui;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;

import com.vaadin.example.ai.service.SampleService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@Menu(title = "AI Chat")
@PageTitle("AI Chat")
public class AiChat extends VerticalLayout {

    private final ChatClient chatClient;
    private final MessageList messageList;

    public AiChat(
        ChatClient.Builder builder,
        ToolCallbackProvider mcpTools, // MCPs configured in application.properties
        SampleService sampleService,
        ChatMemory chatMemory
    ) {
        chatClient = builder
            // Configure the system message, memory, RAG in builder
            // see https://docs.spring.io/spring-ai/reference/api/chatclient.html
            .defaultToolCallbacks(mcpTools)
            .defaultTools(sampleService) // Make @Tool annotated methods available to the LLM
            .defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build()
            )
            .build();

        messageList = new MessageList();
        var messageInput = new MessageInput();

        messageList.setSizeFull();
        messageList.setMarkdown(true);

        messageInput.setWidthFull();
        messageInput.addSubmitListener(this::handleMessage);

        addAndExpand(messageList);
        add(messageInput);
    }

    private void handleMessage(MessageInput.SubmitEvent submitEvent) {
        var userPrompt = submitEvent.getValue();

        messageList.addItem(new MessageListItem(userPrompt, "You"));
        
        var responseItem = new MessageListItem("", "Bot");
        messageList.addItem(responseItem);

        var ui = UI.getCurrent();
        chatClient.prompt()
            .user(userPrompt)
            .stream()
            .content()
            .subscribe(token ->
                ui.access(() -> responseItem.appendText(token))
            );
    }
}
 