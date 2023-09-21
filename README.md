Обучение FRP

1. Пример рефакторинга к функциональному тестируемому коду (игра в угадайку)
2. Общие принципы написания функций
3. Полнота (Totality)
4. Чистота (Purity)
5. Ссылочная прозрачность (referrential transparency, deterministic)
6. Общая работа с функциями
7. extensions
8. рекурсия — пример факториал
9. хвостовая рекурсия — пример фибоначчи
10. функции как аргументы (HOF)
11. каррирование
12. декаррирование
13. функции нескольких аргументов, частичное применение
14. композиция
15. хвостовая лямбда
16. extension к функции
17. экстеншн как аргумент функции
18. Algebraic Data Types
19. Понятие множество значений
20. sum
21. product
22. exponential
23. Проектирование через доменные типы
24. Обобщение понятия эффект, контейнеры и связывающие функции (стрелки клейсли)
25. Классы типов — функтор, моноид
26. Классы типов — монада, аппликатив, альтернатив
27. Cписок (List)
28. of
29. head
30. tail
31. drop
32. dropWhile
33. append
34. foldRight
35. length
36. take?
37. foldLeft — stack safe(!)
38. sum/product using foldLeft
39. foldRight in terms of foldLeft
40. append in terms of foldLeft
41. flatten in terms of foldLeft (list of lists)
42. map
43. filter
44. flatMap
45. filter in terms of flatMap
46. zipWith
47. hasSubsequence
48. FlatMap и умножение
49. Монада Optional — fight for Totality
50. реализация
51. map
52. flatMap
53. getOrElse: A
54. orElse: Option<A>
55. filter
56. lift (f:(A)->B):(O<A>)->O<B>
57. map2 (combine)
58. sequence (L<O<A>>->O<L<A>>)
59. L<A>.traverse(f:(A)->O<B>):O<L<B>>
60. Option comprehension
61. Монада IO (простая реализация)
62. map
63. flatMap
64. safeRun (в optional)
65. реализация простейшего императива
66. trampolining (*hard)
67. Стримы (ленивые списки)
68. Проблематика, структура данных для решения проблемы
69. of()
70. toList()
71. take(n)
72. drop(n)
73. takeWhile()
74. foldRight
75. forAll()
76. takeWhile in terms of foldRight
77. headOption in terms of foldRight()
78. map in terms of foldRight
79. filter in terms of foldRight
80. append in terms of foldRight
81. flatMap in terms of foldRight
82. Монада Either (реализация)
83. map
84. flatMap
85. catches
86. orElse
87. map2
88. flatmap2
89. toOptional
90. sequence
91. traverse
92. Either comprehension
93. IO safeRun to Either
94. Монада Reader — functional DI
95. Потребность и реализация
96. map
97. flatMap
98. Пример реализации на монадах
99. Текущий вариант реализации в котлине
100. Готовящийся вариант реализации в котлине
101. Детальный пример преобразования императивного приложения в мономорфную фп
102. Убираем ошибки парсинга
103. Вводим IO как объект инкапсулирующий функцию (io, map, flatMap)
104. Выделяем эффекты (ввод/вывод + nextRandom)
105. Выделяем новый main с запросом имени
106. Выделяем gameLoop
107. Выделяем checkContinue
108. Выделяем askContinue
109. Выносим методы в интерфесы, а функции по контекстам
110. infinite streams
111. constant
112. from
113. fibonacci
114. unfold
115. fibs in term of unfold
116. from in term of unfold
117. constant in term of unfold
118. map in term of unfold
119. take in term of unfold
120. takeWhile in term of unfold
121. zipWith in term of unfold
122. zipAll in term of unfold
123. startsWith
124. tails in term of unfold
125. scanRight
126. Монада State
127. unit()
128. map()
129. flatMap()
130. map2()
131. L<S<A>>.sequence->S<L<A>>
132. обобщение понятия Эффект для монады
133. Преобразование мономорфного приложения в полиморфное через HKT (tagless final)
134. Вариант реализации через Free Monads
135. CPS, монада continuation
136. реализация корутин в котлине
137. Разбор приложения на arrow и ktor
138. make impossible state impossible (пример с visibility на фронте)
139. Flow
140. Аппликатив (?)
141. Функтор (?)
142. Альтернатив (?)
143. Дуал (?)
144. Валидация с помощью аппликатива (?)
145. Моделирование интерфейсов через функции члены и экстеншны

https://www.youtube.com/watch?v=IcgmSRJHu_8

https://www.youtube.com/watch?v=Up7LcbGZFuo
