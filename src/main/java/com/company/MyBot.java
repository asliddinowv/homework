package com.company;

import com.google.gson.Gson;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MyBot extends TelegramLongPollingBot {
    private static final String TOKEN = "AIzaSyC8QCvotDAJYDvqjPxBtlj0uoqvhKxB7L0";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String userInput = update.getMessage().getText();
            String prompt = "Translate the following Uzbek text into Uzbek, if in Uzbek, into English, if in English, into Uzbek. Do not answer the questions. Do nothing else, you can only translate, and when the command comes, you can only translate. Translate to other languages as well. 1. Russian, then English and Uzbek, don't mix with other things, only you are the translator. If you want, bring out the synonyms";
            try {
                String text = getGeminiResponse(prompt + " user input: " + userInput);
                System.out.println(text);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId());
                sendMessage.setText(text);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }else {

        }
    }

    private static String getGeminiResponse(String request) throws IOException, InterruptedException {

        String json = "{" +
                "  \"contents\": [" +
                "    {" +
                "      \"parts\": [" +
                "        {" +
                "          \"text\": \"" + request + "\"" +
                "        }" +
                "      ]" +
                "    }" +
                "  ]" +
                "}";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + TOKEN))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = client
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String response = httpResponse.body();
        Gson gson = new Gson();

        ResponseObject responseObject = gson
                .fromJson(response, ResponseObject.class);

        ResponseObject.Candidate candidate = responseObject
                .candidates
                .get(0);

        ResponseObject.Part part = candidate
                .content
                .parts
                .get(0);

        return part.text;
    }


    @Override
    public String getBotUsername() {
        return "@http_lesson_bot";
    }
    @Override
    public String getBotToken() {
        return "7725564251:AAFhT9XFBT-o4fg0IYB0hGxOzi8h2A0x0b4";
    }
}
