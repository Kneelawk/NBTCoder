grammar NBTCoder;

@header {
	package com.github.kneelawk.nbtcoder;
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
	LIST_START STRING SIMICOLON
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

SIMICOLON
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
	[._\-a-zA-Z0-9]+
	| '"' [ ._\-a-zA-Z0-9]+ '"'
;

WS
:
	[ \t\r\n]+ -> skip
;
