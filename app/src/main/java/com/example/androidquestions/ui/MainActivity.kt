package com.example.androidquestions.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.androidquestions.R
import com.example.androidquestions.room.AndroidQuestionsDatabase
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.questions.QuestionsRepository
import com.example.androidquestions.room.topics.TopicsRepository
import com.example.androidquestions.ui.questions_list.QuestionsListFragmentDirections
import com.example.androidquestions.ui.search.SearchAdapter
import com.example.androidquestions.ui.topics.TopicsFragmentDirections
import com.example.androidquestions.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var navController: NavController

    private val updateButton: View?
        get() = findViewById(R.id.action_update)

    private val searchButton: View?
        get() = findViewById(R.id.action_search)

    private lateinit var parser: Parser

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
        setupToolbar()
        initProperties()
        setupSearchInput()
        if (isFirstLaunch) {
            isFirstLaunch = false
            GlobalScope.launch(Dispatchers.IO) { uploadData() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                GlobalScope.launch(Dispatchers.IO) {
                    uploadData()
                }
            }
            R.id.action_search -> search_input.requestFocus()
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        if (search_input.hasFocus()) {
            search_input.clearFocus()
        } else super.onBackPressed()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    private fun initProperties() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        parser = Parser()
    }

    private suspend fun uploadData() = coroutineScope {
        withContext(Dispatchers.Main) {
            showProgressBar()
        }
        database.clearAllTables()
        getTopics()
        getQuestions()
        withContext(Dispatchers.Main) {
            hideProgressBar()
        }
    }

    private fun setupSearchInput() {
        search_input.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) onSearchInputFocused(view) else onSearchInputFocusCleared(view)
        }
        search_input.addTextChangedListener { searchQuestions(it.toString()) }
    }

    private fun searchQuestions(searchText: String) {
        questionsRepository.getAllFilteredByTitle(searchText).observe(
            this,
            Observer {
                setupSearchRecycler(it)
            }
        )
    }

    private fun onSearchInputFocused(view: View) {
        hideContent()
        search_container.visible()
        view.showKeyboard()
    }

    private fun onSearchInputFocusCleared(view: View) {
        showContent()
        search_container.gone()
        view.hideKeyboard()
    }

    private fun setupSearchRecycler(questions: List<Question>) {
        search_recycler.adapter = SearchAdapter(questions) {
            navController.navigate(R.id.topicsFragment)
            openQuestionsListFragment(it.topicId)
            openQuestionFragment(it.id)
            search_input.clearFocus()
        }
    }

    private suspend fun getTopics() = coroutineScope {
        val topics = parser.parseTopics()
        topicsRepository.insert(topics)
    }

    private suspend fun getQuestions() = coroutineScope {
        val questions = parser.parseQuestions()
        questionsRepository.insert(questions)
    }

    private fun showProgressBar() {
        hideContent()
        progress_bar.visible()
    }

    private fun hideProgressBar() {
        showContent()
        progress_bar.gone()
    }

    private fun hideContent() {
        updateButton?.gone()
        searchButton?.gone()
        fragment_container.gone()
    }

    private fun showContent() {
        updateButton?.visibleWithScale()
        searchButton?.visibleWithScale()
        fragment_container.visible()
    }

    private fun openQuestionsListFragment(topicId: Int) {
        val action = TopicsFragmentDirections.actionTopicsFragmentToQuestionsListFragment(topicId)
        navController.navigate(action)
    }

    private fun openQuestionFragment(questionId: Int) {
        val action =
            QuestionsListFragmentDirections.actionQuestionsListFragmentToQuestionFragment(questionId)
        navController.navigate(action)
    }
}