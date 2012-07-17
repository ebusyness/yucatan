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

		ArrayList<TemplateToken> templateTokens = getTokens(template);
		String generatedText = "";

		System.out.println("== tokens ==");
		for (TemplateToken token : templateTokens) {
			System.out.println("\ntoken");
			System.out.println("token.tokenType :" + token.tokenType);
			System.out.println("token.plainText :" + token.plainText);
			if (token.tokenType == TemplateToken.TOKENTYPE_TEXT) {
				generatedText += token.plainText;
			} else {
				generatedText += "<PH>";
			}
		}
		System.out.println("generated:" + generatedText);

		return generatedText;
	}

	/**
	 * Forward template String to {@link #tokenizeTemplate} and initializes {@link #statusDescriptors}. The main reason for this method is to inspect the tokens within test cases.
	 * 
	 * @param template The template to parse.
	 * @return A list of tokens.
	 */
	public static ArrayList<TemplateToken> getTokens(String template) {
		if (statusDescriptors == null) {
			statusDescriptors = new HashMap<Byte, TemplateTokenStatusItem>();

			// == PLACHOLDER_STATUS_STOPPED ==
			// stopped configuration
			TemplateTokenStatusItem placeholderStopped = new TemplateTokenStatusItem();
			placeholderStopped.nextExpectedChar = '$';
			placeholderStopped.nextExpectsExplicitChar = true;
			placeholderStopped.successStatus = PLACHOLDER_STATUS_OPENINGCHAR1;
			placeholderStopped.startNewToken = true;
			placeholderStopped.nextStartNewTokenBlocked = true;
			statusDescriptors.put(PLACHOLDER_STATUS_STOPPED, placeholderStopped);

			// == PLACHOLDER_STATUS_OPENINGCHAR1 ==
			// started / first opening char $
			TemplateTokenStatusItem placeholderChar1 = new TemplateTokenStatusItem();
			placeholderChar1.nextExpectedChar = '{';
			placeholderChar1.nextExpectsExplicitChar = true;
			placeholderChar1.successStatus = PLACHOLDER_STATUS_OPENINGCHAR2;
			placeholderChar1.failStatus = PLACHOLDER_STATUS_STOPPED;
			placeholderChar1.createToken = true;
			placeholderChar1.createTokenType = TemplateToken.TOKENTYPE_TEXT;
			placeholderChar1.startNewFromPrevious = true;
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
			actionStarted.nextStartNewTokenBlocked = true;
			statusDescriptors.put(PLACHOLDER_STATUS_ACTION_STARTED, actionStarted);

			// == PLACHOLDER_STATUS_ACTION_STOPPED ==
			// stopped action sequence chars "${@" + any chars + terminator "("
			TemplateTokenStatusItem actionStopped = new TemplateTokenStatusItem();
			actionStopped.nextStatus = new byte[1];
			actionStopped.nextStatus[0] = PLACHOLDER_STATUS_MEMBERQUERY_STARTED;
			actionStopped.createToken = true;
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
			memberQueryStopped.createToken = true;
			memberQueryStopped.createTokenType = TemplateToken.TOKENTYPE_MEMBERQUERY;
			statusDescriptors.put(PLACHOLDER_STATUS_MEMBERQUERY_STOPPED, memberQueryStopped);

			// == PLACHOLDER_STATUS_TERMINATED ==
			// the placeholder terminator was found '}'
			TemplateTokenStatusItem placholderTerminator = new TemplateTokenStatusItem();
			placholderTerminator.nextExpectedChar = '$';
			placholderTerminator.nextExpectsExplicitChar = true;
			placholderTerminator.successStatus = PLACHOLDER_STATUS_OPENINGCHAR1;
			placholderTerminator.failStatus = PLACHOLDER_STATUS_STOPPED;
			placholderTerminator.dropToken = true; // drop/skip the '}' token
			placholderTerminator.nextStartNewTokenBlocked = true;
			statusDescriptors.put(PLACHOLDER_STATUS_TERMINATED, placholderTerminator);

			// == PLACHOLDER_STATUS_TERMINATED_AFTER_ACTIONNAME ==
			// the placeholder terminator was found '}' after the action name
			// * creates an action name token
			TemplateTokenStatusItem placholderTerminatorAfterAction = new TemplateTokenStatusItem();
			placholderTerminatorAfterAction.nextExpectedChar = '$';
			placholderTerminatorAfterAction.nextExpectsExplicitChar = true;
			placholderTerminatorAfterAction.successStatus = PLACHOLDER_STATUS_OPENINGCHAR1;
			placholderTerminatorAfterAction.failStatus = PLACHOLDER_STATUS_STOPPED;
			placholderTerminatorAfterAction.createToken = true;
			placholderTerminatorAfterAction.createTokenType = TemplateToken.TOKENTYPE_ACTIONNAME;
			placholderTerminatorAfterAction.nextStartNewTokenBlocked = true;
			statusDescriptors.put(PLACHOLDER_STATUS_TERMINATED_AFTER_ACTIONNAME, placholderTerminatorAfterAction);

		}
		return tokenizeTemplate(template);
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
			if (currentStatus.createToken) {
				if (startPositionNewToken < i - 1) { // skip emtpy tokens
					TemplateToken templateToken = new TemplateToken();
					// do not add the current char (in most cases it is the identifier for the next sequence)
					templateToken.plainText = template.substring(startPositionNewToken, i - 1);
					templateToken.tokenType = currentStatus.createTokenType;
					templateTokens.add(templateToken);
				}
				if (currentStatus.startNewFromPrevious) {
					startPositionCurrentToken = Math.max(0, i - 1);
					startPositionNewToken = Math.max(0, i - 1);
				} else {
					startPositionCurrentToken = i;
					startPositionNewToken = i;
				}
			}

			// stop token here -> reset position counters
			if (currentStatus.dropToken) {
				startPositionCurrentToken = i;
				startPositionNewToken = i;
			}

			// set a new start position for the next token (only if allowed in previous status)
			if (currentStatus.startNewToken && !lastStatus.nextStartNewTokenBlocked) {
				startPositionNewToken = i;
			}

			// set a new start position for the next token to previous character (only if allowed in previous status)
			if (currentStatus.startNewFromPrevious && !lastStatus.nextStartNewTokenBlocked) {
				startPositionNewToken = Math.max(0, i - 1);
			}

			// store last status
			lastStatus = currentStatus;

			// for debugging purposes
			// System.out.println(c + " " + TemplateTokenStatusItem.inspectProperties(currentStatus) + "  i=" + i + "  cs=" + startPositionCurrentToken + "  ens="
			// + startPositionNewToken);

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
					if (currentStatus.nextStatus.length >= terminatorPosition) {
						currentStatus = statusDescriptors.get(currentStatus.nextStatus[terminatorPosition]);
					} else if (currentStatus.nextStatus.length > 0) {
						currentStatus = statusDescriptors.get(currentStatus.nextStatus[0]);
					}
				}
			} else if (currentStatus.nextStatus != null && currentStatus.nextStatus.length > 0) {
				currentStatus = statusDescriptors.get(currentStatus.nextStatus[0]);
			}
		}
		// create the current token
		if (currentStatus.createToken) {
			TemplateToken templateToken = new TemplateToken();
			templateToken.plainText = template.substring(startPositionNewToken, template.length() - 1); // TODO recheck i-1 - add special testcase
			templateToken.tokenType = currentStatus.createTokenType;
			templateTokens.add(templateToken);

		} else {
			// append everything thats left
			TemplateToken templateToken = new TemplateToken();
			templateToken.plainText = template.substring(startPositionNewToken, template.length());
			templateToken.tokenType = TemplateToken.TOKENTYPE_TEXT;
			templateTokens.add(templateToken);
		}
		return templateTokens;
	}

}