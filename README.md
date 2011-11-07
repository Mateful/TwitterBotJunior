Projekt TwitterBotJunior
========================

Version
-------
* Version 1.0

Autoren
------- 
* Benjamin Hoffmann
* Marco Knietzsch

Kurzbeschreibung
----------------

Der TwitterBot antwortet automatisch auf Tweets und zeigt eingehende Tweets an. Moegliche Antworten stehen in der Datei "answers.txt" (pro Zeile eine Antwort, es wird eine zufaellige Antwort ausgesucht). Zusaetzlich zur Antwort wird ein zufaelliger Smiley (aus "smileys.txt") ausgewaehlt und der Nachricht angefuegt.

Start
-----

Benoetigte Dateien in einem Ordner:
	* TwitterBotJunior.jar
	* MatefulBot
	* answers.txt
	* smileys.txt
	* twitter4j.properties

Starten mit Konsole:
	* "java -jar TwitterBotJunior.jar"

Verbinde TwitterBotJunior mit deinem Twitteraccount
---------------------------------------------------

Um TwitterBotJunior mit dem eigenen Twitteraccount zu verbinden, gebe am Anfang deinen Benutzernamen ein. Danach erhaelst du die Nachricht, dass noch kein Token zu deinem Account existiert, waehle neues Token erstellen. Kopiere den erscheinenden Link in deinen Browser und gib TwitterBotJunior Zugriff auf deinen Account. Gib den auf der naechsten Seite erscheinenden Pin in die Konsole ein. Jetzt bist du mit deinem Account eingeloggt. Ab dem naechsten Start reicht es deinen Twitternamen einzugeben und du wirst verbunden.
Um deinen Account wieder fuer TwitterBotJunior zu sperren gehe in deinem Twitteraccount auf Einstellungen->Applikationen->MatefulBot->Zugriff widerrufen.