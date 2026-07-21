package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.AIChatEntity;
import org.example.cmc_backend.Entity.MessengerEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.ChatHistoryDTO;
import org.example.cmc_backend.Models.DTO.ChatMessageDTO;
import org.example.cmc_backend.Repository.AiChatRepository;
import org.example.cmc_backend.Repository.MessengerRepository;
import org.example.cmc_backend.Service.AiConversationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AiConversationServiceImplement implements AiConversationService {
    private static final int STATUS_ACTIVE = 1;
    private static final int LEGACY_MESSAGE_TEXT_LIMIT = 10000000;
    private static final String SENDER_USER = "USER";
    private static final String SENDER_AI = "AI";
    private static final String TRUNCATED_SUFFIX = "\n[Da rut gon khi luu lich su vi cot MESSAGE_TEXT trong database dang qua ngan.]";

    private final AiChatRepository aiChatRepository;
    private final MessengerRepository messengerRepository;

    public AiConversationServiceImplement(AiChatRepository aiChatRepository, MessengerRepository messengerRepository) {
        this.aiChatRepository = aiChatRepository;
        this.messengerRepository = messengerRepository;
    }

    @Transactional
    @Override
    public AIChatEntity findOrCreateConversation(UserEntity user, String conversationId) {
        if (conversationId != null && !conversationId.isBlank()) {
            return aiChatRepository.findByIdSessionAndUserEntity_IdUser(conversationId.trim(), user.getIdUser())
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay cuoc tro chuyen AI"));
        }

        LocalDateTime now = LocalDateTime.now();
        AIChatEntity aiChat = new AIChatEntity();
        aiChat.setIdSession(UUID.randomUUID().toString());
        aiChat.setCurrentStep("CHAT");
        aiChat.setStatus(STATUS_ACTIVE);
        aiChat.setStartedAt(now);
        aiChat.setCreatedAt(now);
        aiChat.setUpdatedAt(now);
        aiChat.setUserEntity(user);
        return aiChatRepository.save(aiChat);
    }

    @Transactional
    @Override
    public void touchConversation(String conversationId, String intent) {
        AIChatEntity aiChat = aiChatRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay cuoc tro chuyen AI"));
        aiChat.setCurrentStep(intent);
        aiChat.setStatus(STATUS_ACTIVE);
        aiChat.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    @Override
    public ChatMessageDTO saveUserMessage(UserEntity user, String conversationId, String message, String intent) {
        return saveMessage(user, conversationId, SENDER_USER, message, intent, 100);
    }

    @Transactional
    @Override
    public ChatMessageDTO saveAiMessage(UserEntity user, String conversationId, String message, String intent, int confidenceScore) {
        return saveMessage(user, conversationId, SENDER_AI, message, intent, confidenceScore);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatHistoryDTO> getHistory(UserEntity user) {
        return aiChatRepository.findByUserEntity_IdUserOrderByUpdatedAtDesc(user.getIdUser()).stream()
                .map(conversation -> new ChatHistoryDTO(
                        conversation.getIdSession(),
                        conversation.getCurrentStep(),
                        conversation.getStatus(),
                        conversation.getStartedAt(),
                        conversation.getUpdatedAt(),
                        messagesFor(user, conversation.getIdSession())
                ))
                .toList();
    }

    @Transactional
    @Override
    public void deleteHistory(UserEntity user, String conversationId) {
        AIChatEntity conversation = aiChatRepository.findByIdSessionAndUserEntity_IdUser(conversationId, user.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay cuoc tro chuyen AI"));
        messengerRepository.deleteByUserEntity_IdUserAndExtractedData(user.getIdUser(), conversationId);
        aiChatRepository.delete(conversation);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatMessageDTO> recentMessages(UserEntity user) {
        return messengerRepository.findTop12ByUserEntity_IdUserOrderByCreatedAtDesc(user.getIdUser()).stream()
                .sorted(Comparator.comparing(MessengerEntity::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toMessageDto)
                .toList();
    }

    private ChatMessageDTO saveMessage(
            UserEntity user,
            String conversationId,
            String senderType,
            String message,
            String intent,
            Integer confidenceScore
    ) {
        LocalDateTime now = LocalDateTime.now();
        String messageId = UUID.randomUUID().toString();
        MessengerEntity messenger = new MessengerEntity();
        messenger.setIdMessenger(messageId);
        messenger.setSenderType(senderType);
        messenger.setMessageText(toPersistedMessage(message));
        messenger.setIntentDeceted(intent);
        messenger.setExtractedData(conversationId);
        messenger.setConfidentScore(confidenceScore);
        messenger.setCreatedAt(now);
        messenger.setUserEntity(user);
        messengerRepository.save(messenger);
        return new ChatMessageDTO(
                messageId,
                conversationId,
                senderType,
                message,
                intent,
                now
        );
    }

    private List<ChatMessageDTO> messagesFor(UserEntity user, String conversationId) {
        return messengerRepository.findByUserEntity_IdUserAndExtractedDataOrderByCreatedAtAsc(user.getIdUser(), conversationId)
                .stream()
                .map(this::toMessageDto)
                .toList();
    }

    private ChatMessageDTO toMessageDto(MessengerEntity messenger) {
        return new ChatMessageDTO(
                messenger.getIdMessenger(),
                messenger.getExtractedData(),
                messenger.getSenderType(),
                messenger.getMessageText(),
                messenger.getIntentDeceted(),
                messenger.getCreatedAt()
        );
    }

    private String toPersistedMessage(String message) {
        if (message == null || message.length() <= LEGACY_MESSAGE_TEXT_LIMIT) {
            return message;
        }
        int maxContentLength = Math.max(0, LEGACY_MESSAGE_TEXT_LIMIT - TRUNCATED_SUFFIX.length());
        return message.substring(0, maxContentLength) + TRUNCATED_SUFFIX;
    }
}
