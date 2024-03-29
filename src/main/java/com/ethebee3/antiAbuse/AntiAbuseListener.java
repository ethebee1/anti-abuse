package com.ethebee3.antiAbuse;

import com.ethebee3.antiAbuse.AntiAbuseListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AntiAbuseListener implements Listener {
    private final antiAbuse NAA;
    private String player;
    private String mes;
    private final String webhookUrl = "https://discord.com/api/webhooks/1223077338963775528/B2WmjaiYYroaYenh3XLYPQb8YmsiHa5OfalVPeMaWtkPAiI2QU6Mz2Xu-4_fDpXepkEE"; // Discord webhook URL
    private String webhookmessage;

    public AntiAbuseListener(antiAbuse AAL) {
        this.NAA = AAL;
    }


    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void p(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("abuse.spy")) {
            player = event.getPlayer().getDisplayName();
            mes = event.getMessage();
            sendToDiscordWebhook(mes, player);
        }
    }

    public void sendToDiscordWebhook(String message, String player) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            webhookmessage = "```"+player+" sent the command: "+message;

            String jsonInputString = "{\"content\": \"" + webhookmessage + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // required to send the request for some reason
            conn.getResponseCode();

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
