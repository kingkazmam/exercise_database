package core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Language {

	ENGLISH,//ordinal 0
	GERMAN, //ordinal 1
	FRENCH; //ordinal 2
//	RUSSIAN,//ordinal 3
//	SPANISH,//ordinal 4
//	CHINESE,//ordinal 5
//	SWEDISH,//ordinal 6
//	NORWEGIAN,//ordinal 7
//  ESPERANTO,//ordinal 8
//  AFRIKAANS, //ordinal 9
//  GREEK, //ordinal 10
//  LATIN, //ordinal 11 (hey, then we need old english too :)
//  URDU, // ??
//  HINDI, // ??
//  JAPANESE,
//  CHINESE_CLASSICAL,
//  CHINESE_MODERN

	//public static final Map<String, String> translation;

	public static final Map<String, String> english;//en2en;<--not required as english is already english
	public static final Map<String, String> german;//en2de;
	public static final Map<String, String> french;//en2fr;
	// The cleaner not working approach using the enums ordinal().
	//public static final Map<String, String>[] translations
	//	= new HashMap[Language.values().length];
	static {

		/* ENGLISH */
		//position 0 <-- English (see enumeration Languages)
		Map<String, String> aMap;
		aMap = new HashMap<String, String>();
		aMap.clear();

//		aMap.put("part_sheettype", "part");//for distinction simply add '_sheettype'
//		aMap.put("parts_sheettype", "part_sheets");

		english = Collections.unmodifiableMap(aMap);
		//translations[Language.ENGLISH.ordinal()]
		//	= Collections.unmodifiableMap(aMap);








		/* GERMAN */
		// A new map is required as only the last assignment prevails.
		aMap = new HashMap<String, String>();
		//position 1 <-- German (see enumeration Languages)
		//aMap.clear();<-old approach,not working for unmodifiable list
		//,only last assignment is valid
		aMap.put("english", "Englisch");
		aMap.put("german", "Deutsch");
		aMap.put("french", "Franz&ouml;sisch");

		aMap.put("part database", "Aufgaben Datenbank");

		aMap.put("semester", "Semester");
		aMap.put("course", "Kurs");
		aMap.put("courses", "Kurse");
		aMap.put("lecturer", "Dozent");
		aMap.put("type", "Typ");
		aMap.put("types", "Typen");

		aMap.put("filelink", "Dateilink");
		aMap.put("author", "Autor");
		aMap.put("uploaded by", "hochgeladen von");
		aMap.put("splitter", "Splitter");

		aMap.put("filetype", "Dateityp");
		aMap.put("content", "Inhalt");

		//aMap.put("part", "\u00DCbung");
		//TODO Redo this completely, search within all languages!
		aMap.put("sheet", "\u00DCbungsblatt");
		aMap.put("sheet_solution", "\u00DCbungsblattl\u00F6sung");
		aMap.put("spreadsheet", "\u00DCbungsblatt");
		aMap.put("spreadsheet_solution", "\u00DCbungsblattl\u00F6sung");
		aMap.put("part_sheet", "\u00DCbungsblatt");
		aMap.put("part_sheet_solution", "\u00DCbungsl\u00F6sung");
		aMap.put("part_solution", "\u00DCbungsl\u00F6sung");
		aMap.put("parts_solution", "\u00DCbungsl\u00F6sung");
		aMap.put("exam", "Klausur");
		aMap.put("exam_solution", "Klausurl\u00F6sung");

		aMap.put("sheet", "Blatt");
		aMap.put("sheets", "Bl\u00E4tter");
		aMap.put("draft", "Entwurf");
		aMap.put("drafts", "Entw\u00FCrfe");
		aMap.put("part", "Aufgabe");
		aMap.put("parts", "Aufgaben");
		aMap.put("part_sheettype", "\u00DCbung");
		// for distinction simply add '_sheettype'
		aMap.put("parts_sheettype", "\u00DCbungen");

		aMap.put("upload", "hochladen");
		aMap.put("send", "abschicken");
		aMap.put("create", "erstellen");
		aMap.put("to_search", "suchen");
		aMap.put("search", "Suche");
		aMap.put("extended search", "erweiterte Suche");
		aMap.put("more searchcriteria", "mehr Suchkriterien");
		aMap.put("last modified", "letzte \u00DCberarbeitung");
		aMap.put("hello", "Hallo");
		aMap.put("view", "Ansicht");
		// search
		aMap.put("hits", "Treffer");

		// MENU
		aMap.put("start", "Start");// home
		aMap.put("upload sheet", "Blatt hinzuf\u00FCgen");
		aMap.put("add sheet", "Blatt hinzuf\u00FCgen");
		aMap.put("add a sheet", "Blatt hinzuf\u00FCgen");
		aMap.put("statistics", "Statistik");
		aMap.put("profile", "Profil");

		aMap.put("edit", "edit");
		aMap.put("add to draft", "in Entwurf");

		aMap.put("seconds", "Sekunden");
		aMap.put("collapse the whole tree", "Den ganzen Baum zusammenklappen");
		aMap.put("minimize the whole tree", "Den ganzen Baum minimieren");
		aMap.put("unfold the whole tree", "Den ganzen Baum aufklappen");
		aMap.put("expand the whole tree", "Den ganzen Baum ausklappen");

		aMap.put("entries_found", "Eintru00E4ge gefunden");
		aMap.put("entries found", "Eintru00E4ge gefunden");

		aMap.put("fileformat", "Dateiformat");
		aMap.put("remove", "entfernen");
		aMap.put("remove multiple", "Mehrere entfernen");
		aMap.put("multiple", "mehrere");

		// A
		aMap.put("are not distinguished on search", "werden bei der Suche nicht unterschieden");

		// B
		aMap.put("belongs to", "geh\u00F6rt zu");
		aMap.put("belongs", "geh\u00F6rt");

		// C
		aMap.put("category", "Kategorie");
		aMap.put("combination", "Kombination");
		aMap.put("corresponding", "zugeh\u00F6rig");
		aMap.put("corresponding solution", "zugeh\u00F6rige L\u00F6sung");

		// D
		aMap.put("data", "Daten");

		// E
		aMap.put("enter a search term", "geben Sie ein Suchwort ein");

		// F
		aMap.put("field(s)", "Feld(er)");
		aMap.put("format not permitted", "Format nicht erlaubt");

		//G
		aMap.put("upper and lower case", "Gross- und Kleinschreibung");

		//K
		//L
		aMap.put("log in with your user data", "loggen Sie sich mit Ihren Benutzerdaten ein");
		aMap.put("last sheet that has been added to", "Der letzte Entwurf zu dem etwas hinzugef\u00FCgt wurde");

		//N
		aMap.put("new draft", "neuer Entwurf");
		aMap.put("no success", "kein Erfolg");
		aMap.put("no result", "kein Ergebnis");
		aMap.put("no index data found", "keine Index-Daten gefunden");

		//O
		aMap.put("or", "oder");
		aMap.put("origin", "Ursprung");

		//P
		aMap.put("program", "Programm");
		aMap.put("please", "bitte");

		//R
		aMap.put("relevance", "Relevanz");
		aMap.put("replaces exactly one character", "ersetzt genau ein Zeichen");
		aMap.put("replaces zero or more characters", "ersetzt beliebig viele Zeichen");
		aMap.put("required information", "ben\u00F6tigte Information");

		//S
		//aMap.put("settings", "Einstellungen");//"Sonnenunterg\u00E4nge");
		aMap.put("search term", "Suchwort");
		aMap.put("supported types", "unterst\u00FCtzte Typen");

		//T
		aMap.put("tag", "Begriff");
		aMap.put("tags", "Begriffe");
		aMap.put("terms surrounded by quotation marks are searched"
				+ " exactly as entered"
				, "Begriffe in Anf\u00FChrungszeichen werden genau"
				+ " wie eingegeben gesucht");
		aMap.put("the search is conducted within the contents of the"
				+ " file as well as in the database value of the"
				+ " corresponding field(s).", "Es wird sowohl im"
				+ " Inhalt der Datei als auch beim jeweiligen"
				+ " Datenbankeintrag des|r Feldes|r gesucht.");
		aMap.put("this page is under construction"
				, "diese Seite befindet sich im Aufbau");
		aMap.put("to", "zu");

		//U
		aMap.put("upload date", "Hochlade-Datum");

		//V

		//W
		aMap.put("without", "ohne");

		//X

		//Y

		//Z


		german = Collections.unmodifiableMap(aMap);
		//translations[Language.GERMAN.ordinal()]
		//	= Collections.unmodifiableMap(aMap);









		/* FRENCH */
		aMap = new HashMap<String, String>();
		aMap.clear();
		aMap.put("english", "anglais");
		aMap.put("german", "allemand");
		aMap.put("french", "francais");

		aMap.put("part database", "Base de Données d'Exercice");

		aMap.put("semester", "semestre");
		aMap.put("course", "cours");
		aMap.put("courses", "cours");
		aMap.put("lecturer", "chargé de cours");
		aMap.put("type", "type");
		aMap.put("types", "types");

		aMap.put("filelink", "lien du fichier");// URL or URI insead?
		aMap.put("author", "auteur");
		aMap.put("uploaded by", "téléchargéer par");
		aMap.put("splitter", "sectionneur");

		aMap.put("filetype", "type de fichier");
		aMap.put("content", "contenu");

		aMap.put("sheet", "feuille d'exercice");
		aMap.put("sheet_solution", "solution de la feuille d'exercice");
		aMap.put("spreadsheet", "feuille d'exercice");
		aMap.put("spreadsheet_solution", "solution de la feuille d'exercice");
		aMap.put("part_sheet", "feuille d'exercice");
		//TODO Redo this completely, search within all languages!
		aMap.put("part_sheet_solution"
				, "solution de la feuille d'exercice");
		aMap.put("part_solution", "solution d'exercice");
		aMap.put("parts_solution"
				, "solution de la feuille d'exercice");
		aMap.put("exam", "exam");
		aMap.put("exam_solution", "exam solution");

		aMap.put("sheet", "feuille");
		aMap.put("sheets", "feuilles");
		aMap.put("draft", "ébauche");
		aMap.put("drafts", "ébauches");
		aMap.put("part", "exercice");
		aMap.put("parts", "exercice");
		aMap.put("part_sheettype", "feuille d'exercice");
		// for distinction simply add '_sheettype'
		aMap.put("parts_sheettype", "feuilles d'exercice");

		aMap.put("upload", "télécharger");
		aMap.put("send", "envoyer");
		aMap.put("create", "erstellen");
		aMap.put("to_search", "chercher");
		aMap.put("search", "recherche");
		aMap.put("extended search", "recherche avancée");
		aMap.put("more searchcriteria", "plus de critères");
		aMap.put("last modified", "dernière révision");// encode unicode
		aMap.put("hello", "salut");
		aMap.put("view", "voir");
		// search
		aMap.put("hits", "hits");

		// MENU
		aMap.put("start", "maison");// home
		aMap.put("upload sheet", "ajouter une feuille");// home
		aMap.put("add sheet", "ajouter une feuille");// home
		aMap.put("add a sheet", "ajouter une feuille");// home
		aMap.put("statistics", "statistiques");
		aMap.put("profile", "profil");

		aMap.put("edit", "edit");
		aMap.put("add to draft", "ajoute-le au projet"
				/* a_grave la conception" */);

		aMap.put("seconds", "secondes");
		aMap.put("collapse the whole tree", "plier l'arbre entier");
		aMap.put("minimize the whole tree"
				, "minimiser l'arbre entierDen ganzen Baum ausklappen");
		aMap.put("unfold the whole tree", "développez l'arbre entier");
		aMap.put("expand the whole tree"
				, "développez l'arbre entierDen ganzen Baum ausklappen");

		aMap.put("entries found", "trouv\u00E9es");

		aMap.put("fileformat", "fileformat"/* format fichier */);

		aMap.put("remove", "supprimer");
		//aMap.put("remove multiple", "supprimer");
		aMap.put("multiple", "multiple");


		// B
		aMap.put("belongs to", "appartient \u00E0");//TODO: grammar
		aMap.put("belongs", "associ\u00E9");
		// C
		aMap.put("category", "cat\u00E9gorie");
		aMap.put("corresponding", "correspondant");
		aMap.put("corresponding solution", "solution correspondant");
		// D
		aMap.put("data", "data");
		// N
		aMap.put("new draft", "nouveaux \u00E9bauche");
		aMap.put("no success", "pas de succ\u00E9s");
		// O
		aMap.put("or", "ou");
		aMap.put("origin", "origine");
		// P
		aMap.put("program", "programme");
		// S
		//aMap.put("settings", "Einstellungen");

		// T
		aMap.put("tag", "terme");
		aMap.put("tags", "termes");
		aMap.put("to", "\u00E0");// a apostrophe de grave :)
		// U
		aMap.put("upload date", "upload date");
		// V

		// W

		// X

		// Y

		//Z

		// en2fr
		french = Collections.unmodifiableMap(aMap);



	};








	//Map<String, String> translation;
	private Language(/*Map<String, String> translation*/) {
		//this.translation = translation;


	}





	// Used for fishing the correct language strings
	public int toInt() {
		return this.ordinal();
	}

	public String toString() {
		return this.name().substring(0, 1).toUpperCase()
			+ this.name().substring(1).toLowerCase();
	}


	//static
	public static Language getByName(String langname) {
		for (Language language : Language.values()) {
			if (langname.toLowerCase().equals(language.name()
						.toLowerCase())) {
				return language;
			}
		}
		// if this language was not found
		return null;
	}


	public Map<String, String> getTranslationMap() {
		try {
			String language = this.name().toLowerCase();
			Object fieldObject = this.getClass()
				.getDeclaredField(language).get(this);
			if (fieldObject instanceof Map<?, ?>) {
				return (Map<String, String>)this.getClass()
					.getDeclaredField(
							this.name().toLowerCase()).get(this);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	public String getTranslation(String toTranslate) {
		return this.getTranslationMap().get(toTranslate.toLowerCase());
	}





}
