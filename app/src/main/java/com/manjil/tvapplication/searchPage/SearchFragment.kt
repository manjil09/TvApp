package com.manjil.tvapplication.searchPage

import android.os.Bundle
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.app.SearchSupportFragment.SearchResultProvider
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import com.manjil.tvapplication.CardPresenter
import com.manjil.tvapplication.model.MovieRepo

class SearchFragment : SearchSupportFragment(), SearchResultProvider {
    private val movieList = MovieRepo().getMovieList()
    private lateinit var rowsAdapter: ArrayObjectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        setSearchResultProvider(this)
    }

    private fun loadRows(query: String) {
        rowsAdapter.clear()
        val result = movieList.filter { movie ->
            movie.title.lowercase().contains(query.lowercase())
        }
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        listRowAdapter.addAll(0, result)
        val header = HeaderItem("Search Results")

        rowsAdapter.add(ListRow(header, listRowAdapter))
    }

    /**
     *
     * Method invoked some time prior to the first call to onQueryTextChange to retrieve
     * an ObjectAdapter that will contain the results to future updates of the search query.
     *
     *
     * As results are retrieved, the application should use the data set notification methods
     * on the ObjectAdapter to instruct the SearchSupportFragment to update the results.
     *
     * @return ObjectAdapter The result object adapter.
     */
    override fun getResultsAdapter(): ObjectAdapter {
        return rowsAdapter
    }

    /**
     *
     * Method invoked when the search query is updated.
     *
     *
     * This is called as soon as the query changes; it is up to the application to add a
     * delay before actually executing the queries if needed.
     *
     *
     * This method might not always be called before onQueryTextSubmit gets called, in
     * particular for voice input.
     *
     * @param newQuery The current search query.
     * @return weather the results changed as a result of the query.
     */
    override fun onQueryTextChange(newQuery: String?): Boolean {
//        loadRows(newQuery ?: "")
        return true
    }

    /**
     * Method invoked when the search query is submitted, either by dismissing the keyboard,
     * pressing search or next on the keyboard or when voice has detected the end of the query.
     *
     * @param query The query entered.
     * @return weather the results changed as a result of the query.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        loadRows(query ?: "")
        return true
    }

}