# ---------- Build Stage ----------
FROM node:18 AS build

# Set working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the app
COPY . .

# Build the React app (this outputs to /app/build)
RUN npm run build

# ---------- Production Stage ----------
FROM nginx:alpine

# Copy the built React app to Nginx's public directory
COPY --from=build /app/build /usr/share/nginx/html

# Optional: remove default nginx config and use your own
# COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
