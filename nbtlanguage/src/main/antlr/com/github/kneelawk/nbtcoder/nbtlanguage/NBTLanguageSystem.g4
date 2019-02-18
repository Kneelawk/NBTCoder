grammar NBTLanguageSystem;

@header {
	package com.github.kneelawk.nbtcoder.nbtlanguage;
}

nbtFile
:
	STRING COLON tagBody
	| tagBody
;

tagBody
:
	tagString
	| tagList
	| tagTypedArray
	| tagCompound
;

tagString
:
	STRING
;

tagList
:
	LIST_START
	(
		(
			listItem COMMA
		)* listItem
	)? LIST_END
;

listItem
:
	STRING COLON tagBody
	| tagBody
;

tagTypedArray
:
	LIST_START STRING SEMICOLON
	(
		(
			typedArrayItem COMMA
		)* typedArrayItem
	)? LIST_END
;

typedArrayItem
:
	STRING
;

tagCompound
:
	BRACE_OPEN
	(
		(
			compoundItem COMMA
		)* compoundItem
	)? BRACE_CLOSE
;

compoundItem
:
	STRING COLON tagBody
;

COLON
:
	':'
;

SEMICOLON
:
	';'
;

COMMA
:
	','
;

BRACE_OPEN
:
	'{'
;

BRACE_CLOSE
:
	'}'
;

LIST_START
:
	'['
;

LIST_END
:
	']'
;

STRING
:
	~[:;,{}[\] \t\r\n]+
	| '"'
	(
		'\\"'
		| ~'"'
	)+ '"'
;

WS
:
	[ \t\r\n]+ -> skip
;
