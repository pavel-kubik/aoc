grammar GTOut;

snailfishNumber
    : open_bracket node comma node close_bracket EOF
    ;

node
    : open_bracket node comma node close_bracket
    | NUMBER
    ;

open_bracket
    : '['
    ;

comma
    : ','
    ;

close_bracket
    : ']'
    ;

NUMBER
    : ('0'..'9')
    ;