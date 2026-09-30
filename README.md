# AI Email Assistant

A small project I built to make replying to emails a little easier.

The idea is simple — instead of writing every email reply from scratch, the application takes the email content and generates a professional reply using Gemini. I also built a Chrome extension so the feature can be used directly while working with Gmail.

## Live App

🔗 https://ai-email-assistant-rouge.vercel.app

## GitHub Repository

🔗 https://github.com/kanchigoyal/ai-email-assistant

---

## What it does

- Takes the content of an email
- Generates a reply using Gemini
- Provides a simple React interface
- Connects the frontend with a Spring Boot REST API<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/861a6432-c4a6-4b4a-a8b2-d3f818cca789" />

- Includes a Chrome extension for using the assistant inside Gmail

## How it works

Email
  ↓
Chrome Extension / Web App
  ↓
Spring Boot REST API
  ↓
Gemini API
  ↓
Generated Reply
  ↓
Email Compose Box
The Chrome extension reads the email content, sends it to the backend, and places the generated response back into the Gmail compose box.

Tech Stack
Frontend
- React.js
- JavaScript
- HTML
- CSS
- Vite
Backend
- Java
- Spring Boot
- REST API
- WebClient
AI
- Google Gemini API
Extension
- JavaScript
- Chrome Extension Manifest V3

ai-email-assistant/
│
├── email-writer-frontend/
│   └── React frontend
│
├── email-writer-sb/
│   └── Spring Boot backend
│
└── email-writer-ext/
    └── Gmail Chrome extension

API Endpoint
POST /api/email/generate

Request Body:
{
  "emailContent": "Hello, thank you for reaching out to us.",
  "tone": "friendly"
}

Response:
Generated email reply as text.
Aur How it works mein already:
Email → Chrome Extension / Web App → Spring Boot REST API → Gemini API → Generated Reply → Email Compose Box

Running Locally
Frontend
cd email-writer-frontend
npm install
npm run dev

Backend
cd email-writer-sb
./mvnw spring-boot:run

Gmail Extension
The project also contains a Chrome extension that adds an AI Reply option to the Gmail compose window.
Extension files:email-writer-ext/
├── content.js
├── content.css
└── manifest.json
