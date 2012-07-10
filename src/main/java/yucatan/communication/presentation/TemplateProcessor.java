package yucatan.communication.presentation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the yucatan template processor.
 * <p>
 * currently implemented format of template placeholders: ${@<action-name>(<member-query>)}
 * </p>
 * <h3>Examples</h3>
 * <ul>
 * <li>${@data(myProperty)}</li>
 * <li>${@data(myObject.myMethod)}</li>
 * <li>${@list(myObject.myProperty)}</li>
 * </ul>
 */
public final class TemplateProcessor {

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
	 * This status indicates that we are at the terminator of placeholder sequence after an action name eg. ${@count}
	 */
	private static final Byte PLACHOLDER_STATUS_TERMINATED_AFTER_ACTIONNAME = 51;
	/**
	 * This status indicates that we are at the terminator of placeholder sequence after an action name eg. ${@count}
	 */
	private static final Byte PLACHOLDER_STATUS_MEMBERQUERY_STOPPED = 52;
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
			placeholderStopped.nextExpectedChar = '$';
			placeholderStopped.nextExpectsExplicitChar = true;
			placeholderStopped.successStatus = PLACHOLDER_STATUS_OPENINGCHAR1;
			placeholderStopped.startNewToken = true;
			statusDescriptors.put(PLACHOLDER_STATUS_STOPPED, placeholderStopped);

			// == PLACHOLDER_STATUS_OPENINGCHAR1 ==
			// started / first opening char $
			TemplateTokenStatusItem placeholderChar1 = new TemplateTokenStatusItem();
			placeholderChar1.nextExpectedChar = '{';
			placeholderChar1.nextExpectsExplicitChar = true;
			placeholderChar1.successStatus = PLACHOLDER_STATUS_OPENINGCHAR2;
			placeholderChar1.failStatus = PLACHOLDER_STATUS_STOPPED;
			placeholderChar1.stopAndCreateToken = true;
			placeholderChar1.createTokenType = TemplateToken.TOKENTYPE_TEXT;
			statusDescriptors.put(PLACHOLDER_STATUS_OPENINGCHAR1, placeholderChar1);

			// == PLACHOLDER_STATUS_OPENINGCHAR2 ==
			// started / first + second opening char ${
			TemplateTokenStatusItem placeholderChar2 = new TemplateTokenStatusItem();
			placeholderChar2.nextExpectedChar = '@';
			placeholderChar2.nextExpectsExplicitChar = true;
			placeholderChar2.successStatus = PLACHOLDER_STATUS_ACTION_STARTED;
			placeholderChar2.failStatus = PLACHOLDER_STATUS_STOPPED;
			statusDescriptors.put(PLACHOLDER_STATUS_OPENINGCHAR2, placeholderChar2);

			// == PLACHOLDER_STATUS_ACTION_STARTED ==
			// started action sequence chars ${@
			// * wait for '(' or '}'
			// * start new token
			TemplateTokenStatusItem actionStarted = new TemplateTokenStatusItem();
			actionStarted.nextStatus = new byte[2];
			actionStarted.nextStatus[0] = PLACHOLDER_STATUS_ACTION_STOPPED;
			actionStarted.nextStatus[1] = PLACHOLDER_STATUS_TERMINATED_AFTER_ACTIONNAME;
			actionStarted.terminatedBy = new char[2];
			actionStarted.terminatedBy[0] = '(';
			actionStarted.terminatedBy[1] = '}';
			actionStarted.startNewToken = true;
			statusDescriptors.put(PLACHOLDER_STATUS_ACTION_STARTED, actionStarted);

			// == PLACHOLDER_STATUS_ACTION_STOPPED ==
			// stopped action sequence chars "${@" + any chars + terminator "("
			TemplateTokenStatusItem actionStopped = new TemplateTokenStatusItem();
			actionStopped.nextStatus = new byte[1];
			actionStopped.nextStatus[0] = PLACHOLDER_STATUS_MEMBERQUERY_STARTED;
			actionStopped.stopAndCreateToken = true;
			actionStopped.createTokenType = TemplateToken.TOKENTYPE_ACTIONNAME;
			statusDescriptors.put(PLACHOLDER_STATUS_ACTION_STOPPED, actionStopped);

			// == PLACHOLDER_STATUS_MEMBERQUERY_STARTED ==
			// member query sequence
			TemplateTokenStatusItem memberAccessQuery = new TemplateTokenStatusItem();
			memberAccessQuery.nextStatus = new byte[1];
			memberAccessQuery.nextStatus[0] = PLACHOLDER_STATUS_MEMBERQUERY_STOPPED;
			memberAccessQuery.terminatedBy = new char[1];
			memberAccessQuery.terminatedBy[0] = ')';
			statusDescriptors.put(PLACHOLDER_STATUS_MEMBERQUERY_STARTED, memberAccessQuery);

			// == PLACHOLDER_STATUS_MEMBERQUERY_STOPPED ==
			// the memberquery terminator was found ')'
			// * creates an memeber query token
			TemplateTokenStatusItem memberQueryStopped = new TemplateTokenStatusItem();
			memberQueryStopped.nextStatus = new byte[1];
			memberQueryStopped.nextStatus[0] = PLACHOLDER_STATUS_TERMINATED;
			memberQueryStopped.stopAndCreateToken = true;
			memberQueryStopped.createTokenType = TemplateToken.TOKENTYPE_MEMBERQUERY;
			statusDescriptors.put(PLACHOLDER_STATUS_MEMBERQUERY_STOPPED, memberQueryStopped);

			// == PLACHOLDER_STATUS_TERMINATED ==
			// the placeholder terminator was found '}'
			TemplateTokenStatusItem placholderTerminator = new TemplateTokenStatusItem();
			placholderTerminator.nextStatus = new byte[1];
			placholderTerminator.nextStatus[0] = PLACHOLDER_STATUS_STOPPED;
			placholderTerminator.stopAndDropToken = true; // drop/skip the '}' token
			statusDescriptors.put(PLACHOLDER_STATUS_TERMINATED, placholderTerminator);

			// == PLACHOLDER_STATUS_TERMINATED_AFTER_ACTIONNAME ==
			// the placeholder terminator was found '}' after the action name
			// * creates an action name token
			TemplateTokenStatusItem placholderTerminatorAfterAction = new TemplateTokenStatusItem();
			placholderTerminatorAfterAction.nextStatus = new byte[1];
			placholderTerminatorAfterAction.nextStatus[0] = PLACHOLDER_STATUS_STOPPED;
			placholderTerminatorAfterAction.stopAndCreateToken = true;
			placholderTerminatorAfterAction.createTokenType = TemplateToken.TOKENTYPE_ACTIONNAME;
			statusDescriptors.put(PLACHOLDER_STATUS_TERMINATED_AFTER_ACTIONNAME, placholderTerminatorAfterAction);



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

			// set a new start position for the next token (only after status change "event")
			if (lastStatus != currentStatus && currentStatus.startNewToken) {
				startPositionNewToken = i;
			}

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
				int terminatorPosition = 0;
				for (int j = 0; j < currentStatus.terminatedBy.length; j++) {
					if (c == currentStatus.terminatedBy[j]) {
						terminatorFound = true;
						terminatorPosition = j;
						break;
					}
				}
				if (terminatorFound && currentStatus.nextStatus != null) {
					if( currentStatus.nextStatus.length >= terminatorPosition) {
						currentStatus = statusDescriptors.get(currentStatus.nextStatus[terminatorPosition]);
					} else if (currentStatus.nextStatus.length > 0 ) {
						currentStatus = statusDescriptors.get(currentStatus.nextStatus[0]);
					}
				}
			} else if (currentStatus.nextStatus != null && currentStatus.nextStatus.length > 0){
				currentStatus = statusDescriptors.get(currentStatus.nextStatus[0]);
			}
		}
		// append everything thats left
		TemplateToken templateToken = new TemplateToken();
		templateToken.plainText = template.substring(startPositionNewToken, template.length());
		templateToken.tokenType = TemplateToken.TOKENTYPE_TEXT;
		templateTokens.add(templateToken);

		return templateTokens;
	}

}