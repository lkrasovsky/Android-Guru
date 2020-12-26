package com.example.androidquestions.utils

import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.technologies.Technology
import com.example.androidquestions.room.topics.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Parser {

    private val technologiesLinks = mutableListOf<String>()
    private val topicsLinks = mutableListOf<String>()

    suspend fun parseTechnologies(): MutableList<Technology> = coroutineScope {
        val technologies = mutableListOf<Technology>()

        val technologiesDoc = withContext(Dispatchers.IO) {
            Jsoup.connect(BASE_URL).get()
        }

        technologiesDoc.getElementsByClass(SELECTOR_TECHNOLOGIES)
            .select(ATTR_LI)
            .filterIndexed { index, element ->
                val technology = Technology(id = index + 1, title = element.text())
                technologies.add(technology)
                technologiesLinks.add(element.attr(ATTR_HREF))
            }
        return@coroutineScope technologies
    }

    suspend fun parseTopics(): List<Topic> = coroutineScope {
        val topics = mutableListOf<Topic>()
        technologiesLinks.forEachIndexed { technologyId, technologyLink ->
            val topicsDoc = runBlocking(Dispatchers.IO) {
                Jsoup.connect("$BASE_URL$technologyLink").get()
            }
            topicsDoc.getElementsByClass(SELECTOR_POSTS)
                .select(ATTR_LI)
                .forEachIndexed { index, element ->
                    val topic = Topic(
                        id = index + 1,
                        title = element.text(),
                        technologyId = technologyId
                    )
                    topics.add(topic)
                    topicsLinks.add(element.attr(ATTR_HREF))
                }
        }
        return@coroutineScope topics
    }

    suspend fun parseQuestions(): MutableList<Question> {
        var questionId = 1
        val questions = mutableListOf<Question>()
        topicsLinks.forEachIndexed { topicId, topicLink ->
            val questionsDoc = getQuestionsDoc(topicLink)
            questionsDoc?.getElementsByClass(SELECTOR_POSTS)?.select(ATTR_LI)?.forEach {
                val question = Question(
                    id = questionId,
                    topicId = topicId + 1,
                    title = it.text(),
                    url = "$BASE_URL${it.select(ATTR_A).attr(ATTR_HREF)}"
                )
                questions.add(question)
                questionId++
            }
        }
        return questions
    }

    private suspend fun getQuestionsDoc(topicLink: String): Document? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Jsoup.connect("$BASE_URL$topicLink").get()
            } catch (e: HttpStatusException) {
                null
            }
        }

    companion object {
        private const val TAG = "Parser"

        private const val BASE_URL = "https://itsobes.ru"

        private const val SELECTOR_TECHNOLOGIES = "mainmenu contentcol"
        private const val SELECTOR_POSTS = "ContentList_posts__KQ_LP"

        private const val ATTR_LI = "li"
        private const val ATTR_A = "a"
        private const val ATTR_HREF = "href"
    }
}