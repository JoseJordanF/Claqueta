

data class Film(
    val title : String,
    val releaseDate: Date,
    val movieDirectors : List<String> = listOf(),
    val duration : Int = 0,
){


}
