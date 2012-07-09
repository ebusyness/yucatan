package yucatan.communication.presentation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the yucatan template processor.
 * <p>
 * format of template placeholders: ${:action:memberQuery(memberAccessArguments)[formatter1,formatter2(arg1,arg2,...)]}
 * 
 * examples ${:data:myObject.myProperty[html;slice(1,2)]} ${:data:myObject.myMethod(1,"text")[html;slice(1,2)]}
 * 
 * ${:list:myObject.myProperty}
 * </p>
 */
public final class TemplateProcessor {

	/**
	 * Dollar character "$"
	 */
	private static final char PLACEHOLDER_SYMBOL_DOLLAR = '$';

	/**
	 * Curly open bracket "{"
	 */
	private static final char PLACEHOLDER_SYMBOL_CURLY_OPEN = '{';

	/**
	 * Curly close bracket "}"
	 */
	private static final char PLACEHOLDER_SYMBOL_CURLY_CLOSE = '}';

	/**
	 * Colon ":"
	 */
	private static final char PLACEHOLDER_SYMBOL_COLON = ':';

	/**
	 * Indicates that the status is not subject to change.
	 */
	public static final Byte PLACHOLDER_NO_STATUS_CHANGE = -1;

	/**
	 * This status indicates that we are not within a placeholder sequence.
	 */
	private static final Byte PLACHOLDER_STATUS_STOPPED = 0;

	/**
	 * This status indicates that we meight be within a placeholder sequence. First charachter '$' of a placeholder sequence found.
	 */
	private static final Byte PLACHOLDER_STATUS_OPENINGCHAR1 = 1;

	/**
	 * This status indicates that we meight be within a placeholder sequence. First & second charachter ('$' & '{') of a placeholder sequence were found.
	 */
	private static final Byte PLACHOLDER_STATUS_OPENINGCHAR2 = 2;

	/**
	 * This status indicates that the action declaration was started. Folowing sequence found ${:
	 */
	private static final Byte PLACHOLDER_STATUS_ACTION_STARTED = 3;

	/**
	 * This status indicates that the action declaration was stopped. Folowing sequence found "${:" + some chars + terminator ":"
	 */
	private static final Byte PLACHOLDER_STATUS_ACTION_STOPPED = 4;

	/**
	 * This status indicates that the action declaration was started. Folowing sequence found ${:
	 */
	private static final Byte PLACHOLDER_STATUS_MEMBERQUERY_STARTED = 5;

	/**
	 * This status indicates that we are at the terminator of placeholder sequence.
	 */
	private static final Byte PLACHOLDER_STATUS_TERMINATED = 50;

	/**
	 * This hashmap contains the different status items
	 */
	private static HashMap<Byte, TemplateTokenStatusItem> statusDescriptors;

	/**
	 * Template render method. This method may be called recursively.
	 * 
	 * @param dataScope The current datascope to work on. (needs a get(String key) method!)
	 * @param template The template to fill.
	 * @return The rendered String
	 */
	public static String doRender(Object dataScope, String template) {
		if (dataScope == null || template == null) {
			return null;
		}
		if (statusDescriptors == null) {
			statusDescriptors = new HashMap<Byte, TemplateTokenStatusItem>();

			// == PLACHOLDER_STATUS_STOPPED ==
			// stopped configuration
			TemplateTokenStatusItem placeholderStopped = new TemplateTokenStatusItem();
			placeholderStopped.nextExpectedChar = PLACEHOLDER_SYMBOL_DOLLAR;
			placeholderStopped.nextExpectsExplicitChar = true;
			placeholderStopped.successStatus = PLACHOLDER_STATUS_OPENINGCHAR1;
			placeholderStopped.startNewToken = true;
			statusDescriptors.put(PLACHOLDER_STATUS_STOPPED, placeholderStopped);

			// == PLACHOLDER_STATUS_OPENINGCHAR1 ==
			// started / first opening char $
			TemplateTokenStatusItem placeholderChar1 = new TemplateTokenStatusItem();
			placeholderChar1.nextExpectedChar = PLACEHOLDER_SYMBOL_CURLY_OPEN;
			placeholderChar1.nextExpectsExplicitChar = true;
			placeholderChar1.successStatus = PLACHOLDER_STATUS_OPENINGCHAR2;
			placeholderChar1.failStatus = PLACHOLDER_STATUS_STOPPED;
			placeholderChar1.stopAndCreateToken = true;
			placeholderChar1.createTokenType = TemplateToken.TOKENTYPE_TEXT;
			statusDescriptors.put(PLACHOLDER_STATUS_OPENINGCHAR1, placeholderChar1);

			// == PLACHOLDER_STATUS_OPENINGCHAR2 ==
			// started / first + second opening char ${
			TemplateTokenStatusItem placeholderChar2 = new TemplateTokenStatusItem();
			placeholderChar2.nextExpectedChar = PLACEHOLDER_SYMBOL_COLON;
			placeholderChar2.nextExpectsExplicitChar = true;
			placeholderChar2.successStatus = PLACHOLDER_STATUS_ACTION_STARTED;
			placeholderChar2.failStatus = PLACHOLDER_STATUS_STOPPED;
			statusDescriptors.put(PLACHOLDER_STATUS_OPENINGCHAR2, placeholderChar2);

			// == PLACHOLDER_STATUS_ACTION_STARTED ==
			// started action sequence chars ${:
			TemplateTokenStatusItem actionStarted = new TemplateTokenStatusItem();
			actionStarted.nextStatus = PLACHOLDER_STATUS_ACTION_STOPPED;
			actionStarted.terminatedBy = new char[1];
			actionStarted.terminatedBy[0] = PLACEHOLDER_SYMBOL_COLON;
			actionStarted.startNewToken = true;
			statusDescriptors.put(PLACHOLDER_STATUS_ACTION_STARTED, actionStarted);

			// == PLACHOLDER_STATUS_ACTION_STOPPED ==
			// stopped action sequence chars "${:" + any chars + terminator ":"
			TemplateTokenStatusItem actionStopped = new TemplateTokenStatusItem();
			actionStopped.nextExpectedChar = PLACEHOLDER_SYMBOL_CURLY_CLOSE; // early exit ...
			actionStopped.nextExpectsExplicitChar = true;
			actionStopped.successStatus = PLACHOLDER_STATUS_TERMINATED;
			actionStopped.failStatus = PLACHOLDER_STATUS_MEMBERQUERY_STARTED; // ... or register member
			actionStopped.stopAndCreateToken = true;
			actionStopped.createTokenType = TemplateToken.TOKENTYPE_ACTIONNAME;
			statusDescriptors.put(PLACHOLDER_STATUS_ACTION_STOPPED, actionStopped);

			// == PLACHOLDER_STATUS_MEMBERQUERY_STARTED ==
			// memeber query sequence
			TemplateTokenStatusItem memberAccessQuery = new TemplateTokenStatusItem();
			//memberAccessQuery.nextExpectedChar = PLACEHOLDER_SYMBOL_CURLY_CLOSE;
			//memberAccessQuery.nextExpectsExplicitChar = true;
			memberAccessQuery.nextStatus = PLACHOLDER_STATUS_TERMINATED;
			memberAccessQuery.terminatedBy = new char[1];
			memberAccessQuery.terminatedBy[0] = PLACEHOLDER_SYMBOL_CURLY_CLOSE;
			statusDescriptors.put(PLACHOLDER_STATUS_MEMBERQUERY_STARTED, memberAccessQuery);

			// == PLACHOLDER_STATUS_TERMINATED ==
			// the placeholder terminator was found '}'
			TemplateTokenStatusItem placholderTerminator = new TemplateTokenStatusItem();
			placholderTerminator.nextStatus = PLACHOLDER_STATUS_STOPPED;
			placholderTerminator.stopAndCreateToken = true;
			placholderTerminator.createTokenType = TemplateToken.TOKENTYPE_MEMBERQUERY;
			// placholderTerminator.stopAndDropToken = true; // the curly close brace token will be dropped
			statusDescriptors.put(PLACHOLDER_STATUS_TERMINATED, placholderTerminator);

		}

		ArrayList<TemplateToken> templateTokens = tokenizeTemplate(template);
		System.out.println("== tokens ==");
		for (TemplateToken token : templateTokens) {
			System.out.println("\ntoken");
			System.out.println("token.tokenType :" + token.tokenType);
			System.out.println("token.plainText :" + token.plainText);
		}

		return "";
	}

	/**
	 * Parse and tokenize the passed template.
	 * 
	 * @param template The template to parse.
	 * @return A list of tokens.
	 */
	private static ArrayList<TemplateToken> tokenizeTemplate(String template) {
		if (template == null) {
			return null;
		}
		ArrayList<TemplateToken> templateTokens = new ArrayList<TemplateToken>();
		TemplateTokenStatusItem lastStatus = statusDescriptors.get(PLACHOLDER_STATUS_STOPPED);
		TemplateTokenStatusItem currentStatus = statusDescriptors.get(PLACHOLDER_STATUS_STOPPED);
		int startPositionCurrentToken = 0; // start position of the current token (used as fall back to text type)
		int startPositionNewToken = 0; // start position of new intended token (used for intended specialized types)

		for (int i = 0, n = template.length(); i < n; i++) {
			char c = template.charAt(i);

			// stop here and create the current token
			if (currentStatus.stopAndCreateToken) {
				TemplateToken templateToken = new TemplateToken();
				templateToken.plainText = template.substring(startPositionNewToken, i - 1); // TODO recheck i-1 - add special testcase
				templateToken.tokenType = currentStatus.createTokenType;
				templateTokens.add(templateToken);
				startPositionCurrentToken = i;
				startPositionNewToken = i;
			}

			// stop token here -> reset position counters
			if (currentStatus.stopAndDropToken) {
				startPositionCurrentToken = i;
				startPositionNewToken = i;
			}

			// System.out.println(c + "  - status change:" + (lastStatus != currentStatus) + " - start new:" + currentStatus.startNewToken);
			// set a new start position for the next token (only after status change "event")
			if (lastStatus != currentStatus && currentStatus.startNewToken) {
				startPositionNewToken = i;
			}

			// store last status
			lastStatus = currentStatus;

			// check next expected char
			if (currentStatus.nextExpectsExplicitChar && currentStatus.nextExpectedChar == c) {
				// switch status to the specified success status
				if (currentStatus.successStatus != PLACHOLDER_NO_STATUS_CHANGE) {
					currentStatus = statusDescriptors.get(currentStatus.successStatus);
				}
				continue;
			}

			// fail check for next expected char
			if (currentStatus.nextExpectsExplicitChar && currentStatus.nextExpectedChar != c) {
				if (currentStatus.failStatus != PLACHOLDER_NO_STATUS_CHANGE) {
					currentStatus = statusDescriptors.get(currentStatus.failStatus);
				}
				// Patterns didn't match so we have to switch to the old (fall back) position
				// so that the complete text remains in the next token.
				startPositionNewToken = startPositionCurrentToken;
				continue;
			}

			// terminator handling - if one or more terminators are defined the next status can only
			// be reached if the terminator occurs.
			if (currentStatus.terminatedBy != null && currentStatus.terminatedBy.length > 0) {
				boolean terminatorFound = false;
				for (int j = 0; j < currentStatus.terminatedBy.length; j++) {
					if (c == currentStatus.terminatedBy[j]) {
						terminatorFound = true;
						break;
					}
				}
				if (terminatorFound && currentStatus.nextStatus != PLACHOLDER_NO_STATUS_CHANGE) {
					currentStatus = statusDescriptors.get(currentStatus.nextStatus);
				}
			} else {
				currentStatus = statusDescriptors.get(currentStatus.nextStatus);
			}
		}
		TemplateToken templateToken = new TemplateToken();
		templateToken.plainText = template.substring(startPositionNewToken, template.length());
		templateToken.tokenType = TemplateToken.TOKENTYPE_TEXT;
		templateTokens.add(templateToken);

		return templateTokens;
	}

}