## Результаты запуска

```
Benchmark                                Mode  Cnt    Score    Error  Units

TestSerializers.testDeserializeJSON      avgt   10  907,413 ± 14,840  ns/op
TestSerializers.testDeserializeProtobuf  avgt   10  673,834 ± 10,682  ns/op
TestSerializers.testSerializeJSON        avgt   10  505,712 ±  7,779  ns/op
TestSerializers.testSerializeProtobuf    avgt   10    3,787 ±  0,019  ns/op
```

### Выводы:

И при сериализации, и при десериализации Protobuf оказался быстрее JSON.

Использовать Protobuf, если :
- важна скорость сериализации/десериализации;
- есть ограничения по памяти, т.к. объект после Protobuf сериализации занимает меньше места, чем объект после JSON сериализации.

Использовать JSON, если:
- необходима читаемость данных для отладки или взаимодействия с приложением.
