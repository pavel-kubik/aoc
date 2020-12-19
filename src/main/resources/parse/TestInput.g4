grammar TestInput;

rule0:  rule4 rule1 rule5 ;
rule1:  rule2 rule3 | rule3 rule2 ;
rule2:  rule4 rule4 | rule5 rule5 ;
rule3:  rule4 rule5 | rule5 rule4 ;
rule4: 'a' ;
rule5: 'b' ;