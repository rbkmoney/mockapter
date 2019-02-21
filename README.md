# Mockapter

 Адаптер, предназначеный для нагрузочного тестирования.

 ## Работа с consul'ом:

 В consul'е конфигурация хранится по следующему пути:

 ```
 mockapter/${service}/nodes/${hostname}
 ```

 Где:
 * **service** - имя адаптера, под который настроен *mockapter*. Сделано так, чтоб mockapter без проблем можно поднять в несколько различных адаптеров сразу. Хорошо подойдет для отладки failover'а например.
 * **hostname** - хост конктретной ноды

 В случае, если при старте нет конфигурации, *mockapter* инициализирует дефолтную конфигурацию.

 ## Примеры конфигурации:

 ```json
[
   {
      "request":{
         "method":"generate_token"
      },
      "response":{
         "delay":{
            "type":"fixed",
            "value":500
         },
         "intent":{
            "finish":{}
         }
      }
   },
   {
      "request":{
         "method":"handle_recurrent_token_callback"
      },
      "response":{
         "error":{
            "error_definition":{
               "unavailable_result":{
                  "reason":"Deadline reached"
               }
            }
         }
      }
   },
   {
      "request":{
         "method":"process_payment",
         "target_payment_status":"processed"
      },
      "response":{
         "delay":{
            "type":"lognormal",
            "median":80,
            "sigma":0.1
         },
         "intent":{
            "sleep":{
              "timeout": 5000
            }
         }
      }
   },
   {
      "request":{
         "method":"process_payment"
      },
      "response":{
         "delay":{
            "type":"uniform",
            "lower":75,
            "upper":85
         },
         "intent":{
            "finish":{}
         }
      }
   },
   {
      "request":{
         "method":"handle_payment_callback"
      },
      "response":{
         "intent":{
            "failure":{
               "code":"01",
               "reason":"Unsupported Card",
               "sub":"authorization_failed:payment_tool_rejected:bank_card_rejected:card_unsupported"
            }
         }
      }
   }
]
 ```
