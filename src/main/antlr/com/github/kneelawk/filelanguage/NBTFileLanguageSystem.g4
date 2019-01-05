grammar NBTFileLanguageSystem;

@header {
	package com.github.kneelawk.filelanguage;
}

nbtFile
:
	FILE_START properties
	(
		partition*
		| data
	) SECTION_END
;

partition
:
	PARTITION_START properties data padding? SECTION_END
;

properties
:
	PROPERTIES_START PROPERTIES_DATA SECTION_END
;

data
:
	DATA_START NBT_TAG SECTION_END
;

padding
:
	PADDING_START PADDING_BYTE* SECTION_END
;

NBT_TAG
:
	NBT_COMPOUND
	| NBT_LIST
;

fragment
NBT_COMPOUND
:
	'{'
	(
		~( '}' | '"' )
		| QUOTED_STRING
		| NBT_TAG
	)* '}'
;

fragment
NBT_LIST
:
	'['
	(
		~( ']' | '"' )
		| QUOTED_STRING
		| NBT_TAG
	)* ']'
;

fragment
QUOTED_STRING
:
	'"'
	(
		'\\"'
		| ~'"'
	)+ '"'
	| '""'
;

PROPERTIES_DATA
:
	~( '=' | [\r\n] )+ '=' ~( '=' | [\r\n] )*
	| ~( '=' | [\r\n] )+ '=' ~( '=' | [\r\n] )* [\r\n]+ PROPERTIES_DATA
;

FILE_START
:
	'(file'
;

PROPERTIES_START
:
	'(properties'
;

PARTITION_START
:
	'(partition'
;

DATA_START
:
	'(data'
;

PADDING_START
:
	'(padding'
;

SECTION_END
:
	')'
;

PADDING_BYTE
:
	[0-9a-fA-F] [0-9a-fA-F]
;

COMMENT
:
	'#' ~[\n\r]*
	(
		[\n\r]
		| EOF
	) -> skip
;

WS
:
	[ \t\n\r]+ -> skip
;
