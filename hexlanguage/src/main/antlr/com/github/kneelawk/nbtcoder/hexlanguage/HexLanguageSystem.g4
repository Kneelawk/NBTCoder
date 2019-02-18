grammar HexLanguageSystem;

@header {
	package com.github.kneelawk.nbtcoder.hexlanguage;
}

data
:
	BYTE*
;

BYTE
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
