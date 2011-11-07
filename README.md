Projekt TwitterBotJunior
========================

Autoren
------- 
* Benjamin Hoffmann
* Marco Knietzsch

Kurzbeschreibung
----------------

Der TwitterBot antwortet automatisch auf Tweets und zeigt eingehende Tweets an. Mögliche Antworten stehen in der Datei "answers.txt" (pro Zeile eine Antwort, es wird eine zufällige Antwort ausgesucht).

Start
-----

Benötigte Files in einem Ordner:
	-TwitterBotJunior.jar
	-MatefulBot
	-answers.txt
	-twitter4j.properties
Starten mit Konsole:
	"java -jar TwitterBotJunior.jar"

Verbinde TwitterBotJunior mit deinem Twitteraccount
---------------------------------------------------

Um TwitterBotJunior mit dem eigenen Twitteraccount zu verbinden, gebe am Anfang deinen Benutzernamen ein. Danach erhälst du die Nachricht, dass noch kein Token zu deinem Account existiert, wähle neues Token erstellen. Kopiere den erscheinenden Link in deinen Browser und gib TwitterBotJunior Zugriff auf deinen Account. Gib den auf der nächsten Seite erscheinenden Pin in die Konsole ein. Jetzt bist du mit deinem Account eingeloggt. Ab dem nächsten Start reicht es deinen Twitternamen einzugeben und du wirst verbunden.
Um deinen Account wieder für TwitterBotJunior zu sperren gehe in deinem Twitteraccount auf Einstellungen->Applikationen->MatefulBot->Zugriff widerrufen.