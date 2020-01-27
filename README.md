# web-crawler

Основной стек: Scala, sbt, Monix, Jsoup

Собрать приложение: `sbt assembly`

Output path: `assembly/web-crawler-0.1.jar`

Пример запуска приложения: 
`java -jar $PATH_JAR_FILE -i short.txt -o result.tsv`

Описание параметров запуска:

- -i, --in - путь до файла с именами сайтов
- -o, --out - результат в виде tsv файла, формат (title, keywords, description)
