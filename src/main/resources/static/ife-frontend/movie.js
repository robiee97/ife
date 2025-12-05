function getQueryParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

const API_BASE = window.location.hostname === "localhost"
  ? "http://localhost:8080"
  : `http://${window.location.hostname}:8080`;

async function loadMovie() {
  const movieName = getQueryParam("movie");

  const res = await fetch(`${API_BASE}/api/movies`);
  const movies = await res.json();

  const movie = movies.find(m => m.title === movieName);
  if (!movie) return alert("Movie not found");

  // Banner image
  document.getElementById('banner').style.backgroundImage =
    `url('${movie.thumbnail}')`;

  const video = document.getElementById('videoPlayer');

  if (Hls.isSupported()) {
    const hls = new Hls();
    hls.loadSource(movie.streamUrl);
    hls.attachMedia(video);
  } else {
    video.src = movie.streamUrl;
  }
}

loadMovie();
