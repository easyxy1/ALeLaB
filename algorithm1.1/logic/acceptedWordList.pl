

% return whether w is accepted by Teacher's language from Q.
isacceptedbyT(W, Q):- length(W,Length), accept_length_T(Q,W,Length).

acceptedT(Q, [], []) :- tfinalStates(F), !, member(Q, F).
acceptedT(Q, [_|K], [A|W]) :- ttransition(Q, A, Q1), acceptedT(Q1, K, W).

% list all positive K-length words accepted by given M finite automata
% Q is initial state, Length is the number of a string we want , R is the result.
accept_length_T(Q, R, Length) :-
    length(K, Length),
    acceptedT(Q, K, R).
   % atomic_list_concat(Rl, '', R)

% return whether w is accepted by a language from Q.
isacceptedbyL(W, Q):- length(W,Length), accept_length_L(Q,W,Length).

acceptedL(Q, [], []) :- lfinalStates(F), !, member(Q, F).
acceptedL(Q, [_|K], [A|W]) :- ltransition(Q, A, Q1), acceptedL(Q1, K, W).

% list all positive K-length words accepted by given M finite automata
% Q is initial state, Length is the number of a string we want , R is the result.
accept_length_L(Q, R, Length) :-
    length(K, Length),
    acceptedL(Q, K, R).
:- set_prolog_flag(answer_write_options,[max_depth(0)]).
% compare two K-length accepted words of Teacher's language and Learner's language


%issame(Q,Length,Cs,L2,L11):-(accept_length_T(Q,Rt,Length),accept_length_L(Q,Rl,Length))->
%		(setof(Rt,accept_length_T(Q,Rt,Length),L1),
%		string_in_list(L1, L11),
%		setof(Rl,accept_length_L(Q,Rl,Length),L),
%		string_in_list(L, L2),
%		selectout(L2,L11,Cs));
%		(accept_length_L(Q,Rl,Length)->
%		 sort(Rl,Cs);
%		 setof(Rt,accept_length_T(Q,Rt,Length),Cs)).

issame(Q,Length,Cs):-
	setof(Rt,accept_length_T(Q,Rt,Length),Lt)->
	string_in_list(Lt, Lt1),
	(setof(Rl,accept_length_L(Q,Rl,Length),Ll)->
		string_in_list(Ll, Ll1),
		selectout(Lt1,Ll1,Cs);
		Cs=Lt1);
	(setof(Rl,accept_length_L(Q,Rl,Length),Ll)->
		string_in_list(Ll, Ll1),
		Cs=Ll1;
		Cs=[]).


% select out the member in either one list or another.
selectout(L,L1,Cs):- union(L,L1,U),
			intersection(L,L1,I),
			subtract(U,I,Cs).
% change list members to string type.
string_in_list([], []).
string_in_list([Hs|Xs], [S|Ys]):-atomic_list_concat(Hs, '', S),string_in_list(Xs, Ys).


