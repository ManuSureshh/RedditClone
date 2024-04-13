
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



# Use a Node.js base image with version 19
#FROM node:19-alpine3.15

# Set the working directory in the container
#WORKDIR /usr/src/app

# Copy package.json and package-lock.json to the working directory
#COPY package*.json ./

# Install dependencies
#RUN npm install --production

# Copy the rest of the application code to the working directory
#COPY . .

# Build the Next.js application
#RUN npm run build

# Expose the port the app runs on
#EXPOSE 3000

# Command to run the application
#CMD ["npm", "start"]
--!


