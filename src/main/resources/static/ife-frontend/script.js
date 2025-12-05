const API_BASE = window.location.hostname === "localhost"
  ? "http://localhost:8080"
  : `http://${window.location.hostname}:8080`;

async function loadMovies() {
  const loading = document.getElementById('loading');
  const gallery = document.getElementById('gallery');

  let movies = [];
  try {
    const res = await fetch(`${API_BASE}/api/movies`);
    movies = await res.json();
  } catch (e) {
    loading.innerText = 'Failed to load movies.';
    return;
  }

  gallery.innerHTML = '';
  loading.classList.add('hidden');
  gallery.classList.remove('hidden');

  movies.forEach(movie => {
    const div = document.createElement('div');
    div.className = 'thumbnail';
    div.style.backgroundImage = `url('${movie.thumbnail}')`;

    div.onclick = () => {
      window.location.href = `/ife-frontend/movie.html?movie=${movie.title}`;
    };

    gallery.appendChild(div);
  });
}

loadMovies();
