# A股 Level1 行情接入指南
本指南将帮助您快速接入 [tendQuant](https://tendquant.cn) 服务，开始获取实时 A股 行情数据。

## 准备工作
1. [注册平台账户](https://tendquant.cn/register)
2. [在管理应用中创建应用](https://tendquant.cn/dashboard/manage-apps)

## 基本概念
基于 MQTT 协议提供实时行情数据推送服务。您只需要：
1. 创建应用并获取密码
2. 建立 MQTT 连接
3. 订阅您关注的股票代码
4. 接收实时行情数据

## 简介
tendQuant 是一个低门槛的 A股 行情数据服务平台，基于 MQTT 协议提供实时、稳定、高效的行情数据推送服务。

### 适用场景
- 实时行情监控
- 算法交易系统
- 金融数据分析
- 投资组合管理

### 认证方式
根据用户名和密码进行身份验证。

测试账号：`a2dZMmrflhY4MwRN`

测试密码：`6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu`

注意：此账号为应用账号，用于 API 调用，非平台账号，正式使用时请先注册平台账号再创建应用，测试账号最大连接数为20，超过20将会断开连接。

允许权限：

|    服务类型     | 订阅类型 | 最大订阅  |
|----------------|---------|--------|
| lv1 成交明细    | 单股票   | 50    |
|                | 全市场   | 5     |
| lv1 行情快照    | 单股票   | 50    |
|                | 全市场   | 5     |
| lv1 五档盘口    | 单股票   | 50    |
|                | 全市场   | 5     |

### 获取连接信息
1. 登录控制台
2. 进入"应用管理"页面
3. 创建新应用或选择已有应用
4. 复制用户名和密码

### 安全提示
- 请妥善保管您的应用密码
- 不要在客户端代码中硬编码密钥

## 行情接口

### 接口概览
#### 数据类型
| 订阅等级 | 数据类型   | 主题格式               | 说明               |
|----------|------------|------------------------|--------------------|
| level 1  | 成交明细   | lv1/tradeList/{code}   | 单只股票成交明细   |
|          |            | lv1/tradeList/#        | 所有股票成交明细   |
| level 1  | 行情快照   | lv1/snapshots/{code}   | 单只股票行情快照   |
|          |            | lv1/snapshots/#        | 所有股票行情快照   |
| level 1  | 五档盘口   | lv1/orderBook/{code}   | 单只股票五档盘口   |
|          |            | lv1/orderBook/#        | 所有股票五档盘口   |

**注意**
对于需要订阅大量股票的场景，推荐使用"批量行情"接口，一次请求即可订阅全市场股票，大幅降低连接数和成本。

### 订阅行情
通过 MQTT 订阅主题来接收实时行情数据，以下使用Python SDK演示：
#### 初始化客户端
```python
client = Client(username="a2dZMmrflhY4MwRN", password="6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu") 
```
#### 定义消息处理回调函数
```python
def on_message(client, userdata, msg):
    print(f">>> 收到主题数据 {msg.topic}: {msg.payload.decode()}")
```
#### 连接客户端
```python
client.connect(on_message) # 传递消息处理回调函数
```
#### 单只股票订阅
```python
client.subscribe("lv1/tradeList/000001")
```
#### 批量订阅
```python
client.subscribe("lv1/tradeList/#")
```
#### 取消订阅
```python
client.unsubscribe('lv1/tradeList/000001')
```

#### 注意事项
- 应用最大支持的订阅数取决于您选择的订阅套餐
- 取消订阅可以释放资源，不再占用订阅数
- 如果需要获取大量股票数据，建议使用批量订阅接口

### 成交明细
**主题**：lv1/tradeList/{code}
```json
{
  "currentPrice": "10.83",
  "volume": "30.00",
  "timestamp": "1772780160",
  "tradeCount": "3"
}
```
| 字段名       | 类型   | 说明         |
|--------------|--------|--------------|
| currentPrice | String | 成交价格     |
| volume       | String | 成交量(手)   |
| timestamp         | String | 时间戳(秒)   |
| tradeCount   | String | 成交笔数     |

### 行情快照
**主题**：lv1/snapshots/{code}
```json
{
    "limitDown": "9.74",
    "change": "-0.06",
    "circulationShares": "194.1亿",
    "peTtm": "4.84",
    "currentPrice": "10.76",
    "stockCode": "000001",
    "yesterdayPrice": "10.82",
    "totalVolume": "83.59万",
    "peStatic": "4.08",
    "circulationMarketValue": "2088.0亿",
    "stockName": "平安银行",
    "changePercent": "-0.55%",
    "limitUp": "11.90",
    "pb": "0.47",
    "openPrice": "10.79",
    "amplitude": "0.74%",
    "totalMarketValue": "2088.1亿",
    "lowPrice": "10.74",
    "turnoverRate": "0.43%",
    "volume": "6194",
    "highPrice": "10.82",
    "turnover": "9.01亿",
    "turnoverRate5d": "0.97%"
}
```
| 字段名               | 类型   | 说明           |
|----------------------|--------|----------------|
| stockCode            | String | 股票代码       |
| stockName            | String | 股票名称       |
| currentPrice         | String | 当前价格       |
| yesterdayPrice       | String | 昨收价格       |
| openPrice            | String | 今开价格       |
| highPrice            | String | 最高价格       |
| lowPrice             | String | 最低价格       |
| change               | String | 涨跌额         |
| changePercent        | String | 涨跌幅(%)      |
| amplitude            | String | 振幅(%)        |
| limitUp              | String | 涨停价         |
| limitDown            | String | 跌停价         |
| volume               | String | 成交量(手)     |
| turnover             | String | 成交额(元)     |
| turnoverRate         | String | 换手率(%)      |
| circulationShares    | String | 流通股本       |
| circulationMarketValue | String | 流通市值    |
| totalMarketValue     | String | 总市值         |
| peTtm                | String | 市盈率TTM      |
| peStatic             | String | 市盈率静态     |
| pb                   | String | 市净率         |
| totalVolume          | String | 总成交量       |
| turnoverRate5d       | String | 5日换手率(%)   |

### 五档盘口
**主题**：lv1/orderBook/{code}
```json
{
  "bid": {
    "bid5": { "volume": "5041.00", "price": "10.72" },
    "bid4": { "volume": "13138.00", "price": "10.73" },
    "bid3": { "volume": "12305.00", "price": "10.74" },
    "bid2": { "volume": "11690.49", "price": "10.75" },
    "bid1": { "volume": "7421.02", "price": "10.76" }
  },
  "ask": {
    "ask5": { "volume": "7999.00", "price": "10.81" },
    "ask4": { "volume": "8550.56", "price": "10.80" },
    "ask3": { "volume": "7481.29", "price": "10.79" },
    "ask2": { "volume": "1351.00", "price": "10.78" },
    "ask1": { "volume": "217.00", "price": "10.77" }
  }
}
```
| 字段名   | 类型   | 说明           |
|----------|--------|----------------|
| bid      | Object | 买盘（包含5个档位） |
| bid1-bid5| Object | 买盘1-5档位    |
| ask      | Object | 卖盘（包含5个档位） |
| ask1-ask5| Object | 卖盘1-5档位    |
| volume   | String | 委托量(手)     |
| price    | String | 委托价格       |

## 示例代码
我们提供多种语言的官方 SDK，帮助您快速集成 tendQuant 服务。

### Python SDK
Python SDK 简化了数据分析和量化交易的开发。
#### 安装
```bash
pip install tendquant
```
#### CLI 使用
```bash
# 打开 cmd 命令行，输入以下命令
tendquant -u a2dZMmrflhY4MwRN -p 6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu -s lv1/orderBook/000001
```
#### 使用示例
```python
import time
from tendquant import Client

username = "a2dZMmrflhY4MwRN"
password = "6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu"

# 创建MQTT客户端
client = Client(
    username=username,
    password=password
)

# 自定义消息处理回调函数
def on_message(client, userdata, msg):
    print(f">>> 收到主题数据 {msg.topic}: {msg.payload.decode()}")

# 连接到MQTT broker
client.connect(on_message)

# 订阅主题
client.subscribe("lv1/tradeList/000001")

try:
    while True:
        # 保持连接
        time.sleep(1)
except KeyboardInterrupt:
    client.disconnect() # 断开连接
```


### JavaScript SDK
JavaScript SDK 支持浏览器环境，可以在浏览器中直接使用。
#### 引入 SDK
```html
<script src="https://tendquant.cn/sdk/tendquant.js"></script>
```
#### 使用示例
```javascript
(async () => {
    console.log("开始初始化");

    // 初始化客户端配置
    const client = new TendQuant({
        username: 'a2dZMmrflhY4MwRN',
        password: '6WUc6unvD4G3VVPx4K6t2Nl5mVq4fjtu',
    });

    // 设置事件监听
    client.onConnected(() => {
        console.log('[System] 连接成功');
    });

    client.onConnectionLost((responseObject) => {
        console.error('[System] 连接丢失:', responseObject.errorMessage);
    });

    client.onFailure((err) => {
        console.error('[System] 连接失败:', err.errorMessage);
    });

    // 连接服务器
    try {
        await client.connect();
        console.log('[System] 正在订阅主题...');
        
        const topic = 'lv1/tradeList/#';
        await client.subscribe(topic);
        console.log('[System] 已订阅: ' + topic);
        
        client.onMessage(topic, (message) => {
            // 这里会收到所有股票的推送，直接打印
            console.log(message.destinationName, message.payloadString);
        });

    } catch (error) {
        console.error("[System] 初始化错误:", error);
    }
})();
```

### Java SDK
Java SDK 提供完整的行情数据接入功能。
#### 依赖
```xml
<repositories>
  <repository>
    <id>Eclipse Paho Repository</id>
    <url>https://repo.eclipse.org/content/repositories/paho-releases/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>org.eclipse.paho</groupId>
    <artifactId>org.eclipse.paho.mqttv5.client</artifactId>
    <version>1.2.5</version>
  </dependency>
</dependencies>
```
#### 使用示例
```java
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
```

## 收费定价
采用灵活的积分计费模式，按需订阅计费。

### 价格表
| 服务类型       | 订阅类型 | 积分消耗       | 说明                     |
|----------------|----------|----------------|--------------------------|
| lv1 成交明细   | 单股票   | 5 积分/月      | 单只股票3s合单成交数据   |
|                | 全市场   | 10000 积分/月  | 全市场所有股票3s合单成交数据 |
| lv1 行情快照   | 单股票   | 8 积分/月      | 单只股票的行情快照       |
|                | 全市场   | 15000 积分/月  | 全市场所有股票的行情快照 |
| lv1 五档盘口   | 单股票   | 8 积分/月      | 单只股票的五档盘口数据   |
|                | 全市场   | 15000 积分/月  | 全市场所有股票的五档盘口数据 |

### 新用户优惠
注册即送 100 积分，体验完整服务功能。

### 计费模式
- 积分制度：购买积分，使用积分创建订阅服务
- 按需付费：按实际业务需求订阅主题，灵活计费
- 批量优惠：批量订阅享受折扣优惠

### 充值套餐
| 套餐       | 积分数量 | 价格   | 每元优惠积分 |
|------------|----------|--------|--------------|
| 基础套餐   | 100 积分 | ¥1     | -            |
| 入门套餐   | 1000 积分| ¥10    | -            |
| 标准套餐   | 2000 积分| ¥20    | -            |
| 进阶套餐   | 4000 积分| ¥40    | -            |
| 高级套餐   | 7350 积分| ¥70    | 5            |
| 超值套餐   | 11000 积分| ¥100   | 10           |
| 至尊套餐   | 24000 积分| ¥200   | 20           |

### 温馨提示
积分用于创建应用，应用一旦创建，不支持修改，积分也无法退还。

## 常见问题
**Q: 如何获取认证用户和认证密码？**
A: 登录后，进入"应用管理"页面，创建新应用即可获取认证用户和认证密码。

**Q: 积分如何使用？**
A: 积分用于订阅行情、查询数据等操作。每次操作会自动扣除相应积分。

**Q: 批量订阅有什么优势？**
A: 批量订阅可以一次订阅全市场股票，大幅降低连接数和开发复杂度，成本更低。

**Q: 数据延迟是多少？**
A: 平均推送延迟为 10ms，确保您获取最新的市场行情。

**Q: 支持哪些股票？**
A: 支持A股全市场，包括主板、创业板、科创板等所有上市股票。

**Q: 如何联系技术支持？**
A: 您可以通过发送邮件至 support@tendquant.cn 联系我们。