package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.example.cmc_backend.Service.AiPromptService;
import org.springframework.stereotype.Service;

@Service
public class AiPromptServiceImplement implements AiPromptService {

    public static final String EXTERNAL_SOURCE_NOTE = "Lưu ý: Thông tin này không thuộc dữ liệu chính thức của website Greeny.";

    @Override
    public String buildSystemPrompt(String customPrompt) {
        String basePrompt = String.join("\n",
                "Bạn là trợ lý AI của hệ thống đặt vé xem phim CMC.",
                "DATABASE_CONTEXT là dữ liệu chính thức của website CMC về phim, lịch chiếu, rạp chiếu, phòng chiếu, ghế ngồi, giá vé, khuyến mãi, người dùng và đơn hàng.",
                "Ưu tiên DATABASE_CONTEXT khi câu hỏi liên quan trực tiếp đến website CMC.",
                "Được sử dụng kiến thức chung hoặc thông tin bên ngoài để trả lời khi DATABASE_CONTEXT không có dữ liệu phù hợp.",
                "Mọi thông tin không nằm trong DATABASE_CONTEXT phải được đánh dấu rõ bằng câu: \"" + EXTERNAL_SOURCE_NOTE + "\"",
                "Không được tự bịa thông tin về phim, lịch chiếu, giá vé, ghế trống, khuyến mãi, rạp chiếu, chính sách hoặc đơn hàng của CMC.",
                "Khi người dùng hỏi về phim đang chiếu, lịch chiếu, giá vé hoặc khuyến mãi, chỉ trả lời dựa trên dữ liệu trong DATABASE_CONTEXT.",
                "Nếu không tìm thấy dữ liệu trong DATABASE_CONTEXT, hãy thông báo rõ ràng rằng hệ thống hiện không có thông tin đó.",
                "Trả lời thân thiện, ngắn gọn, dễ hiểu, ưu tiên tiếng Việt.",
                "Với thông tin cá nhân, vé đã đặt hoặc lịch sử giao dịch, chỉ sử dụng dữ liệu của người dùng hiện tại nếu DATABASE_CONTEXT có cung cấp.",
                "Hỗ trợ người dùng tra cứu phim, lịch chiếu, giá vé, khuyến mãi, thông tin rạp và tình trạng đơn đặt vé."
        );

        if (customPrompt == null || customPrompt.isBlank()) {
            return basePrompt;
        }
        return basePrompt + "\n\nCUSTOM_AI_SETTING:\n" + customPrompt.trim();
    }

    @Override
    public String buildUserPrompt(AiContextResult context, String question) {
        String databaseContext = context.hasDatabaseData()
                ? context.getDatabaseContext()
                : "No matching database records were found for this question.";
        return "DATABASE_CONTEXT_STATUS: "
                + (context.hasDatabaseData() ? "MATCHED" : "NO_MATCH")
                + "\nDATABASE_CONTEXT:\n"
                + databaseContext
                + "\n\nUSER_QUESTION:\n\""
                + safe(question)
                + "\"";
    }

    @Override
    public String ensureExternalSourceNote(String content, AiContextResult context) {
        if (content == null || content.isBlank() || context.hasDatabaseData()) {
            return content;
        }
        if (content.contains(EXTERNAL_SOURCE_NOTE)) {
            return content;
        }
        return EXTERNAL_SOURCE_NOTE + "\n\n" + content.trim();
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }
}
