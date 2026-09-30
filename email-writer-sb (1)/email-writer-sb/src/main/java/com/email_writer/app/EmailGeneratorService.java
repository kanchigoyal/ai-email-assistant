package com.email_writer.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
 import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    @Autowired
    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;


    public String generateEmailReply(EmailRequest emailRequest) {
        //    Build the prompt
        String prompt = buildPrompt(emailRequest);

        //craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        //do request and get response

        String response = webClient.post()
                .uri(geminiApiUrl)
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(reactor.util.retry.Retry.backoff(3, java.time.Duration.ofSeconds(2))
                        .filter(ex -> ex instanceof org.springframework.web.reactive.function.client.WebClientResponseException w
                                && w.getStatusCode().value() == 503))
                .block();

        //return response

        // Extract Response and Return
        return extractResponseContent(response);
    }

        private String extractResponseContent(String response) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                return rootNode.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();

            } catch (Exception e) {
                return "Error processing request: " + e.getMessage();
            }
        }

    private String buildPrompt(EmailRequest emailRequest) {
                StringBuilder prompt = new StringBuilder();

                prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line");
                if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
                    prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
                }
                prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
                return prompt.toString();
            }
        }

