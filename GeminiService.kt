package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent?
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>?
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: GeminiApi = retrofit.create(GeminiApi::class.java)

    suspend fun getStudyCoachInsight(
        streakDays: Int,
        totalHours: Float,
        mockScore: Int,
        syllabusCompletionPercent: Int,
        physicsCompletion: Int,
        chemistryCompletion: Int,
        biologyCompletion: Int
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getFallbackInsight(mockScore, physicsCompletion, biologyCompletion)
        }

        val prompt = """
            You are 'NEETRACKER AI Coach', a highly elite, compassionate, and sharp NEET Medical mentor.
            The student's current preparation stats are:
            - Study Streak: $streakDays days
            - Total Hours Studied: $totalHours hrs
            - Latest Mock Test Score: $mockScore / 720
            - Overall Syllabus Completion: $syllabusCompletionPercent%
            - Physics Completion: $physicsCompletion%
            - Chemistry Completion: $chemistryCompletion%
            - Biology Completion: $biologyCompletion%

            Provide a concise, ultra-motivating 3-part strategic insight:
            1. 🎯 Precision Focus (Highlight what needs immediate attention based on subject stats)
            2. 💡 High-Yield Advice (One actionable tip for NCERT revision or PYQ solving)
            3. 🔥 Doctor's Motivation (One powerful sentence to keep the fire burning towards AIIMS)

            Keep the tone clean, premium, crisp, and under 120 words.
        """.trimIndent()

        try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(parts = listOf(GeminiPart(text = prompt)))
                )
            )
            val response = api.generateContent(apiKey, request)
            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!reply.isNullOrBlank()) reply else getFallbackInsight(mockScore, physicsCompletion, biologyCompletion)
        } catch (e: Exception) {
            getFallbackInsight(mockScore, physicsCompletion, biologyCompletion)
        }
    }

    suspend fun generateChapterQuizOrTips(chapterName: String, subject: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext """
                📌 3 High-Yield NCERT Exam Insights for $chapterName ($subject):
                1. Focus on direct diagram-based questions and labeled exceptions.
                2. Revise past 10-year PYQ patterns—at least 2 questions are directly derived from standard formulas.
                3. Beware of assertion-reason traps: always re-verify if the reason explains the assertion directly.
            """.trimIndent()
        }

        val prompt = """
            Provide 3 high-yield NEET exam tips, common mistake traps, and key formulas/concepts for the chapter '$chapterName' in '$subject'.
            Keep it structured with bullet points, crisp, and directly focused on NEET exam questions.
        """.trimIndent()

        try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(parts = listOf(GeminiPart(text = prompt)))
                )
            )
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Revise NCERT line by line and solve 50+ PYQs to master $chapterName."
        } catch (e: Exception) {
            "Revise NCERT line by line and solve 50+ PYQs to master $chapterName."
        }
    }

    private fun getFallbackInsight(mockScore: Int, physicsCompletion: Int, biologyCompletion: Int): String {
        val weakest = if (physicsCompletion < biologyCompletion) "Physics numericals & formulas" else "Botany NCERT line-by-line tables"
        return """
            🎯 Strategic Focus: Your Biology is pacing strong! Shift 45 minutes daily to $weakest to boost your score beyond 680.
            💡 High-Yield Habit: Review your Error Notebook before starting any new chapter. Revisiting 5 past mistakes gives +20 marks in mocks.
            🔥 AIIMS Mindset: "Consistency beats talent every single day. White coat and stethoscope are waiting for you!"
        """.trimIndent()
    }
}
