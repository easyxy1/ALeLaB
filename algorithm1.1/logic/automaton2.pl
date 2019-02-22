lstates([init, q1, q2, q3, q4, q5, q6]).
lsymbols([a,<,>,1]).
ltransition(init, a, q1).
ltransition(init, <, q6).
ltransition(q1, a, q5).
ltransition(q1, <, q2).
ltransition(q2, a, q6).
ltransition(q2, 1, q3).
ltransition(q2, >, q5).
ltransition(q3, > ,q4).
ltransition(q3, a ,q6).
ltransition(q3, 1 ,q3).
ltransition(q4, <, q6).
ltransition(q4, a, q5).
ltransition(q5, a, q5).
ltransition(q5, <, q6).
ltransition(q6, >, q5).
ltransition(q6, 1, q6).
lltransition(q6, a, q6).
lstartState(init).
lfinalStates([q4]).
