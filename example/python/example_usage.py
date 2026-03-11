import time
from tendquant import Client

username = "a2dZMmrflhY4MwRN"
password = "6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu"

client = Client(username=username, password=password) # 创建MQTT客户端

def on_message(client, userdata, msg):
    # 定义消息处理回调函数
    print(f">>> 收到主题数据 {msg.topic}: {msg.payload.decode()}")

client.connect(on_message) # 连接到MQTT broker

client.subscribe("lv1/tradeList/#") # 订阅主题

try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    client.disconnect() # 断开连接