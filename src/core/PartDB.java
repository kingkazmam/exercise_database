package core;


import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;





public class PartDB {


	//======= ATTRIBUTES
	private static Map<String, Sheetdraft> allSheetdrafts = new HashMap<String, Sheetdraft>();
	private static Map<String, Sheetdraft> drafts = new HashMap<String, Sheetdraft>();
	private static Sheetdraft latestChangedDraft;

	// To allow for switching the context. So native, common and plaintext
	// parts are stored:
	public static DocType commonFormat = DocType.HTML; // <-- HTML has been chosen common storage format.


	//======= METHODS
	/**
	 * Extracts the individual parts from the given sheet.
	 * Depending on the inputFile it delivers other files too.
	 * <br />
	 * Attention: This method writes (besides on RTF) no files to disk!
	 * Files are written to disk later via toString methods of the return.
	 * <br />
	 * Attention: It is assumed that a PDF file also exists in the same
	 * folder with the same filename if a TEX is given to this method.
	 * The PDF is required for finding the part heads as the style
	 * of TEX files can vary very much.
	 *
	 * @param filePath Filelink
	 * @param root
	 * @param target Target
	 * @return Sheetdraft object
	 * @throws Exception
	 */
	/**
	 * Sometimes the filelink on which base we operate should be updated
	 * e.g.
	 * For DOC->DOCX as conversion to DOCX is doable and XML is parsable.
	 * Thus the DOCX becomes the base as if it had always been a DOCX.
	 * That's why there is the static filelink attribute and the
	 * processUploaded method call without parameter (so that in this
	 * method the attribute is used and not the parameter).
	 * This allows to easily change it on the fly.
	 * Warning: In the sheetdraft itself still the no-longer-base
	 * filelink resides, which may be useful for statistics.
	 *
	 * @param filelink - absolute filelink to uploaded file.
	 */
	public static String filelink;
	public static Sheetdraft processUploadedSheetdraft(String filelink)
//			throws Exception {
//		//PartDB.filelink = filelink;
//		return processUploadedSheetdraft(filelink);
//	}
	//private static Sheetdraft processUploadedSheetdraft()
			throws Exception {

		// ------- superfluous?
		// Check file ending using the DocType enum.
		DocType sheetType;
		sheetType = DocType.getByEnding(Global.extractEnding(filelink));
//			for(DocType type : DocType.values()) {//this is a relict.
//				if (filePath.endsWith(type.getCode())) {
//					sheetType = type;
//					break;
//				}
//			}
		if (sheetType == null) {
			//TODO Specialize the exception to e.g.FiletypeNotSupported?
			System.out.println("PartDB: Not implemented: " + Global.extractEnding(filelink));
			throw new NotImplementedException();
		}
		System.out.println("It is a " + sheetType + " document.");
		// ------- superfluous? -END




		//======= BASEFILE HAS CHANGED?
		System.out.println("Basefile as uploaded: (prior to compatibility conversion)\r\n" + filelink);
		//PartDB.convertInputFormatIntoSupportedFormat();
		//generatedFlavours.addAll(
		if (filelink.endsWith("doc")) {
			Global.addMessage("Unsupported basefile. Trying to convert."
					, "warning");

			String hypo_filelink = Global.replaceEnding(filelink, "docx");
			/*filelink = */Global.convertFile(filelink, "docx");
			if (new File(hypo_filelink).exists()) {// <-- absolute path
				Global.addMessage("Unsupported basefile successfully"
						+ " converted to a compatible one: "
						+ hypo_filelink, "success");
			}
			else {
				Global.addMessage("Unsupported basefile converted but"
						+ " could not be found on storage medium."
						, "nosuccess");
			}

			// not to forget:
			filelink = hypo_filelink;
		}
		//);
		System.out.println("Basefile as supported: " + filelink);


		Sheetdraft sheetdraft = new Sheetdraft(filelink);



		// Here follow the methods for dealing with the original files,
		// the filetype or content is being converted, the parts are
		// cut out.
		// 0) create flavours of this document.
		List<String> flavoursCreated_filelinks
			= sheetdraft.createFlavours();
		String common_filelink = null;
		if (flavoursCreated_filelinks != null
				&& flavoursCreated_filelinks.size() > 0) {

			for (String flavour : flavoursCreated_filelinks) {
				System.out.println("Generated filetype flavour: "
						+ flavour);
				String commonFormatLowerCase = commonFormat.name()
					.toLowerCase();
				if (flavour.endsWith(commonFormat.name())
						|| flavour.endsWith(commonFormatLowerCase)) {

					common_filelink = flavour;
					System.out.println("Found " + commonFormatLowerCase
							+ " file.");
					if (new File(common_filelink).exists()) {
						System.out.println("[confirmed] Found it in the"
								+ " filesystem at location:" + flavour);
						break; // Found what was wanted, thus stop.
					}
				}
			}
		}


		// 1) plain text / raw content
		sheetdraft.extractPlainText();
		System.out.println(Global.addMessage("[ready] Plain text"
					+ " extracted from Sheet. ----", "success"));


		// 2) The search for part declarations.
		int declarationsFoundCount = 0;
		sheetdraft.setDeclarationSet(
				DeclarationFinder.findeDeklarationen(sheetdraft));
		if (sheetdraft.getDeclarationSet() != null) {
			declarationsFoundCount
				= sheetdraft.getDeclarationSet().declarations.size();
		}
		System.out.println(
				Global.addMessage("[ready] Part and solution"
					+ " declarations have been searched. Found "
					+ declarationsFoundCount + " in the set with the"
					+ " highest score (total count, may contain mixed"
					+ " types). ----", (declarationsFoundCount > 0 ?
						"success" : "nosuccess danger")
					)
				);

		if (Global.extractEnding(sheetdraft.getFilelinkRelative())
				.equals("rtf")
				&& sheetdraft.getClass().toString().equals("Sheetdraft")
				) {// sheetdraft only! not part

			System.out.println("Looking for identifiers in the given"
					+ " RTF document.");
			new processors.RTFProcessor((Sheetdraft)sheetdraft));
		}


		// 3) Create parts. Look for solutions for each part
		// (within the part's content).
		sheetdraft.extractPartsPlainText();
		/*
		TODO either split into Part and Solution at once or
		otherwise employ an algorithm that besides for Loesung/Solution
		splitting keywords checks for repeating content (must support
		formulas) too.
		Reason:  No formatting to determine whether something new (e.g.
		paragraph, completely new style, ...) begins.
		*/
		System.out.println(Global.addMessage("[ready] Einzelaufgaben"
					+ " (Plain Text) wurden erstellt. ----", "success")
				);

		//Native format:
		sheetdraft.extractPartsNativeFormat();
		System.out.println(Global.addMessage("[ready] Einzelaufgaben"
					+ " (Nativ Format) wurden erstellt. ----", "success"
					)
				);

		// COMMON FORMAT - CONTEXT SWITCH
		// a1) store the native filelink:
		String native_filelink = sheetdraft.getFilelink();
		sheetdraft.setFilelink(common_filelink);
		// <- overwrite, because the only alternative is to pass around
		// arguments, and that proved vulnerable here.
		// b1) store the old native format part collection before
		// overriding:
		Map<String, Part> allPartsRawFormat
			= sheetdraft.getAllParts();
		// Operate on the common basis (common format).
		sheetdraft.setAllPartsRawFormat(
				sheetdraft.getAllPartsCommonFormat());

		// Not the same as above as the context changed,
		// i.e. the sheetdraft's filelink:
		sheetdraft.extractPartsNativeFormat(/*html_filelink*/);
		System.out.println(Global.addMessage("[ready] Einzelaufgaben"
					+ " (Common Format, i.e. " + commonFormat.name()
					+") wurden erstellt. ----", "success"));

		// COMMON FORMAT -END - CONTEXT SWITCH

		// a2) restore the native filelink: (We never operate fully on
		// HTML basis. This way fusion can choose between both.)
		sheetdraft.setFilelink(native_filelink); // prefer native format
		// fusion if all parts are of one type.

		// b2) restore native format parts:
		sheetdraft.setAllPartsCommonFormat(
				sheetdraft.getAllPartsCommonFormat());
		sheetdraft.setAllPartsRawFormat(allPartsRawFormat);


		// 4) synchronize filesystem/harddrive
		sheetdraft.writePartsToHarddisk(
				sheetdraft.getAllPartsPlainText());
		//sheetdraft.writePartsToHarddisk(sheetdraft.getAllParts());
		System.out.println(
				Global.addMessage("[ready] Einzelaufgaben wurden"
					+ " auf Festplatte geschrieben. ----", "success"));

		return sheetdraft;

	}



	/**
	 * Lists all files of the given folder and splits every file.
	 *
	 * @author Artiom Kichojal, J.R.I.B.-Wein
	 * @param source
	 *			Quellpfad
	 * @ param target
	 *			Zielpfad
	 * @throws Exception
	 */
	public static void splitAllFilesInDirectory(String source/*, String target*/)
			throws Exception {
		// Also check for existence and if it's a directory:
		File sourceFile = new File(source);
		String[] entries = (sourceFile.isDirectory() ? sourceFile.list()
				: (sourceFile.exists() ? new String[]{source} : new String[0]));
		for (int i = 0; i < entries.length; i++) {
			PartDB.processUploadedSheetdraft(
					source
					+ System.getProperty("file.separator")
					+ entries[i]
					);
		}

	}



	/**
	 * Load all sheet|drafts.
	 *
	 * @param user DB lecturer/author
	 * @param includeSheetsWhereUserIsLecturer
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void loadAllSheetdrafts() // thus also the parts
		throws SQLException, IOException {

		loadAllSheetdrafts((String)Global.session.getAttribute("user"), false);

	}

	public static void loadAllSheetdrafts( // thus also the parts
			boolean includeSheetsWhereUserIsLecturer)
		throws SQLException, IOException {

		loadAllSheetdrafts((String)Global.session.getAttribute("user")
				, includeSheetsWhereUserIsLecturer);

	}

	public static void loadAllSheetdrafts( // thus also the parts
			String user, boolean includeSheetsWhereUserIsLecturer)
			throws SQLException, IOException {

		//if (user != null && !user.isEmpty()) {
		//	Global.session.setAttribute("user", user);
		//}

		// Look for this user's drafts
		String query = "SELECT DISTINCT *"
				+ " FROM sheetdraft"
				+ (includeSheetsWhereUserIsLecturer ? ", lecturer" : "")
				+ " WHERE author = '"+ user +"'";
		if (includeSheetsWhereUserIsLecturer) {
			// Include sheetdrafts where user is lecturer because hiwis
			// could assist this way by uploading, editing for them.
			query = query + " OR lecturer = '" + user + "'"
					+ " AND lecturer_id = lecturer.id";
		}
		ResultSet res; res = Global.query(query);


//		int i = -1;
//		int drafts_i = -1;
		if (res == null) {
			System.out.println("No sheetdrafts could be loaded for"
					+ " this lecturer: " + user
					+ ".  includeheetsWhereUserIsLecturer: "
					+ includeSheetsWhereUserIsLecturer);
			return ;
		}
		res.last();
		int resL; resL = res.getRow();
		if (resL == 0) {
			// The last changed one is the active draft!
			latestChangedDraft = new Sheetdraft();
			// this results in this one ALWAYS being the latest changed
			// one if the resource length is not checked for being zero!
		}
		res.beforeFirst();
		List<Map<String, Object>> resRows
			= new ArrayList<Map<String, Object>>();
		while (res.next()) {
//			++i;
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("filelink", res.getString("filelink"));
			resMap.put("is_draft", res.getInt("is_draft"));
			resRows.add(resMap);
		}
		// Tackle memory leaks by closing result set, its statement:
		Global.queryTidyUp(res);

		for (Map<String, Object> row : resRows) {
			String filelink_debug = (String)row.get("filelink");
			if (filelink_debug.equals("-1")) {
				// Delete such entries from the database
				Global.query("DELETE FROM sheetdraft"
						+ " WHERE filelink = -1 OR filelink = '-1';");
				Global.addMessage("Sheetdraft with erroneous Filelink"
						+ " detected: Filelink='-1'. Deleted all"
						+ " such entries from the database."
						, "info");
				Global.addMessage("Skipped erroneous filelink=-1 entry."
						, "success");
				continue;
			}
			allSheetdrafts.put(
				filelink_debug,
				new Sheetdraft(
					filelink_debug
					/*
					,res.getString("filelink")
					,res.getString("type")
					,res.getString("course")
					,res.getString("semester")
					,res.getInt("lecturer_id")
					,res.getString("description")
					,res.getString("author")
					,res.getLong("whencreated")
					,res.getInt"is_draft")
					,res.getString("headermixture")
					,res.getLong("whenchanged")
					*/
				)//.synchronizeWithDatabaseLoadFromIt(res.getInt("id"))
				// Ensure that allSheetdrafts.keys (filelink) are
				// resynchronized to the changed lecturer = user.
				.synchronizeWithDatabaseLoadFromItBecomeIdentical(
						filelink_debug)
			);
			// DRAFT?
			//if (Integer.valueOf(res.getString("is_draft")) == 1) {
			if (((int)row.get("is_draft")) == 1) {
//				++drafts_i;
				drafts.put(
						//res.getString("id"),
						(String)row.get("filelink"),
						allSheetdrafts.get((String)row.get("filelink"))
				);
				// Determine if this is the new latest changed draft
				Calendar calFromDb = Calendar.getInstance();
				calFromDb.setTimeInMillis(res.getLong("whenchanged"));
				if (latestChangedDraft == null
						|| latestChangedDraft.getWhenchanged() == null
//						Integer.valueOf(res.getString("whenchanged"))
//						> Integer.valueOf(latestChangedDraft.getWhenchanged()) ) {
						|| calFromDb.after(
								latestChangedDraft.getWhenchanged())
						) {
					// This draft has been changed more recently
					//latestChangedDraft = allSheetdrafts.get(i);
					latestChangedDraft = allSheetdrafts.get(filelink_debug);
				}
			}
		}

	}



	/**
	 * Delete a sheet.
	 *
	 * @param filelink Identifier for the sheet to delete.
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void delete_sheet(String filelink) throws IOException, SQLException {

		String message = ""; // not to forget: reset after printing out

		// Some data that has to be loaded from the database:
		int lecturer_id = 0; // Do not delete the lecturer.
		String lecturer = ""; // For the message what has been deleted.
		String semester;

		// Is set to true if no more sheetdrafts within the same
		// semester the sheetdraft is in can be found.
		// (Because semester is the highest level folder in the
		// Global.uploadTarget filesystem directory.)
		boolean delete_highest_level_folder = false;


		// ======= DATABASE
		String str_query = "SELECT * FROM sheetdraft WHERE filelink='"
			+ filelink + "';";
		ResultSet res = Global.query(str_query);
		if (res.next()) {
			lecturer_id = res.getInt("lecturer_id");
			semester = res.getString("semester");
		}
		else {
			System.out.println(
				Global.addMessage("No sheetdraft found in database!"
					+ " Sheet/Draft already deleted?", "danger")
			);
			return ;
		}



		// Delete preview images from storage medium:
		str_query = "SELECT filelink FROM part"
			+ " WHERE sheetdraft_filelink ='" + filelink + "';";
		ResultSet res0 = Global.query(str_query);
		while (res0.next()) {
			File f = new File(Global.root + Global.convertToImageLink(
						res0.getString("filelink")));
			if (Global.deleteFile(f)) {
				message += "<p>The picture to part "
					+ Global.extractFilename(filelink)
					+ " was deleted successfully.</p>";
			}
		}
		// Tackle the island's memory leaks:
		Global.queryTidyUp(res0);

		// Print generated message:
		System.out.print(
			Global.addMessage(message, "success")
		);


		// Delete sheetdraft from the database:
		str_query = "DELETE FROM sheetdraft WHERE filelink='"
			+ filelink + "';";
		Global.query(str_query);




		// Delete draft parts:
		str_query = "DELETE FROM draftpartassignment"
			+ " WHERE sheetdraft_filelink ='" + filelink + "';";
		// This should be the case for drafts only (is_draft == 1).
		Global.query(str_query);

		// Delete sheet|draft parts:
		// TODO: Should the originsheedraft_filelink be reset to N.A.?
		// IMPORTANT: Do not delete parts that are still referenced
		// by drafts! (*)
//		str_query = "DELETE FROM"
//					+ " (SELECT COUNT(*) AS referencecount, part_filelink"
//					+ " FROM draftpartassignment, part"
//					+ " WHERE part.sheetdraft_filelink = '" + filelink + "'"
//					+ " AND draftpartassignment.part_filelink = part.filelink)"
//					+ " AS referenced"//=> no hits if no part is referenced!
//					+ ", part"//if it IS NOT 0 then we keep it as other sheets reference it
//				+ " WHERE"
//				//+ " referenced.referencecount IS 0" /*(*)that's what we deal with here!*/
//				+ " AND part.sheetdraft_filelink = '" + filelink + "'"//all parts of this sheetdraft
//				+ " AND part.filelink <> referenced.part_filelink"//parts of this sheetdraft without those that are referenced!
//				//+ " OR referenced.referencecount IS 0 OR referenced IS NULL);"//referenced.part_filelink IS NULL
//				//+ " AND part.sheetdraft_filelink = refs.sheetdraft_filelink;"
//		;
		// Because DELETE queries don't like nested queries.
		// Select all parts that are not referenced in a draft:
		str_query = " SELECT part.filelink"
		+ " FROM part"
		+ " INNER JOIN (" /* LEFT JOIN would keep all parts
					instead of only the not referenced ones. */
				+ " SELECT part_filelink"
				+ " FROM draftpartassignment, part"
				+ " WHERE part.sheetdraft_filelink='"
				+ filelink + "'"// => all parts of this sheetdraft
				+ " AND draftpartassignment.part_filelink"
				+ " = part.filelink"/* => parts of this
					sheetdraft without those that are referenced! */
		+ " ) referenced"
		+ " ON part.filelink <> referenced.part_filelink";
		// <-- filters out the referenced ones! The others remain.

		//SELECT part.filelink FROM part INNER JOIN ( SELECT part_filelink FROM draftpartassignment, part WHERE part.sheetdraft_filelink='uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.docx.docx' AND draftpartassignment.part_filelink = part.filelink) referenced ON part.filelink <> referenced.part_filelink
		//<-- this turned out to not work in all cases
		//So here comes another way:
		str_query = " SELECT part.filelink"
				+ " FROM part"
				+ " WHERE part.sheetdraft_filelink='"+ filelink + "'"
				+ " AND NOT EXISTS("/* <--whether to get the not
						referenced or the referenced ones (ie.those
						from the subquery for the latter) */
					+ " SELECT part_filelink"
					+ " FROM draftpartassignment"
					+ " WHERE part.sheetdraft_filelink='" + filelink + "'"
					+ " AND draftpartassignment.part_filelink = part.filelink"
				+ ")"
				+ " AND part.filelink <> '';"
				;
		ResultSet res_parts_not_referenced = Global.query(str_query);
		res_parts_not_referenced.beforeFirst();
		String filelinks_of_parts_not_referenced = "";
		String comma_or_not = "";
		// Chain all parts that are not referenced for deletion.
		// <-holocaust is horrible :/
		while (res_parts_not_referenced.next()) {
			// On the other hand less parts in homework makes
			// little fox's pupils happier? :)
			filelinks_of_parts_not_referenced = comma_or_not + "'"
					+ res_parts_not_referenced.getString("filelink") + "'";
			comma_or_not = ","; /* using this idea we can skip one|the
			first entry, why did I only get to it now, it's so easy! */
		}

		res_parts_not_referenced.last();
		int parts_not_referenced_count = res_parts_not_referenced.getRow();
		// Tackle memory leaks by closing result set and its statement:
		Global.queryTidyUp(res_parts_not_referenced);


		str_query = "DELETE FROM part WHERE sheetdraft_filelink = '"
			+ filelink + "'";
		// Any parts that are referenced by other sheets|drafts?
		if (parts_not_referenced_count > 1) {
			// Then restrict delete action on those.
			str_query += " AND filelink IN(" + filelinks_of_parts_not_referenced + ")";
		}
		else if (parts_not_referenced_count > 0) {
			// Then restrict delete action on this part.
			str_query += " AND filelink = '" + filelinks_of_parts_not_referenced + "'";
		}
		//else all parts of this sheetdraft that is being deleted
		// can be deleted too.
		Global.query(str_query);

		/*
		ATTENTION: Eventually make all part assignments real where
		the sheetdraft to be deleted is set as originsheetdraft!
		ie. reference -> instance.
		*/
		// Fetch the sheetdraft to which the part has been assigned
		// to/been referenced from:
		str_query = "SELECT draftpartassignment.sheetdraft_filelink, part_filelink"
				+ " FROM draftpartassignment, part"
				+ " WHERE part.sheetdraft_filelink = '" + filelink + "'"
				+ " AND part.filelink = draftpartassignment.part_filelink";
		//SELECT draftpartassignment.sheetdraft_filelink, part_filelink FROM draftpartassignment, part WHERE part.sheetdraft_filelink = 'uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.docx.docx' AND part.filelink = draftpartassignment.part_filelink

		// Iterate over all referenced parts:
		// (The others are already deleted at this point.)
		ResultSet res_parts_referenced = Global.query(str_query);
		Map<String, List<String>> map_part_to_sheetdraftsThatReferenceThisPart = new HashMap<String, List<String>>();
		List<String> referencingSheetdrafts = new ArrayList<String>();
		String part_filelink_previously = "";
		if (res_parts_referenced.next()) {
			int parts_referenced_count = res_parts_referenced.getRow();
			res_parts_referenced.beforeFirst();
			while (res_parts_referenced.next()) {

				//get the filelinks from the result set:
				String part_filelink = res_parts_referenced.getString("part_filelink");
				String sheetdraft_that_references_filelink = res_parts_referenced.getString("sheetdraft_filelink");

				if (!part_filelink_previously.equals(part_filelink)) {
					// If they are not equal we have to react:
					// Store away the list as a copy, then clear it.
					map_part_to_sheetdraftsThatReferenceThisPart.put(
							part_filelink_previously, new ArrayList<String>(referencingSheetdrafts)
							);
					// Now that we have copied the values over
					// clear the helper list:
					referencingSheetdrafts = new ArrayList<String>();
				}

				// Fill in the current entry
				referencingSheetdrafts.add(sheetdraft_that_references_filelink);

				// Always store the previous part filelink:
				part_filelink_previously = part_filelink;

			}
			// not to forget to handle the last entry:
			res_parts_referenced.last();
			String lastEntry_part_filelink = res_parts_referenced.getString("part_filelink");
			if (!part_filelink_previously.equals(lastEntry_part_filelink)) {
				// => only 1 element in the referencingSheetdrafts list
				map_part_to_sheetdraftsThatReferenceThisPart.put(
						lastEntry_part_filelink, new ArrayList<String>(referencingSheetdrafts)
				);
			}
			else {
				// As this and previous part filelink are equal, it
				// has been added to the list already. Remains only
				// to put this now complete list into the map:
				map_part_to_sheetdraftsThatReferenceThisPart.put(
						part_filelink_previously, new ArrayList<String>(referencingSheetdrafts)
				);
			}
		}
		// Tackle memory leaks by closing result set and its statement:
		Global.queryTidyUp(res_parts_referenced);
		// React on that: select the best fitting, new sheetdraft owners
		// i. e. for the parts of one and the same deleted
		// sheetdraft the new owner should be the owner of as many of
		// those referenced parts as possible. TODO


		// This is the short way, that interestingly was what I had
		// created first:
		str_query = "UPDATE part, draftpartassignment"
				+ " SET part.sheetdraft_filelink = draftpartassignment.sheetdraft_filelink"
				+ " WHERE part.sheetdraft_filelink = '" + filelink + "'"
				+ " AND part.filelink = draftpartassignment.part_filelink";
		/* My debug sequence: (J. R.I.B. Wein)

		// Insert reference to part 3: (If more than 1 reference the
		// same part,e.g.3, this is no problem as it does not matter
		// which part becomes the new owner of this part!)
		INSERT INTO draftpartassignment VALUES('uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.odt.odt', 0, 'uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.docx.docx__Part_3__splitby_INTDOT.docx.txt');

		// Insert reference to part 2: (a tex sheetdraft references)
		INSERT INTO draftpartassignment VALUES('uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.tex.tex', 0, 'uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.docx.docx__Part_2__splitby_INTDOT.docx.txt');

		// Reset parts:
		UPDATE part SET part.sheetdraft_filelink = 'uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.docx.docx';

		// The test (whether each from different draft (tex vs. odt)
		// referenced part gets the correct new sheetdraft_filelink
		// value):
		UPDATE part, draftpartassignment SET part.sheetdraft_filelink = draftpartassignment.sheetdraft_filelink WHERE part.sheetdraft_filelink = 'uploads/WS_2013/Mateh/Gauss/Uebung/Blatt7.docx.docx' AND part.filelink = draftpartassignment.part_filelink;

		=> It works in this test! The correct sheetdraft_filelink is
		written to the referenced part. Success.
		*/
		// Now that it's safe to use this query, let's use it:
		Global.query(str_query);








		/*
		AT THIS POINT THE PARTS THAT ARE NO LONGER REFERENCED
		ARE DELETED.
		THE SHEETDRAFT TO WHICH THE STILL REFERENCED PARTS
		BELONG TO HAVE BEEN REASSIGNED.
		=> TODO move those in the filesystem too.
		*/
		// 1) Extract new sheetdraft filelinks: (already happend above
		// in the failed complicated attempt)
		// 2) Iterate them,
		// 3) get the newly assigned sheetdraft_filelink and
		// 4) rename all part flavours of this referenced part!
		for (String part_filelink : map_part_to_sheetdraftsThatReferenceThisPart.keySet()) {

			// 3) Fetch the newly assigned sheetdraft filelink:
			str_query = "SELECT sheetdraft_filelink, filelink"
					+ " FROM part"
					+ " WHERE filelink = '" + part_filelink + "'"
					;
			ResultSet res_new_sheetdraft_filelink = Global.query(str_query);
			// Make this (hopefully 1) sheetdraft_filelink available
			String sheetdraft_filelink = null;
			while (res_new_sheetdraft_filelink.next()) {
				sheetdraft_filelink = res_new_sheetdraft_filelink.getString("sheetdraft_filelink");
			}
			// Tackle memory leaks by closing result set, its statement:
			Global.queryTidyUp(res_new_sheetdraft_filelink);


			// 4) Rename all part flavours of this referenced
			// part in the filesystem:
			if (sheetdraft_filelink != null) {
				String sheetdraft_directory = new File(Global.root + sheetdraft_filelink).getParentFile().getAbsolutePath();
				String destination = sheetdraft_directory + System.getProperty("file.separator")
						+ Global.extractFilename(part_filelink) + "." + Global.extractEnding(part_filelink);
				Global.renameFile(Global.root + part_filelink, destination);

				// All derivatives and flavours and its images:
				Global.renameAllDerivativesOfFilelink(part_filelink, sheetdraft_directory);
			}

		}













		// Check if this was the only sheet in this semester. As
		// semester is the highest level directory in the filesystem,
		// we then can and must delete it completely.
		str_query = "SELECT COUNT(*) AS sheetcountofsheetdraftsemester FROM sheetdraft WHERE semester = '" + semester + "';";
		ResultSet res5 = Global.query(str_query);
		if (res5.next()) {
			if(res5.getString("sheetcountofsheetdraftsemester").equals("0")){
				delete_highest_level_folder = true;
			}
		}
		// Tackle memory leaks by closing result set and its statement:
		Global.queryTidyUp(res5);

		// Delete lecturer if this lecturer has no more sheetdrafts:
		str_query = "SELECT COUNT(*) AS row_count FROM sheetdraft"
			+ " WHERE lecturer_id=" + lecturer_id + ";";
		ResultSet res7 = Global.query(str_query);
		if (res7.next()) {
			if (res7.getString("row_count").equals("0")) {

				str_query = "DELETE FROM lecturer WHERE id = "
					+ lecturer_id + ";";
				Global.query(str_query);
			}
		}
		// Tackle memory leaks by closing result set and its statement:
		Global.queryTidyUp(res7);



		// ======= FILESYSTEM
		// Delete sheet from the disk:
		File f = new File(Global.root + filelink);
		String filename_and_ending = Global.extractFilename(filelink)
			+ "." + Global.extractEnding(filelink);
		if (Global.deleteFile(f)) {
			System.out.print(
				Global.addMessage("<p>Die Datei: <strong>"
					+ filename_and_ending
					+ "</strong> wurde gel&ouml;scht.</p>"
					, "success")
			);
		}
		else {
			System.out.print(
				Global.addMessage("<p>Die Datei: <strong>"
					+ filename_and_ending
					+ "</strong> existiert nicht.</p>"
					, "nosuccess")
			);
		}

		// Delete folder that contained the parts:
		String part_dir =  ""; // the same directory as sheetdraft's
		if (delete_highest_level_folder) {
			Global.deleteDir(
					f
					/*
					Attention: There are several
					files|sheetdrafts within this directory! Thus:
					*/
					.getParentFile() /* above type */
					.getParentFile() /* above lecturer */
					.getParentFile() /* above course */
					//.getParentFile()
					//<-above semester =>in uploadTarget/ =>no deletion!
			);
			boolean keep_database_in_synch = false;
			if (keep_database_in_synch/*truncate_database_too*/) {
				// => Truncate the whole DB except the lecturers:
				PartDB.clearDatabase();
			}
		}


		// Try to delete all possible file derivations. Double endings
		// ensure that no other sheetdraft in the same directory is
		// deleted accidentally.
		Global.deleteAllDerivativesOfFilelink(filelink);



	}



	/**
	 * Clears the database tables using TRUNCATE for MySQL,
	 * DELETE without a WHERE clause for SQLite.
	 *
	 * @throws IOException
	 */
	public static void clearDatabaseAllButLecturer() throws IOException {
		clearTable("draftpartassignment");
		clearTable("part");
		clearTable("sheetdraft");
	}
	public static void clearDatabase() throws IOException {
		// Truncate the whole database but the lecturers too then.
		clearDatabaseAllButLecturer();
		clearTable("lecturer");
	}



	/**
	 * Clears the table using TRUNCATE for MySQL,
	 * DELETE without a WHERE clause for SQLite.
	 *
	 * @throws IOException
	 */
	public static void clearTable(String table) throws IOException {
		String sql = "TRUNCATE `" + table + "`";
		if (Global.msqh.isConnSQLite()) {
			System.out.println(
					"PartDB: Clear table: Connection is SQLite.");
			sql = "DELETE FROM `" + table + "`";
		}
		Global.query(sql);
	}






	//======= ON THE FLY LOADING METHODS
	/**
	 *
	 */
//	public static Sheetdraft[] getAllDraftsOf(String user) {
//
//
//
//	}








	//======= HELPER
//	public static Sheetdraft getSheetdraftById(String sheetdraft_id) {
//		if (allSheetdrafts.get(sheetdraft_id) == null) {
//			return null;
//		}
//		return allSheetdrafts.get(sheetdraft_id);
//	}
	/**
	 * Get a sheetdraft by giving the filelink.
	 *
	 * @param sheetdraft_filelink The identifier|key of the sheetdraft.
	 * @return The sheetdraft or null.
	 */
	public static Sheetdraft getSheetdraftByFilelink(String sheetdraft_filelink) {
		if (allSheetdrafts.get(sheetdraft_filelink) == null) {
			return null;
		}
		return allSheetdrafts.get(sheetdraft_filelink);
	}



	/**
	 * Get all loaded sheetdrafts. (includes drafts)
	 *
	 * @return All sheetdrafts in a Map of its respective key
	 * to its object reference.
	 */
	public static Map<String, Sheetdraft> getLoadedSheetdrafts() {
		return allSheetdrafts;
	}



	/**
	 * Get all loaded drafts only.
	 *
	 * @return All drafts in a Map of its respective key
	 * to its object reference.
	 */
	public static Map<String, Sheetdraft> getLoadedDraftsOnly() {
		return drafts;
	}



	/**
	 * Get the latest changed sheetdrafts.
	 *
	 * @return The sheetdraft that has been modified most recently.
	 */
	public static Sheetdraft getLatestChangedDraft() {
		return latestChangedDraft;
	}




}
