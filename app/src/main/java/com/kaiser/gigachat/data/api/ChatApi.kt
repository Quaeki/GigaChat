import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApi {
    @POST("/chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse

    @GET("v1/models")
    suspend fun getModels(): ModelsResponse
}

data class ChatRequest(
    val messages: List<Message>,
    val model: String = "deepseek-chat",
    val temperature: Double = 0.7,
    val max_tokens: Int = 2048,
    val stream: Boolean = false // Добавлено
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>,
    val usage: Usage?
)

data class Choice(
    val message: Message,
    val finish_reason: String?
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class ModelsResponse(
    val data: List<Model>,
    @SerializedName("object") val `object`: String
)

data class Model(
    val id: String,
    @SerializedName("object") val `object`: String,
    val created: Long,
    val owned_by: String
)