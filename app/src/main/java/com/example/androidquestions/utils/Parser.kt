package com.example.androidquestions.utils

import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.topics.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class Parser {

    companion object {
        private const val BASE_URL = "https://itsobes.ru"
        private const val URL_TAGS = "/AndroidSobes/tags/"
        private const val TAGS_POSTS = "tags posts"
        private const val LI = "li"
        private const val A = "a"
        private const val HREF = "href"
    }

    private var topicsDoc: Document = runBlocking { getTopicsDoc() }

    suspend fun parseTopics(): List<Topic> = coroutineScope {
        val topics = mutableListOf<Topic>()
        topicsDoc.getElementsByClass(TAGS_POSTS)
            .select(LI)
            .forEachIndexed { index, element ->
                val topic = Topic(id = index + 1, title = element.text())
                topics.add(topic)
            }
        return@coroutineScope topics
    }

    suspend fun parseQuestions(): MutableList<Question> {
        var questionId = 1
        val questions = mutableListOf<Question>()
        val topicsLinks: MutableList<String>? = topicsDoc.getElementsByClass(TAGS_POSTS).links()
        topicsLinks?.forEachIndexed { topicId, topicLink ->
            val questionsDoc = getQuestionsDoc(topicLink)
            questionsDoc.getElementsByClass(TAGS_POSTS).select(LI).forEach {
                val question = Question(
                    id = questionId,
                    topicId = topicId + 1,
                    title = it.text(),
                    url = "$BASE_URL${it.select(A).attr(HREF)}"
                )
                questions.add(question)
                questionId++
            }
        }
        return questions
    }

    private suspend fun getTopicsDoc(): Document = withContext(Dispatchers.IO) {
        Jsoup.connect("$BASE_URL$URL_TAGS").get()
    }

    private suspend fun getQuestionsDoc(topicLink: String): Document = withContext(Dispatchers.IO) {
        Jsoup.connect("$BASE_URL$topicLink").get()
    }

    private fun Elements.links(): MutableList<String>? {
        return select(LI)
            .select(A)
            .eachAttr(HREF)
    }
}