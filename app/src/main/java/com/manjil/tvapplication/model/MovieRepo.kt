package com.manjil.tvapplication.model

class MovieRepo {
    fun getMovieList(): List<Movie>{
        val movieList = ArrayList<Movie>()
        movieList.add(
            Movie(
                "First Title",
                "Description for first title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
            )
        )
        movieList.add(
            Movie(
                "Second Title",
                "Description for second title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg",
            )
        )
        movieList.add(
            Movie(
                "Third Title",
                "Description for third title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/card.jpg",
            )
        )
        movieList.add(
            Movie(
                "Google Demo Slam_ 20ft Search",
                "Description for third title",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
                )
        )
        movieList.add(
            Movie(
                "Introducing Gmail Blue",
                "Description for third title",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg"
            )
        )

        return movieList
    }
}