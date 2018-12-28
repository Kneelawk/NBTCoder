grammar NBTFileLanguageSystem;

@header {
	package com.github.kneelawk.filelanguage;
}

nbtFile
:
	NL* FILE_START NL+ properties NL+
	(
		partition
		(
			NL+ partition
		)*
		| data
	) NL+ SECTION_END NL*
;

partition
:
	PARTITION_START NL+ properties NL+ data NL+ SECTION_END
;

properties
:
	PROPERTIES_START NL+ PROPERTIES_DATA NL+ SECTION_END
;

data
:
	DATA_START NL+ NBT_TAG NL+ SECTION_END
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

SECTION_END
:
	')'
;

NL
:
	[\r\n]
;

WS
:
	[ \t]
;
