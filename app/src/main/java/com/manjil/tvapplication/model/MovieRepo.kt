package com.manjil.tvapplication.model

class MovieRepo {
    fun getMovieList(): List<Movie>{
        val movieList = ArrayList<Movie>()
        movieList.add(
            Movie(
                "First Title",
                "Description for first title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
            )
        )
        movieList.add(
            Movie(
                "Second Title",
                "Description for second title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
            )
        )
        movieList.add(
            Movie(
                "Third Title",
                "Description for third title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            )
        )
        return movieList
    }
}