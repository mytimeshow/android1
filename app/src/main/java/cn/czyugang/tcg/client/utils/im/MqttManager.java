package cn.czyugang.tcg.client.utils.im;

import android.support.annotation.NonNull;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.rxbus.MqttMsgEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;

/**
 * @author ruiaa
 * @date 2018/1/10
 */

public class MqttManager {
    // 单例
    private static MqttManager mInstance = null;


    private MqttClient client;
    private MqttConnectOptions options;

    private MqttManager() {

    }

    /*
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {

        }
    }

    public static void connect(){
        MqUserInfo mqUserInfo=AppKeyStorage.getMqUserInfo();
        if (mqUserInfo==null) return;
        if (mInstance!=null) return;
        mInstance = new MqttManager();
        mInstance.createConnect(mqUserInfo.username,mqUserInfo.password,mqUserInfo.clientId);
    }

    /*
     * 创建Mqtt 连接
     *
     */
    private void createConnect( @NonNull String userName, @NonNull String password, @NonNull String clientId) {
        /*
        *   ip：123.207.125.128
        *   port：1883
        *   username、password为mq的登陆账号密码
        *   clientId为用户登陆的clientId
        *   topic是登陆连接后需要订阅的话题
        *   注意订阅的qos都是0
        *   登陆的cleansession必须设置为true
        *   keepalive心跳时间设置为1秒
        * */
        String url="tcp://123.207.125.128:1883";
        try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(password.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(1);

            // Construct an MQTT blocking mode client     ,  MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(url, clientId, new MemoryPersistence());

            // Set this wrapper as the callback handler
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    LogRui.d("****connectionLost####","连接断开");
                    reconnect();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    LogRui.i("messageArrived####",topic+"       msg: "+message.toString());
                    RxBus.post(new MqttMsgEvent(topic, message));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            doConnect();
        } catch (Exception e) {
            LogRui.e("creatConnect####",e);
        }
    }

    /*
     * 建立连接
     */
    private void doConnect() {
        if (client != null) {
            try {
                client.connect(options);
                MqUserInfo mqUserInfo=AppKeyStorage.getMqUserInfo();
                if (mqUserInfo!=null){
                    for(String s:mqUserInfo.topic){
                        subscribe(s,0);
                    }
                }

                LogRui.d("Connected to " + client.getServerURI() + " with client ID " + client.getClientId());
            } catch (Exception e) {
                LogRui.e("doConnect####",e);
            }
        }
    }

    private ScheduledExecutorService scheduler;

    //重新链接
    public void reconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!client.isConnected()) {
                    doConnect();
                }else {
                    if (scheduler!=null)scheduler.shutdown();
                    scheduler=null;
                }
            }
        }, 0, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    /*
     * 取消连接
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }


    /*
     * 订阅频道
     */
    public void subscribe(String topicName, int qos) {

        if (client != null && client.isConnected()) {
            try {
                client.subscribe(topicName, qos);
            } catch (MqttException e) {
                LogRui.e("subscribe####",e);
            }
        }

    }

}
