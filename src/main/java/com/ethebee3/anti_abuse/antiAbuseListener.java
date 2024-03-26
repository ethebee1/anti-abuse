package com.ethebee3.anti_abuse;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class antiAbuseListener implements Listener {
    private final antiAbuseListener NAA;
    private final BlockingQueue<String> messageQueue;
    private String player;
    private String mes;
    private final Thread queueThread;
    private final String webhookUrl; // Discord webhook URL

    public antiAbuseListener(antiAbuseListener AAL, String webhookUrl) {
        this.NAA = AAL;
        this.webhookUrl = webhookUrl;
        this.messageQueue = new LinkedBlockingQueue<>();
        
        // Create a thread to handle the queue
        this.queueThread = new Thread(this::processQueue);
        this.queueThread.start();
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void p(PlayerCommandPreprocessEvent event) {
            player = event.getPlayer().getDisplayName();
            mes = event.getMessage();

            // Add message to the queue
            messageQueue.offer(mes);
    }

    private void processQueue() {
        while (true) {
            try {
                // Take a message from the queue (blocking if empty)
                String message = messageQueue.take();
                sendToDiscordWebhook(message, player);
                // Sleep to ensure we don't exceed the rate limit (adjust as needed)
                TimeUnit.MILLISECONDS.sleep(200); // Adjust the sleep time as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void sendToDiscordWebhook(String message, String player) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Construct JSON payload
            String jsonInputString = "{\"content\": \"" + message + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // This line is required to actually send the request
            conn.getResponseCode();

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Make sure to stop the queue thread when disabling the listener
    public void disable() {
        queueThread.interrupt();
    }
}
