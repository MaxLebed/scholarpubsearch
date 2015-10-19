# Запуск системы #

java –jar "scholarPubSearch.jar" <config xml file>
<config xml file> - XML-файл, описывающий конфигурацию системы. Если файл не задан, система пытается взять его из папки ..\examples\config.xml

Пример такого файла<system>
<!--> Агент пользователя <--!>
<input name = "userInputAgent" file = "examples\userpref.xml" agent = "preferenceAgent"/>
<!--> Классификатор <--!>
<classifier name = "classifierAgent"/>
<!-->Агент-поисковик сайта arxiv.org <--!>
<arxiv name = "arxivOrgAgent"/>
<!-->Агент-поисковик сайт Google Scholar <--!>
<google name = "googleScholarAgent"/>
</system> }}}

Атрибут input.file задает имя файла, в котором система хранит предпочтения пользователя
Атрибут input.agent – Агента, работающего с предпочтениями.  Этот агент создается автоматически при поиске.

Необходимые библиотеки:
   # appframework-1.0.3.jar
   # swing-worker-1.1.jar
 * pache Http Client
   # commons-codec-1.3.jar 
   # commons-httpclient-3.1.jar
   # commons-logging-1.1.1.jar
 * JADE
   # http.jar
   # iiop.jar
   # jade.jar
   # jadeTools.jar```