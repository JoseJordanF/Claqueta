

data class Film(
    val title : String,
    val movieDirectors : List<String> = listOf(),
    val screenwriters : List<String> = listOf(),
    val releaseDate: Date,
    val producers: List<String> = listOf(),
    val consPlataforms : List<String> = listOf()
){


}
