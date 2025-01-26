package com.matin.taxi.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

    private Utils() {
    }

    public static SignalMessage getObject(final String message) throws Exception {
        return objectMapper.readValue(message, SignalMessage.class);
    }

    public static String getString(final SignalMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
    
    
    public static SignalMessageRPC getObjectRPC(final String message) throws Exception {
        return objectMapper.readValue(message, SignalMessageRPC.class);
    }

    public static String getStringRPC(final SignalMessageRPC message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
    
    public static String getString(final ResultMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}