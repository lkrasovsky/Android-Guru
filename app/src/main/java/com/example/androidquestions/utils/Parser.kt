package com.example.androidquestions.utils

import com.example.androidquestions.R
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.technologies.Technology
import com.example.androidquestions.room.topics.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup

class Parser {

    private val technologiesLinks = mutableListOf<String>()
    private val topicsLinks = mutableMapOf<Int, MutableList<String>>()

    suspend fun parseTechnologies(): MutableList<Technology> = coroutineScope {
        val technologies = mutableListOf<Technology>()

        val technologiesDoc = withContext(Dispatchers.IO) {
            Jsoup.connect(START_URL).get()
        }

        technologiesDoc.getElementsByClass(SELECTOR_TECHNOLOGIES)
            .select(ATTR_LI)
            .select(ATTR_A)
            .forEachIndexed { technologyId, element ->
                val technology = Technology(
                    id = technologyId,
                    title = element.text(),
                    imageResourceId = when (technologyId) {
                        0 -> R.drawable.programming_logo
                        1 -> R.drawable.java_logo
                        2 -> R.drawable.android_logo
                        else -> throw Exception("Unknown technology id.")
                    }
                )
                technologies.add(technology)
                technologiesLinks.add(element.attr(ATTR_HREF))
            }

        return@coroutineScope technologies
    }

    suspend fun parseTopics(): List<Topic> = coroutineScope {
        var topicId = 0
        val topics = mutableListOf<Topic>()


        technologiesLinks.forEachIndexed { technologyId, technologyLink ->

            topicsLinks[technologyId] = mutableListOf()

            val topicsDoc = runBlocking(Dispatchers.IO) {
                Jsoup.connect("$BASE_URL$technologyLink").get()
            }

            topicsDoc.getElementsByClass(SELECTOR_POSTS)
                .select(ATTR_LI)
                .select(ATTR_A)
                .forEach { element ->
                    val topic = Topic(
                        id = topicId,
                        title = element.text(),
                        technologyId = technologyId
                    )
                    topics.add(topic)
                    topicsLinks[technologyId]?.add(element.attr(ATTR_HREF))
                    topicId++
                }
        }

        return@coroutineScope topics
    }

    suspend fun parseQuestions(): MutableList<Question> = coroutineScope {
        var questionId = 0
        var topicId = 0
        val questions = mutableListOf<Question>()

        topicsLinks.forEach { (technologyId, links) ->
            links.forEach { topicLink ->
                val questionsDoc = try {
                    runBlocking(Dispatchers.IO) {
                        Jsoup.connect("$BASE_URL$topicLink").get()
                    }
                } catch (e: HttpStatusException) {
                    return@forEach
                }

                questionsDoc?.getElementsByClass(SELECTOR_POSTS)?.select(ATTR_LI)?.forEach {
                    val question = Question(
                        id = questionId,
                        topicId = topicId,
                        title = it.text(),
                        url = "$BASE_URL${it.select(ATTR_A).attr(ATTR_HREF)}",
                        technologyId = technologyId
                    )
                    questions.add(question)
                    questionId++
                }
                topicId++
            }
        }

        return@coroutineScope questions
    }

    companion object {
        private const val TAG = "Parser"

        private const val BASE_URL = "https://itsobes.ru"
        private const val START_URL = "$BASE_URL/ITSobes/tags/"

        private const val SELECTOR_TECHNOLOGIES = "Header_mainmenu__3U2Z5"
        private const val SELECTOR_POSTS = "ContentList_posts__KQ_LP"

        private const val ATTR_LI = "li"
        private const val ATTR_A = "a"
        private const val ATTR_HREF = "href"
    }
}