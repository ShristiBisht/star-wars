# Use official Node.js image for building the app
FROM node:18 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if present) and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of your app’s source code
COPY . .

# Build the React app (production build)
RUN npm run build

# Use Nginx to serve the build files
FROM nginx:alpine

# Copy the built React app from the previous stage into Nginx's default HTML directory
COPY --from=build /app/build /usr/share/nginx/html

# Expose port 80 for HTTP traffic
EXPOSE 80

# Copy custom nginx configuration if needed (optional)
# COPY nginx.conf /etc/nginx/nginx.conf

# Make sure the service worker file is in the build directory
# This is necessary for offline support and caching to work
COPY ./public/service-worker.js /usr/share/nginx/html/service-worker.js


# Start Nginx to serve the app and allow offline capabilities
CMD ["nginx", "-g", "daemon off;"]
