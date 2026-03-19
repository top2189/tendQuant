# A股全市场行情获取
个人量化开发必备，快速获取 A股 全市场5000+股票和场内 ETF 基金的实时行情数据，基于 MQTT 协议提供实时、稳定、高效的行情数据推送服务，免费获取常见 A股 指数的实时行情数据。

## 准备工作
1. [注册 tendQuant 账户](https://tendquant.cn/register)
2. [在管理应用中创建应用](https://tendquant.cn/dashboard/manage-apps)

## 行情接口

### 接口概览

| 订阅目标 | 数据类型   | 主题格式               | 说明               |
|----------|------------|------------------------|--------------------|
| 股票  | 成交明细   | lv1/tradeList/{code}   | 单只股票成交明细   |
|          |            | lv1/tradeList/#        | 所有股票成交明细   |
| 股票  | 行情快照   | lv1/snapshots/{code}   | 单只股票行情快照   |
|          |            | lv1/snapshots/#        | 所有股票行情快照   |
| 股票  | 五档盘口   | lv1/orderBook/{code}   | 单只股票五档盘口   |
|          |            | lv1/orderBook/#        | 所有股票五档盘口   |
| ETF 基金  | 成交明细   | lv1/tradeList_etf/{code}   | 单个 ETF 成交明细   |
|          |            | lv1/tradeList_etf/#        | 所有 ETF 成交明细   |
| ETF 基金  | 行情快照   | lv1/snapshots_etf/{code}   | 单个 ETF 行情快照   |
|          |            | lv1/snapshots_etf/#        | 所有 ETF 行情快照   |
| ETF 基金  | 五档盘口   | lv1/orderBook_etf/{code}   | 单个 ETF 五档盘口   |
|          |            | lv1/orderBook_etf/#        | 所有 ETF 五档盘口   |
| 指数  | 指数行情   | lv1/index/{code}   | 单个指数行情快照   |
|          |            | lv1/index/#        | 常见指数行情快照    |

以下是支持的 ETF 基金和指数列表，请根据需要选择订阅。

[ETF列表](./docs/etf.md)

[指数列表](./docs/index.md)

**注意**
对于需要订阅大量股票的场景，推荐使用"批量行情"接口，一次请求即可订阅全市场股票，大幅降低连接数和成本。

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
| lv1 指数行情    | 单股票   | 50    |
|                | 全市场   | 5     |

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

**主题**：lv1/tradeList/000001
```json
{
  "price": 10.88,
  "volume": 284,
  "count": 22,
  "timestamp": 1773903360
}
```
| 字段名       | 类型   | 说明         |
|--------------|--------|--------------|
| price        | Float  | 成交价格     |
| volume       | Float    | 成交数量     |
| count        | Int    | 成交笔数     |
| timestamp    | Long    | 成交时间戳   |

### 行情快照
**主题**：lv1/snapshots/000001
```json
{
  "currentPrice": 10.96,
  "stockCode": "000001",
  "stockName": "平安银行",
  "yesterdayPrice": 11.03,
  "changePercent": -0.63,
  "change": -0.07,
  "peTtm": 4.93,
  "pb": 0.47,
  "turnoverRate": 0.41,
  "highPrice": 11.04,
  "lowPrice": 10.91,
  "openPrice": 11.04,
  "totalMarketValue": 212690000000,
  "circulationMarketValue": 212690000000,
  "circulationShares": 19410000000,
  "turnover": 868000000,
  "totalVolume": 791100,
  "averagePrice": 10.97,
  "amplitude": 1.18,
  "volumeRatio": 0.96,
  "peDynamic": 4.16,
  "turnoverRealRate": 0.92,
  "limitUp": 12.13,
  "limitDown": 9.93
}
```

| 字段名 | 类型    | 说明 |
|------------|---------|------------------|
| currentPrice         | Float   | 当前股价|
| stockCode            | String  | 股票代码|
| stockName            | String  | 股票名称|
| yesterdayPrice       | Float   | 昨日收盘价|
| changePercent        | Float   | 涨跌幅（单位：%）|
| change               | Float   | 涨跌额，当前价-昨日收盘价|
| peTtm                | Float   | 滚动市盈率（单位：%，亏损显示 0）   |
| pb                   | Float   | 市净率（PB），股价与每股净资产的比值|
| turnoverRate         | Float   | 换手率（单位：%）
| highPrice            | Float   | 最高价|
| lowPrice             | Float   | 最低价    |
| openPrice            | Float   | 开盘价    |
| totalMarketValue     | Long    | 总市值 |
| circulationMarketValue | Long  | 流通市值  |
| circulationShares    | Long    | 流通股本（单位：股）   |
| turnover             | Long    | 成交金额，核心交易规模指标|
| totalVolume          | Long    | 总成交量（单位：手） |
| averagePrice         | Float   | 成交均价    |
| amplitude            | Float   | 振幅（单位：%） |
| volumeRatio          | Float   | 量比，成交量相对过去5日的变化|
| peDynamic            | Float   | 动态市盈率（单位：%，亏损显示 0） |
| turnoverRealRate     | Float   | 实际换手率（单位：%）
| limitUp              | Float   | 涨停价|
| limitDown            | Float   | 跌停价 |


### 五档盘口
**主题**：lv1/orderBook/000001
```json
{
  "bid": {
    "bid5": {"volume": 4373,   "price": 10.92},
    "bid4": {"volume": 3407,   "price": 10.93},
    "bid3": {"volume": 1527,   "price": 10.94},
    "bid2": {"volume": 4743,   "price": 10.95},
    "bid1": {"volume": 128.03, "price": 10.96}
  },
  "ask": {
    "ask5": {"volume": 4254.2, "price": 11.01},
    "ask4": {"volume": 5635,   "price": 11},
    "ask3": {"volume": 6435,   "price": 10.99},
    "ask2": {"volume": 5178,   "price": 10.98},
    "ask1": {"volume": 37,     "price": 10.97}
  }
}
```
| 字段名   | 类型   | 说明           |
|----------|--------|----------------|
| bid      | Object | 买盘（包含5个档位） |
| bid1-bid5| Object | 买盘1-5档位    |
| ask      | Object | 卖盘（包含5个档位） |
| ask1-ask5| Object | 卖盘1-5档位    |
| volume   | Float | 委托量(手)     |
| price    | Float | 委托价格       |

### 指数行情
**主题**：lv1/index/SZZS
```json
{
  "indexName": "上证指数",
  "amplitude": 1.05,
  "highPrice": 4065.37,
  "yesterdayPrice": 4049.91,
  "changePercent": 0.32,
  "currentPrice": 4062.98,
  "openPrice": 4053.31,
  "change": 13.07,
  "lowPrice": 4023.03,
  "volumeRatio": 0.84
}
```
| 字段名         | 类型   | 说明           |
|----------------|--------|----------------|
| indexName      | String | 指数名称       |
| amplitude      | Float  | 振幅           |
| highPrice      | Float  | 最高价         |
| yesterdayPrice | Float  | 昨日收盘价     |
| changePercent  | Float  | 涨跌幅         |
| currentPrice   | Float  | 当前价         |
| openPrice      | Float  | 开盘价         |
| change         | Float  | 涨跌额         |
| lowPrice       | Float  | 最低价         |
| volumeRatio    | Float  | 量比           |

`注：ETF 数据和股票格式一致，这里不具体给出。`

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

#### 股票订阅
| 服务类型       | 订阅类型 | 积分消耗       | 说明                     |
|----------------|----------|----------------|--------------------------|
| lv1 成交明细   | 单股票   | 5 积分/月      | 单只股票3s合单成交数据   |
|                | 全市场   | 10000 积分/月  | 全市场所有股票3s合单成交数据 |
| lv1 行情快照   | 单股票   | 8 积分/月      | 单只股票的行情快照       |
|                | 全市场   | 15000 积分/月  | 全市场所有股票的行情快照 |
| lv1 五档盘口   | 单股票   | 8 积分/月      | 单只股票的五档盘口数据   |
|                | 全市场   | 15000 积分/月  | 全市场所有股票的五档盘口数据 |

#### ETF 订阅
| 服务类型       | 订阅类型 | 积分消耗       | 说明                     |
|----------------|----------|----------------|--------------------------|
| lv1 成交明细   | 单个 ETF   | 5 积分/月      | 单只 ETF 3s合单成交数据   |
|                | 全部 ETF   | 3000 积分/月  | 全市场所有 ETF 3s合单成交数据 |
| lv1 行情快照   | 单个 ETF   | 8 积分/月      | 单只 ETF 的行情快照       |
|                | 全部 ETF   | 5000 积分/月  | 全市场所有 ETF 的行情快照 |
| lv1 五档盘口   | 单个 ETF   | 5 积分/月      | 单只 ETF 的五档盘口数据   |
|                | 全部 ETF   | 3000 积分/月  | 全市场所有 ETF 的五档盘口数据 |

#### 指数订阅
| 服务类型       | 订阅类型 | 积分消耗       | 说明                     |
|----------------|----------|----------------|--------------------------|
| lv1 指数行情   |  单个指数   | 免费      | 单个指数的行情数据   |
|                |   常用指数  | 免费  | 常用指数的行情数据 |

`注：对于免费的指数行情订阅，也需要去控制台单独创建应用并开通权限。`

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