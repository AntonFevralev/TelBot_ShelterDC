<h2>TelBot_ShelterDC - Телеграмм-бот приюта кошек и собак.</h2>
  
Проект TelBot_ShelterD&amp;C создан с целью обеспечить возможность пользователю узнать больше о содержании питомцев, о процедуре усыновления и о наших приютах. 

<li><a href="https://skyengpublic.notion.site/47bcac1b049f4af6b351e2ab5d05afb4">Техническое задание на проект</a></li>
<li><a href="">Видео с демонстрацией функционала приложения.</a></li>
<h4>В разработке принимали участие: Евгения Ганич, Дмитрий Рубцов, Руслан Черемисин, Антон Февралев.</h4>

Проект работает на фреймворке Spring. Взаимодействие с ботом происходит посредством библиотеки pengrad. База данных h2.

<h2>Backend:</h2>
    Java 17, Maven, Spring Boot, Spring Web, Spring JPA, Spring Date, REST, Swagger, Stream API, SQL, Liquibase
 <h2>Testing:</h2>
    JUnit, Mockito 
<h2>Frontend:</h2>
    Telegram
 <h2>Запуск приложения</h2>
    Для запуска приложения необходимо скопировать к себе код. Далее в файле src/main/resouces/application.properties прописать токен телеграм бота. В этом же файле прописать абсолютные пути к файлам src/main/resources/dogShelter.json и src/main/resources/catShelter.json в переменные name.of.dog.data.file и name.of.cat.data.file.  После чего собрать проект с помощью Maven в режиме игноририрования тестов. Собранный Jar файл запустить из командной строки с помощью команды java -jar абсолютный_путь_к_файлу
   
