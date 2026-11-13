package com.jcaa.appcrudusers.entrypoint.util;

import android.util.Base64;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public final class SessionUtils {

    private SessionUtils() {}

    /**
     * Extracts user ID (subject claim) from a standard JWT token.
     */
    public static String extractUserIdFromToken(final String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        final String[] parts = token.split("\\.");
        if (parts.length >= 2) {
            try {
                final byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                final String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);
                final JSONObject jsonObject = new JSONObject(payloadJson);
                if (jsonObject.has("sub")) {
                    return jsonObject.getString("sub");
                }
            } catch (final Exception ignored) {
                // If decoding fails, fall back to null
            }
        }
        return null;
    }
}
