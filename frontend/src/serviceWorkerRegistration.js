/* eslint-disable no-restricted-globals */
const isLocalhost = Boolean(
  window.location.hostname === 'localhost' ||
  window.location.hostname === '[::1]' ||
  window.location.hostname === '127.0.0.1'
);

// Define cache name and files to cache
const CACHE_NAME = 'star-wars-cache-v1';
const urlsToCache = [
  '/',
  '/index.html',
  '/static/js/bundle.js',
  '/static/css/main.css', // Add other assets you want to cache
  // Add your API URLs here
  'https://swapi.dev/api/starships',
  'https://swapi.dev/api/people'
];
export function register() {
  if (process.env.NODE_ENV === 'production' && 'serviceWorker' in navigator) {
    const publicUrl = new URL(process.env.PUBLIC_URL, window.location.href);
    if (publicUrl.origin !== window.location.origin) return;

    window.addEventListener('load', () => {
      const swUrl = `${process.env.PUBLIC_URL}/service-worker.js`;

      navigator.serviceWorker
        .register(swUrl)
        .then((registration) => {
          console.log('Service Worker registered: ', registration);
        })
        .catch((error) => {
          console.error('Service Worker registration failed: ', error);
        });
    });
  }
}


// During installation, cache required files
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(urlsToCache);
    })
  );
});

// Activate the service worker
self.addEventListener('activate', (event) => {
  const cacheWhitelist = [CACHE_NAME];

  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cacheName) => {
          if (!cacheWhitelist.includes(cacheName)) {
            return caches.delete(cacheName); // Remove old caches
          }
        })
      );
    })
  );
});

// Intercept network requests and serve cached content if offline
self.addEventListener('fetch', (event) => {
  // Check if the request is for an API or static asset
  const url = event.request.url;

  // If it's a request for a known URL (e.g., API or asset), cache it
  if (url.includes('swapi.dev') || url.includes('.css') || url.includes('.js')) {
    event.respondWith(
      caches.match(event.request).then((cachedResponse) => {
        // Return cached response if available, otherwise fetch from network
        return (
          cachedResponse ||
          fetch(event.request)
            .then((networkResponse) => {
              // Cache the new API response
              if (event.request.url.includes('swapi.dev')) {
                caches.open(CACHE_NAME).then((cache) => {
                  cache.put(event.request, networkResponse.clone());
                });
              }
              return networkResponse;
            })
            .catch(() => {
              // Fallback to cached content when offline
              return cachedResponse || fetch(event.request);
            })
        );
      })
    );
  }
});

// Fallback to cache if offline
self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      return (
        response ||
        fetch(event.request).catch(() => {
          return caches.match('/offline.html'); // Serve offline page if offline
        })
      );
    })
  );
});

export function unregister() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.ready
      .then((registration) => {
        registration.unregister();
      })
      .catch((error) => {
        console.error(error.message);
      });
  }
}
