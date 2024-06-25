
const authToken = `Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YjI1ZWIwYWIxNGM3MGFjNzk3NTU5ZTkzYWUzM2Y1MiIsInN1YiI6IjY2NzE3NmMyZjZiZjNjMmQ2MmUyMmJiZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kmDJk6-AfXwM330s-auN0shof0bNGZ4XK-ModVgEzBE`

const bt = document.querySelector('.cta');
const divUser = document.querySelector(".enter__text");
const title = document.querySelector(".hero__title");
const paragraph = document.querySelector(".hero__paragraph");
const btFilms = document.getElementById("nav__items1");
const btRecommends = document.getElementById("nav__items2");
const btExit = document.getElementById("nav__items3");
const menu = document.querySelector(".nav__link");
const fcontainer = document.querySelector(".film__container");
const inputName = document.querySelector(".username");
const gridFilms = document.querySelector(".grid__film");
const detailContainer = document.querySelector(".detail__container");
const reviewContainer = document.querySelector(".review__container");
const listReview = document.querySelector(".list__review");
const modalView = document.querySelector(".modalView");
const modalForm = document.querySelector(".modalForm");
const btAddReview = document.querySelector(".add__review");
const btReview = document.querySelector(".bt__review")
const oGeneral = document.querySelector(".o__general");
const oPerformance = document.querySelector(".o__performance");
const oDirection = document.querySelector(".o__direction");
const reviews = new Map();
var clickedFilm = [];
var clickedReview = [];
var user = "";
var currentFilm = "";

document.addEventListener("DOMContentLoaded", enter, false);
function enter() {

    bt.addEventListener('click', login, false);
}


function login() {
    title.textContent = "Dinos como quieres que te conozcan los demas usuarios";
    paragraph.textContent = "Se te conocera por el nombre que elijas";
    bt.setAttribute('style', "visibility: hidden;");
    divUser.setAttribute('style', "visibility: visible;");

    var btInput = document.querySelector(".cta__login");

    btInput.addEventListener("click", function () {
        if (inputName.value.trim().length != 0) {
            postUser(inputName.value)
                .then(success => {
                    if (success) {
                        user = inputName.value;
                        filmsView();
                    }
                })
        }
    });
}

btFilms.addEventListener("click", function () {
    oGeneral.value = '';
    oPerformance.value = '';
    oDirection.value = '';
    gridFilms.innerHTML = '';
    gridFilms.style.gridTemplateColumns = "repeat(4, minmax(100px, 1fr))";
    fcontainer.style.justifyContent = "center"
    detailContainer.style.display = "none"
    reviewContainer.style.display = "none"
    filmsView();
});

btRecommends.addEventListener("click", function () {
    oGeneral.value = '';
    oPerformance.value = '';
    oDirection.value = '';
    gridFilms.innerHTML = '';
    gridFilms.style.gridTemplateColumns = "repeat(4, minmax(100px, 1fr))";
    fcontainer.style.justifyContent = "center"
    detailContainer.style.display = "none"
    reviewContainer.style.display = "none"
    recommendsView();
});


gridFilms.addEventListener("click", function () {
    var a = [];
    clickedFilm.forEach(id => {
        a.push(document.getElementById(id));
        a[a.length - 1].addEventListener("click", function () {
            filmDetail(id);
        });
    });
});

listReview.addEventListener("click", function () {
    var a = [];
    clickedReview.forEach(id => {
        a.push(document.getElementById(id));
        a[a.length - 1].addEventListener("click", function () {
            const detail = document.querySelector(".detail__review");
            detail.innerHTML = `<span>Opinion general</span><br> ${reviews.get(id).contentPlot}<br>
            <span>Opinion sobre las actuaciones</span><br> ${reviews.get(id).contentPerformance}<br>
            <span>Opinion sobre la direccion</span><br> ${reviews.get(id).contentDirection}<br>
            <span>Autor</span><br> ${reviews.get(id).userName}<br>
            <span>Fecha de la reseña</span><br> ${reviews.get(id).creationDate}`
            modalView.showModal();
        });
    });
});


btAddReview.addEventListener("click", function () {
    modalForm.showModal();
});

btReview.addEventListener("click", function () {
    if (oGeneral.value.trim().length != 0 && oPerformance.value.trim().length != 0 && oDirection.value.trim().length != 0) {
        postReview(new Review(oGeneral.value, oPerformance.value, oDirection.value, user, currentFilm))
            .then(success => {
                if (success) {
                    filmDetail(currentFilm)
                    modalForm.close();
                }
            })
    }
});

btExit.addEventListener("click", function () {
    title.textContent = "Entra y encuentra las respuestas que buscas sobre el cine";
    paragraph.style.display = "block";
    paragraph.textContent = "Comparte tu opinion sobre las peliculas que ves, disfruta conociendo la de otros usuarios y obten recomendaciones de que peliculas ver";
    bt.setAttribute('style', "visibility: visible;");
    menu.style.visibility = "hidden";
    fcontainer.style.display = "none";
    inputName.value = '';
    gridFilms.innerHTML = '';
    detailContainer.style.display = "none"
    fcontainer.style.justifyContent = "center"
    gridFilms.style.gridTemplateColumns = "repeat(4, minmax(100px, 1fr))";
    reviewContainer.style.display = "none"
});

function recommendsView() {
    clickedFilm = [];
    clickedReview = [];
    title.textContent = "Recomendaciones";
    getRecommendations()
        .then(data => {
            data.map((film) => {
                getCover(film.title, film.releaseDate)
                    .then(data => {
                        var element = document.createElement('li');
                        element.className = "film__item";
                        element.innerHTML = `<img src="${data}" id="${film.id}" alt="${film.title}">`;
                        gridFilms.appendChild(element);
                        clickedFilm.push(film.id);
                    })
            });
        })
    fcontainer.style.display = "flex";
}

function filmsView() {
    clickedFilm = [];
    clickedReview = [];
    title.textContent = "Películas";
    paragraph.style.display = "none";
    divUser.style.display = "none";
    menu.style.visibility = "visible";
    getFilms()
        .then(data => {
            data.map((film) => {
                getCover(film.title, film.releaseDate)
                    .then(data => {
                        var element = document.createElement('li');
                        element.className = "film__item";
                        element.innerHTML = `<img src="${data}" id="${film.id}" alt="${film.title}">`;
                        gridFilms.appendChild(element);
                        clickedFilm.push(film.id);
                    });
            });
        });
    fcontainer.style.display = "flex";
}


function filmDetail(id) {

    getFilmDetails(id)
        .then(film => {
            currentFilm = film.id
            title.textContent = film.title;
            detailContainer.style.display = "initial"
            gridFilms.style.gridTemplateColumns = "repeat(1, minmax(100px, 1fr))";
            fcontainer.style.justifyContent = "left"
            let f = document.getElementById(film.id)
            gridFilms.innerHTML = '';
            var element = document.createElement('li');
            element.className = "film__item";
            element.innerHTML = `<img src="${f.src}" id="${f.id}" alt="${f.alt}">`;
            gridFilms.appendChild(element);
            const detail = document.querySelector(".detail__film")
            detail.innerHTML = `<span>Titulo</span> ${film.title}<br>
            <span>Directores</span> ${film.movieDirectors}<br>
            <span>Grionistas</span> ${film.screenwriters}<br>
            <span>Fecha de estreno</span> ${film.releaseDate}<br>
            <span>Productores</span> ${film.producers}<br>
            <span>Plataformas donde poder verlo</span> ${film.consPlatforms}`
            reviewContainer.style.display = "initial"
            getReviews()
                .then(data => {
                    listReview.innerHTML = "";
                    data.map((review) => {
                        var elementReview = document.createElement('li');
                        elementReview.className = "review__item";
                        elementReview.innerHTML = `<a href="#" id="${review.userName}">Reseña de ${review.userName}</a>`;
                        listReview.appendChild(elementReview);
                        clickedReview.push(review.userName);
                        reviews.set(review.userName, review);
                    });
                });

        });
}

function reviewDetail(id) {
    const detail = document.querySelector(".detail__review")
    detail.innerHTML = `<span>Titulo</span> ${film.title}<br>
            <span>Directores</span> ${film.movieDirectors}<br>
            <span>Grionistas</span> ${film.screenwriters}<br>
            <span>Fecha de estreno</span> ${film.releaseDate}<br>
            <span>Productores</span> ${film.producers}<br>
            <span>Plataformas donde poder verlo</span> ${film.consPlatforms}`
    modalView.showModal()
}



async function postUser(data) {
    const url = "http://127.0.0.1:8082/users";
    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    };

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        }
        const responseData = await response.json();
        return true;

    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

async function postReview(data) {
    const url = `http://127.0.0.1:8082/films/${currentFilm}/reviews`;
    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    };

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        }
        const responseData = await response.json();
        return true;

    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

async function getReviews() {
    const url = `http://127.0.0.1:8082/films/${currentFilm}/reviews`;
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    };

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        } else {
            const responseData = await response.json();
            return responseData;
        }
    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

async function getFilms() {
    const url = "http://127.0.0.1:8082/films";
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    };

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        } else {
            const responseData = await response.json();
            return responseData;
        }
    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

async function getRecommendations() {
    const url = `http://127.0.0.1:8082/users/${user}`
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    };

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        } else {
            const responseData = await response.json();
            return responseData;
        }
    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

async function getFilmDetails(id) {
    const url = `http://127.0.0.1:8082/films/${id}`
    const options = {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    };

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        } else {
            const responseData = await response.json();
            return responseData;
        }
    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

async function getCover(title, year) {

    const url = `https://api.themoviedb.org/3/search/movie?query=${encodeURIComponent(title)}&year=${encodeURIComponent(year)}`;
    const options = {
        method: "GET",
        headers: {
            accept: 'application/json',
            Authorization: authToken
        }
    };
    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error("Error en la solicitud");
        } else {
            const responseData = await response.json();
            const coverPath = responseData.results[0].poster_path;
            const coverUrl = `https://image.tmdb.org/t/p/w185${coverPath}`
            return coverUrl
        }
    } catch (error) {
        console.error("Problema en la solicitud", error);
        return false;
    }
}

class Film {
    constructor(
        id,
        title,
        movieDirectors,
        screenwriters,
        releaseDate,
        producers,
        consPlatforms
    ) {
        this.id = id;
        this.title = title;
        this.movieDirectors = movieDirectors;
        this.screenwriters = screenwriters;
        this.releaseDate = releaseDate;
        this.producers = producers;
        this.consPlatforms = consPlatforms;
    }
}

class Review {
    constructor(
        contentPlot,
        contentPerformance,
        contentDirection,
        userName,
        filmId,
        creationDate,
    ) {
        this.contentPlot = contentPlot;
        this.contentPerformance = contentPerformance;
        this.contentDirection = contentDirection;
        this.userName = userName;
        this.filmId = filmId;
        this.creationDate = creationDate;
    }
}

class Pair {
    constructor(key, value) {
        this.key = key;
        this.value = value;
    }
    get(key) {
        return this.key === key ? this.value : undefined;
    }
}