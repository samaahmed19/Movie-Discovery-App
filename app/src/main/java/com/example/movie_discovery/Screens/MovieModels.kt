package com.example.movie_discovery.Screens

import com.example.movie_discovery.data.Movie
import com.google.gson.annotations.SerializedName


data class MovieDetailsResponse(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("genre_ids")
    val genreIds: List<Int> = emptyList()
)

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int
)

data class MovieSearchResponse(
    val results: List<Movie> = emptyList(),
    val page: Int = 1,
    @SerializedName("total_pages")
    val totalPages: Int = 0
)

data class CategoryMoviesResponse(
    val results: List<Movie> = emptyList(),
    val page: Int = 1,
    @SerializedName("total_pages")
    val totalPages: Int = 0
)

data class MovieDetails(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("genres") val genres: List<Genre> = emptyList()
)

data class Genre(
    val id: Int,
    val name: String
)

data class Category(
    val id: Int,
    val name: String,
    val imageUrl: String
)



val sampleCategories = listOf(
    Category(28, "Action", "https://preview.redd.it/who-in-this-sub-considers-the-wick-series-the-best-action-v0-s8mx3thw9v7b1.jpg?width=1080&crop=smart&auto=webp&s=3c76a06dd55a7d483ffd341e1be18806de5587eb"),
    Category(12, "Adventure", "https://assetsio.gnwcdn.com/IndianaJones_Hero-Key-Art_4K.jpg"),
    Category(16, "Animation", "https://cdn.mos.cms.futurecdn.net/MBWubBiH52qZyvpT8t6FKc.jpg"),
    Category(35, "Comedy", "https://www.usmagazine.com/wp-content/uploads/2021/09/The-Hangover-Cast-Where-Are-They-Now-Feature.jpg"),
    Category(80, "Crime", "https://i0.wp.com/www.dailymaverick.co.za/wp-content/uploads/2022/12/The-Godfather-1-e1672225720710.jpg"),
    Category(18, "Drama", "https://static0.colliderimages.com/wordpress/wp-content/uploads/2023/11/the-shawshank-redemption-poster.jpg"),
    Category(14, "Fantasy", "https://sm.ign.com/ign_mear/cover/h/harry-pott/harry-potter-the-series_qz8f.jpg"),
    Category(36, "History", "https://variety.com/wp-content/uploads/2021/04/300.jpg"),
    Category(27, "Horror", "https://blogs-images.forbes.com/jeffewing/files/2018/09/Valak.jpg"),
    Category(10402, "Music", "https://www.delfontmackintosh.co.uk/imgs/media/45f3b43409d36bb96dfa3243d8711612/mamma-mia-london-production-1102-1332-photo-by-brinkhoff-moegenburg.jpg"),
    Category(9648, "Mystery", "https://files.pikosky.sk/files/uploads/2012/02/sherlock-holmes-game-of-shadows-poster-final.jpg"),
    Category(10749, "Romance", "https://occ-0-8407-114.1.nflxso.net/dnm/api/v6/E8vDc_W8CLv7-yMQu8KMEC7Rrr8/AAAABegKrvKk6xSPjRB3xULrR7smkeT-j9WRASWE55g7DrVYxF8Lml5d15bCEmpoEUEufxBgcU2_WgZr0FX9gndmeVtvlnBkZYtvIjYk.jpg?r=1e8"),
    Category(878, "Sci-Fi", "https://www.slamedia.org/wp-content/uploads/2014/11/interstellar-feat.jpeg"),
    Category(10752, "War", "https://m.media-amazon.com/images/M/MV5BMGQ0N2Y3MzQtNWVhYi00OWYwLWFlZmQtYTkyOTliYzI4YzJmXkEyXkFqcGdeQWFybm8@._V1_.jpg"),
    Category(37, "Western", "https://www.hollywoodreporter.com/wp-content/uploads/2013/02/django_1.jpg")
)

