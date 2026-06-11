package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.AIChatEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.*;
import org.example.cmc_backend.Models.Request.ChatRequest;
import org.example.cmc_backend.Models.Response.ChatResponse;
import org.example.cmc_backend.Service.AiChatService;
import org.example.cmc_backend.Service.AiContextService;
import org.example.cmc_backend.Service.AiConversationService;
import org.example.cmc_backend.Service.AiProviderService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class AiChatServiceImplement implements AiChatService {
    private static final int MAX_MESSAGE_LENGTH = 1000;

    private final AiConversationService conversationService;
    private final AiContextService contextService;
    private final AiProviderService aiProviderService;
    private final Executor aiChatTaskExecutor;

    public AiChatServiceImplement(
            @Qualifier("aiConversationServiceImplement") AiConversationService conversationService,
            @Qualifier("aiContextServiceImplement") AiContextService contextService,
            AiProviderService aiProviderService,
            @Qualifier("aiChatTaskExecutor") Executor aiChatTaskExecutor
    ) {
        this.conversationService = conversationService;
        this.contextService = contextService;
        this.aiProviderService = aiProviderService;
        this.aiChatTaskExecutor = aiChatTaskExecutor;
    }

    @Override
    public ChatResponse chat(UserEntity user, ChatRequest request) {
        String message = validateMessage(request);
        AIChatEntity conversation = conversationService.findOrCreateConversation(user, request == null ? null : request.getConversationId());
        AiContextResult context = contextService.buildContext(user, message);
        ChatMessageDTO userMessage = conversationService.saveUserMessage(user, conversation.getIdSession(), message, context.getIntent());

        AiGenerationResult generation = aiProviderService.generate(user, message, context);

        ChatMessageDTO aiMessage = conversationService.saveAiMessage(
                user,
                conversation.getIdSession(),
                generation.getContent(),
                context.getIntent(),
                context.hasDatabaseData() ? 90 : 0
        );
        conversationService.touchConversation(conversation.getIdSession(), context.getIntent());

        return new ChatResponse(
                conversation.getIdSession(),
                userMessage,
                aiMessage,
                context.getIntent(),
                context.toDto(),
                generation.getProvider(),
                generation.getModel(),
                generation.isDatabaseFallback(),
                aiProviderService.usageLimits(generation.getProvider(), generation.getModel())
        );
    }

    @Override
    public SseEmitter stream(UserEntity user, ChatRequest request) {
        SseEmitter emitter = new SseEmitter(120_000L);
        aiChatTaskExecutor.execute(() -> {
            try {
                safeSend(emitter, "status", ChatStreamEvent.status(null, "connected"));
                ChatResponse response = chat(user, request);
                safeSend(emitter, "metadata", ChatStreamEvent.status(response.getConversationId(), "answering"));
                for (String chunk : chunks(response.getAiMessage().getMessageText())) {
                    safeSend(emitter, "chunk", ChatStreamEvent.chunk(response.getConversationId(), chunk));
                }
                safeSend(emitter, "done", ChatStreamEvent.done(response));
                safeComplete(emitter);
            } catch (Exception exception) {
                safeSend(emitter, "error", ChatStreamEvent.error(exception.getMessage()));
                safeComplete(emitter);
            }
        });
        return emitter;
    }

    @Override
    public List<ChatHistoryDTO> history(UserEntity user) {
        return conversationService.getHistory(user);
    }

    @Override
    public AiUsageLimitDTO usageLimits(UserEntity user) {
        return aiProviderService.usageLimits(user);
    }

    @Override
    public void deleteHistory(UserEntity user, String conversationId) {
        if (conversationId == null || conversationId.isBlank()) {
            throw new IllegalArgumentException("conversationId khong hop le");
        }
        conversationService.deleteHistory(user, conversationId);
    }

    private String validateMessage(ChatRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            throw new IllegalArgumentException("Noi dung chat khong duoc de trong");
        }
        String message = request.getMessage().trim();
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Noi dung chat toi da " + MAX_MESSAGE_LENGTH + " ky tu");
        }
        return message;
    }

    private List<String> chunks(String content) {
        if (content == null || content.isBlank()) {
            return List.of("");
        }
        return content.lines()
                .flatMap(line -> splitLine(line).stream())
                .toList();
    }

    private List<String> splitLine(String line) {
        if (line.length() <= 90) {
            return List.of(line + "\n");
        }
        ArrayList<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < line.length()) {
            int end = Math.min(start + 90, line.length());
            chunks.add(line.substring(start, end));
            start = end;
        }
        chunks.set(chunks.size() - 1, chunks.get(chunks.size() - 1) + "\n");
        return chunks;
    }

    private void send(SseEmitter emitter, String eventName, ChatStreamEvent event) throws IOException {
        emitter.send(SseEmitter.event().name(eventName).data(event));
    }

    private void safeSend(SseEmitter emitter, String eventName, ChatStreamEvent event) {
        try {
            send(emitter, eventName, event);
        } catch (Exception ignored) {

        }
    }

    private void safeComplete(SseEmitter emitter) {
        try {
            emitter.complete();
        } catch (Exception ignored) {

        }
    }
}
