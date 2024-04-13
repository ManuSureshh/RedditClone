
#update DOckerfile by Manvith

# Use an official Node.js runtime as a base image
FROM node:16

# Set the working directory to /app
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install application dependencies
RUN npm install --production

# Copy the rest of the application code to the working directory
COPY . .

# Build the frontend application
RUN npm run build

# Expose the port on which the app runs
EXPOSE 3000

# Command to run the application
CMD ["npm", "start"]



