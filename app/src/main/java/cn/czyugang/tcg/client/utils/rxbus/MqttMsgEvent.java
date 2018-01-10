package cn.czyugang.tcg.client.utils.rxbus;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author ruiaa
 * @date 2018/1/10
 */

public class MqttMsgEvent {
    public String topic;
    public MqttMessage message;

    public MqttMsgEvent(String topic, MqttMessage message) {
        this.topic = topic;
        this.message = message;
    }
}
