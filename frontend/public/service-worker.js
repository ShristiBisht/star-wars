const CACHE_NAME = 'star-wars-cache-v1';
const urlsToCache = [
  '/',
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
      // If cached response exists, return it
      if (cached) {
        return cached;
      }

      // If not cached, fetch from network
      return fetch(event.request)
        .then((response) => {
          // If the response is valid, cache it
          if (
            response &&
            response.status === 200 &&
            response.type !== 'opaque' &&
            shouldCache(event.request) // Ensure this function exists and works
          ) {
            const responseClone = response.clone();
            caches.open(CACHE_NAME).then((cache) =>
              cache.put(event.request, responseClone)
            );
          }
          return response;  // return the network response
        })
        .catch((err) => {
          console.error("Network fetch failed:", err);
          // Fallback to a static response (if the network request fails)
          return caches.match('/index.html').then((fallbackResponse) => {
            return fallbackResponse || new Response("Offline", { status: 503 }); // Fallback to a hardcoded response
          });
        });
    })
  );
});

// Function to determine whether or not to cache
function shouldCache(request) {
  // Implement logic to decide whether or not to cache
  // For example, caching only API requests, etc.
  return request.url.includes('swapi.dev/api');
}
