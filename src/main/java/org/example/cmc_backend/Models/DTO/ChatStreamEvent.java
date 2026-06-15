package org.example.cmc_backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.cmc_backend.Models.Response.ChatResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatStreamEvent {
    private String type;
    private String conversationId;
    private String content;
    private ChatResponse response;
    private String error;

    public static ChatStreamEvent status(String conversationId, String content) {
        return new ChatStreamEvent("status", conversationId, content, null, null);
    }

    public static ChatStreamEvent chunk(String conversationId, String content) {
        return new ChatStreamEvent("chunk", conversationId, content, null, null);
    }

    public static ChatStreamEvent done(ChatResponse response) {
        return new ChatStreamEvent("done", response.getConversationId(), null, response, null);
    }

    public static ChatStreamEvent error(String message) {
        return new ChatStreamEvent("error", null, null, null, message);
    }
}
