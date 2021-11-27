#include <cctype>
#include "Lexer.h"
#include "Token.h"

Lexer::Lexer() {
	currentLine = 1;
}

Lexer::Lexer(const char* inputFile) {
	inFS.open(inputFile);
	currentLine = 1;
}

void Lexer::ScanTokens() {
	while (!inFS.eof()) {
		string charSequence = "";

		char currentSymbol = inFS.get();
		switch (currentSymbol) {
		case ',':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(COMMA, charSequence, currentLine));
			break;
		case '.':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(PERIOD, charSequence, currentLine));
			break;
		case '?':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(Q_MARK, charSequence, currentLine));
			break;
		case '(':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(LEFT_PAREN, charSequence, currentLine));
			break;
		case ')':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(RIGHT_PAREN, charSequence, currentLine));
			break;
		case ':':
			if (inFS.peek() == '-') {
				charSequence += currentSymbol;
				charSequence += inFS.get();
				listOfTokens.push_back(Token(COLON_DASH, charSequence, currentLine));
			}
			else {
				charSequence += currentSymbol;
				listOfTokens.push_back(Token(COLON, charSequence, currentLine));
			}
			break;
		case '*':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(MULTIPLY, charSequence, currentLine));
			break;
		case '+':
			charSequence += currentSymbol;
			listOfTokens.push_back(Token(ADD, charSequence, currentLine));
			break;
		case '#':
			if (inFS.peek() == '|') {
				bool isTerminated = false;
				int tempLine = currentLine;
				charSequence += currentSymbol;
				charSequence += inFS.get();
				while (!isTerminated) {
					while (inFS.peek() != '|') {
						if (inFS.peek() == EOF) {
							listOfTokens.push_back(Token(UNDEFINED, charSequence, tempLine));
							break;
						}
						if (inFS.peek() == '\n') {
							charSequence += inFS.get();
							++currentLine;
						}
						else {
							charSequence += inFS.get();
						}
					}
					if (inFS.peek() == EOF) {
						break;
					}
					charSequence += inFS.get();
					if (inFS.peek() == '#') {
						charSequence += inFS.get();
						//listOfTokens.push_back(Token(COMMENT, charSequence, tempLine));
						isTerminated = true;
					}
					else if (inFS.peek() == '\n') {
						charSequence += inFS.get();
						++currentLine;
					}
					else {
						charSequence += inFS.get();
					}
				}
			}

			else {
				charSequence += currentSymbol;
				while (inFS.peek() != '\n' && inFS.peek() != EOF) {
					charSequence += inFS.get();
				}
				//listOfTokens.push_back(Token(COMMENT, charSequence, currentLine));
			}
			break;
		case '\'': {
			bool isTerminated = false;
			int tempLine = currentLine;
			charSequence += currentSymbol;
			while (!isTerminated) {
				while (inFS.peek() != '\'') {
					if (inFS.peek() == EOF) {
						listOfTokens.push_back(Token(UNDEFINED, charSequence, tempLine));
						break;
					}
					if (inFS.peek() == '\n') {
						charSequence += inFS.get();
						++currentLine;
					}
					else {
						charSequence += inFS.get();
					}
				}
				if (inFS.peek() == EOF) {
					break;
				}
				charSequence += inFS.get();
				if (inFS.peek() == '\'') {
					charSequence += inFS.get();
				}
				else {
					listOfTokens.push_back(Token(STRING, charSequence, tempLine));
					isTerminated = true;
				}
			}
			break;
		}
		case '\n':
			++currentLine;
			break;
		case EOF:
			break;
		default:
			if (isalpha(currentSymbol)) {
				charSequence += currentSymbol;
				while (isalnum(inFS.peek()) && inFS.peek() != EOF) {
					charSequence += inFS.get();
				}
				if (charSequence == "Schemes") {
					listOfTokens.push_back(Token(SCHEMES, charSequence, currentLine));
				}
				else if (charSequence == "Facts") {
					listOfTokens.push_back(Token(FACTS, charSequence, currentLine));
				}
				else if (charSequence == "Rules") {
					listOfTokens.push_back(Token(RULES, charSequence, currentLine));
				}
				else if (charSequence == "Queries") {
					listOfTokens.push_back(Token(QUERIES, charSequence, currentLine));
				}
				else {
					listOfTokens.push_back(Token(ID, charSequence, currentLine));
				}
			}
			else if (isspace(currentSymbol)) {
				break;
			}
			else {
				charSequence += currentSymbol;
				listOfTokens.push_back(Token(UNDEFINED, charSequence, currentLine));
			}
			break;
		}
	}
	listOfTokens.push_back(Token(ENDF, "", currentLine));
	inFS.close();
}

string Lexer::PrintAllTokens() {
	ostringstream oss;

	for (size_t i = 0; i < listOfTokens.size(); ++i) {
		oss << listOfTokens.at(i).ToStr() << endl;
	}

	oss << "Total Tokens = " << listOfTokens.size() << endl;
	return oss.str();
}

vector<Token> Lexer::GetTokenList() {
	return listOfTokens;
}
