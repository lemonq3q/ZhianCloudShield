package org.example.config;

import ch.qos.logback.classic.pattern.MessageConverter;

public class RabbitmqConfig {
    public static final String ONENET_EXCHANGE = "oneNetExchange";
    public static final String DETECT_EXCHANGE = "detectExchange";


    public static final String TEMP_QUEUE = "temperatureQueue";
    public static final String HUM_QUEUE = "humidityQueue";
    public static final String ATD_QUEUE = "attendanceQueue";
    public static final String ONLINE_QUEUE = "onlineStatusQueue";
    public static final String RISK_QUEUE = "riskQueue";

    public static final String TEMP_ROUTING_KEY = "temperature";
    public static final String HUM_ROUTING_KEY = "humidity";
    public static final String ATD_ROUTING_KEY = "attendance";
    public static final String ONLINE_ROUTING_KEY = "onlineStatus";
    public static final String RISK_ROUTING_KEY = "risk";

}
