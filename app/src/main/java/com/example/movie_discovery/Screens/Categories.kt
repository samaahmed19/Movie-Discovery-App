package com.example.movie_discovery.Screens

data class Category(
    val name: String,
    val imageUrl: String
)

val sampleCategories = listOf(
    Category("Action", "https://preview.redd.it/who-in-this-sub-considers-the-wick-series-the-best-action-v0-s8mx3thw9v7b1.jpg?width=1080&crop=smart&auto=webp&s=3c76a06dd55a7d483ffd341e1be18806de5587eb"),
    Category("Adventure", "https://assetsio.gnwcdn.com/IndianaJones_Hero-Key-Art_4K.jpg?width=1200&height=900&fit=crop&quality=100&format=png&enable=upscale&auto=webp"),
    Category("Animation", "https://cdn.mos.cms.futurecdn.net/MBWubBiH52qZyvpT8t6FKc.jpg"),
    Category("Comedy", "https://www.usmagazine.com/wp-content/uploads/2021/09/The-Hangover-Cast-Where-Are-They-Now-Feature.jpg?w=1200&quality=55&strip=all"),
    Category("Crime", "https://i0.wp.com/www.dailymaverick.co.za/wp-content/uploads/2022/12/The-Godfather-1-e1672225720710.jpg?fit=1280%2C1817&quality=89&ssl=1"),
    Category("Drama", "https://static0.colliderimages.com/wordpress/wp-content/uploads/2023/11/the-shawshank-redemption-poster.jpg?w=1600&h=1200&fit=crop"),
    Category("Fantasy", "https://sm.ign.com/ign_mear/cover/h/harry-pott/harry-potter-the-series_qz8f.jpg"),
    Category("History", "https://variety.com/wp-content/uploads/2021/04/300.jpg?w=1000&h=563&crop=1"),
    Category("Horror", "https://blogs-images.forbes.com/jeffewing/files/2018/09/Valak.jpg"),
    Category("Music", "https://www.delfontmackintosh.co.uk/imgs/media/45f3b43409d36bb96dfa3243d8711612/mamma-mia-london-production-1102-1332-photo-by-brinkhoff-moegenburg.jpg"),
    Category("Mystery", "https://files.pikosky.sk/files/uploads/2012/02/sherlock-holmes-game-of-shadows-poster-final-450x599.jpg"),
    Category("Romance", "https://occ-0-8407-114.1.nflxso.net/dnm/api/v6/E8vDc_W8CLv7-yMQu8KMEC7Rrr8/AAAABegKrvKk6xSPjRB3xULrR7smkeT-j9WRASWE55g7DrVYxF8Lml5d15bCEmpoEUEufxBgcU2_WgZr0FX9gndmeVtvlnBkZYtvIjYk.jpg?r=1e8"),
    Category("Sci-Fi", "https://www.slamedia.org/wp-content/uploads/2014/11/interstellar-feat.jpeg"),
    Category("War", "https://m.media-amazon.com/images/M/MV5BMGQ0N2Y3MzQtNWVhYi00OWYwLWFlZmQtYTkyOTliYzI4YzJmXkEyXkFqcGdeQWFybm8@._V1_.jpg"),
    Category("Western", "https://www.hollywoodreporter.com/wp-content/uploads/2013/02/django_1.jpg?w=1440&h=810&crop=1"),
)