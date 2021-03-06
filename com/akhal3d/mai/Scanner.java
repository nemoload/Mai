package com.akhal3d.mai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();

	private static final Map<String, TokenType> keywords;
	static {
		keywords = new HashMap<>();
		keywords.put("and", TokenType.AND);
		keywords.put("or", TokenType.OR);
		keywords.put("class", TokenType.CLASS);
		keywords.put("else", TokenType.ELSE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("for", TokenType.FOR);
		keywords.put("func", TokenType.FUNC);
		keywords.put("if", TokenType.IF);
		keywords.put("nil", TokenType.NIL);
		keywords.put("print", TokenType.PRINT);
		keywords.put("return", TokenType.RETURN);
		keywords.put("super", TokenType.SUPER);
		keywords.put("this", TokenType.THIS);
		keywords.put("true", TokenType.TRUE);
		keywords.put("while", TokenType.WHILE);
		keywords.put("do", TokenType.DO);
		keywords.put("break", TokenType.BREAK);
		keywords.put("pass", TokenType.PASS);
	}

	/* Offset */

	/* First character in the current lexeme */
	private int start = 0;

	/* The current character we're scanning */
	private int current = 0;
	private int line = 1;

	Scanner(String source) {
		this.source = source;
	}

	List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}
		addToken(TokenType.NEWLINE);
		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		char c = next();
		switch (c) {
		case '(':
			addToken(TokenType.LEFT_PAREN);
			break;
		case ')':
			addToken(TokenType.RIGHT_PAREN);
			break;
		case '{':
			addToken(TokenType.LEFT_BRACE);
			break;
		case '}':
			addToken(TokenType.RIGHT_BRACE);
			break;
		case ',':
			addToken(TokenType.COMMA);
			break;
		case '.':
			addToken(TokenType.DOT);
			break;
		case '-':
			addToken(TokenType.MINUS);
			break;
		case '+':
			addToken(TokenType.PLUS);
			break;
		case ';':
			addToken(TokenType.SEMICOLON);
			break;
		case '*':
			addToken(TokenType.STAR);
			break;

		case '=':
			addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
			break;
		case '!':
			addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
			break;
		case '<':
			addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
			break;
		case '>':
			addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
			break;

		case '/':
			if (match('/')) {
				while (peek() != '\n' && !isAtEnd()) {
					next();
				}
			} else if (match('*')) {
				while( (peek() != '*' && peekNext() != '/') && !isAtEnd() ) {
					next();
				}
				next();
				next();
				
			} else {
				addToken(TokenType.SLASH);
			}
			break;

		case ' ':
		case '\r':
		case '\t':
			break;

		case '\n':
				addToken(TokenType.NEWLINE);
			line++;
			break;
		
		case '\'':
		case '"':
			if (peekNext() == '"' || peekNext() == '\'') {
				System.out.println("CHARACTER!");
			} 
			string();
			break;

		default:
			if (isDigit(c)) {
				number();
			} else if (isAlpha(c)) {
				identifier();
			} else {
				Mai.error(line, "Unexpected character");
			}
		}
	}

	private void identifier() {
		while(isAlphaNumeric(peek())) next();
		
		String text = source.substring(start, current);
		
		TokenType type = keywords.get(text);
		if (type == null) type = TokenType.IDENTIFIER;
		addToken(type);

	}

	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	private void number() {
		while (isDigit(peek())) {
			next();
		}

		if (peek() == '.' && isDigit(peekNext())) {
			next();
			while (isDigit(peek())) {
				next();
			}
		}

		addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
	}

	private char peekNext() {
		if (current + 1 >= source.length())
			return '\0';

		return source.charAt(current + 1);
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void string() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') {
				line++;
			}
			next();
		}

		if (isAtEnd()) {
			Mai.error(line, "Unterminated string");
			return;
		}

		next();

		String value = source.substring(start + 1, current - 1);
		addToken(TokenType.STRING, value);
	}

	private char peek() {
		if (isAtEnd()) {
			return '\0';
		}

		return source.charAt(current);
	}

	private boolean match(char expected) {
		if (isAtEnd())
			return false;
		if (source.charAt(current) != expected)
			return false;
		current++;
		return true;
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	private char next() {
		current++;
		return source.charAt(current - 1);
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}
}
