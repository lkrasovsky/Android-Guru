package com.example.androidquestions.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidquestions.R
import com.example.androidquestions.room.AndroidQuestionsDatabase
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.questions.QuestionsRepository
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.room.topics.TopicsRepository
import com.example.androidquestions.utils.SharedPrefKeys
import com.example.androidquestions.utils.goneWithScale
import com.example.androidquestions.utils.putBoolean
import com.example.androidquestions.utils.visibleWithScale
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val database: AndroidQuestionsDatabase by inject()
    private val topicsRepository: TopicsRepository by inject()
    private val questionsRepository: QuestionsRepository by inject()
    private val preferences: SharedPreferences by inject()
    private var isFirstLaunch: Boolean = true
        get() {
            return preferences.getBoolean(SharedPrefKeys.IS_FIRST_LAUNCH, true)
        }
        set(value) {
            field = value
            preferences.putBoolean(SharedPrefKeys.IS_FIRST_LAUNCH, value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (isFirstLaunch) {
            isFirstLaunch = false
            GlobalScope.launch { uploadData() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> uploadData()
            R.id.action_search -> search()
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun uploadData() {
        val updateButton = findViewById<View>(R.id.action_update)
        updateButton?.goneWithScale()
        GlobalScope.async {
            database.clearAllTables()
        }.invokeOnCompletion {
            GlobalScope.async {
                val topicsDoc = getTopicsDoc()
                parseTopics(topicsDoc)
                parseQuestions(topicsDoc)
            }.invokeOnCompletion {
                runOnUiThread { updateButton?.visibleWithScale() }
            }
        }
    }

    private suspend fun parseTopics(topicsDoc: Document) = coroutineScope {
        val topics = mutableListOf<Topic>()
        topicsDoc.getElementsByClass("tags posts")
            .select("li")
            .forEachIndexed { index, element ->
                val topic = Topic(id = index + 1, title = element.text())
                topics.add(topic)
            }
        topicsRepository.insert(topics)
    }

    private suspend fun parseQuestions(topicsDoc: Document) = coroutineScope {
        var questionId = 1
        val questions = mutableListOf<Question>()
        val topicsLinks: MutableList<String>? = topicsDoc.getElementsByClass("tags posts").links()
        topicsLinks?.forEachIndexed { topicId, topicLink ->
            val questionsDoc = getQuestionsDoc(topicLink)
            questionsDoc.getElementsByClass("tags posts").select("li").forEach {
                val question = Question(
                    id = questionId,
                    topicId = topicId + 1,
                    title = it.text(),
                    link = "https://itsobes.ru${it.select("a").attr("href")}"
                )
                questions.add(question)
                questionId++
            }
        }
        Log.d(TAG, questions.toString())
        questionsRepository.insert(questions)
    }

    private suspend fun getTopicsDoc(): Document = withContext(Dispatchers.IO) {
        Jsoup.connect("https://itsobes.ru/AndroidSobes/tags/").get()
    }

    private suspend fun getQuestionsDoc(topicLink: String): Document = withContext(Dispatchers.IO) {
        Jsoup.connect("https://itsobes.ru$topicLink").get()
    }

    private fun Elements.links(): MutableList<String>? {
        return select("li")
            .select("a")
            .eachAttr("href")
    }

    private fun search() {}
}