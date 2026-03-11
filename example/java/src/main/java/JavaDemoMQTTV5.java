import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import java.util.UUID;

public class JavaDemoMQTTV5 {
    public static String getClientId() {
        return "java-" + UUID.randomUUID().toString().replace("-", "").substring(20);
    }

    public static void main(String[] args) {
        String username = "a2dZMmrflhY4MwRN";
        String password = "6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu";

        // 订阅等级，建议为2，消息仅送达一次，通过四次握手确保不丢不重
        int subQos = 2;
        
        try {
            MqttClient client = new MqttClient("wss://quant.top2189.cn/mqtt", getClientId());
            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setUserName(username);
            options.setPassword(password.getBytes());
            
            client.setCallback(new MqttCallback() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    System.out.println("connected to: " + serverURI);
                }
                
                public void disconnected(MqttDisconnectResponse disconnectResponse) {
                    System.out.println("disconnected: " + disconnectResponse.getReasonString());
                }
                
                public void deliveryComplete(IMqttToken token) {
                    System.out.println("deliveryComplete: " + token.isComplete());
                }
                
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // 在这里处理业务逻辑
                    System.out.println("topic: " + topic);
                    System.out.println("message content: " + new String(message.getPayload()));
                }

                public void mqttErrorOccurred(MqttException exception) {
                    System.out.println("mqttErrorOccurred: " + exception.getMessage());
                }
                
                public void authPacketArrived(int reasonCode, MqttProperties properties) {
                    System.out.println("authPacketArrived");
                }
            });
            
            client.connect(options);

            // 订阅的主题
            client.subscribe("lv1/tradeList/#", subQos);

            
        } catch (MqttException e) {
            e.printStackTrace();
        }


        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            
    }
}
