# Java fast ip-checker

## Быстрая проверка корректности IP-адреса (IPv4).

**Особенности!** Незначащие нули недопустимы. Например адрес `0.0.0.01` считается некорректным, адрес `0.0.0.1` корректный.

[//]:-------------------------------------------------------------------------

## Архитектура решения.

* Язык: `Java`.
* Анализатор IP-адреса построен на основе **иерархической системы конечных автоматов**.
* **Главный** автомат-распознаватель IP-адреса взаимодействует с **подчинённым** автоматом-распознавателем диапазона допустимых значений.


[//]:-------------------------------------------------------------------------

## Содержание репозитория.

* Документация
    * [UML-диаграмма классов](./Automaton_based_ip-checking/doc.md/#uml-диаграмма-классов)
    * [Автомат-распознаватель IP-адреса](./Automaton_based_ip-checking/doc.md/#автомат-распознаватель-ip-адреса-класс-ipautomaton)
    * [Автомат-распознаватель диапазона [0..255]](./Automaton_based_ip-checking/doc.md/#автомат-распознаватель-диапазона-0255-класс-rangeautomaton)
* Класс [IPChecker](./Automaton_based_ip-checking/Code/IPChecker.java)
* Класс [IpAutomaton](./Automaton_based_ip-checking/Code/IpAutomaton.java)
* Класс [RangeAutomaton](./Automaton_based_ip-checking/Code/RangeAutomaton.java)
* Интерфейс [IAutomaton](./Automaton_based_ip-checking/Code/IAutomaton.java)
* Метод [ipCheck](./Integer_parsing_based_ip-checking/ipCheck.java) (для сравнения с временем работы ip-checker-а)


[//]:-------------------------------------------------------------------------

## Сравнение с другими вариантами проверки.

Тестирование проводилось на массиве (`String[] ipPool`), состоящем из **миллиона** IP-адресов (50% адресов корректные).


[//]:-------------------------------------------------------------------------

### 1. Проверка на основе **регулярных выражений** (пакет `java.util.regex`).

```Java
String ipRegex = "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$";
Pattern pattern = Pattern.compile(ipRegex);
long start = System.currentTimeMillis();
for (String ip : ipPool) {
    Matcher matcher = pattern.matcher(ip);
    boolean result = matcher.matches();
}
long end = System.currentTimeMillis();
System.out.println("Время: " + (end - start) + " мс"); // 310
```
Время проверки: **310 мс**.


[//]:-------------------------------------------------------------------------

### 2. Проверка на основе **анализа подстрок** (`substring`) и их **парсинга** (`Integer.parseInt`).

```Java
long start = System.currentTimeMillis();
for (String ip : ipPool) {
    boolean result = ipCheck(ip);
}
long end = System.currentTimeMillis();
System.out.println("Время: " + (end - start) + " мс"); // 160
```
Код метода [ipCheck](./Integer_parsing_based_ip-checking/ipCheck.java).

Время проверки: **160 мс**.


[//]:-------------------------------------------------------------------------

### 3. Проверка на основе **иерархической системы конечных автоматов-распознавателей**.

```Java
long start = System.currentTimeMillis();
for (String ip : ipPool) {
    boolean result = IPChecker.check(ip);
}
long end = System.currentTimeMillis();
System.out.println("Время: " + (end - start) + " мс"); // 110
```
Код класса [IPChecker](./Automaton_based_ip-checking/Code/IPChecker.java).

Время проверки: **110 мс**.