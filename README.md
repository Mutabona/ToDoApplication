# ToDoApplication
Для компиляции проекта требуется maven, а также java 19-ой версии (https://maven.apache.org/)

Чтобы скомпилировать проект нужно:

 В директории с pom.xml файлом, в котором находятся все зависимости, прописать следующие комынды:
 
  1) mvn compile
  2) mvn package
  3) mvn install
  
 После чего в папке target появится файл {Название}-{версия приложения}.jar
 Его и нужно запустить обычной джавой (версия java должна совпадать с версией, указанной в pom файле, по умолчанию это 19)
