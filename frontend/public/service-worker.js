const CACHE_NAME = 'star-wars-cache-v1';
const urlsToCache = [
  '/',
  '/index.html',
  'https://swapi.dev/api/starships',
  'https://swapi.dev/api/people'
];

// Install
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(urlsToCache);
    })
  );
  self.skipWaiting();
});

// Activate
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.map((key) => key !== CACHE_NAME && caches.delete(key)))
    )
  );
  self.clients.claim();
});

// Fetch
self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((cached) => {
      if (cached) return cached;
      return fetch(event.request)
        .then((response) => {
          if (
            response &&
            response.status === 200 &&
            response.type !== 'opaque' &&
            shouldCache(event.request)
          ) {
            const responseClone = response.clone();
            caches.open(CACHE_NAME).then((cache) =>
              cache.put(event.request, responseClone)
            );
          }
          return response;
        })
        .catch((err) => {
          console.error("Network fetch failed:", err);
          return caches.match('/');
        });
    })
  );
});

