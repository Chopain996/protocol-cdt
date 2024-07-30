
CDT目前支持：
①遥测解析
②遥信解析
③遥脉解析
④变位插帧遥信报文解析
⑤SOE解析

CDT目前不支持：
①下行报文解析，即主站发往从站的控制报文


运行环境：Java1.8+Maven
使用到的库：Netty+Jedis+JSerial


### 1.配置文件json

解析数据存入Redis，配置文件位于`protocol-core/src/main/resources/config.json`，配置Redis相关，串口相关

```json
{
  "COM": "COM2",	
  "baudRate": 9600,		
  "redisHost": "192.168.232.128",
  "redisPass": "123456",
  "redisPort": 6379
}
```


### 2.redis存入json格式
#### 遥脉
```json
[
  {
    "消息类型": "遥脉(D2帧)",
    "消息类型码": "0x85",
    "信息字数": 56,
    "源站址": 1,
    "目的站址": 1,
    "变位": "否",
    "Datas": [
      {
        "160": "[1489149,  有效]"
      },
      {
        "161": "[0,  有效]"
      },
      {
        "162": "[659129,  有效]"
      },
      {
        "163": "[64,  有效]"
      },
      {
        "164": "[0,  有效]"
      },
      {
        "165": "[0,  有效]"
      },
      {
        "166": "[0,  有效]"
      },
      {
        "167": "[0,  有效]"
      },
      {
        "168": "[0,  有效]"
      },
      {
        "169": "[0,  有效]"
      },
      {
        "170": "[0,  有效]"
      },
      {
        "171": "[0,  有效]"
      },
      {
        "172": "[1537680,  有效]"
      },
      {
        "173": "[0,  有效]"
      },
      {
        "174": "[868920,  有效]"
      },
      {
        "175": "[160,  有效]"
      },
      {
        "176": "[1647250,  有效]"
      },
      {
        "177": "[0,  有效]"
      },
      {
        "178": "[1219200,  有效]"
      },
      {
        "179": "[0,  有效]"
      },
      {
        "180": "[2470,  有效]"
      },
      {
        "181": "[0,  有效]"
      },
      {
        "182": "[8850,  有效]"
      },
      {
        "183": "[0,  有效]"
      },
      {
        "184": "[1128730,  有效]"
      },
      {
        "185": "[160,  有效]"
      },
      {
        "186": "[615670,  有效]"
      },
      {
        "187": "[350,  有效]"
      },
      {
        "188": "[1085270,  有效]"
      },
      {
        "189": "[0,  有效]"
      },
      {
        "190": "[753650,  有效]"
      },
      {
        "191": "[0,  有效]"
      },
      {
        "192": "[1690,  有效]"
      },
      {
        "193": "[0,  有效]"
      },
      {
        "194": "[6330,  有效]"
      },
      {
        "195": "[0,  有效]"
      },
      {
        "196": "[1106152,  有效]"
      },
      {
        "197": "[10,  有效]"
      },
      {
        "198": "[471303,  有效]"
      },
      {
        "199": "[1,  有效]"
      },
      {
        "200": "[672741,  有效]"
      },
      {
        "201": "[0,  有效]"
      },
      {
        "202": "[295011,  有效]"
      },
      {
        "203": "[12,  有效]"
      },
      {
        "204": "[672785,  有效]"
      },
      {
        "205": "[0,  有效]"
      },
      {
        "206": "[295012,  有效]"
      },
      {
        "207": "[12,  有效]"
      },
      {
        "208": "[625398,  有效]"
      },
      {
        "209": "[0,  有效]"
      },
      {
        "210": "[273752,  有效]"
      },
      {
        "211": "[10,  有效]"
      },
      {
        "212": "[625416,  有效]"
      },
      {
        "213": "[0,  有效]"
      },
      {
        "214": "[273793,  有效]"
      },
      {
        "215": "[10,  有效]"
      }
    ]
  }
]
```

#### 遥测
**由于遥测每个信息字带2个数据，因此Datas中List每个map有两个点位的值**
```json
[
  {
    "消息类型": "重要遥测(A帧)",
    "消息类型码": "0x61",
    "信息字数": 16,
    "源站址": 1,
    "目的站址": 0,
    "变位": "否",
    "Datas": [
      {
        "0": "[-1,  有效]",
        "1": "[-1,  有效]"
      },
      {
        "2": "[0,  有效]",
        "3": "[-1,  有效]"
      },
      {
        "4": "[0,  有效]",
        "5": "[0,  有效]"
      },
      {
        "6": "[0,  有效]",
        "7": "[0,  有效]"
      },
      {
        "8": "[0,  有效]",
        "9": "[0,  有效]"
      },
      {
        "10": "[0,  有效]",
        "11": "[0,  有效]"
      },
      {
        "12": "[1515,  有效]",
        "13": "[-29,  有效]"
      },
      {
        "14": "[1215,  有效]",
        "15": "[0,  有效]"
      },
      {
        "16": "[0,  有效]",
        "17": "[0,  有效]"
      },
      {
        "18": "[0,  有效]",
        "19": "[0,  有效]"
      },
      {
        "20": "[0,  有效]",
        "21": "[0,  有效]"
      },
      {
        "22": "[0,  有效]",
        "23": "[0,  有效]"
      },
      {
        "24": "[0,  有效]",
        "25": "[0,  有效]"
      },
      {
        "26": "[0,  有效]",
        "27": "[1,  有效]"
      },
      {
        "28": "[1,  有效]",
        "29": "[1,  有效]"
      },
      {
        "30": "[10,  有效]",
        "31": "[-274, 溢出 无效]"
      }
    ]
  }
]
```

#### 遥信
**由于遥信每个信息字最多携带32个信息，因此Datas的List长度为2，携带了64个子站信息，Datas[0]为第1个信息字携带的前32个站点信息，Datas[1]为第2个信息字携带的后32个站点信息**
```json
[
  {
    "消息类型": "遥信状态(D1帧)",
    "消息类型码": "0xf4",
    "信息字数": 2,
    "源站址": 1,
    "目的站址": 1,
    "变位": "否",
    "Datas": [
      {
        "0": false,
        "1": false,
        "2": false,
        "3": false,
        "4": false,
        "5": false,
        "6": false,
        "7": false,
        "8": false,
        "9": false,
        "10": false,
        "11": true,
        "12": true,
        "13": true,
        "14": true,
        "15": true,
        "16": true,
        "17": true,
        "18": true,
        "19": true,
        "20": true,
        "21": true,
        "22": true,
        "23": true,
        "24": true,
        "25": true,
        "26": true,
        "27": true,
        "28": true,
        "29": true,
        "30": true,
        "31": true
      },
      {
        "32": true,
        "33": true,
        "34": true,
        "35": true,
        "36": true,
        "37": true,
        "38": true,
        "39": false,
        "40": false,
        "41": false,
        "42": false,
        "43": false,
        "44": false,
        "45": false,
        "46": false,
        "47": true,
        "48": true,
        "49": false,
        "50": false,
        "51": false,
        "52": false,
        "53": false,
        "54": false,
        "55": false,
        "56": false,
        "57": false,
        "58": false,
        "59": false,
        "60": false,
        "61": false,
        "62": false,
        "63": false
      }
    ]
  }
]
```
#### 变位遥信插帧
```json
[
    {
        "消息类型": "重要遥测(A帧)",
        "消息类型码": "0x61",
        "信息字数": 32,
        "源站址": 2,
        "目的站址": 2,
        "变位": "遥信插针",
        "Datas": [
            {
                "96": false,
                "97": false,
                "98": true,
                "99": true,
                "100": true,
                "101": false,
                "102": true,
                "103": false,
                "104": false,
                "105": false,
                "106": true,
                "107": false,
                "108": false,
                "109": false,
                "110": true,
                "111": true,
                "112": true,
                "113": false,
                "114": false,
                "115": true,
                "116": true,
                "117": true,
                "118": true,
                "119": false,
                "120": false,
                "121": true,
                "122": true,
                "123": false,
                "124": true,
                "125": true,
                "126": true,
                "127": false
            },
            {
                "2": "[20,  有效]",
                "3": "[31,  有效]"
            },
            {
                "4": "[43,  有效]",
                "5": "[53,  有效]"
            },
            {
                "6": "[63,  有效]",
                "7": "[76,  有效]"
            },
            {
                "8": "[81,  有效]",
                "9": "[91,  有效]"
            },
            {
                "10": "[104,  有效]",
                "11": "[118,  有效]"
            },
            {
                "12": "[124,  有效]",
                "13": "[132,  有效]"
            },
            {
                "14": "[150,  有效]",
                "15": "[160,  有效]"
            },
            {
                "16": "[164,  有效]",
                "17": "[183,  有效]"
            },
            {
                "18": "[182,  有效]",
                "19": "[197,  有效]"
            },
            {
                "20": "[206,  有效]",
                "21": "[219,  有效]"
            },
            {
                "22": "[223,  有效]",
                "23": "[234,  有效]"
            },
            {
                "24": "[241,  有效]",
                "25": "[252,  有效]"
            },
            {
                "26": "[260,  有效]",
                "27": "[294,  有效]"
            },
            {
                "28": "[293,  有效]",
                "29": "[310,  有效]"
            },
            {
                "30": "[305,  有效]",
                "31": "[319,  有效]"
            },
            {
                "32": "[331,  有效]",
                "33": "[354,  有效]"
            },
            {
                "34": "[341,  有效]",
                "35": "[378,  有效]"
            },
            {
                "36": "[368,  有效]",
                "37": "[394,  有效]"
            },
            {
                "38": "[391,  有效]",
                "39": "[407,  有效]"
            },
            {
                "40": "[437,  有效]",
                "41": "[436,  有效]"
            },
            {
                "42": "[430,  有效]",
                "43": "[446,  有效]"
            },
            {
                "44": "[445,  有效]",
                "45": "[470,  有效]"
            },
            {
                "46": "[483,  有效]",
                "47": "[505,  有效]"
            },
            {
                "48": "[489,  有效]",
                "49": "[492,  有效]"
            },
            {
                "50": "[501,  有效]",
                "51": "[552,  有效]"
            },
            {
                "52": "[529,  有效]",
                "53": "[570,  有效]"
            },
            {
                "54": "[575,  有效]",
                "55": "[573,  有效]"
            },
            {
                "56": "[604,  有效]",
                "57": "[619,  有效]"
            },
            {
                "58": "[608,  有效]",
                "59": "[596,  有效]"
            },
            {
                "60": "[606,  有效]",
                "61": "[660,  有效]"
            },
            {
                "62": "[653,  有效]",
                "63": "[687,  有效]"
            }
        ]
    }
]
```

### 3.运行

修改配置文件后，`Main`函数位于`mainuse`的Module下。运行main函数后，等待子站连接发送数据，接收到数据控制台会进行打印。